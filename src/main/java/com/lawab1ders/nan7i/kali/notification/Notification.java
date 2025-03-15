package com.lawab1ders.nan7i.kali.notification;

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

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenAlpha;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

@RequiredArgsConstructor
public class Notification implements InstanceAccess {

    @Getter
    private final MillisTimer timer = new MillisTimer();
    private final ScreenAlpha screenAlpha = new ScreenAlpha();
    private final String title;
    private final String message;
    private final NotificationType type;

    @Getter
    private InterpolatableAnimation animation;

    public void draw() {
        float value = animation.getValue();

        screenAlpha.wrap(() -> drawNanoVG(value), value);
    }

    private void drawNanoVG(float value) {

        AccentColor currentColor = col.getCurrentColor();
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        float maxWidth;
        float titleWidth = nvg.getTextWidth(title, 9.6F, Fonts.MEDIUM);
        float messageWidth = nvg.getTextWidth(message, 7.6F, Fonts.REGULAR);

        maxWidth = Math.max(titleWidth, messageWidth) + 31;

        int x = (int) (sr.getScaledWidth() - maxWidth) - 8;
        int y = sr.getScaledHeight() - 29 - 8;

        if (timer.delay(3000)) {
            animation.setDirection(Direction.BACKWARDS);
        }

        nvg.save();
        nvg.translate(160 - (value * 160), 0);

        nvg.drawShadow(x, y, maxWidth, 29, 6);
        nvg.drawGradientRoundedRect(x, y, maxWidth, 29, 6,
                ColorUtils.applyAlpha(currentColor.getColor1(), 220),
                ColorUtils.applyAlpha(currentColor.getColor2(), 220));

        nvg.drawText(type.getIcon(), x + 5, y + 6F, Color.WHITE, 17, Fonts.ICON);
        nvg.drawText(title, x + 26, y + 6F, Color.white, 9.6F, Fonts.MEDIUM);
        nvg.drawText(message, x + 26, y + 17.5F, Color.WHITE, 7.5F, Fonts.REGULAR);

        nvg.restore();
    }

    public void show() {
        animation = new EaseBackInAnimation(1.0F, 300, 0);
        animation.setDirection(Direction.FORWARDS);
        animation.reset();
        timer.reset();
    }

    public boolean isShown() {
        return !animation.isDone(Direction.BACKWARDS);
    }
}
