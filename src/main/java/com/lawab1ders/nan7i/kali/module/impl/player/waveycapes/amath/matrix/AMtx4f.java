package com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.matrix;

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
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.vec.AVec3f;
import lombok.NoArgsConstructor;

import java.nio.FloatBuffer;

@NoArgsConstructor
public final class AMtx4f {

    public float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

    public AMtx4f(AMtx4f AMtx4f) {
        this.m00 = AMtx4f.m00;
        this.m01 = AMtx4f.m01;
        this.m02 = AMtx4f.m02;
        this.m03 = AMtx4f.m03;
        this.m10 = AMtx4f.m10;
        this.m11 = AMtx4f.m11;
        this.m12 = AMtx4f.m12;
        this.m13 = AMtx4f.m13;
        this.m20 = AMtx4f.m20;
        this.m21 = AMtx4f.m21;
        this.m22 = AMtx4f.m22;
        this.m23 = AMtx4f.m23;
        this.m30 = AMtx4f.m30;
        this.m31 = AMtx4f.m31;
        this.m32 = AMtx4f.m32;
        this.m33 = AMtx4f.m33;
    }

    public AMtx4f(AQuaternion quaternion) {
        float f = quaternion.i();
        float g = quaternion.j();
        float h = quaternion.k();
        float i = quaternion.r();
        float j = 2.0F * f * f;
        float k = 2.0F * g * g;
        float l = 2.0F * h * h;
        this.m00 = 1.0F - k - l;
        this.m11 = 1.0F - l - j;
        this.m22 = 1.0F - j - k;
        this.m33 = 1.0F;
        float m = f * g;
        float n = g * h;
        float o = h * f;
        float p = f * i;
        float q = g * i;
        float r = h * i;
        this.m10 = 2.0F * (m + r);
        this.m01 = 2.0F * (m - r);
        this.m20 = 2.0F * (o - q);
        this.m02 = 2.0F * (o + q);
        this.m21 = 2.0F * (n + p);
        this.m12 = 2.0F * (n - p);
    }

    public boolean isInteger() {
        AMtx4f AMtx4f = new AMtx4f();
        AMtx4f.m30 = 1.0F;
        AMtx4f.m31 = 1.0F;
        AMtx4f.m32 = 1.0F;
        AMtx4f.m33 = 0.0F;
        AMtx4f AMtx4f2 = copy();
        AMtx4f2.multiply(AMtx4f);
        return (isInteger(AMtx4f2.m00 / AMtx4f2.m03) && isInteger(AMtx4f2.m10 / AMtx4f2.m13)
                && isInteger(AMtx4f2.m20 / AMtx4f2.m23) && isInteger(AMtx4f2.m01 / AMtx4f2.m03)
                && isInteger(AMtx4f2.m11 / AMtx4f2.m13) && isInteger(AMtx4f2.m21 / AMtx4f2.m23)
                && isInteger(AMtx4f2.m02 / AMtx4f2.m03) && isInteger(AMtx4f2.m12 / AMtx4f2.m13)
                && isInteger(AMtx4f2.m22 / AMtx4f2.m23));
    }

    private static boolean isInteger(float f) {
        return (Math.abs(f - Math.round(f)) <= 1.0E-5D);
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        AMtx4f AMtx4f = (AMtx4f) object;
        return (Float.compare(AMtx4f.m00, this.m00) == 0 && Float.compare(AMtx4f.m01, this.m01) == 0
                && Float.compare(AMtx4f.m02, this.m02) == 0 && Float.compare(AMtx4f.m03, this.m03) == 0
                && Float.compare(AMtx4f.m10, this.m10) == 0 && Float.compare(AMtx4f.m11, this.m11) == 0
                && Float.compare(AMtx4f.m12, this.m12) == 0 && Float.compare(AMtx4f.m13, this.m13) == 0
                && Float.compare(AMtx4f.m20, this.m20) == 0 && Float.compare(AMtx4f.m21, this.m21) == 0
                && Float.compare(AMtx4f.m22, this.m22) == 0 && Float.compare(AMtx4f.m23, this.m23) == 0
                && Float.compare(AMtx4f.m30, this.m30) == 0 && Float.compare(AMtx4f.m31, this.m31) == 0
                && Float.compare(AMtx4f.m32, this.m32) == 0 && Float.compare(AMtx4f.m33, this.m33) == 0);
    }

