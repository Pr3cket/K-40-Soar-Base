package com.lawab1ders.nan7i.kali.utils.animation;

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

import java.awt.*;

public class ColorAnimation {

    private final Animation[] animation = new Animation[3];

    public ColorAnimation() {
        for (int i = 0; i < animation.length; i++) {
            animation[i] = new Animation();
        }
    }

    public Color getColor(Color color) {
        return getColor(color, 12);
    }

    public Color getColor(Color color, int speed) {
        animation[0].setAnimation(color.getRed(), speed);
        animation[1].setAnimation(color.getGreen(), speed);
        animation[2].setAnimation(color.getBlue(), speed);

        return new Color((int) animation[0].getValue(), (int) animation[1].getValue(), (int) animation[2].getValue(),
                color.getAlpha());
    }

    public void setColor(Color color) {
        animation[0].setValue(color.getRed());
        animation[1].setValue(color.getGreen());
        animation[2].setValue(color.getBlue());
    }
}
