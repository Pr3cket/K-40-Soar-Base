package com.lawab1ders.nan7i.kali.utils.buffers;

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
import com.lawab1ders.nan7i.kali.utils.ScreenBuffer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;

public class ScreenAnimation extends ScreenBuffer {

    public void wrap(Runnable glRender, Runnable task, float x, float y, float width, float height,
                     float animationProgress, float alphaProgress, boolean stencil) {
        drawFirst(task, GlStateManager::enableTexture2D, glRender);

        int factor = KaliAPI.INSTANCE.getScaledResolution().getScaleFactor();

        nvg.setupAndDraw(() -> {
            nvg.setAlpha(Math.min(alphaProgress, 1.0F));
            nvg.scale(x * factor, y * factor, width * factor, height * factor, animationProgress);

            NVGPaint paint = NVGPaint.create();

            NanoVG.nvgBeginPath(nvg.getContext());

            if (stencil) {
                NanoVG.nvgRect(nvg.getContext(), x * factor, y * factor, width * factor, height * factor);
            } else {
                NanoVG.nvgRect(nvg.getContext(), 0, 0, mc.displayWidth, mc.displayHeight);
            }

            NanoVG.nvgFillPaint(nvg.getContext(), NanoVG.nvgImagePattern(nvg.getContext(), 0, 0, mc.displayWidth,
                    mc.displayHeight, 0, fb.image(), 1, paint));
            NanoVG.nvgFill(nvg.getContext());
        });
    }

    public void wrap(Runnable task, float x, float y, float width, float height, float animationProgress,
                     float alphaProgress) {
        wrap(null, task, x, y, width, height, animationProgress, alphaProgress, false);
    }

    public void wrap(Runnable glRender, Runnable task, float x, float y, float width, float height,
                     float animationProgress, float alphaProgress) {
        wrap(glRender, task, x, y, width, height, animationProgress, alphaProgress, false);
    }

    public void wrap(Runnable task, float x, float y, float width, float height, float animationProgress,
                     float alphaProgress, boolean stencil) {
        wrap(null, task, x, y, width, height, animationProgress, alphaProgress, stencil);
    }

    public void wrap(Runnable task, float x, float y, float width, float height, float progress) {
        wrap(null, task, x, y, width, height, progress, progress, false);
    }

    public void wrap(Runnable task, float progress) {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();
        wrap(null, task, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), progress, progress, false);
    }

    public void wrap(Runnable task, float animationProgress, float alphaProgress) {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();
        wrap(null, task, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), animationProgress, alphaProgress, false);
    }
}