    public int hashCode() {
        int i = (this.m00 != 0.0F) ? Float.floatToIntBits(this.m00) : 0;
        i = 31 * i + ((this.m01 != 0.0F) ? Float.floatToIntBits(this.m01) : 0);
        i = 31 * i + ((this.m02 != 0.0F) ? Float.floatToIntBits(this.m02) : 0);
        i = 31 * i + ((this.m03 != 0.0F) ? Float.floatToIntBits(this.m03) : 0);
        i = 31 * i + ((this.m10 != 0.0F) ? Float.floatToIntBits(this.m10) : 0);
        i = 31 * i + ((this.m11 != 0.0F) ? Float.floatToIntBits(this.m11) : 0);
        i = 31 * i + ((this.m12 != 0.0F) ? Float.floatToIntBits(this.m12) : 0);
        i = 31 * i + ((this.m13 != 0.0F) ? Float.floatToIntBits(this.m13) : 0);
        i = 31 * i + ((this.m20 != 0.0F) ? Float.floatToIntBits(this.m20) : 0);
        i = 31 * i + ((this.m21 != 0.0F) ? Float.floatToIntBits(this.m21) : 0);
        i = 31 * i + ((this.m22 != 0.0F) ? Float.floatToIntBits(this.m22) : 0);
        i = 31 * i + ((this.m23 != 0.0F) ? Float.floatToIntBits(this.m23) : 0);
        i = 31 * i + ((this.m30 != 0.0F) ? Float.floatToIntBits(this.m30) : 0);
        i = 31 * i + ((this.m31 != 0.0F) ? Float.floatToIntBits(this.m31) : 0);
        i = 31 * i + ((this.m32 != 0.0F) ? Float.floatToIntBits(this.m32) : 0);
        i = 31 * i + ((this.m33 != 0.0F) ? Float.floatToIntBits(this.m33) : 0);
        return i;
    }

    private static int bufferIndex(int i, int j) {
        return j * 4 + i;
    }

    public void load(FloatBuffer floatBuffer) {
        this.m00 = floatBuffer.get(bufferIndex(0, 0));
        this.m01 = floatBuffer.get(bufferIndex(0, 1));
        this.m02 = floatBuffer.get(bufferIndex(0, 2));
        this.m03 = floatBuffer.get(bufferIndex(0, 3));
        this.m10 = floatBuffer.get(bufferIndex(1, 0));
        this.m11 = floatBuffer.get(bufferIndex(1, 1));
        this.m12 = floatBuffer.get(bufferIndex(1, 2));
        this.m13 = floatBuffer.get(bufferIndex(1, 3));
        this.m20 = floatBuffer.get(bufferIndex(2, 0));
        this.m21 = floatBuffer.get(bufferIndex(2, 1));
        this.m22 = floatBuffer.get(bufferIndex(2, 2));
        this.m23 = floatBuffer.get(bufferIndex(2, 3));
        this.m30 = floatBuffer.get(bufferIndex(3, 0));
        this.m31 = floatBuffer.get(bufferIndex(3, 1));
        this.m32 = floatBuffer.get(bufferIndex(3, 2));
        this.m33 = floatBuffer.get(bufferIndex(3, 3));
    }

