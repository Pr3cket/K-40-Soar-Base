package com.lawab1ders.nan7i.kali.color;

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

import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Theme {

    DARK(0, "Dark",
            new Color(19, 19, 20), new Color(34, 35, 39),
            new Color(255, 255, 255), new Color(235, 235, 235), true),

    LIGHT(1, "Light",
            new Color(254, 254, 254), new Color(238, 238, 238),
            new Color(54, 54, 54), new Color(107, 117, 129), false),

    DARK_BLUE(2, "Dark Blue",
            new Color(22, 28, 41), new Color(27, 36, 52),
            new Color(157, 175, 211), new Color(116, 131, 164), true),

    MIDNIGHT(3, "Midnight",
            new Color(47, 54, 61), new Color(36, 41, 46),
            new Color(255, 255, 255), new Color(235, 235, 235), true),

    DARK_PURPLE(4, "Dark Purple",
            new Color(44, 14, 72), new Color(53, 24, 90),
            new Color(234, 226, 252), new Color(194, 186, 212), true),

    SEA(5, "Sea",
            new Color(203, 224, 255), new Color(190, 216, 238),
            new Color(255, 255, 255), new Color(245, 245, 245), false),

    SAKURA(6, "Sakura",
            new Color(255, 191, 178), new Color(255, 223, 226),
            new Color(255, 255, 255), new Color(245, 245, 245), false);

    private final int id;
    private final String name;
    private final Color darkBackgroundColor, normalBackgroundColor;
    private final Color darkFontColor, normalFontColor;

    // 是否黑色调，决定 K 徽标的黑白
    private final boolean darkTint;

    private final Animation animation = new Animation();

    public static Theme getThemeById(int id) {
        // 将 Theme 枚举值转换为 Stream 流，然后筛选出符合条件的 Theme 元素
        Optional<Theme> themeOptional = Stream.of(Theme.values())
                .filter(t -> t.getId() == id)
                .findFirst();

        // 如果找到了符合条件的 Theme 元素，就返回它，否则返回默认的 LIGHT 主题
        return themeOptional.orElse(LIGHT);
    }

    public Color getDarkBackgroundColor(int alpha) {
        return ColorUtils.applyAlpha(darkBackgroundColor, alpha);
    }

    public Color getNormalBackgroundColor(int alpha) {
        return ColorUtils.applyAlpha(normalBackgroundColor, alpha);
    }

    public Color getDarkFontColor(int alpha) {
        return ColorUtils.applyAlpha(darkFontColor, alpha);
    }

    public Color getNormalFontColor(int alpha) {
        return ColorUtils.applyAlpha(normalFontColor, alpha);
    }
}
