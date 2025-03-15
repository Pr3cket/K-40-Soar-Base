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

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DensityUtils implements InstanceAccess {

    // 基准密度（Windows 默认基准 DPI）
    private static final float BASE_DENSITY = 96f;

    // 当前窗口密度（动态更新）
    private static float currentDensity = BASE_DENSITY;

    private static final Map<Float, Float> CACHES_2PX = new HashMap<>();
    private static final Map<Float, Float> CACHES_2DP = new HashMap<>();

    public static float dpDisplayWidth = mc.displayWidth, dpDisplayHeight = mc.displayHeight;

    /**
     * 将 dp 转换为像素
     *
     * @param dp 设计稿中的 dp 值
     */
    public static float dp2px(float dp) {
        if (CACHES_2PX.containsKey(dp)) return CACHES_2PX.get(dp);
        return dp * (currentDensity / BASE_DENSITY);
    }

    /**
     * 将像素转换为 dp
     *
     * @param px 像素值
     */
    public static float px2dp(float px) {
        if (CACHES_2DP.containsKey(px)) return CACHES_2DP.get(px);
        return px / (currentDensity / BASE_DENSITY);
    }

    /**
     * 更新窗口密度（需在窗口大小变化时调用）
     */
    public static void updateDensity() {
        CACHES_2PX.clear();
        CACHES_2DP.clear();

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            DisplayMode dm = gd.getDisplayMode();

            // 获取物理屏幕参数
            int physicalWidth = dm.getWidth();
            int physicalDpi = Toolkit.getDefaultToolkit().getScreenResolution();

            // 计算窗口密度（基于宽度比例）
            if (physicalWidth > 0 && mc.displayWidth > 0) {
                currentDensity = (physicalDpi * mc.displayWidth) / (float) physicalWidth;
            }
            else {
                currentDensity = BASE_DENSITY; // 默认值
            }
        } catch (Exception e) {
            currentDensity = BASE_DENSITY; // 异常回退
        }

        dpDisplayWidth = px2dp(mc.displayWidth);
        dpDisplayHeight = px2dp(mc.displayHeight);
    }
}
