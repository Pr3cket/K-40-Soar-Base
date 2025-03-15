package com.lawab1ders.nan7i.kali.module.impl.hud.mcef;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;

@Getter
@AllArgsConstructor
public class McefRenderer {

    private final boolean transparent;
    private final int[] textureID = new int[1];

    public void initialize() {
        textureID[0] = GL11.glGenTextures();
        GlStateManager.bindTexture(textureID[0]);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.bindTexture(0);
    }

    public int getTextureID() {
        return textureID[0];
    }

    protected void cleanup() {
        if (textureID[0] != 0) {
            GL11.glDeleteTextures(textureID[0]);
            textureID[0] = 0;
        }
    }

    protected void onPaint(ByteBuffer buffer, int width, int height) {
        if (textureID[0] == 0) {
            return;
        }

        if (transparent) {
            GlStateManager.enableBlend();
        }

        GlStateManager.bindTexture(textureID[0]);
        GL11.glPixelStoref(GL11.GL_UNPACK_ROW_LENGTH, width);
        GL11.glPixelStoref(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStoref(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer
        );
    }

    protected void onPaint(ByteBuffer buffer, int x, int y, int width, int height) {
        GL11.glTexSubImage2D(
                GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL12.GL_BGRA,
                GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer
        );
    }
}
