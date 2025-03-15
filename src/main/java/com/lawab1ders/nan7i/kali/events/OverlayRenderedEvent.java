package com.lawab1ders.nan7i.kali.events;

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

import com.lawab1ders.nan7i.kali.KaliProcessor;
import com.lawab1ders.nan7i.kali.events.api.AccessEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@Getter
@AllArgsConstructor
public class OverlayRenderedEvent extends AccessEvent {

    private final float partialTicks;

    public void drawItemStack(ItemStack stack, int x, int y) {
        if (KaliProcessor.BLUR_TICK) return;

        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        RenderHelper.disableStandardItemLighting();
    }

    public void drawTexturedModalRect(double x, double y,
                                      double textureX, double textureY,
                                      double width, double height) {

        if (KaliProcessor.BLUR_TICK) return;

        double uvScale = 0.00390625F;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0).tex(textureX * uvScale, (textureY + height) * uvScale).endVertex();
        worldrenderer.pos(x + width, y + height, 0).tex(
                (textureX + width) * uvScale,
                (textureY + height) * uvScale
        ).endVertex();
        worldrenderer.pos(x + width, y, 0).tex((textureX + width) * uvScale, (textureY) * uvScale).endVertex();
        worldrenderer.pos(x, y, 0).tex(textureX * uvScale, textureY * uvScale).endVertex();
        tessellator.draw();
    }

    public static void drawModalRectWithCustomSizedTexture(double x, double y,
                                                           float textureX, float textureY,
                                                           double width, double height,
                                                           double textureWidth, double textureHeight) {
        if (KaliProcessor.BLUR_TICK) return;

        double uvScaleX = 1.0F / textureWidth;
        double uvScaleY = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        // 左下角
        worldRenderer.pos(x, y + height, 0.0)
                     .tex(textureX * uvScaleX, (textureY + height) * uvScaleY).endVertex();
        // 右下角
        worldRenderer.pos(x + width, y + height, 0.0)
                     .tex((textureX + width) * uvScaleX, (textureY + height) * uvScaleY).endVertex();
        // 右上角
        worldRenderer.pos(x + width, y, 0.0)
                     .tex((textureX + width) * uvScaleX, textureY * uvScaleY).endVertex();
        // 左上角
        worldRenderer.pos(x, y, 0.0)
                     .tex(textureX * uvScaleX, textureY * uvScaleY).endVertex();

        tessellator.draw();
    }

    public void drawImage(ResourceLocation res, double x, double y, double width, double height, Color color) {
        if (KaliProcessor.BLUR_TICK) return;

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GL11.glColor4d(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        mc.getTextureManager().bindTexture(res);

        drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GlStateManager.resetColor();
        GlStateManager.disableBlend();
    }

    //---------------------------------------------------------------------------
    // GL
    //---------------------------------------------------------------------------

    public void startScale(float x, float y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-x, -y, 0);
    }

    public void startScale(float x, float y, float width, float height, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((x + (x + width)) / 2, (y + (y + height)) / 2, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-(x + (x + width)) / 2, -(y + (y + height)) / 2, 0);
    }

    public void stopScale() {
        GlStateManager.popMatrix();
    }

    public void startTranslate(float x, float y) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
    }

    public void stopTranslate() {
        GlStateManager.popMatrix();
    }
}