    public void loadTransposed(FloatBuffer floatBuffer) {
        this.m00 = floatBuffer.get(bufferIndex(0, 0));
        this.m01 = floatBuffer.get(bufferIndex(1, 0));
        this.m02 = floatBuffer.get(bufferIndex(2, 0));
        this.m03 = floatBuffer.get(bufferIndex(3, 0));
        this.m10 = floatBuffer.get(bufferIndex(0, 1));
        this.m11 = floatBuffer.get(bufferIndex(1, 1));
        this.m12 = floatBuffer.get(bufferIndex(2, 1));
        this.m13 = floatBuffer.get(bufferIndex(3, 1));
        this.m20 = floatBuffer.get(bufferIndex(0, 2));
        this.m21 = floatBuffer.get(bufferIndex(1, 2));
        this.m22 = floatBuffer.get(bufferIndex(2, 2));
        this.m23 = floatBuffer.get(bufferIndex(3, 2));
        this.m30 = floatBuffer.get(bufferIndex(0, 3));
        this.m31 = floatBuffer.get(bufferIndex(1, 3));
        this.m32 = floatBuffer.get(bufferIndex(2, 3));
        this.m33 = floatBuffer.get(bufferIndex(3, 3));
    }

    public void load(FloatBuffer floatBuffer, boolean bl) {
        if (bl) {
            loadTransposed(floatBuffer);
        }
        else {
            load(floatBuffer);
        }
    }

    public void load(AMtx4f AMtx4f) {
        this.m00 = AMtx4f.m00;
        this.m01 = AMtx4f.m01;
        this.m02 = AMtx4f.m02;
        this.m03 = AMtx4f.m03;
        this.m10 = AMtx4f.m10;
        this.m11 = AMtx4f.m11;
        this.m12 = AMtx4f.m12;
        this.m13 = AMtx4f.m13;
        this.m20 = AMtx4f.m20;
        this.m21 = AMtx4f.m21;
        this.m22 = AMtx4f.m22;
        this.m23 = AMtx4f.m23;
        this.m30 = AMtx4f.m30;
        this.m31 = AMtx4f.m31;
        this.m32 = AMtx4f.m32;
        this.m33 = AMtx4f.m33;
    }

    public String toString() {
        return "AMtx4f:\n" +
                this.m00 +
                " " +
                this.m01 +
                " " +
                this.m02 +
                " " +
                this.m03 +
                "\n" +
                this.m10 +
                " " +
                this.m11 +
                " " +
                this.m12 +
                " " +
                this.m13 +
                "\n" +
                this.m20 +
                " " +
                this.m21 +
                " " +
                this.m22 +
                " " +
                this.m23 +
                "\n" +
                this.m30 +
                " " +
                this.m31 +
                " " +
                this.m32 +
                " " +
                this.m33 +
                "\n";
    }

