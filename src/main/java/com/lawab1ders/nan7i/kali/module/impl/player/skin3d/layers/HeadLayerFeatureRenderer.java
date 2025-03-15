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
import com.lawab1ders.nan7i.kali.utils.SkinUtils;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class HeadLayerFeatureRenderer implements LayerRenderer<AbstractClientPlayer> {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final boolean thinArms;
    private final Set<Item> hideHeadLayers = Sets.newHashSet(Items.skull);
    private final RenderPlayer playerRenderer;

    public HeadLayerFeatureRenderer(RenderPlayer playerRenderer) {
        thinArms = ((IRenderPlayer) playerRenderer).k40$isSmallArms();
        this.playerRenderer = playerRenderer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer player, float paramFloat1, float paramFloat2, float paramFloat3,
                              float deltaTick, float paramFloat5, float paramFloat6, float paramFloat7) {

        if (!player.hasSkin() || player.isInvisible()) {
            return;
        }

        if (mc.thePlayer.getPositionVector().squareDistanceTo(
                player.getPositionVector()) > SkinLayers3DModule.RENDER_DISTANCE_LOD * SkinLayers3DModule.RENDER_DISTANCE_LOD) {
            return;
        }

        ItemStack itemStack = player.getEquipmentInSlot(1);

        if (itemStack != null && hideHeadLayers.contains(itemStack.getItem())) {
            return;
        }

        IEntityPlayer settings = (IEntityPlayer) player;

        if (settings.k40$getHeadLayers() == null && !setupModel(player, settings)) {
            return;
        }

        renderCustomHelmet(settings, player);
    }

    private boolean setupModel(AbstractClientPlayer abstractClientPlayerEntity, IEntityPlayer settings) {

        if (SkinUtils.noCustomSkin(abstractClientPlayerEntity)) {
            return false;
        }

        SkinUtils.setup3dLayers(abstractClientPlayerEntity, settings, thinArms, null);

        return true;
    }

    public void renderCustomHelmet(IEntityPlayer settings, AbstractClientPlayer abstractClientPlayer) {
        if (settings.k40$getHeadLayers() == null) {
            return;
        }

        if (playerRenderer.getMainModel().bipedHead.isHidden) {
            return;
        }

        float voxelSize = SkinLayers3DModule.HEAD_VOXEL_SIZE;

        GlStateManager.pushMatrix();

        if (abstractClientPlayer.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }

        playerRenderer.getMainModel().bipedHead.postRender(0.0625F);
        GlStateManager.scale(0.0625, 0.0625, 0.0625);
        GlStateManager.scale(voxelSize, voxelSize, voxelSize);

        boolean tintRed = abstractClientPlayer.hurtTime > 0 || abstractClientPlayer.deathTime > 0;
        settings.k40$getHeadLayers().render(tintRed);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
