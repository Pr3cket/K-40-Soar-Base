package com.lawab1ders.nan7i.kali.module.impl.player.skin3d.opengl;

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

import lombok.Getter;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public final class NativeImage implements AutoCloseable {

    private final Format format;

    @Getter
    private final int width, height;
    private final ByteBuffer buffer;

    public NativeImage(int i, int j) {
        this(Format.RGBA, i, j);
    }

    public NativeImage(Format format, int i, int j) {

        if (i <= 0 || j <= 0) {
            throw new IllegalArgumentException("Invalid texture size: " + i + "x" + j);
        }

        this.format = format;
        this.width = i;
        this.height = j;
        this.buffer = ByteBuffer.allocateDirect(i * j * format.components());
    }

    public void close() {}

    public Format format() {
        return this.format;
    }

    public int getPixelRGBA(int i, int j) {
        int l = (i + j * this.width) * 4;
        return buffer.getInt(l);
    }

    public void setPixelRGBA(int i, int j, int k) {
        int l = (i + j * this.width) * 4;
        buffer.putInt(l, k);
    }

    public byte getLuminanceOrAlpha(int i, int j) {
        int k = (i + j * this.width) * this.format.components() + this.format.luminanceOrAlphaOffset() / 8;
        return buffer.get(k);
    }

    public void downloadTexture(int i, boolean bl) {

        this.format.setPackPixelStoreState();
        GL11.glGetTexImage(3553, i, this.format.glFormat(), 5121, this.buffer);

        if (bl && this.format.hasAlpha()) {
            for (int j = 0; j < getHeight(); j++) {
                for (int k = 0; k < getWidth(); k++) {
                    setPixelRGBA(k, j, getPixelRGBA(k, j) | 255 << this.format.alphaOffset());
                }
            }
        }
    }

    public enum Format {
        RGBA(4, 6408, false, true, 255, 24), RGB(3, 6407, false, false, 255, 255),
        LUMINANCE_ALPHA(2, 33319, true, true, 0, 8), LUMINANCE(1, 6403, true, false, 0, 255);

        final int components;

        private final int glFormat;

        private final boolean hasLuminance;

        private final boolean hasAlpha;

        private final int luminanceOffset;

        private final int alphaOffset;

        Format(int j, int k, boolean bl4, boolean bl5, int o, int p) {
            this.components = j;
            this.glFormat = k;
            this.hasLuminance = bl4;
            this.hasAlpha = bl5;
            this.luminanceOffset = o;
            this.alphaOffset = p;
        }

        public int components() {
            return this.components;
        }

        public void setPackPixelStoreState() {
            GL11.glPixelStorei(3333, components());
        }

        public int glFormat() {
            return this.glFormat;
        }

        public boolean hasAlpha() {
            return this.hasAlpha;
        }

        public int alphaOffset() {
            return this.alphaOffset;
        }

        public int luminanceOrAlphaOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
        }
    }
}
