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
import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.injection.StaticStation;
import com.lawab1ders.nan7i.kali.injection.interfaces.IRenderPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import com.lawab1ders.nan7i.kali.module.impl.player.SkinLayers3DModule;
import com.lawab1ders.nan7i.kali.module.impl.render.HitColorModule;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    @Unique
    private float k40$red;

    @Unique
    private float k40$green;

    @Unique
    private float k40$blue;

    @Unique
    private float k40$alpha;

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "setBrightness", at = @At("HEAD"))
    public void k40$hitColor(T entitylivingbaseIn, float partialTicks, boolean combineTextures,
                             CallbackInfoReturnable<Boolean> cir) {

        HitColorModule module = InstanceAccess.mod.getModule(HitColorModule.class);

        if (module.isActivated()) {
            AccentColor currentColor = InstanceAccess.col.getCurrentColor();
            Color lastColor = currentColor.getInterpolateColor();

            k40$red = lastColor.getRed() / 255F;
            k40$green = lastColor.getGreen() / 255F;
            k40$blue = lastColor.getBlue() / 255F;
            k40$alpha = module.alphaSetting.getValue();
        }
        else {
            k40$red = 1;
            k40$green = 0;
            k40$blue = 0;
            k40$alpha = 0.3F;
        }
    }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 1, ordinal = 0))
    public float k40$setBrightnessRed(float original) {
        return k40$red;
    }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 0, ordinal = 0))
    public float k40$setBrightnessGreen(float original) {
        return k40$green;
    }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 0, ordinal = 1))
    public float k40$setBrightnessBlue(float original) {
        return k40$blue;
    }

    @ModifyConstant(method = "setBrightness", constant = @Constant(floatValue = 0.3F, ordinal = 0))
    public float k40$setBrightnessAlpha(float original) {
        return k40$alpha;
    }

    @Inject(method = "renderModel", at = @At("TAIL"))
    private void k40$renderModelLayers(T p_renderModel_1_, float p_renderModel_2_, float p_renderModel_3_,
                                       float p_renderModel_4_, float p_renderModel_5_, float p_renderModel_6_,
                                       float p_renderModel_7_, CallbackInfo info) {

        if (!InstanceAccess.mod.getModule(SkinLayers3DModule.class).isActivated()) return;
        if (!(this instanceof IRenderPlayer)) return;

        boolean flag = !p_renderModel_1_.isInvisible();
        boolean flag1 = (!flag && !p_renderModel_1_.isInvisibleToPlayer((Minecraft.getMinecraft()).thePlayer));

        if (flag || flag1) {

            IRenderPlayer playerRenderer = (IRenderPlayer) this;

            if (flag1) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }
            playerRenderer.k40$getHeadLayer()
                          .doRenderLayer(
                                  (AbstractClientPlayer) p_renderModel_1_, p_renderModel_2_, 0f, p_renderModel_3_,
                                  p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_
                          );
            playerRenderer.k40$getBodyLayer()
                          .doRenderLayer(
                                  (AbstractClientPlayer) p_renderModel_1_, p_renderModel_2_, 0f, p_renderModel_3_,
                                  p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_
                          );
            if (flag1) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }

    //---------------------------------------------------------------------------
    // Overflow Animations
    //---------------------------------------------------------------------------

    @Inject(
            method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
            )
    )
    public void k40$movePlayerModel(T entity, double x, double y, double z, float entityYaw, float partialTicks,
                                    CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (entity instanceof EntityPlayerSP && entity.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
            if (entity.isSneaking()) {
                GlStateManager.translate(0.0F, -0.2F, 0.0F);
            }

            GlStateManager.translate(0.0F, 1.62F - StaticStation.sneakingHeight, 0.0F);
        }
    }
}
