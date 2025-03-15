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
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinRendererLivingEntity;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import lombok.val;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    @Unique
    private static ModelBase k40$t = null;

    @Shadow
    @Final
    private RendererLivingEntity<?> renderer;

    @Unique
    private static int k40$getRGB(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF));
    }

    @ModifyVariable(
            method = "renderLayer", at = @At(value = "STORE"),
            index = 12
    )
    private T k40$captureT(T t) {
        k40$t = t;
        return t;
    }

    @Inject(
            method = "renderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    public void k40$addRender(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_,
                              float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_,
                              float scale, int armorSlot, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (((IMixinRendererLivingEntity) renderer).k40$invokeSetDoRenderBrightness(entitylivingbaseIn, partialTicks)) {
            k40$t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
            ((IMixinRendererLivingEntity) renderer).k40$invokeUnsetBrightness();
        }
    }

    @Inject(
            method = "renderGlint(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/model/ModelBase;FFFFFFF)V",
            at = @At(
                    value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V", ordinal = 0
            )
    )
    private void k40$renderNewArmorGlintPre(EntityLivingBase entitylivingbaseIn, T modelbaseIn,
                                            float p_177183_3_, float p_177183_4_, float p_177183_5_,
                                            float p_177183_6_, float p_177183_7_, float p_177183_8_,
                                            float p_177183_9_, CallbackInfo info) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        float light = 240.0F;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light, light);
    }

    @Inject(
            method = "renderGlint",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/model/ModelBase.render(Lnet/minecraft/entity/Entity;FFFFFF)V"
            )
    )
    private void k40$renderNewArmorGlintPost(EntityLivingBase entitylivingbaseIn, T modelbaseIn,
                                             float p_177183_3_, float p_177183_4_, float partialTicks,
                                             float p_177183_6_, float p_177183_7_, float p_177183_8_,
                                             float scale, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
    }

    @ModifyArgs(
            method = "renderGlint",
            at = @At(
                    value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V", ordinal = 1
            )
    )
    private void k40$newArmorGlintColor(Args args) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        int rgb = k40$getRGB(
                (int) (((float) args.get(0)) * 255),
                (int) (((float) args.get(1)) * 255), (int) (((float) args.get(2)) * 255),
                (int) (((float) args.get(3)) * 255)
        );

        if (rgb == -8372020 || rgb == -10473317) {
            args.set(0, 0.5608F);
            args.set(1, 0.3408F);
            args.set(2, 0.8608F);
            args.set(3, 1.0F);
        }
    }
}
