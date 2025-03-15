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

import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.Comp;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.animation.ColorAnimation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class CompButton extends Comp {

    @Getter
    public final BooleanSetting setting;
    private final Animation opacityAnimation = new Animation();
    private final Animation toggleAnimation = new Animation();
    private final ColorAnimation circleAnimation = new ColorAnimation();

    @Getter
    @Setter
    private float scale;

    public CompButton(BooleanSetting setting) {
        super(0, 0);

        this.setting = setting;
        this.scale = 1.0F;
        toggleAnimation.setValue(setting.isActivated() ? 20.5F : 2.5F);
        circleAnimation.setColor(setting.isActivated() ? Color.WHITE : palette.getBackgroundColor(ColorType.DARK));
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        AccentColor accentColor = col.getCurrentColor();

        float x = this.getX();
        float y = this.getY();
        float width = 34 * scale;
        float height = 16 * scale;
        float circle = 11 * scale;
        boolean toggled = setting.isActivated();

        opacityAnimation.setAnimation(toggled ? 1.0F : 0.0F, 14);
        toggleAnimation.setAnimation(toggled ? 20.5F : 2.5F, 14);

        nvg.drawRoundedRect(x, y, width, height, (7 * scale), palette.getBackgroundColor(ColorType.NORMAL));
        nvg.drawGradientRoundedRect(
                x, y, width, height, (7 * scale), ColorUtils.applyAlpha(
                        accentColor.getColor1(),
                        (int) (opacityAnimation.getValue() * 255)
                ), ColorUtils.applyAlpha(
                        accentColor.getColor2(),
                        (int) (opacityAnimation.getValue() * 255)
                )
        );
        nvg.drawRoundedRect(
                x + (toggleAnimation.getValue() * scale), y + (2.5F * scale), circle, circle, circle / 2,
                circleAnimation.getColor(toggled ? Color.WHITE : palette.getBackgroundColor(ColorType.DARK), 16)
        );
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        float x = this.getX();
        float y = this.getY();
        float width = 34 * scale;
        float height = 16 * scale;

        if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height)) {
            setting.toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {}

    @Override
    public void keyTyped(char typedChar, int keyCode) {}
}
