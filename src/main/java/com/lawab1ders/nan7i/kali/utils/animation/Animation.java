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

import lombok.Getter;
import lombok.Setter;

public class Animation {

    @Getter @Setter
    private float value;
    private long lastMS;

    public Animation() {
        this.value = 0.0F;
        this.lastMS = System.currentTimeMillis();
    }

    public Animation(float value) {
        this.value = value;
        this.lastMS = System.currentTimeMillis();
    }

    private static float calculateCompensation(final float target, float current, final double speed, long delta) {
        final float diff = current - target;

        double add = (delta * (speed / 50));

        if (diff > speed) {
            if (current - add > target) {
                current -= (float) add;
            } else {
                current = target;
            }
        } else if (diff < -speed) {
            if (current + add < target) {
                current += (float) add;
            } else {
                current = target;
            }
        } else {
            if (Math.abs(current - target) < 0.03) {
                current = target;
            }
        }

        return current;
    }

    public void setAnimation(final float value, double speed) {

        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;

        double deltaValue = 0.0;

        if (speed > 28) {
            speed = 28;
        }

        if (speed != 0.0) {
            deltaValue = Math.abs(value - this.value) * 0.35f / (10.0 / speed);
        }

        this.value = calculateCompensation(value, this.value, deltaValue, delta);
    }
}