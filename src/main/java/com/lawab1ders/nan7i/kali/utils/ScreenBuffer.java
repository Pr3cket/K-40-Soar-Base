package com.lawab1ders.nan7i.kali.utils;

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
import org.lwjgl.nanovg.NVGLUFramebuffer;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;
import org.lwjgl3.BufferUtils;

import java.nio.FloatBuffer;

public class ScreenBuffer implements InstanceAccess {

    protected int fbWidth, fbHeight;
    protected NVGLUFramebuffer fb;

    protected void drawFirst(Runnable... task) {
        if (fbWidth != mc.displayWidth || fbHeight != mc.displayHeight) {
            this.close();
        }

        if (fb == null) {
            fbWidth = mc.displayWidth;
            fbHeight = mc.displayHeight;
            fb = NanoVGGL2.nvgluCreateFramebuffer(nvg.getContext(), mc.displayWidth, mc.displayHeight, 0);
        }

        NanoVGGL2.nvgluBindFramebuffer(nvg.getContext(), fb);

        GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);

        FloatBuffer floaty = BufferUtils.createFloatBuffer(16);

        if (task.length == 3 && task[1] != null) {
            task[1].run();
        }

        GL11.glGetFloat(GL11.GL_COLOR_CLEAR_VALUE, floaty);

        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glClearColor(floaty.get(0), floaty.get(1), floaty.get(2), floaty.get(3));

        nvg.setupAndDraw(task[0]);

        if (task.length == 3 && task[2] != null) {
            task[2].run();
        }

        mc.getFramebuffer().bindFramebuffer(true);
    }

    protected void close() {
        if (fb != null) {
            NanoVGGL2.nvgluDeleteFramebuffer(nvg.getContext(), fb);
            fb = null;
        }
    }
}
