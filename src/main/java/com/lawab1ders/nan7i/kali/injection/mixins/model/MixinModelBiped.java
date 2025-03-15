package com.lawab1ders.nan7i.kali.injection.mixins.model;

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
import lombok.val;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {

    @Shadow
    public ModelRenderer bipedRightArm;

    @Inject(
            method = "setRotationAngles",
            at = @At(
                    value = "FIELD", opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/model/ModelRenderer;rotateAngleY:F", shift = At.Shift.AFTER
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD", opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/client/model/ModelBiped;heldItemRight:I", ordinal = 0
                    ),
                    to = @At(
                            value = "FIELD", opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/client/model/ModelBiped;heldItemRight:I", ordinal = 2
                    )
            )
    )
    private void k40$reAssignArmPosition(float limbSwing, float limbSwingAmount, float ageInTicks,
                                         float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn,
                                         CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        bipedRightArm.rotateAngleY = 0.0f;
    }
}
