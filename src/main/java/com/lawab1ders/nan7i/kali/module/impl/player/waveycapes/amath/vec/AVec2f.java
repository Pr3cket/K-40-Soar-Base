package com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.vec;

/*
 * Copyright (c) 2025 EldoDebug, Nan7.南起
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

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AVec2f {

    public float x, y;

    public AVec2f copy() {
        return new AVec2f(x, y);
    }

    public void copyFrom(AVec2f vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public AVec2f add(AVec2f vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public AVec2f subtract(AVec2f vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    public AVec2f div(float amount) {
        this.x /= amount;
        this.y /= amount;
        return this;
    }

    public AVec2f mul(float amount) {
        this.x *= amount;
        this.y *= amount;
        return this;
    }

    public AVec2f normalize() {
        float f = (float) Math.sqrt(this.x * this.x + this.y * this.y);
        if (f < 1.0E-4F) {
            this.x = 0;
            this.y = 0;
        }
        else {
            this.x /= f;
            this.y /= f;
        }
        return this;
    }

    @Override
    public String toString() {
        return "AVec2f [x=" + x + ", y=" + y + "]";
    }
}
