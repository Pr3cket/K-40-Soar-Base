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

import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.color.Theme;
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompComboBox;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompKeybind;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.SettingCategory;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.scene.SettingScene;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import com.lawab1ders.nan7i.kali.utils.mouse.Scroll;
import lombok.val;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class AppearanceScene extends SettingScene {

    public final CompComboBox modTheme;
    public final CompKeybind clickGuiKey;

    private final Scroll scroll = new Scroll();

    public AppearanceScene(SettingCategory parent) {
        super(parent, "gui.appearance", "gui.appearance.desc", TextureCode.MONITOR);

        val instance = KaliAPI.INSTANCE;

        // 金正恩
        instance.setHudThemeSetting(new EnumSetting("gui.appearance.opts.hudtheme",
                // 被控“知新卖旧”：比亚迪遭数百车主声讨！多家车企曾遇类似情况
                "gui.appearance.opts.hudtheme.normal",
                "gui.appearance.opts.hudtheme.glow",
                "gui.appearance.opts.hudtheme.outline",
                "gui.appearance.opts.hudtheme.vanilla",
                "gui.appearance.opts.hudtheme.shadow"
        ));

        instance.setClickGuiKeySetting(new KeybindSetting(Keyboard.KEY_RSHIFT));

        modTheme = new CompComboBox(75, KaliAPI.INSTANCE.getHudThemeSetting());
        clickGuiKey = new CompKeybind(75, KaliAPI.INSTANCE.getClickGuiKeySetting());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        AccentColor currentColor = col.getCurrentColor();

        float offsetX = 0;
        int index = 1;

        nvg.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), 76, 6,
                palette.getBackgroundColor(ColorType.DARK));
        nvg.drawText(TranslateComponent.i18n("gui.appearance.opts.theme"), this.getX() + 8, this.getY() + 8,
                palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);

        for (Theme t : Theme.values()) {

            int alpha = (int) (t.getAnimation().getValue() * 255);

            nvg.drawRoundedRect(this.getX() + offsetX + 12, this.getY() + 28, 36, 36, 6,
                    t.getNormalBackgroundColor(255));

            t.getAnimation().setAnimation(t.equals(col.getTheme()) ? 1.0F : 0.0F, 16);

            nvg.drawGradientOutlineRoundedRect(this.getX() + offsetX + 12, this.getY() + 28, 36, 36, 6,
                    1.4F * t.getAnimation().getValue(), ColorUtils.applyAlpha(currentColor.getColor1(), alpha),
                    ColorUtils.applyAlpha(currentColor.getColor2(), alpha));

            offsetX += 46;
        }

        offsetX = 0;

        nvg.drawRoundedRect(this.getX(), this.getY() + 91, this.getWidth(), 72, 6,
                palette.getBackgroundColor(ColorType.DARK));
        nvg.drawText(TranslateComponent.i18n("gui.appearance.opts.accentcolor"), this.getX() + 8, this.getY() + 99,
                palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);

        nvg.save();
        nvg.scissor(this.getX(), this.getY() + 91, this.getWidth(), 72);
        nvg.translate(scroll.getValue(), 0);

        scroll.onScroll();
        scroll.onAnimation();

        for (AccentColor c : col.getColors()) {

            nvg.drawGradientRoundedRect(this.getX() + offsetX + 12, this.getY() + 28 + 91, 32, 32, 6, c.getColor1(),
                    c.getColor2());

            c.getAnimation().setAnimation(c.equals(currentColor) ? 1.0F : 0.0F, 16);

            nvg.drawCenteredText(TextureCode.CHECK, this.getX() + offsetX + 12 + ((float) 32 / 2),
                    this.getY() + 28 + 99, new Color(255, 255, 255, (int) (c.getAnimation().getValue() * 255)), 16,
                    Fonts.ICON);

            offsetX += 40F;
            index++;
        }

        scroll.setMaxScroll((index - 10.3F) * 40);

        nvg.restore();

        nvg.drawRoundedRect(this.getX(), this.getY() + 91 + 87, this.getWidth(), 41, 6,
                palette.getBackgroundColor(ColorType.DARK));

        float textWidth = nvg.getTextWidth(TranslateComponent.i18n("gui.appearance.opts.hudtheme"), 13, Fonts.MEDIUM);
        float lineWidth = nvg.getTextWidth("|", 13, Fonts.MEDIUM);

        nvg.drawText(TranslateComponent.i18n("gui.appearance.opts.hudtheme"), this.getX() + 8, this.getY() + 11.5F + (91 * 2),
                palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);
        nvg.drawText("|", this.getX() + 8 * 2 + textWidth, this.getY() + 11.5F + (91 * 2),
                palette.getFontColor(ColorType.NORMAL), 13, Fonts.MEDIUM);
        nvg.drawText(TranslateComponent.i18n("gui.appearance.opts.clickguikey"), this.getX() + 8 * 3 + textWidth + lineWidth,
                this.getY() + 11.5F + (91 * 2), palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);


        modTheme.setX(this.getX() + this.getWidth() - 87 * 2);
        modTheme.setY(this.getY() + 9.5F + (91 * 2));
        modTheme.draw(mouseX, mouseY);

        clickGuiKey.setX(this.getX() + this.getWidth() - 87);
        clickGuiKey.setY(this.getY() + 9.5F + (91 * 2));
        clickGuiKey.draw(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        float offsetX = 0;

        for (Theme t : Theme.values()) {

            if (MouseUtils.isInside(mouseX, mouseY, this.getX() + offsetX + 12, this.getY() + 28, 36, 36)) {
                col.setTheme(t);
                return;
            }

            offsetX += 46;
        }

        offsetX = scroll.getValue();

        for (AccentColor c : col.getColors()) {

            if (MouseUtils.isInside(mouseX, mouseY, this.getX() + offsetX + 12, this.getY() + 28 + 91, 32, 32)) {
                col.setCurrentColor(c);
                return;
            }

            offsetX += 40F;
        }

        modTheme.mouseClicked(mouseX, mouseY);
        clickGuiKey.mouseClicked(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        clickGuiKey.keyTyped(typedChar, keyCode);
    }
}
