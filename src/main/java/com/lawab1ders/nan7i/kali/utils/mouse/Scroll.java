package com.lawab1ders.nan7i.kali.utils.mouse;

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

import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Scroll {

    private final float minScroll = 0;
    private final Animation scrollAnimation = new Animation(0.0F);

    @Getter @Setter
    private boolean locked = false;

    @Setter
    private float maxScroll = Float.MAX_VALUE;
    private float rawScroll;

    public void onScroll() {
        if (locked) return;

        rawScroll += (float) Mouse.getDWheel() / (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ? 2 : 4);
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
    }

    public void onAnimation() {
        if (locked) return;

        scrollAnimation.setAnimation(rawScroll, 14);
    }

    public void reset() {
        rawScroll = minScroll;
        scrollAnimation.setValue(minScroll);
    }

    public float getValue() {
        return scrollAnimation.getValue();
    }
}
