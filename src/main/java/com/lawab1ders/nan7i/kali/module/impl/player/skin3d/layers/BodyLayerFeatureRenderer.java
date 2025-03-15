package com.lawab1ders.nan7i.kali.module.impl.player.skin3d.layers;

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

import com.lawab1ders.nan7i.kali.injection.interfaces.IEntityPlayer;
import com.lawab1ders.nan7i.kali.injection.interfaces.IRenderPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.SkinLayers3DModule;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.render.CustomizableModelPart;
import com.lawab1ders.nan7i.kali.utils.SkinUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BodyLayerFeatureRenderer implements LayerRenderer<AbstractClientPlayer> {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final boolean thinArms;
    private final List<Layer> bodyLayers = new ArrayList<>();

    public BodyLayerFeatureRenderer(RenderPlayer playerRenderer) {
        thinArms = ((IRenderPlayer) playerRenderer).k40$isSmallArms();
        bodyLayers.add(new Layer(
                0, false, EnumPlayerModelParts.LEFT_PANTS_LEG, Shape.LEGS,
                () -> playerRenderer.getMainModel().bipedLeftLeg
        ));
        bodyLayers.add(new Layer(
                1, false, EnumPlayerModelParts.RIGHT_PANTS_LEG, Shape.LEGS,
                () -> playerRenderer.getMainModel().bipedRightLeg
        ));
        bodyLayers.add(new Layer(
                2, false, EnumPlayerModelParts.LEFT_SLEEVE, thinArms ? Shape.ARMS_SLIM : Shape.ARMS,
                () -> playerRenderer.getMainModel().bipedLeftArm
        ));
        bodyLayers.add(new Layer(
                3, true, EnumPlayerModelParts.RIGHT_SLEEVE, thinArms ? Shape.ARMS_SLIM : Shape.ARMS,
                () -> playerRenderer.getMainModel().bipedRightArm
        ));
        bodyLayers.add(new Layer(
                4, false, EnumPlayerModelParts.JACKET, Shape.BODY,
                () -> playerRenderer.getMainModel().bipedBody
        ));
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float paramFloat1, float paramFloat2, float paramFloat3,
                              float deltaTick, float paramFloat5, float paramFloat6, float paramFloat7) {

        if (!player.hasSkin() || player.isInvisible()) return;
        if (mc.theWorld == null) return;
        if (mc.thePlayer.getPositionVector()
                        .squareDistanceTo(player.getPositionVector()) > SkinLayers3DModule.RENDER_DISTANCE_LOD) return;

        IEntityPlayer settings = (IEntityPlayer) player;

        // only render if the player has a skin
        if (settings.k40$getSkinLayers() == null && !setupModel(player, settings)) return;

        renderLayers(player, settings.k40$getSkinLayers());
    }

    private boolean setupModel(AbstractClientPlayer abstractClientPlayerEntity, IEntityPlayer settings) {
        if (SkinUtils.noCustomSkin(abstractClientPlayerEntity)) return false;

        SkinUtils.setup3dLayers(abstractClientPlayerEntity, settings, thinArms, null);
        return true;
    }

    public void renderLayers(AbstractClientPlayer abstractClientPlayer, CustomizableModelPart[] layers) {
        if (layers == null) return;

        float pixelScaling = SkinLayers3DModule.BASE_VOXEL_SIZE;
        float heightScaling = 1.035f, widthScaling;

        boolean redTint = abstractClientPlayer.hurtTime > 0 || abstractClientPlayer.deathTime > 0;

        for (Layer layer : bodyLayers) {
            if (abstractClientPlayer.isWearing(layer.modelPart) && !layer.vanillaGetter.get().isHidden) {
                GlStateManager.pushMatrix();
                if (abstractClientPlayer.isSneaking()) GlStateManager.translate(0.0F, 0.2F, 0.0F);

                layer.vanillaGetter.get().postRender(0.0625F);

                if (layer.shape == Shape.ARMS) {
                    layers[layer.layersId].x = 0.998f * 16;
                }
                else if (layer.shape == Shape.ARMS_SLIM) {
                    layers[layer.layersId].x = 0.499f * 16;
                }

                if (layer.shape == Shape.BODY) {
                    widthScaling = SkinLayers3DModule.BODY_VOXEL_WIDTH_SIZE;
                }
                else {
                    widthScaling = SkinLayers3DModule.BASE_VOXEL_SIZE;
                }

                if (layer.mirrored) {
                    layers[layer.layersId].x *= -1;
                }
                GlStateManager.scale(0.0625, 0.0625, 0.0625);
                GlStateManager.scale(widthScaling, heightScaling, pixelScaling);
                layers[layer.layersId].y = layer.shape.yOffsetMagicValue;

                layers[layer.layersId].render(redTint);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private enum Shape {
        HEAD(0), BODY(0.6f), LEGS(-0.2f), ARMS(0.4f), ARMS_SLIM(0.4f);

        private final float yOffsetMagicValue;

        Shape(float yOffsetMagicValue) {
            this.yOffsetMagicValue = yOffsetMagicValue;
        }
    }

    static class Layer {

        int layersId;
        boolean mirrored;
        EnumPlayerModelParts modelPart;
        Shape shape;
        Supplier<ModelRenderer> vanillaGetter;

        public Layer(int layersId, boolean mirrored, EnumPlayerModelParts modelPart, Shape shape,
                     Supplier<ModelRenderer> vanillaGetter) {
            this.layersId = layersId;
            this.mirrored = mirrored;
            this.modelPart = modelPart;
            this.shape = shape;
            this.vanillaGetter = vanillaGetter;
        }
    }
}