package com.lawab1ders.nan7i.kali.injection.mixins.renderer;

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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSnowball.class)
public abstract class MixinRenderSnowball<T extends Entity> extends Render<T> {

    public MixinRenderSnowball(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderSnowball;bindTexture(Lnet/minecraft/util/ResourceLocation;)V",
                    shift = At.Shift.AFTER
            )
    )
    public void k40$projectileTransforms(T entity, double x, double y, double z, float entityYaw,
                                         float partialTicks, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
    }

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 0
            ), index = 0
    )
    private float k40$fixRotationY(float original) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        return module.isActivated() ? 180.0F + original : original;
    }

    @ModifyArg(
            method = "doRender",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 1
            ), index = 0
    )
    private float k40$fixRotationX(float original) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        return (module.isActivated() ? -1F : 1F) * original;
    }

    @Inject(
            method = "doRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    public void k40$shiftProjectile(T entity, double x, double y, double z, float entityYaw,
                                    float partialTicks, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        GlStateManager.translate(0.0F, 0.25F, 0.0F);
    }
}