    public void setIdentity() {
        this.m00 = 1.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public void multiply(AMtx4f AMtx4f) {
        float f = this.m00 * AMtx4f.m00 + this.m01 * AMtx4f.m10 + this.m02 * AMtx4f.m20 + this.m03 * AMtx4f.m30;
        float g = this.m00 * AMtx4f.m01 + this.m01 * AMtx4f.m11 + this.m02 * AMtx4f.m21 + this.m03 * AMtx4f.m31;
        float h = this.m00 * AMtx4f.m02 + this.m01 * AMtx4f.m12 + this.m02 * AMtx4f.m22 + this.m03 * AMtx4f.m32;
        float i = this.m00 * AMtx4f.m03 + this.m01 * AMtx4f.m13 + this.m02 * AMtx4f.m23 + this.m03 * AMtx4f.m33;
        float j = this.m10 * AMtx4f.m00 + this.m11 * AMtx4f.m10 + this.m12 * AMtx4f.m20 + this.m13 * AMtx4f.m30;
        float k = this.m10 * AMtx4f.m01 + this.m11 * AMtx4f.m11 + this.m12 * AMtx4f.m21 + this.m13 * AMtx4f.m31;
        float l = this.m10 * AMtx4f.m02 + this.m11 * AMtx4f.m12 + this.m12 * AMtx4f.m22 + this.m13 * AMtx4f.m32;
        float m = this.m10 * AMtx4f.m03 + this.m11 * AMtx4f.m13 + this.m12 * AMtx4f.m23 + this.m13 * AMtx4f.m33;
        float n = this.m20 * AMtx4f.m00 + this.m21 * AMtx4f.m10 + this.m22 * AMtx4f.m20 + this.m23 * AMtx4f.m30;
        float o = this.m20 * AMtx4f.m01 + this.m21 * AMtx4f.m11 + this.m22 * AMtx4f.m21 + this.m23 * AMtx4f.m31;
        float p = this.m20 * AMtx4f.m02 + this.m21 * AMtx4f.m12 + this.m22 * AMtx4f.m22 + this.m23 * AMtx4f.m32;
        float q = this.m20 * AMtx4f.m03 + this.m21 * AMtx4f.m13 + this.m22 * AMtx4f.m23 + this.m23 * AMtx4f.m33;
        float r = this.m30 * AMtx4f.m00 + this.m31 * AMtx4f.m10 + this.m32 * AMtx4f.m20 + this.m33 * AMtx4f.m30;
        float s = this.m30 * AMtx4f.m01 + this.m31 * AMtx4f.m11 + this.m32 * AMtx4f.m21 + this.m33 * AMtx4f.m31;
        float t = this.m30 * AMtx4f.m02 + this.m31 * AMtx4f.m12 + this.m32 * AMtx4f.m22 + this.m33 * AMtx4f.m32;
        float u = this.m30 * AMtx4f.m03 + this.m31 * AMtx4f.m13 + this.m32 * AMtx4f.m23 + this.m33 * AMtx4f.m33;
        this.m00 = f;
        this.m01 = g;
        this.m02 = h;
        this.m03 = i;
        this.m10 = j;
        this.m11 = k;
        this.m12 = l;
        this.m13 = m;
        this.m20 = n;
        this.m21 = o;
        this.m22 = p;
        this.m23 = q;
        this.m30 = r;
        this.m31 = s;
        this.m32 = t;
        this.m33 = u;
    }

    public void multiply(AQuaternion quaternion) {
        multiply(new AMtx4f(quaternion));
    }

    public void add(AMtx4f AMtx4f) {
        this.m00 += AMtx4f.m00;
        this.m01 += AMtx4f.m01;
        this.m02 += AMtx4f.m02;
        this.m03 += AMtx4f.m03;
        this.m10 += AMtx4f.m10;
        this.m11 += AMtx4f.m11;
        this.m12 += AMtx4f.m12;
        this.m13 += AMtx4f.m13;
        this.m20 += AMtx4f.m20;
        this.m21 += AMtx4f.m21;
        this.m22 += AMtx4f.m22;
        this.m23 += AMtx4f.m23;
        this.m30 += AMtx4f.m30;
        this.m31 += AMtx4f.m31;
        this.m32 += AMtx4f.m32;
        this.m33 += AMtx4f.m33;
    }

    public void translate(AVec3f AVec3f) {
        this.m03 += AVec3f.x();
        this.m13 += AVec3f.y();
        this.m23 += AVec3f.z();
    }

    public AMtx4f copy() {
        return new AMtx4f(this);
    }

    public void multiplyWithTranslation(float f, float g, float h) {
        this.m03 = this.m00 * f + this.m01 * g + this.m02 * h + this.m03;
        this.m13 = this.m10 * f + this.m11 * g + this.m12 * h + this.m13;
        this.m23 = this.m20 * f + this.m21 * g + this.m22 * h + this.m23;
        this.m33 = this.m30 * f + this.m31 * g + this.m32 * h + this.m33;
    }

    public static AMtx4f createScaleMatrix(float f, float g, float h) {
        AMtx4f AMtx4f = new AMtx4f();
        AMtx4f.m00 = f;
        AMtx4f.m11 = g;
        AMtx4f.m22 = h;
        AMtx4f.m33 = 1.0F;
        return AMtx4f;
    }
}