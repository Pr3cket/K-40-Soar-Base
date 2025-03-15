package com.lawab1ders.nan7i.kali.utils;

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
import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

@UtilityClass
public class ColorUtils implements InstanceAccess {

    public static Color interpolateColors(int speed, int index, Color start, Color end) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;

        return ColorUtils.interpolateColorHue(start, end, angle / 360f);
    }

    private static Color interpolateColorHue(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        Color resultColor = Color.getHSBColor(
                MathUtils.interpolateFloat(color1HSB[0], color2HSB[0], amount),
                MathUtils.interpolateFloat(color1HSB[1], color2HSB[1], amount),
                MathUtils.interpolateFloat(color1HSB[2], color2HSB[2], amount)
        );

        return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(),
                MathUtils.interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static float getHue(Color color) {
        return rgbToHsb(color)[0];
    }

    public static float getSaturation(Color color) {
        return rgbToHsb(color)[1];
    }

    public static float getBrightness(Color color) {
        return rgbToHsb(color)[2];
    }

    private static float[] rgbToHsb(Color color) {
        float[] hsv = new float[3];

        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);

        return hsv;
    }

    public static void setColor(int color) {
        setColor(color, (float) (color >> 24 & 255) / 255.0F);
    }

    public static void setColor(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        GlStateManager.color(r, g, b, alpha);
    }

    public static void resetColor() {
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    public static Color getColorByInt(int color) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) (color >> 24 & 255) / 255.0F;

        return new Color(r, g, b, a);
    }

    public static Color applyAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static float getAlphaByInt(int color) {
        return (float)(color >> 24 & 255) / 255.0F;
    }
}
