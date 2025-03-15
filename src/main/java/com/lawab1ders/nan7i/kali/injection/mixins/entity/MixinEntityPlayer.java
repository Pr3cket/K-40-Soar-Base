package com.lawab1ders.nan7i.kali.injection.mixins.entity;

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
import com.lawab1ders.nan7i.kali.injection.interfaces.IEntityPlayer;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinEntityLivingBase;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.render.CustomizableModelPart;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.StickSimulation;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements IEntityPlayer {

    @Unique
    private CustomizableModelPart k40$headLayers;

    @Unique
    private CustomizableModelPart[] k40$skinLayers;

    @Unique
    private final StickSimulation k40$simulation = new StickSimulation();

    @Inject(
            method = "dropItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getEyeHeight()F")
    )
    public void k40$dropItemSwing(ItemStack droppedItem, boolean dropAround, boolean traceItem,
                                  CallbackInfoReturnable<EntityItem> cir) {
        val mc = Minecraft.getMinecraft();
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (mc.theWorld.isRemote) {
            if (mc.currentScreen instanceof GuiChest) return;

            if (!mc.thePlayer.isSwingInProgress
                    || mc.thePlayer.swingProgressInt >= ((IMixinEntityLivingBase) mc.thePlayer).k40$invokeGetArmSwingAnimationEnd() / 2
                    || mc.thePlayer.swingProgressInt < 0) {
                mc.thePlayer.swingProgressInt = -1;
                mc.thePlayer.isSwingInProgress = true;
            }
        }
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void k40$moveCloakUpdate(CallbackInfo info) {
        if ((Object) this instanceof EntityPlayer) {
            k40$simulate((EntityPlayer) (Object) this);
        }
    }

    @Override
    public CustomizableModelPart k40$getHeadLayers() {
        return k40$headLayers;
    }

    @Override
    public CustomizableModelPart[] k40$getSkinLayers() {
        return k40$skinLayers;
    }

    @Override
    public void k40$setHeadLayers(CustomizableModelPart box) {
        k40$headLayers = box;
    }

    @Override
    public void k40$setSkinLayers(CustomizableModelPart[] box) {
        k40$skinLayers = box;
    }

    @Override
    public StickSimulation k40$getSimulation() {
        return k40$simulation;
    }
}
