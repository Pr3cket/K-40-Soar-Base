package com.lawab1ders.nan7i.kali.shader.impl;

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

import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.shader.Shader;
import com.lawab1ders.nan7i.kali.utils.StencilUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;

import static net.minecraft.client.renderer.GlStateManager.bindTexture;

public final class GaussianBlurShader extends Shader {

    public static final int MAX_RADIUS = 8;
    private static final float COMPRESSION = 2f;

    private final FloatBuffer[] buffer = new FloatBuffer[MAX_RADIUS];
    private Framebuffer output = new Framebuffer(mc.displayWidth, mc.displayHeight, true);

    public GaussianBlurShader() throws IOException {
        super("gaussian_blur.frag");

        for (int i = 1; i <= MAX_RADIUS; i++) {
            float[] kernel = new float[i];
            float sigma = i / 2f, kernelSum = 0;

            for (int j = 0; j < i; ++j) {
                float multiplier = j / sigma;
                kernel[j] = 1f / (Math.abs(sigma) * 2.50662827463f) * (float) Math.exp(-0.5 * multiplier * multiplier);
                kernelSum += j > 0 ? kernel[j] * 2 : kernel[0];
            }

            for (int j = 0; j < i; ++j) {
                kernel[j] /= kernelSum;
            }

            this.buffer[i - 1] = BufferUtils.createFloatBuffer(i);
            this.buffer[i - 1].put(kernel);
            this.buffer[i - 1].flip();
        }
    }

    public void blur(List<Runnable> runnables, int radius) {

        if (radius <= 0 || radius > 8) return;

        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        update();
        StencilUtils.initStencilToWrite();

        for (Runnable runnable : runnables) {
            runnable.run();
        }

        StencilUtils.readStencilBuffer(1);
        output.bindFramebuffer(false);

        attach();
        uniformi("inTexture", 0);
        uniformf("texelSize", 1f / mc.displayWidth, 1f / mc.displayHeight);
        uniformf("radius", radius);
        uniformFB("weights", buffer[radius - 1]);

        uniformf("direction", COMPRESSION, 0);
        mc.getFramebuffer().bindFramebufferTexture();
        drawQuads(0, 0, sr.getScaledWidth(), sr.getScaledHeight());
        output.unbindFramebuffer();

        mc.getFramebuffer().bindFramebuffer(false);
        uniformf("direction", 0, COMPRESSION);
        output.bindFramebufferTexture();
        drawQuads(0, 0, sr.getScaledWidth(), sr.getScaledHeight());

        detach();
        bindTexture(0);
        StencilUtils.uninitStencilBuffer();
    }

    private void update() {
        if (mc.displayWidth != output.framebufferWidth || mc.displayHeight != output.framebufferHeight) {
            output.deleteFramebuffer();
            output = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        } else {
            output.framebufferClear();
            output.framebufferClear();
        }
    }
}
