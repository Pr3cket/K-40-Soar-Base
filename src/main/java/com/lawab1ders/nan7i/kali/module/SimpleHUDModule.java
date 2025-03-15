package com.lawab1ders.nan7i.kali.module;

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
import com.lawab1ders.nan7i.kali.nanovg.Fonts;

public abstract class SimpleHUDModule extends HUDModule {

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        nvg.setupAndDraw(() -> {
            float addX = nvg.getTextWidth(getIcon(), 9.5F, Fonts.ICON) + 4;

            this.drawBackground(((nvg.getTextWidth(this.getText(), 9, Fonts.REGULAR) + 10) + addX), 18);
            this.drawText(this.getText(), 5.5F + addX, 5.5F, 9, Fonts.REGULAR);
            this.drawText(getIcon(), 5.5F, 4F, 10.4F, Fonts.ICON);
        });
    }

    public abstract String getText();

    public abstract String getIcon();
}
