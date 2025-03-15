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
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.Comp;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;

import java.awt.*;

public class CompComboBox extends Comp {

    public final EnumSetting setting;
    private final Animation changeAnimation = new Animation();
    private final float width;
    private int changeDirection;

    public CompComboBox(float width, EnumSetting setting) {
        super(0, 0);
        this.width = width;
        this.setting = setting;
        this.changeDirection = 1;
        this.changeAnimation.setValue(1);
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        // 为重置设置添加动画
        if (setting.isReset() && setting.getLastSelected() != 0) {
            changeAnimation.setValue(0);
            changeDirection = 1;
        }

        AccentColor accentColor = col.getCurrentColor();

        changeAnimation.setAnimation(changeDirection, 16);

        nvg.drawGradientRoundedRect(
                this.getX(), this.getY(), width, 16, 4, accentColor.getColor1(),
                accentColor.getColor2()
        );

        nvg.drawCenteredText(
                setting.getSelectedEntryText(),
                this.getX() + (width / 2) + ((changeDirection - changeAnimation.getValue()) * 22), this.getY() + 5F,
                new Color(255, 255, 255, (int) (MathUtils.abs(changeAnimation.getValue() * 255))), 8, Fonts.REGULAR
        );

        nvg.drawText("<", this.getX() + 4, this.getY() + 4.5F, Color.WHITE, 10, Fonts.REGULAR);
        nvg.drawText(">", this.getX() + width - 10, this.getY() + 4.5F, Color.WHITE, 10, Fonts.REGULAR);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), 16, 16)) {
            changeAnimation.setValue(0);
            setting.last();
            changeDirection = 1;
        }
        else if (MouseUtils.isInside(mouseX, mouseY, this.getX() + width - 16, this.getY(), 16, 16)) {
            changeAnimation.setValue(0);
            setting.next();
            changeDirection = -1;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }
}
