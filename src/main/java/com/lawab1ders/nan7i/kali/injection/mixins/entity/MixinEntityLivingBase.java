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
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import com.lawab1ders.nan7i.kali.module.impl.render.OverlayEditorModule;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    @Shadow
    public float swingProgress;

    @Shadow
    public float renderYawOffset;

    @Shadow
    public float rotationYawHead;

    @Unique
    protected float k40$newHeadYaw;

    @Unique
    protected int k40$headYawLerpWeight;

    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "getArmSwingAnimationEnd", at = @At("HEAD"), cancellable = true)
    public void k40$changeSwingSpeed(CallbackInfoReturnable<Integer> cir) {
        val mod = InstanceAccess.mod.getModule(OverlayEditorModule.class);

        if (mod.isActivated()) {
            cir.setReturnValue(mod.handSwingDelaySetting.getValue());
        }
    }

    //---------------------------------------------------------------------------
    // Overflow Animations
    //---------------------------------------------------------------------------

    @Inject(method = "setRotationYawHead", at = @At("HEAD"), cancellable = true)
    public void k40$setAsNewHeadYaw(float rotation, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        k40$newHeadYaw = MathHelper.wrapAngleTo180_float(rotation);
        k40$headYawLerpWeight = 3;
        ci.cancel();
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void k40$updateHeadYaw(CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated() || k40$headYawLerpWeight <= 0) return;

        rotationYawHead += MathHelper.wrapAngleTo180_float(
                k40$newHeadYaw - rotationYawHead) / k40$headYawLerpWeight;
        rotationYawHead = MathHelper.wrapAngleTo180_float(rotationYawHead);
        k40$headYawLerpWeight--;
    }

    @ModifyArg(
            method = "onUpdate",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;updateDistance(FF)F"
            ), index = 0
    )
    public float k40$modifyYaw(float p_1101461) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        double d0 = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float h = renderYawOffset;

        if (f > 0.0025000002F) {
            float f1 = (float) MathHelper.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
            float g = MathHelper.abs(MathHelper.wrapAngleTo180_float(rotationYaw) - f1);
            h = f1 - (95.0F < g && g < 265.0F ? 180.0F : 0.0F);
        }

        if (swingProgress > 0.0F) {
            h = rotationYaw;
        }

        return module.isActivated() ? h : p_1101461;
    }
}
