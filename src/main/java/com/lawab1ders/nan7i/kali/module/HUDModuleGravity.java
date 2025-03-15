package com.lawab1ders.nan7i.kali.module;

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

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum HUDModuleGravity {
    TOP(1), BOTTOM(2), LEFT(4), RIGHT(8), CENTER(16);

    /**
     * deprecate reason: 草碧的时候写的，当时糕超了，醒了发现有点脱裤子放屁了
     */
    @Deprecated
    private final int value;

    /**
     * deprecate reason: 草碧的时候写的，当时糕超了，醒了发现有点脱裤子放屁了
     */
    // 判断输入的组合中是否包含当前枚举常量
    @Deprecated
    public boolean isSet(int combinedValue) {
        return (combinedValue & value) != 0;
    }

    /**
     * deprecate reason: 草碧的时候写的，当时糕超了，醒了发现有点脱裤子放屁了
     */
    // 获取输入组合中包含的所有枚举常量
    @Deprecated
    public static List<HUDModuleGravity> getGravities(int combinedValue) {
        List<HUDModuleGravity> gravities = new ArrayList<>();

        for (HUDModuleGravity gravity : values()) {
            if (gravity.isSet(combinedValue)) gravities.add(gravity);
        }

        return gravities;
    }
}
