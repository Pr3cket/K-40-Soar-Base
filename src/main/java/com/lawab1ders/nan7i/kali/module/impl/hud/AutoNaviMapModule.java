package com.lawab1ders.nan7i.kali.module.impl.hud;

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

import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.events.WorldLoadedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.minimap.ChunkAtlas;
import com.lawab1ders.nan7i.kali.module.impl.hud.minimap.ChunkTile;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenStencil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@IModule(
        name = "module.autonavimap",
        description = "module.autonavimap.desc",
        category = EModuleCategory.HUD
)
public class AutoNaviMapModule extends HUDModule {

    public final IntSetting widthSetting = new IntSetting("module.opts.width", 150, 70, 180);
    public final IntSetting heightSetting = new IntSetting("module.opts.height", 70, 70, 180);

    private final ScreenStencil stencil = new ScreenStencil();
    private final ChunkAtlas chunkAtlas = new ChunkAtlas(10);

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        int w = widthSetting.getValue();
        int h = heightSetting.getValue();

        this.setSize(w, h);

        nvg.setupAndDraw(() -> nvg.drawShadow(this.x, this.y, width, height, from(6 * this.scale)));

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        stencil.wrap(() -> drawMap(event), this.x, this.y, width, height, from(6 * this.scale), 1F);
    }

    private void drawMap(OverlayRenderedEvent event) {
        int width = widthSetting.getValue();
        int height = heightSetting.getValue();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        EntityPlayer p = mc.thePlayer;

        double x = lerp(p.prevPosX, p.posX, event.getPartialTicks());
        double z = lerp(p.prevPosZ, p.posZ, event.getPartialTicks());
        double yaw = lerp(p.prevRotationYaw, p.rotationYaw, event.getPartialTicks());

        chunkAtlas.loadChunks((int) x >> 4, (int) z >> 4);

        event.startTranslate(
                this.x + from(width / 2f * this.scale),
                this.y + from(height / 2f * this.scale)
        );

        GL11.glRotated(180 - yaw, 0, 0, 1);

        GlStateManager.color(1F, 1F, 1F);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.bindTexture(chunkAtlas.getTextureHandle());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        double chunkWidth = chunkAtlas.getSpriteWidth();
        double chunkHeight = chunkAtlas.getSpriteHeight();

        for (ChunkTile sprite : chunkAtlas) {

            double minX = chunkAtlas.getSpriteX(sprite.getOffset());
            double minY = chunkAtlas.getSpriteY(sprite.getOffset());

            double maxX = minX + chunkWidth;
            double maxY = minY + chunkHeight;

            double renderX = (sprite.getChunkX() << 4) - x;
            double renderY = (sprite.getChunkZ() << 4) - z;

            worldRenderer.pos(renderX, renderY, 0).tex(minX, minY).endVertex();
            worldRenderer.pos(renderX, renderY + 16, 0).tex(minX, maxY).endVertex();
            worldRenderer.pos(renderX + 16, renderY + 16, 0).tex(maxX, maxY).endVertex();
            worldRenderer.pos(renderX + 16, renderY + 0, 0).tex(maxX, minY).endVertex();
        }

        tessellator.draw();

        event.stopTranslate();
    }

    @SubscribeEvent
    public void onWorldLoaded(WorldLoadedEvent event) {
        chunkAtlas.clear();
    }

    @Override
    public void onEnable() {
        chunkAtlas.clear();
    }

    private double lerp(double prev, double current, float partialTicks) {
        return prev + (current - prev) * partialTicks;
    }
}
