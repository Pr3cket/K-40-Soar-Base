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

import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.AQuaternion;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class AVec3f {

    public static AVec3f XP = new AVec3f(1.0F, 0.0F, 0.0F);
    public static AVec3f YP = new AVec3f(0.0F, 1.0F, 0.0F);
    public static AVec3f ZP = new AVec3f(0.0F, 0.0F, 1.0F);

    private float x, y, z;

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        AVec3f AVec3f = (AVec3f) object;
        if (Float.compare(AVec3f.x, this.x) != 0)
            return false;
        if (Float.compare(AVec3f.y, this.y) != 0)
            return false;
        return (Float.compare(AVec3f.z, this.z) == 0);
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.x);
        i = 31 * i + Float.floatToIntBits(this.y);
        i = 31 * i + Float.floatToIntBits(this.z);
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

    public void set(float f, float g, float h) {
        this.x = f;
        this.y = g;
        this.z = h;
    }

    public void load(AVec3f AVec3f) {
        this.x = AVec3f.x;
        this.y = AVec3f.y;
        this.z = AVec3f.z;
    }

    public void add(float f, float g, float h) {
        this.x += f;
        this.y += g;
        this.z += h;
    }

    public void add(AVec3f AVec3f) {
        this.x += AVec3f.x;
        this.y += AVec3f.y;
        this.z += AVec3f.z;
    }

    public AQuaternion rotation(float f) {
        return new AQuaternion(this, f, false);
    }

    public AQuaternion rotationDegrees(float f) {
        return new AQuaternion(this, f, true);
    }

    public AVec3f copy() {
        return new AVec3f(this.x, this.y, this.z);
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}
