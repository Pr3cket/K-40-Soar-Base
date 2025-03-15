package com.lawab1ders.nan7i.kali.gui.clickgui.clickeffect;

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
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClickEffect implements InstanceAccess {

    private final int x, y;
    private final InterpolatableAnimation animation = new EaseBackInAnimation(1.0F, 650, 0.0F);

    public void draw() {
        float value = animation.getValue();

        nvg.setupAndDraw(() -> nvg.drawArc(x, y, value * 8, 0, 360, 2, ColorUtils.applyAlpha(
                col.getCurrentColor().getInterpolateColor(0), (int) (255 - (value * 255))
        )));
    }

    public boolean isDone() {
        return animation.isDone(Direction.FORWARDS);
    }
}
