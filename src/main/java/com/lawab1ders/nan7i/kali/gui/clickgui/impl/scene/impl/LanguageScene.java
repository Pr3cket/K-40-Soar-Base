package com.lawab1ders.nan7i.kali.gui.clickgui.impl.scene.impl;

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
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.SettingCategory;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.scene.SettingScene;
import com.lawab1ders.nan7i.kali.language.Language;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;

public class LanguageScene extends SettingScene {

    public LanguageScene(SettingCategory parent) {
        super(parent, "gui.language", "gui.language.desc", TextureCode.TRANSLATE);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {

        AccentColor currentColor = col.getCurrentColor();

        float offsetY = 0;

        for (Language lang : Language.values()) {

            nvg.drawRoundedRect(this.getX(), this.getY() + offsetY, this.getWidth(), 40, 8,
                    palette.getBackgroundColor(ColorType.DARK));
            nvg.drawRoundedImage(lang.getFlag(), this.getX() + 6, this.getY() + offsetY + 6, 3 * 14, 2 * 14, 4);
            nvg.drawText(lang.getName(), this.getX() + 56, this.getY() + offsetY + 15F,
                    palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);

            lang.getAnimation().setAnimation(lang.equals(lag.getCurrentLanguage()) ?
                    1.0F : 0.0F, 16);

            nvg.drawText(TextureCode.CHECK, this.getX() + this.getWidth()
                            // ProfileCategory & EModuleCategory 的值
                            - 12 - (9F / 13 * 16), this.getY() + offsetY + 12,
                    ColorUtils.applyAlpha(currentColor.getInterpolateColor(),
                            (int) (lang.getAnimation().getValue() * 255)), 16, Fonts.ICON);

            offsetY += 50;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        float offsetY = 0;

        for (Language lang : Language.values()) {

            if (MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY() + offsetY, this.getWidth(), 40)) {
                lag.setCurrentLanguage(lang);
                return;
            }

            offsetY += 50;
        }
    }
}
