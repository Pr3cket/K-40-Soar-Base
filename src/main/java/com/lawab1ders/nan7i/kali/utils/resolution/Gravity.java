package com.lawab1ders.nan7i.kali.utils.resolution;

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

import com.lawab1ders.nan7i.kali.InstanceAccess;
import org.lwjgl.util.vector.Vector2f;

public interface Gravity extends InstanceAccess {

    int LEFT   = 0x01;  // 左对齐
    int TOP    = 0x02;  // 顶部对齐
    int RIGHT  = 0x04;  // 右对齐
    int BOTTOM = 0x08;  // 底部对齐
    int CENTER = 0x10;  // 居中（水平或垂直）

    /**
     * 根据 Gravity 计算子元素在窗口中的坐标
     *
     * @param width    子元素宽度（dp）
     * @param height   子元素高度（dp）
     * @param gravity  对齐方式组合（如 Gravity.LEFT | Gravity.CENTER）
     * @param offsetX  相对于对齐顶点的水平偏移量
     * @param offsetY  相对于对齐顶点的垂直偏移量
     *
     * @return 子元素的坐标 (x, y)
     */
    static Vector2f applyGravity(float width, float height, int gravity, float offsetX, float offsetY) {
        float x = calcPosX(
                DensityUtils.dp2px(width),
                gravity,
                DensityUtils.dp2px(offsetX)
        );
        float y = calcPosY(
                DensityUtils.dp2px(height),
                gravity,
                DensityUtils.dp2px(offsetY)
        );

        return new Vector2f(x, y);
    }

    // 计算水平方向坐标
    static float calcPosX(float width, int gravity, float offsetX) {
        if ((gravity & LEFT) != 0) {
            return offsetX; // 左对齐 + 偏移
        }
        else if ((gravity & RIGHT) != 0) {
            return mc.displayWidth - width + offsetX; // 右对齐 + 偏移
        }
        else if ((gravity & CENTER) != 0) {
            return (mc.displayWidth - width) / 2f + offsetX; // 水平居中 + 偏移
        }
        else {
            return offsetX; // 默认左对齐 + 偏移
        }
    }

    // 计算垂直方向坐标
    static float calcPosY(float height, int gravity, float offsetY) {
        if ((gravity & TOP) != 0) {
            return offsetY; // 顶部对齐 + 偏移
        }
        else if ((gravity & BOTTOM) != 0) {
            return mc.displayHeight - height + offsetY; // 底部对齐 + 偏移
        }
        else if ((gravity & CENTER) != 0) {
            return (mc.displayHeight - height) / 2f + offsetY; // 垂直居中 + 偏移
        }
        else {
            return offsetY; // 默认顶部对齐 + 偏移
        }
    }
}
