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
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.awt.*;

@Getter
public class ColorSetting extends ModuleSetting<Color> {

    private final Color defColor;
    private final boolean showAlpha;

    @Setter
    private Color color;

    private float hue, saturation, brightness;
    private int alpha;

    public ColorSetting(String text, Color color, boolean showAlpha) {
        super(text);

        this.color = color;
        this.defColor = color;

        this.hue = ColorUtils.getHue(color);
        this.saturation = ColorUtils.getSaturation(color);
        this.brightness = ColorUtils.getBrightness(color);
        this.alpha = color.getAlpha();
        this.showAlpha = showAlpha;
    }

    @Override
    public void reset() {
        this.hue = ColorUtils.getHue(defColor);
        this.saturation = ColorUtils.getSaturation(defColor);
        this.brightness = ColorUtils.getBrightness(defColor);
        this.alpha = defColor.getAlpha();

        notifyColorChange(color, defColor);
        color = defColor;
    }

    public void setHue(float hue) {
        if (this.hue == hue) return;

        this.hue = hue;

        val oldValue = color;
        this.color = ColorUtils.applyAlpha(Color.getHSBColor(hue, saturation, brightness), alpha);
        notifyColorChange(oldValue, color);
    }

    public void setSaturation(float saturation) {
        if (this.saturation == saturation) return;

        this.saturation = saturation;

        val oldValue = color;
        this.color = ColorUtils.applyAlpha(Color.getHSBColor(hue, saturation, brightness), alpha);
        notifyColorChange(oldValue, color);
    }

    public void setBrightness(float brightness) {
        if (this.brightness == brightness) return;

        this.brightness = brightness;

        val oldValue = color;
        this.color = ColorUtils.applyAlpha(Color.getHSBColor(hue, saturation, brightness), alpha);
        notifyColorChange(oldValue, color);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;

        val oldValue = color;
        this.color = ColorUtils.applyAlpha(color, alpha);
        notifyColorChange(oldValue, color);
    }

    private void notifyColorChange(Color oldValue, Color newValue) {
        handleListener(oldValue, newValue);
    }
}
