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
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.SimpleHUDModule;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;

@IModule(
        name = "module.memoryusagedisplay",
        description = "module.memoryusagedisplay.desc",
        category = EModuleCategory.HUD
)
public class MemoryUsageDisplayModule extends SimpleHUDModule {

    public final EnumSetting designSetting = new EnumSetting(
            "module.opts.design",
            "module.opts.design.simple", "module.opts.design.fancy"
    );

    private final Animation animation = new Animation();

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        if (designSetting.getSelectedEntryKey().equals("module.opts.design.fancy")) {
            nvg.setupAndDraw(this::drawNanoVG);
        }
        else super.onOverlayRendered(event);
    }

    private void drawNanoVG() {
        animation.setAnimation(((this.getUsingMemory() / 100F) * 360), 16);

        this.drawBackground(54, 60);
        this.drawCenteredText("Memory", 54 / 2f, 6, 9, Fonts.REGULAR);
        this.drawCenteredText(this.getUsingMemory() + "%", 54 / 2f, 32, 9, Fonts.REGULAR);

        this.drawArc(27, 35.5F, 16.5F, -90, 360, 1.6F, this.getFontColor(120));
        this.drawArc(27, 35.5F, 16.5F, -90, animation.getValue() - 90, 1.6F, this.getFontColor());
    }

    @Override
    public String getText() {
        return "Mem: " + getUsingMemory() + "%";
    }

    @Override
    public String getIcon() {
        return TextureCode.CPU;
    }

    private long getUsingMemory() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) * 100L / runtime.maxMemory();
    }
}
