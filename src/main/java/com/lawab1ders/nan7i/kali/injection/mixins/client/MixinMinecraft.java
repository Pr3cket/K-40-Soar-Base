package com.lawab1ders.nan7i.kali.injection.mixins.client;

/*
 * Copyright (c) 2025 EldoDebug, Nan7i.南起
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.injection.interfaces.IMinecraft;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinEntityLivingBase;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft implements IMinecraft {

    @Shadow
    public int displayWidth;

    @Shadow
    public int displayHeight;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public MovingObjectPosition objectMouseOver;

    @Shadow
    public EffectRenderer effectRenderer;

    @Shadow
    public WorldClient theWorld;

    @Shadow
    public GameSettings gameSettings;

    @Shadow
    private int leftClickCounter;

    @Shadow
    private boolean fullscreen;

    @Shadow
    int fpsCounter;

    @Override
    public void k40$increaseFPS() {
        fpsCounter++;
    }

    @Override
    public int k40$getFPS() {
        return fpsCounter;
    }

    @Inject(method = "clickMouse", at = @At("HEAD"))
    public void k40$fixHitDelay(CallbackInfo ci) {
        leftClickCounter = 0;
    }

    @Inject(method = "checkGLError", at = @At("HEAD"), cancellable = true)
    private void k40$fdisCheckGLError(String p_checkGLError_1_, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
            method = "toggleFullscreen",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setFullscreen(Z)V", remap = false)
    )
    private void k40$resolveScreenState(CallbackInfo ci) {
        if (!this.fullscreen && SystemUtils.IS_OS_WINDOWS) {
            Display.setResizable(false);
            Display.setResizable(true);
        }
    }

    //---------------------------------------------------------------------------
    // Overflow Animations
    //---------------------------------------------------------------------------

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"))
    public void k40$blockHitAnimation(boolean leftClick, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (gameSettings.keyBindUseItem.isKeyDown() && leftClickCounter <= 0 && leftClick && objectMouseOver != null) {
            thePlayer.isUsingItem();

            BlockPos posBlock = objectMouseOver.getBlockPos();

            if (posBlock != null && !theWorld.isAirBlock(posBlock) && thePlayer.isAllowEdit()) {
                effectRenderer.addBlockHitEffects(posBlock, objectMouseOver.sideHit);
            }

            if (!thePlayer.isSwingInProgress ||
                    thePlayer.swingProgressInt >= ((IMixinEntityLivingBase) thePlayer).k40$invokeGetArmSwingAnimationEnd() / 2 ||
                    thePlayer.swingProgressInt < 0) {

                thePlayer.swingProgressInt = -1;
                thePlayer.isSwingInProgress = true;
            }
        }
    }

    @Inject(method = "clickMouse", at = @At(value = "TAIL"))
    public void k40$onHitParticles(CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (leftClickCounter > 0) {
            if (objectMouseOver != null
                    && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
                    && !objectMouseOver.entityHit.hitByEntity(thePlayer)
                    && objectMouseOver.entityHit instanceof EntityLivingBase) {

                if (thePlayer.fallDistance > 0.0F && !thePlayer.onGround && !thePlayer.isOnLadder()
                        && !thePlayer.isInWater()
                        && !thePlayer.isPotionActive(Potion.blindness)
                        && thePlayer.ridingEntity == null) {

                    thePlayer.onCriticalHit(objectMouseOver.entityHit);
                }

                if (EnchantmentHelper.getModifierForCreature(
                        thePlayer.getHeldItem(),
                        ((EntityLivingBase) objectMouseOver.entityHit).getCreatureAttribute()
                ) > 0.0F) {
                    thePlayer.onEnchantmentCritical(objectMouseOver.entityHit);
                }
            }

            if (!thePlayer.isSwingInProgress ||
                    thePlayer.swingProgressInt >= ((IMixinEntityLivingBase) thePlayer).k40$invokeGetArmSwingAnimationEnd() / 2 ||
                    thePlayer.swingProgressInt < 0) {

                thePlayer.swingProgressInt = -1;
                thePlayer.isSwingInProgress = true;
            }
        }
    }

    @Redirect(
            method = "rightClickMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z"
            )
    )
    public boolean k40$enabledRightClick(PlayerControllerMP instance) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        return !module.isActivated() && instance.getIsHittingBlock();
    }

    @Inject(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;dropOneItem(Z)Lnet/minecraft/entity/item/EntityItem;",
                    shift = At.Shift.AFTER
            )
    )
    public void k40$dropItemSwing(CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (thePlayer.getHeldItem() != null) {
            if (!thePlayer.isSwingInProgress ||
                    thePlayer.swingProgressInt >= ((IMixinEntityLivingBase) thePlayer).k40$invokeGetArmSwingAnimationEnd() / 2 ||
                    thePlayer.swingProgressInt < 0) {

                thePlayer.swingProgressInt = -1;
                thePlayer.isSwingInProgress = true;
            }
        }
    }
}
