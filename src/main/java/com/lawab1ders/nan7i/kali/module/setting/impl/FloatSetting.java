package com.lawab1ders.nan7i.kali.module.setting.impl;

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

import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;
import lombok.Getter;
import lombok.val;

public class FloatSetting extends ModuleSetting<Float> {

    private final float min, max;

    @Getter
    private final float defValue;
    private float value;

    public FloatSetting(String name, float value, float min, float max) {
        super(name);
        this.min = min;
        this.max = max;
        this.setValue(value);
        defValue = value;
    }

    @Override
    public void reset() {
        setValue(defValue);
    }

    @Override
    public Float getMin() {
        return min;
    }

    @Override
    public Float getMax() {
        return max;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Float value) {
        if (this.value == value) return; // (avoid unnecessary calls)

        val oldValue = this.value;
        this.value = Math.max(Math.min(value, max), min);
        handleListener(oldValue, this.value);
    }
}
