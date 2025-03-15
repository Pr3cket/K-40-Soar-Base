package com.lawab1ders.nan7i.kali.color.palette;

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
import com.lawab1ders.nan7i.kali.color.Theme;
import com.lawab1ders.nan7i.kali.utils.animation.ColorAnimation;

import java.awt.*;

public class ColorPalette implements InstanceAccess {

    private final ColorAnimation[] backgroundColorAnimations = new ColorAnimation[3];
    private final ColorAnimation[] fontColorAnimations = new ColorAnimation[3];

    public ColorPalette() {
        for (int i = 0; i < backgroundColorAnimations.length; i++) {
            backgroundColorAnimations[i] = new ColorAnimation();
        }

        for (int i = 0; i < fontColorAnimations.length; i++) {
            fontColorAnimations[i] = new ColorAnimation();
        }
    }

    public Color getBackgroundColor(ColorType type) {
        return getBackgroundColor(type, 255);
    }

    public Color getBackgroundColor(ColorType type, int alpha) {
        return backgroundColorAnimations[type.getIndex()].getColor(getRawBackgroundColor(type, alpha));
    }

    private Color getRawBackgroundColor(ColorType type, int alpha) {
        Theme theme = getTheme();

        switch (type) {
            case DARK:
                return theme.getDarkBackgroundColor(alpha);

            case NORMAL:
                return theme.getNormalBackgroundColor(alpha);

            default:
                return new Color(255, 0, 0, alpha);
        }
    }

    public Color getFontColor(ColorType type) {
        return getFontColor(type, 255);
    }

    public Color getFontColor(ColorType type, int alpha) {
        return fontColorAnimations[type.getIndex()].getColor(getRawFontColor(type, alpha));
    }

    private Color getRawFontColor(ColorType type, int alpha) {
        Theme theme = getTheme();

        switch (type) {
            case DARK:
                return theme.getDarkFontColor(alpha);

            case NORMAL:
                return theme.getNormalFontColor(alpha);

            default:
                return new Color(255, 0, 0, alpha);
        }
    }

    private Theme getTheme() {
        return col.getTheme();
    }

    public Color getMaterialRed() {
        return getMaterialRed(255);
    }

    public Color getMaterialRed(int alpha) {
        return new Color(232, 38, 52, alpha);
    }
}
