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
import com.lawab1ders.nan7i.kali.module.ModuleManager;
import com.lawab1ders.nan7i.kali.module.impl.hud.RearviewModule;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import com.lawab1ders.nan7i.kali.module.impl.render.OverlayEditorModule;
import lombok.val;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Unique
    private static float k40$f1 = 0.0F;

    @Shadow
    private ItemStack itemToRender;

    @Shadow
    @Final
    private RenderItem itemRenderer;

    @Unique
    private static void k40$itemTransforms() {
        float scale = 1.5F / 1.7F;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(5.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.29F, 0.149F, -0.0328F);
    }

    @Inject(method = "renderItemInFirstPerson", at = @At("HEAD"))
    public void k40$renderItemInFirstPerson(CallbackInfo ci) {
        OverlayEditorModule module = InstanceAccess.mod.getModule(OverlayEditorModule.class);

        if (module.isActivated()) {
            GlStateManager.translate(
                    module.handXPosSetting.getValue(), module.handYPosSetting.getValue(),
                    module.handZPosSetting.getValue()
            );
            GlStateManager.scale(
                    module.handXScaleSetting.getValue(), module.handYScaleSetting.getValue(),
                    module.handZScaleSetting.getValue()
            );
        }
    }

    @Inject(at = @At("HEAD"), method = "renderFireInFirstPerson", cancellable = true)
    private void k40$renderFireInFirstPerson(CallbackInfo ci) {
        ModuleManager moduleManager = InstanceAccess.mod;
        RearviewModule rearviewModule = moduleManager.getModule(RearviewModule.class);
        OverlayEditorModule overlayEditorModule = moduleManager.getModule(OverlayEditorModule.class);

        if ((rearviewModule.isActivated() && rearviewModule.rearviewCamera.isRecording())
                || (overlayEditorModule.isActivated() && overlayEditorModule.hideFireSetting.isActivated())) {

            ci.cancel();
        }
    }

    @Inject(method = "renderWaterOverlayTexture", at = @At("HEAD"), cancellable = true)
    private void k40$preRenderWaterOverlayTexture(CallbackInfo ci) {
        RearviewModule rearviewModule = InstanceAccess.mod.getModule(RearviewModule.class);

        if (rearviewModule.isActivated() && rearviewModule.rearviewCamera.isRecording()) {
            ci.cancel();
        }
    }

    //---------------------------------------------------------------------------
    // Overflow Animations
    //---------------------------------------------------------------------------

    @ModifyVariable(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "STORE"
            ),
            index = 4
    )
    private float k40$captureF1(float f1) {
        k40$f1 = f1;
        return f1;
    }

    @ModifyArg(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;transformFirstPersonItem(FF)V"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;performDrinking(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/renderer/ItemRenderer;doBowTransformations(FLnet/minecraft/client/entity/AbstractClientPlayer;)V"
                    )
            ), index = 1
    )
    private float k40$useF1(float swingProgress) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return swingProgress;

        return k40$f1;
    }

    @Inject(
            method = "doBowTransformations",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"
            )
    )
    private void k40$preBowTransform(float partialTicks, AbstractClientPlayer clientPlayer,
                                     CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        GlStateManager.rotate(-335.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.5F, 0.0F);
    }

    @Inject(method = "doBowTransformations", at = @At(value = "TAIL"))
    private void k40$postBowTransform(float partialTicks, AbstractClientPlayer clientPlayer,
                                      CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        GlStateManager.translate(0.0F, -0.5F, 0.0F);
        GlStateManager.rotate(50.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(335.0F, 0.0F, 0.0F, 1.0F);
    }

    @Inject(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            )
    )
    private void k40$firstPersonItemPositions(float partialTicks, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (!itemRenderer.shouldRenderItemIn3D(itemToRender)) {
            if (itemToRender.getItem().shouldRotateAroundWhenRendering()) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                k40$itemTransforms();

            }
            else if (!(itemToRender.getItem() instanceof ItemSword)) {
                k40$itemTransforms();
            }
        }
    }
}
