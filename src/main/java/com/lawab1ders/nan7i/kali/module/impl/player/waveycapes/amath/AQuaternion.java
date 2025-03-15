package com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath;

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

import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.vec.AVec3f;

public final class AQuaternion {

    private float i;

    private float j;

    private float k;

    private float r;

    public AQuaternion(AVec3f vector3f, float f, boolean bl) {

        if (bl) {
            f *= 0.017453292F;
        }

        float g = sin(f / 2.0F);
        this.i = vector3f.x() * g;
        this.j = vector3f.y() * g;
        this.k = vector3f.z() * g;
        this.r = cos(f / 2.0F);
    }

    public AQuaternion(AQuaternion quaternion) {
        this.i = quaternion.i;
        this.j = quaternion.j;
        this.k = quaternion.k;
        this.r = quaternion.r;
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        AQuaternion quaternion = (AQuaternion) object;
        if (Float.compare(quaternion.i, this.i) != 0)
            return false;
        if (Float.compare(quaternion.j, this.j) != 0)
            return false;
        if (Float.compare(quaternion.k, this.k) != 0)
            return false;
        return (Float.compare(quaternion.r, this.r) == 0);
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.i);
        i = 31 * i + Float.floatToIntBits(this.j);
        i = 31 * i + Float.floatToIntBits(this.k);
        i = 31 * i + Float.floatToIntBits(this.r);
        return i;
    }

    public String toString() {
        return "Quaternion[" + r() + " + " +
                i() + "i + " +
                j() + "j + " +
                k() + "k]";
    }

    public float i() {
        return this.i;
    }

    public float j() {
        return this.j;
    }

    public float k() {
        return this.k;
    }

    public float r() {
        return this.r;
    }

    public void set(float f, float g, float h, float i) {
        this.i = f;
        this.j = g;
        this.k = h;
        this.r = i;
    }

    private static float cos(float f) {
        return (float) Math.cos(f);
    }

    private static float sin(float f) {
        return (float) Math.sin(f);
    }

    public AQuaternion copy() {
        return new AQuaternion(this);
    }
}
