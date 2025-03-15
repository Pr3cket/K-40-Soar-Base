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
import lombok.NoArgsConstructor;

import java.nio.FloatBuffer;

@NoArgsConstructor
public final class AMtx3f {

    private float m00, m01, m02, m10, m11, m12, m20, m21, m22;

    public AMtx3f(AQuaternion quaternion) {
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

    public static AMtx3f createScaleMatrix(float f, float g, float h) {
        AMtx3f AMtx3f = new AMtx3f();
        AMtx3f.m00 = f;
        AMtx3f.m11 = g;
        AMtx3f.m22 = h;
        return AMtx3f;
    }

    public AMtx3f(AMtx3f AMtx3f) {
        this.m00 = AMtx3f.m00;
        this.m01 = AMtx3f.m01;
        this.m02 = AMtx3f.m02;
        this.m10 = AMtx3f.m10;
        this.m11 = AMtx3f.m11;
        this.m12 = AMtx3f.m12;
        this.m20 = AMtx3f.m20;
        this.m21 = AMtx3f.m21;
        this.m22 = AMtx3f.m22;
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        AMtx3f AMtx3f = (AMtx3f) object;
        return (Float.compare(AMtx3f.m00, this.m00) == 0 && Float.compare(AMtx3f.m01, this.m01) == 0
                && Float.compare(AMtx3f.m02, this.m02) == 0 && Float.compare(AMtx3f.m10, this.m10) == 0
                && Float.compare(AMtx3f.m11, this.m11) == 0 && Float.compare(AMtx3f.m12, this.m12) == 0
                && Float.compare(AMtx3f.m20, this.m20) == 0 && Float.compare(AMtx3f.m21, this.m21) == 0
                && Float.compare(AMtx3f.m22, this.m22) == 0);
    }

    public int hashCode() {
        int i = (this.m00 != 0.0F) ? Float.floatToIntBits(this.m00) : 0;
        i = 31 * i + ((this.m01 != 0.0F) ? Float.floatToIntBits(this.m01) : 0);
        i = 31 * i + ((this.m02 != 0.0F) ? Float.floatToIntBits(this.m02) : 0);
        i = 31 * i + ((this.m10 != 0.0F) ? Float.floatToIntBits(this.m10) : 0);
        i = 31 * i + ((this.m11 != 0.0F) ? Float.floatToIntBits(this.m11) : 0);
        i = 31 * i + ((this.m12 != 0.0F) ? Float.floatToIntBits(this.m12) : 0);
        i = 31 * i + ((this.m20 != 0.0F) ? Float.floatToIntBits(this.m20) : 0);
        i = 31 * i + ((this.m21 != 0.0F) ? Float.floatToIntBits(this.m21) : 0);
        i = 31 * i + ((this.m22 != 0.0F) ? Float.floatToIntBits(this.m22) : 0);
        return i;
    }

    private static int bufferIndex(int i, int j) {
        return j * 3 + i;
    }

    public void load(FloatBuffer floatBuffer) {
        this.m00 = floatBuffer.get(bufferIndex(0, 0));
        this.m01 = floatBuffer.get(bufferIndex(0, 1));
        this.m02 = floatBuffer.get(bufferIndex(0, 2));
        this.m10 = floatBuffer.get(bufferIndex(1, 0));
        this.m11 = floatBuffer.get(bufferIndex(1, 1));
        this.m12 = floatBuffer.get(bufferIndex(1, 2));
        this.m20 = floatBuffer.get(bufferIndex(2, 0));
        this.m21 = floatBuffer.get(bufferIndex(2, 1));
        this.m22 = floatBuffer.get(bufferIndex(2, 2));
    }

    public void loadTransposed(FloatBuffer floatBuffer) {
        this.m00 = floatBuffer.get(bufferIndex(0, 0));
        this.m01 = floatBuffer.get(bufferIndex(1, 0));
        this.m02 = floatBuffer.get(bufferIndex(2, 0));
        this.m10 = floatBuffer.get(bufferIndex(0, 1));
        this.m11 = floatBuffer.get(bufferIndex(1, 1));
        this.m12 = floatBuffer.get(bufferIndex(2, 1));
        this.m20 = floatBuffer.get(bufferIndex(0, 2));
        this.m21 = floatBuffer.get(bufferIndex(1, 2));
        this.m22 = floatBuffer.get(bufferIndex(2, 2));
    }

    public void load(FloatBuffer floatBuffer, boolean bl) {
        if (bl) {
            loadTransposed(floatBuffer);
        }
        else {
            load(floatBuffer);
        }
    }

    public void load(AMtx3f AMtx3f) {
        this.m00 = AMtx3f.m00;
        this.m01 = AMtx3f.m01;
        this.m02 = AMtx3f.m02;
        this.m10 = AMtx3f.m10;
        this.m11 = AMtx3f.m11;
        this.m12 = AMtx3f.m12;
        this.m20 = AMtx3f.m20;
        this.m21 = AMtx3f.m21;
        this.m22 = AMtx3f.m22;
    }

    public String toString() {
        return "AMtx3f:\n" +
                this.m00 +
                " " +
                this.m01 +
                " " +
                this.m02 +
                "\n" +
                this.m10 +
                " " +
                this.m11 +
                " " +
                this.m12 +
                "\n" +
                this.m20 +
                " " +
                this.m21 +
                " " +
                this.m22 +
                "\n";
    }

    public void setIdentity() {
        this.m00 = 1.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
    }

    public void set(int i, int j, float f) {
        if (i == 0) {
            if (j == 0) {
                this.m00 = f;
            }
            else if (j == 1) {
                this.m01 = f;
            }
            else {
                this.m02 = f;
            }
        }
        else if (i == 1) {
            if (j == 0) {
                this.m10 = f;
            }
            else if (j == 1) {
                this.m11 = f;
            }
            else {
                this.m12 = f;
            }
        }
        else if (j == 0) {
            this.m20 = f;
        }
        else if (j == 1) {
            this.m21 = f;
        }
        else {
            this.m22 = f;
        }
    }

    public void mul(AMtx3f AMtx3f) {
        float f = this.m00 * AMtx3f.m00 + this.m01 * AMtx3f.m10 + this.m02 * AMtx3f.m20;
        float g = this.m00 * AMtx3f.m01 + this.m01 * AMtx3f.m11 + this.m02 * AMtx3f.m21;
        float h = this.m00 * AMtx3f.m02 + this.m01 * AMtx3f.m12 + this.m02 * AMtx3f.m22;
        float i = this.m10 * AMtx3f.m00 + this.m11 * AMtx3f.m10 + this.m12 * AMtx3f.m20;
        float j = this.m10 * AMtx3f.m01 + this.m11 * AMtx3f.m11 + this.m12 * AMtx3f.m21;
        float k = this.m10 * AMtx3f.m02 + this.m11 * AMtx3f.m12 + this.m12 * AMtx3f.m22;
        float l = this.m20 * AMtx3f.m00 + this.m21 * AMtx3f.m10 + this.m22 * AMtx3f.m20;
        float m = this.m20 * AMtx3f.m01 + this.m21 * AMtx3f.m11 + this.m22 * AMtx3f.m21;
        float n = this.m20 * AMtx3f.m02 + this.m21 * AMtx3f.m12 + this.m22 * AMtx3f.m22;
        this.m00 = f;
        this.m01 = g;
        this.m02 = h;
        this.m10 = i;
        this.m11 = j;
        this.m12 = k;
        this.m20 = l;
        this.m21 = m;
        this.m22 = n;
    }

    public void mul(AQuaternion quaternion) {
        mul(new AMtx3f(quaternion));
    }

    public void mul(float f) {
        this.m00 *= f;
        this.m01 *= f;
        this.m02 *= f;
        this.m10 *= f;
        this.m11 *= f;
        this.m12 *= f;
        this.m20 *= f;
        this.m21 *= f;
        this.m22 *= f;
    }

    public void add(AMtx3f AMtx3f) {
        this.m00 += AMtx3f.m00;
        this.m01 += AMtx3f.m01;
        this.m02 += AMtx3f.m02;
        this.m10 += AMtx3f.m10;
        this.m11 += AMtx3f.m11;
        this.m12 += AMtx3f.m12;
        this.m20 += AMtx3f.m20;
        this.m21 += AMtx3f.m21;
        this.m22 += AMtx3f.m22;
    }

    public AMtx3f copy() {
        return new AMtx3f(this);
    }
}
