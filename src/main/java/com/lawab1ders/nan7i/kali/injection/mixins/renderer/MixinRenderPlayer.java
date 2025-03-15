package com.lawab1ders.nan7i.kali.injection.mixins.renderer;

/*
 * Copyright (c) 2025 EldoDebug, Nan7.南起
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
import com.lawab1ders.nan7i.kali.injection.interfaces.IRenderPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.SkinLayers3DModule;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.layers.BodyLayerFeatureRenderer;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.layers.HeadLayerFeatureRenderer;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.CustomCapeRenderLayer;
import com.lawab1ders.nan7i.kali.utils.SkinUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity<AbstractClientPlayer> implements IRenderPlayer {

    @Shadow
    private boolean smallArms;

    @Shadow
    public abstract ModelPlayer getMainModel();

    @Unique
    private HeadLayerFeatureRenderer k40$headLayer;

    @Unique
    private BodyLayerFeatureRenderer k40$bodyLayer;

    public MixinRenderPlayer(RenderManager p_i46156_1_, ModelBase p_i46156_2_, float p_i46156_3_) {
        super(p_i46156_1_, p_i46156_2_, p_i46156_3_);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    @SuppressWarnings("ALL")
    public void k40$onCreate(CallbackInfo info) {
        k40$headLayer = new HeadLayerFeatureRenderer((RenderPlayer) (Object) this);
        k40$bodyLayer = new BodyLayerFeatureRenderer((RenderPlayer) (Object) this);
        addLayer(new CustomCapeRenderLayer((RenderPlayer)(Object)this, getMainModel()));
    }

    @Inject(method = "setModelVisibilities", at = @At("HEAD"))
    private void k40$setModelProperties(AbstractClientPlayer abstractClientPlayer, CallbackInfo info) {
        if (!InstanceAccess.mod.getModule(SkinLayers3DModule.class).isActivated()) return;

        if (Minecraft.getMinecraft().thePlayer.getPositionVector()
                                              .squareDistanceTo(abstractClientPlayer.getPositionVector())
                >= SkinLayers3DModule.RENDER_DISTANCE_LOD * SkinLayers3DModule.RENDER_DISTANCE_LOD
                && !abstractClientPlayer.isSpectator()) {

            ModelPlayer playerModel = getMainModel();

            playerModel.bipedHeadwear.isHidden = false;
            playerModel.bipedBodyWear.isHidden = false;
            playerModel.bipedLeftArmwear.isHidden = false;
            playerModel.bipedRightArmwear.isHidden = false;
            playerModel.bipedLeftLegwear.isHidden = false;
            playerModel.bipedRightLegwear.isHidden = false;
        }
    }

    @Inject(method = "renderRightArm", at = @At("RETURN"))
    public void k40$renderRightArm(AbstractClientPlayer player, CallbackInfo info) {
        if (InstanceAccess.mod.getModule(SkinLayers3DModule.class).isActivated()) {
            k40$renderFirstPersonArm(player, 3, (IEntityPlayer) player);
        }
    }

    @Inject(method = "renderLeftArm", at = @At("RETURN"))
    public void k40$renderLeftArm(AbstractClientPlayer player, CallbackInfo info) {
        if (InstanceAccess.mod.getModule(SkinLayers3DModule.class).isActivated()) {
            k40$renderFirstPersonArm(player, 2, (IEntityPlayer) player);
        }
    }

    @Unique
    private void k40$renderFirstPersonArm(AbstractClientPlayer player, int layerId, IEntityPlayer settings) {
        if (settings.k40$getSkinLayers() == null && !k40$setupModel(player, settings)) return;

        float pixelScaling = SkinLayers3DModule.BASE_VOXEL_SIZE;

        GlStateManager.pushMatrix();
        getMainModel().bipedRightArm.postRender(0.0625F);
        GlStateManager.scale(0.0625, 0.0625, 0.0625);
        GlStateManager.scale(pixelScaling, pixelScaling, pixelScaling);

        if (!smallArms) {
            settings.k40$getSkinLayers()[layerId].x = -0.998f * 16f;
        }
        else {
            settings.k40$getSkinLayers()[layerId].x = -0.499f * 16;
        }

        settings.k40$getSkinLayers()[layerId].render(false);
        GlStateManager.popMatrix();
    }

    @Unique
    private boolean k40$setupModel(AbstractClientPlayer abstractClientPlayerEntity, IEntityPlayer settings) {
        if (SkinUtils.noCustomSkin(abstractClientPlayerEntity)) return false;

        SkinUtils.setup3dLayers(abstractClientPlayerEntity, settings, smallArms, null);
        return true;
    }

    @Redirect(
            method = "renderRightArm",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z", ordinal = 0)
    )
    private void k40$resetArmState(ModelPlayer modelPlayer, boolean value) {
        modelPlayer.isRiding = modelPlayer.isSneak = false;
    }

    @Override
    public HeadLayerFeatureRenderer k40$getHeadLayer() {
        return k40$headLayer;
    }

    @Override
    public BodyLayerFeatureRenderer k40$getBodyLayer() {
        return k40$bodyLayer;
    }

    @Override
    public boolean k40$isSmallArms() {
        return smallArms;
    }
}
