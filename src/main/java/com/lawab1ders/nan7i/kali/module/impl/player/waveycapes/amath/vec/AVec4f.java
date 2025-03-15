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

import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.matrix.AMtx4f;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AVec4f {

    private float x, y, z, w;

    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        AVec4f AVec4f = (AVec4f) object;

        if (Float.compare(AVec4f.x, this.x) != 0) {
            return false;
        }

        if (Float.compare(AVec4f.y, this.y) != 0) {
            return false;
        }

        if (Float.compare(AVec4f.z, this.z) != 0) {
            return false;
        }

        return (Float.compare(AVec4f.w, this.w) == 0);
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.x);
        i = 31 * i + Float.floatToIntBits(this.y);
        i = 31 * i + Float.floatToIntBits(this.z);
        i = 31 * i + Float.floatToIntBits(this.w);
        return i;
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public float z() {
        return this.z;
    }

    public float w() {
        return this.w;
    }

    public void set(float f, float g, float h, float i) {
        this.x = f;
        this.y = g;
        this.z = h;
        this.w = i;
    }

    public void add(float f, float g, float h, float i) {
        this.x += f;
        this.y += g;
        this.z += h;
        this.w += i;
    }

    public void transform(AMtx4f AMtx4f) {
        float f = this.x;
        float g = this.y;
        float h = this.z;
        float i = this.w;
        this.x = AMtx4f.m00 * f + AMtx4f.m01 * g + AMtx4f.m02 * h + AMtx4f.m03 * i;
        this.y = AMtx4f.m10 * f + AMtx4f.m11 * g + AMtx4f.m12 * h + AMtx4f.m13 * i;
        this.z = AMtx4f.m20 * f + AMtx4f.m21 * g + AMtx4f.m22 * h + AMtx4f.m23 * i;
        this.w = AMtx4f.m30 * f + AMtx4f.m31 * g + AMtx4f.m32 * h + AMtx4f.m33 * i;
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
}
