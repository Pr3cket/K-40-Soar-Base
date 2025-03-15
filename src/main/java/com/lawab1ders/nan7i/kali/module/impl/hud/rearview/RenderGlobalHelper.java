package com.lawab1ders.nan7i.kali.module.impl.hud.rearview;

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
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinRenderGlobal;
import net.minecraft.client.renderer.RenderGlobal;

public final class RenderGlobalHelper implements InstanceAccess {

    public final RenderGlobal rg;
    public RenderGlobal orig;
    public boolean fancy_graphics;
    public int ambient_occlusion;

    public RenderGlobalHelper() {
        rg = new RenderGlobal(mc);
        orig = null;
    }

    public void getSettings() {
        fancy_graphics = mc.gameSettings.fancyGraphics;
        ambient_occlusion = mc.gameSettings.ambientOcclusion;
    }

    public boolean settingsChanged() {
        return fancy_graphics != mc.gameSettings.fancyGraphics ||
                ambient_occlusion != mc.gameSettings.ambientOcclusion;
    }

    public void switchTo() {
        if (orig == null)
            orig = mc.renderGlobal;
        if (((IMixinRenderGlobal) orig).k40$getTheWorld() != ((IMixinRenderGlobal) rg).k40$getTheWorld()) {
            rg.setWorldAndLoadRenderers(((IMixinRenderGlobal) orig).k40$getTheWorld());
            getSettings();
        } else if (settingsChanged()) {
            rg.loadRenderers();
            getSettings();
        }
        mc.renderGlobal = rg;
    }

    public void switchFrom() {
        if (orig != null) {
            mc.renderGlobal = orig;
        }
        orig = null;
    }
}