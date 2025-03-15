package com.lawab1ders.nan7i.kali.gui.clickgui.impl;

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
import com.lawab1ders.nan7i.kali.cosmetics.Cosmetics;
import com.lawab1ders.nan7i.kali.cosmetics.ECosmeticsCategory;
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.gui.clickgui.Category;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;

import java.awt.*;
import java.util.List;

public class CosmeticsCategory extends Category {

    private ECosmeticsCategory currentCategory = ECosmeticsCategory.ALL;

    public CosmeticsCategory(GuiClickMenu parent) {
        super(parent, "categories.cosmetics", TextureCode.SHOPPING, true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        AccentColor accentColor = col.getCurrentColor();

        int offsetX = 0;
        float offsetY = 13;
        int index = 1;
        int prevIndex = 1;

        nvg.save();
        nvg.translate(0, scroll.getValue());

        for (ECosmeticsCategory c : ECosmeticsCategory.values()) {

            float textWidth = nvg.getTextWidth(c.getName(), 9, Fonts.MEDIUM);
            boolean isCurrentCategory = c.equals(currentCategory);

            c.getBackgroundAnimation().setAnimation(isCurrentCategory ? 1.0F : 0.0F, 16);

            Color defaultColor = palette.getBackgroundColor(ColorType.DARK);
            Color color1 = ColorUtils.applyAlpha(accentColor.getColor1(),
                    (int) (c.getBackgroundAnimation().getValue() * 255));
            Color color2 = ColorUtils.applyAlpha(accentColor.getColor2(),
                    (int) (c.getBackgroundAnimation().getValue() * 255));
            Color textColor = c.getTextColorAnimation().getColor(isCurrentCategory ? Color.WHITE :
                    palette.getFontColor(ColorType.DARK), 20);

            nvg.drawRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6,
                    defaultColor);
            nvg.drawGradientRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6,
                    color1, color2);

            nvg.drawText(c.getName(), this.getX() + 15 + offsetX + ((textWidth + 20) - textWidth) / 2,
                    this.getY() + offsetY + 1.5F, textColor, 9, Fonts.MEDIUM);

            offsetX += (int) (textWidth + 28);
        }

        offsetX = 0;
        offsetY = offsetY + 23;

        List<Cosmetics> cosmeticsList = currentCategory.equals(ECosmeticsCategory.ALL) ? cos : cos.getCosmetics(currentCategory);
        for (Cosmetics cosmetics : cosmeticsList) {

            if (filterCosmetics(cosmetics)) {
                continue;
            }

            cosmetics.getAnimation().setAnimation(cosmetics.equals(cos.getCurrentCosmetics()) ? 1.0F :
                    0.0F, 16);

            nvg.drawGradientRoundedRect(this.getX() + 15 + offsetX - 2, this.getY() + offsetY - 2, 88 + 4, 135 + 4,
                    8.5F,
                    ColorUtils.applyAlpha(accentColor.getColor1(), (int) (cosmetics.getAnimation().getValue() * 255)),
                    ColorUtils.applyAlpha(accentColor.getColor2(), (int) (cosmetics.getAnimation().getValue() * 255)));

            nvg.drawRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY, 88, 135, 8,
                    palette.getBackgroundColor(ColorType.DARK));

            if (cosmetics.getModel() != null) {
                nvg.drawPlayerCape(cosmetics.getModel(), this.getX() + 24 + offsetX, this.getY() + offsetY + 9, 70,
                        105, 8);
            } else {
                nvg.drawRoundedRect(this.getX() + 24 + offsetX, this.getY() + offsetY + 9, 70, 105, 8,
                        palette.getBackgroundColor(ColorType.NORMAL));
            }

            nvg.drawCenteredText(
                    cosmetics.getModel() == null ? TranslateComponent.i18n("cosmetics.none") :
                            nvg.getLimitText(cosmetics.getName().getText(), 10, Fonts.MEDIUM, 75),
                    this.getX() + 15 + offsetX + 44, this.getY() + offsetY + 120.5F,
                    palette.getFontColor(ColorType.DARK), 10, Fonts.MEDIUM);

            offsetX += 100;

            if (index % 4 == 0) {
                offsetX = 0;
                offsetY += 147;
                prevIndex++;
            }

            index++;
        }

        // 上个世纪的问题
        if (cosmeticsList.size() % 4 == 0) {
            offsetY -= 147;
        }

        scroll.setMaxScroll(prevIndex == 1 ? 0 : offsetY - (147 / 1.48F));

        nvg.restore();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        int offsetX = 0;
        float offsetY = 13 + scroll.getValue();
        int index = 1;

        for (ECosmeticsCategory c : ECosmeticsCategory.values()) {

            float textWidth = nvg.getTextWidth(c.getName(), 9, Fonts.MEDIUM);

            if (MouseUtils.isInside(mouseX, mouseY, this.getX() + 15 + offsetX, this.getY() + offsetY - 3,
                    textWidth + 20, 16)) {
                currentCategory = c;
                return;
            }

            offsetX += (int) (textWidth + 28);
        }

        offsetX = 0;
        offsetY = offsetY + 23;

        for (Cosmetics cosmetics : currentCategory.equals(ECosmeticsCategory.ALL) ? cos :
                cos.getCosmetics(currentCategory)) {

            if (filterCosmetics(cosmetics)) {
                continue;
            }

            if (MouseUtils.isInside(mouseX, mouseY, this.getX() + 15 + offsetX, this.getY() + offsetY, 88, 135)) {

                if (cosmetics.equals(cos.getCurrentCosmetics())) {
                    cos.setCurrentCosmetics(null);
                } else {
                    cos.setCurrentCosmetics(cosmetics);
                }

                return;
            }

            offsetX += 100;

            if (index % 4 == 0) {
                offsetX = 0;
                offsetY += 147;
            }

            index++;
        }
    }

    private boolean filterCosmetics(Cosmetics cosmetics) {

        return !this.getSearchBox().getText().isEmpty() && dissimilar(cosmetics.getName().getText(),
                this.getSearchBox().getText());
    }
}
