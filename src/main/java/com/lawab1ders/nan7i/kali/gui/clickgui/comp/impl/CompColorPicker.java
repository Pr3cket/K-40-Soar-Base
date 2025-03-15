package com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl;

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

import com.lawab1ders.nan7i.kali.gui.clickgui.comp.Comp;
import com.lawab1ders.nan7i.kali.module.setting.impl.ColorSetting;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CompColorPicker extends Comp {

    public final ColorSetting colorSetting;
    private final Animation openAnimation = new Animation();
    private final Animation hueAnimation = new Animation();
    private final Animation saturationAnimation = new Animation();
    private final Animation brightnessAnimation = new Animation();
    private final Animation alphaAnimation = new Animation();
    private final ResourceLocation hue = new ResourceLocation("k40", "textures/misc/hue.png");
    private final ResourceLocation alpha = new ResourceLocation("k40", "textures/misc/alpha.png");

    @Getter
    @Setter
    private boolean open;

    @Getter
    @Setter
    private float scale;
    private boolean hueDragging, sbDragging, alphaDragging;

    public CompColorPicker(ColorSetting setting) {
        super(0, 0);
        this.colorSetting = setting;
        this.scale = 1.0F;
        this.open = false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        openAnimation.setAnimation(open ? 1.1F : 0.0F, 16);

        nvg.save();
        nvg.translate(0, 26 * scale);

        float hueMaxValue = 1.0F;
        float hueMinValue = 0.0F;
        float hueValue = colorSetting.getHue();
        float size = 100 * scale;

        nvg.scale(this.getX(), this.getY(), size, 0, Math.max(0, openAnimation.getValue() - 0.1F));

        float hueValueHeight = (size - (12 * scale)) * (hueValue - hueMinValue) / (hueMaxValue - hueMinValue);

        double hueDiff = Math.min(size, Math.max(0, mouseY - (this.getY() + (28 * scale))));

        hueAnimation.setAnimation(hueValueHeight, 16);

        if (hueDragging) {
            if (hueDiff == 0) {
                colorSetting.setHue(hueMinValue);
            }
            else {
                colorSetting.setHue(
                        (float) MathUtils.roundToPlace(
                                ((hueDiff / size) * (hueMaxValue - hueMinValue) + hueMinValue),
                                2
                        ));
            }
        }

        float sbMaxValue = 1.0F;
        float sbMinValue = 0.0F;
        float saturationValue = colorSetting.getSaturation();
        float brightnessValue = colorSetting.getBrightness();

        float saturationValueSize = (size - (12 * scale)) * (saturationValue - sbMinValue) / (sbMaxValue - sbMinValue);
        float brightnessValueSize = (size - (12 * scale)) * (brightnessValue - sbMinValue) / (sbMaxValue - sbMinValue);

        double brightnessDiff = Math.min(size, Math.max(0, (this.getY() + (30 * scale)) + size - mouseY));
        double saturationDiff = Math.min(size, Math.max(0, mouseX - (this.getX() - (4 * scale))));

        brightnessAnimation.setAnimation(brightnessValueSize, 20);
        saturationAnimation.setAnimation(saturationValueSize, 20);

        if (sbDragging) {
            if (brightnessDiff == 0) {
                colorSetting.setBrightness(sbMinValue);
            }
            else {
                colorSetting.setBrightness((float) MathUtils.roundToPlace(
                        ((brightnessDiff / size) * (sbMaxValue - sbMinValue) + sbMinValue), 2));
            }

            if (saturationDiff == 0) {
                colorSetting.setSaturation(sbMinValue);
            }
            else {
                colorSetting.setSaturation((float) MathUtils.roundToPlace(
                        ((saturationDiff / size) * (sbMaxValue - sbMinValue) + sbMinValue), 2));
            }
        }

        float alphaMaxValue = 255;
        float alphaMinValue = 0;
        float alphaValue = colorSetting.getAlpha();
        float alphaWidth = size + (18 * scale);

        double alphaDiff = Math.min(alphaWidth, Math.max(0, mouseX - (this.getX() - (4 * scale))));

        float alphaValueSize =
                (alphaWidth - (12 * scale)) * (alphaValue - alphaMinValue) / (alphaMaxValue - alphaMinValue);

        alphaAnimation.setAnimation(alphaValueSize, 20);

        if (alphaDragging) {
            if (colorSetting.isShowAlpha()) {
                if (alphaDiff == 0) {
                    colorSetting.setAlpha(0);
                }
                else {
                    colorSetting.setAlpha((int) MathUtils.roundToPlace(
                            ((alphaDiff / alphaWidth) * (alphaMaxValue - alphaMinValue) + alphaMinValue), 2));
                }
            }
            else {
                colorSetting.setAlpha(255);
            }
        }

        nvg.drawHSBBox(
                this.getX(), this.getY(), size, size, 6F * scale, Color.getHSBColor(
                        colorSetting.getHue(), 1,
                        1
                )
        );
        nvg.drawRoundedImage(hue, this.getX() + (106 * scale), this.getY(), 12 * scale, size, 3 * scale);
        nvg.drawArc(
                this.getX() + (112 * scale), this.getY() + hueAnimation.getValue() + (6 * scale), 3 * scale, 0,
                360, 1.2F * scale, Color.WHITE
        );
        nvg.drawArc(
                this.getX() + saturationAnimation.getValue() + (6 * scale),
                this.getY() + size - brightnessAnimation.getValue() - (6 * scale), 3 * scale, 0, 360, 1.2F * scale,
                Color.WHITE
        );

        if (colorSetting.isShowAlpha()) {
            nvg.drawRoundedImage(
                    alpha, this.getX(), this.getY() + (106 * scale), size + (18 * scale), 12 * scale,
                    3 * scale
            );
            nvg.drawAlphaBar(
                    this.getX(), this.getY() + (106 * scale), alphaWidth, 12 * scale, 3 * scale,
                    Color.getHSBColor(colorSetting.getHue(), 1, 1)
            );
            nvg.drawArc(
                    this.getX() + alphaAnimation.getValue() + (6 * scale), this.getY() + (112 * scale), 3 * scale
                    , 0, 360, 1.2F * scale, Color.WHITE
            );
        }

        nvg.restore();

        nvg.drawRoundedRect(
                this.getX() + (106 * scale), this.getY(), 16 * scale, 16 * scale, 4,
                colorSetting.getColor()
        );
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        if (open) {
            float size = 100 * scale;
            float alphaWidth = size + (18 * scale);
            float addY = 26 * scale;
            if (MouseUtils.isInside(
                    mouseX, mouseY, this.getX() + (106 * scale), this.getY() + addY, 12 * scale,
                    size
            )) {
                hueDragging = true;
            }

            if (MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY() + addY, size, size)) {
                sbDragging = true;
            }

            if (MouseUtils.isInside(
                    mouseX, mouseY, this.getX(), this.getY() + (106 * scale) + addY, alphaWidth,
                    12 * scale
            ) && colorSetting.isShowAlpha()) {
                alphaDragging = true;
            }
        }

        if (isInsideOpen(mouseX, mouseY)) {
            open = !open;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        hueDragging = false;
        sbDragging = false;
        alphaDragging = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }

    public boolean isShowAlpha() {
        return colorSetting.isShowAlpha();
    }

    public boolean isInsideOpen(int mouseX, int mouseY) {
        return MouseUtils.isInside(mouseX, mouseY, this.getX() + (106 * scale), this.getY(), 16 * scale, 16 * scale);
    }
}
