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
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.gui.clickgui.Category;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompTextBox;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseSmoothStepAnimation;
import com.lawab1ders.nan7i.kali.utils.config.Config;
import com.lawab1ders.nan7i.kali.utils.config.ConfigIcon;
import com.lawab1ders.nan7i.kali.utils.config.ConfigManager;
import com.lawab1ders.nan7i.kali.utils.config.ConfigType;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Objects;

public class ProfileCategory extends Category {

    public static Config currentConfig, settingConfig;
    private final InterpolatableAnimation profileAnimation = new EaseSmoothStepAnimation(1.0, 260);
    private final CompTextBox nameBox = new CompTextBox();
    private final CompTextBox serverIpBox = new CompTextBox();
    private ConfigType currentType = ConfigType.ALL;
    private ConfigIcon currentIcon = ConfigIcon.COMMAND;

    // 保存 & 创建
    private boolean edit;

    public ProfileCategory(GuiClickMenu parent) {
        super(parent, "categories.profile", TextureCode.EDIT, true);
    }

    @Override
    public void initCategory() {
        ConfigManager.update();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        AccentColor accentColor = col.getCurrentColor();

        int offsetX = 0;
        float offsetY = 13;
        int index = 1;
        float scrollValue = scroll.getValue();
        float value = profileAnimation.getValue();

        profileAnimation.setDirection(scroll.isLocked() ? Direction.BACKWARDS : Direction.FORWARDS);

        if (profileAnimation.isDone(Direction.FORWARDS)) {
            this.setCanClose(true);
            nameBox.setText("");
            serverIpBox.setText("");
        }

        nvg.save();
        nvg.translate(-(600 - (value * 600)), 0);

        // Draw profile scene

        nvg.save();
        nvg.translate(0, scrollValue);

        for (ConfigType t : ConfigType.values()) {

            float textWidth = nvg.getTextWidth(t.getNameTranslate().getText(), 9, Fonts.MEDIUM);
            boolean isCurrentCategory = t.equals(currentType);

            t.getBackgroundAnimation().setAnimation(isCurrentCategory ? 1.0F : 0.0F, 16);

            Color defaultColor = palette.getBackgroundColor(ColorType.DARK);
            Color color1 = ColorUtils.applyAlpha(accentColor.getColor1(),
                    (int) (t.getBackgroundAnimation().getValue() * 255));
            Color color2 = ColorUtils.applyAlpha(accentColor.getColor2(),
                    (int) (t.getBackgroundAnimation().getValue() * 255));
            Color textColor = t.getTextColorAnimation().getColor(isCurrentCategory ? Color.WHITE :
                    palette.getFontColor(ColorType.DARK), 20);

            nvg.drawRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6,
                    defaultColor);
            nvg.drawGradientRoundedRect(this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6,
                    color1, color2);

            nvg.drawText(t.getNameTranslate().getText(),
                    this.getX() + 15 + offsetX + ((textWidth + 20) - textWidth) / 2, this.getY() + offsetY + 1.5F,
                    textColor, 9, Fonts.MEDIUM);

            offsetX += (int) (textWidth + 28);
        }

        offsetY = offsetY + 23;

        for (int i = 0; i < ConfigManager.getConfigs().size() + 1; i++) {

            Config c = i != ConfigManager.getConfigs().size() ? ConfigManager.getConfigs().get(i) : new Config();

            if (filter(c)) {
                continue;
            }

            if (offsetY + scrollValue + 45 > 0 && offsetY + scrollValue < this.getHeight()) {
                nvg.drawRoundedRect(this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, 40, 8,
                        palette.getBackgroundColor(ColorType.DARK));

                if (c.getFile() == null) {
                    nvg.drawRoundedRect(this.getX() + 21, this.getY() + offsetY + 6, 28, 28, 6,
                            palette.getBackgroundColor(ColorType.NORMAL));
                } else {
                    nvg.drawRoundedImage(c.getIcon().getIcon(), this.getX() + 21, this.getY() + offsetY + 6, 28, 28, 6);
                }

                // 已加载配置轮廓
                int alpha = (int) (c.getAnimation().getValue() * 255);

                c.getAnimation().setAnimation(c.equals(currentConfig) ? 1.0F : 0.0F, 16);

                nvg.drawGradientOutlineRoundedRect(this.getX() + 21, this.getY() + offsetY + 6, 28, 28, 6,
                        1.6F * c.getAnimation().getValue(), ColorUtils.applyAlpha(accentColor.getColor1(), alpha),
                        ColorUtils.applyAlpha(accentColor.getColor2(), alpha));
                // 已加载配置轮廓

                if (!Objects.equals(c.getName(), "")) {
                    nvg.drawText(
                            nvg.getLimitText(c.getName(), 13, Fonts.MEDIUM, (float) this.getWidth() / 2),
                            this.getX() + 56, this.getY() + offsetY + 15F, palette.getFontColor(ColorType.DARK), 13,
                            Fonts.MEDIUM);
                }

                if (c.getFile() == null) {
                    nvg.drawCenteredText(TextureCode.PLUS, this.getX() + 23 + 10, this.getY() + offsetY + 10,
                            palette.getFontColor(ColorType.DARK), 20, Fonts.ICON);
                } else {
                    Color backgroundColor = palette.getFontColor(ColorType.NORMAL);
                    Color starFullColor = ColorUtils.applyAlpha(backgroundColor,
                            (int) (c.getStarAnimation().getValue() * 255));

                    c.getStarAnimation().setAnimation(c.isFavorite() ? 1.0F : 0.0F, 16);

                    nvg.drawText(TextureCode.TRASH, this.getX() + this.getWidth() - 30 - 9 * 3 - 13 * 2,
                            this.getY() + offsetY + 13.5F, palette.getMaterialRed(), 13, Fonts.ICON);

                    nvg.drawText(TextureCode.STAR, this.getX() + this.getWidth() - 30 - 9 * 2 - 13,
                            this.getY() + offsetY + 13.5F, backgroundColor, 13, Fonts.ICON);
                    nvg.drawText(TextureCode.STAR_FILL, this.getX() + this.getWidth() - 30 - 9 * 2 - 13,
                            this.getY() + offsetY + 13.5F, starFullColor, 13, Fonts.ICON);

                    nvg.drawText(TextureCode.SETTINGS, this.getX() + this.getWidth() - 30 - 9,
                            this.getY() + offsetY + 13.5F, backgroundColor, 13, Fonts.ICON);
                }
            }

            offsetY += 50;
            index++;
        }

        nvg.restore();
        nvg.restore();

        // Draw profile add scene

        nvg.save();
        nvg.translate(value * 600, 0);

        offsetY = 15;
        offsetX = 0;

        nvg.drawRoundedRect(this.getX() + 15, this.getY() + offsetY,
                this.getWidth() - 30, this.getHeight() - 30, 10,
                palette.getBackgroundColor(ColorType.DARK));

        nvg.drawRect(this.getX() + 15, this.getY() + offsetY + 27,
                this.getWidth() - 30, 1,
                palette.getBackgroundColor(ColorType.NORMAL));

        nvg.drawText(TranslateComponent.i18n(edit ? "profile.text.setprofile" : "profile.text.addprofile"),
                this.getX() + 26, this.getY() + offsetY + 9,
                palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);

        nvg.drawText(TranslateComponent.i18n("profile.text.icon"), this.getX() + 30, this.getY() + offsetY + 35,
                palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);

        for (ConfigIcon icon : ConfigIcon.values()) {

            int alpha = (int) (icon.getAnimation().getValue() * 255);

            icon.getAnimation().setAnimation(currentIcon.equals(icon) ? 1.0F : 0.0F, 16);

            nvg.drawRoundedImage(icon.getIcon(), this.getX() + 32 + offsetX, this.getY() + offsetY + 53, 32, 32, 6);

            nvg.drawGradientOutlineRoundedRect(this.getX() + 32 + offsetX, this.getY() + offsetY + 53, 32, 32, 6,
                    1.6F * icon.getAnimation().getValue(), ColorUtils.applyAlpha(accentColor.getColor1(), alpha),
                    ColorUtils.applyAlpha(accentColor.getColor2(), alpha));

            offsetX += 40;
        }

        nvg.drawText(TranslateComponent.i18n("profile.text.name"), this.getX() + 30, this.getY() + offsetY + 96,
                palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);

        nameBox.setPosition(this.getX() + 32, this.getY() + 128, 130, 18);
        nameBox.setDefaultText(TranslateComponent.i18n_component("profile.text.name"));
        nameBox.setBgColor(palette.getBackgroundColor(ColorType.NORMAL));
        nameBox.draw(mouseX, mouseY);

        nvg.drawText(TranslateComponent.i18n("profile.text.autoload"),
                this.getX() + ((float) this.getWidth() / 2),
                this.getY() + offsetY + 95, palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM);

        serverIpBox.setPosition(this.getX() + ((float) this.getWidth() / 2) + 2, this.getY() + 128, 130, 18);
        serverIpBox.setDefaultText(TranslateComponent.i18n_component("profile.text.serverip"));
        serverIpBox.setBgColor(palette.getBackgroundColor(ColorType.NORMAL));
        serverIpBox.draw(mouseX, mouseY);

        nvg.drawRoundedRect(this.getX() + this.getWidth() - 124, this.getY() + this.getHeight() - 44,
                100, 21, 6, palette.getBackgroundColor(ColorType.NORMAL));

        nvg.drawCenteredText(TranslateComponent.i18n(edit ? "profile.text.save" : "profile.text.create"),
                this.getX() + this.getWidth() - 124 + 50, this.getY() + this.getHeight() - 37.5F,
                palette.getFontColor(ColorType.DARK), 10, Fonts.REGULAR);

        nvg.restore();

        scroll.setMaxScroll((index - (index > 5 ? 5.18F : index)) * 50);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        int offsetX = 0;
        float offsetY = 13 + scroll.getValue();

        if (!scroll.isLocked()) {
            for (ConfigType t : ConfigType.values()) {

                float textWidth = nvg.getTextWidth(t.getNameTranslate().getText(), 9, Fonts.MEDIUM);

                if (MouseUtils.isInside(mouseX, mouseY, this.getX() + 15 + offsetX, this.getY() + offsetY - 3,
                        textWidth + 20, 16)) {
                    currentType = t;
                    return;
                }

                offsetX += (int) (textWidth + 28);
            }

            offsetY = offsetY + 23;

            for (int i = 0; i < ConfigManager.getConfigs().size() + 1; i++) {

                Config c = i != ConfigManager.getConfigs().size() ? ConfigManager.getConfigs().get(i) : new Config();

                if (filter(c)) {
                    continue;
                }

                if (MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 30 - 9 * 2 - 13,
                        this.getY() + offsetY + 13.5F, 13, 13) && c.getFile() != null) {
                    c.setFavorite(!c.isFavorite());
                    ConfigManager.refresh(c);

                } else if (MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 30 - 9 * 3 - 13 * 2,
                        this.getY() + offsetY + 13.5F, 13, 13) && c.getFile() != null) {

                    if (c.equals(currentConfig)) {
                        currentConfig = null;
                    }

                    ConfigManager.delete(c, true);

                } else if (MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 30 - 9,
                        this.getY() + offsetY + 13.5F, 13, 13) && c.getFile() != null) {
                    nameBox.setText(c.getName());
                    serverIpBox.setText(c.getServerIp());
                    currentIcon = c.getIcon();

                    scroll.setLocked(true);
                    edit = true;
                    settingConfig = c;
                    this.setCanClose(false);

                } else if (MouseUtils.isInside(mouseX, mouseY, this.getX() + 15, this.getY() + offsetY,
                        this.getWidth() - 30, 40)) {
                    if (c.getFile() == null) {
                        scroll.setLocked(true);
                        edit = false;
                        this.setCanClose(false);
                    } else if (!c.equals(currentConfig)) {

                        if (currentConfig == null) {
                            ConfigManager.save(null, false, "", ConfigIcon.COMMAND, true, false, false);
                        }

                        ConfigManager.load(c, false, true);
                        currentConfig = c;
                    } else {
                        ConfigManager.load(null, true, false);
                        currentConfig = null;
                    }
                }

                offsetY += 50;
            }
        } else {

            offsetY = 15;

            for (ConfigIcon icon : ConfigIcon.values()) {

                if (MouseUtils.isInside(mouseX, mouseY, this.getX() + 32 + offsetX, this.getY() + offsetY + 53, 32,
                        32)) {
                    currentIcon = icon;
                    return;
                }

                offsetX += 40;
            }

            nameBox.mouseClicked(mouseX, mouseY);
            serverIpBox.mouseClicked(mouseX, mouseY);

            if (MouseUtils.isInside(mouseX, mouseY, this.getX() + this.getWidth() - 124,
                    this.getY() + this.getHeight() - 44, 100, 21)) {

                String serverIp = serverIpBox.getText();

                boolean favorite = false;

                if (edit) {
                    favorite = settingConfig.isFavorite();
                    ConfigManager.delete(settingConfig, false);
                }

                boolean has = false;

                for (Config c : ConfigManager.getConfigs()) {
                    if (Objects.equals(c.getName(), nameBox.getText())) {
                        has = true;
                        break;
                    }
                }

                if (has) {
                    ntf.post(
                            TranslateComponent.i18n("categories.profile"),
                            TranslateComponent.i18n("profile.noti.fail"),
                            NotificationType.INFO
                    );
                } else {
                    Config save = ConfigManager.save(nameBox.getText(), edit && favorite, serverIp, currentIcon,
                            false, true, !edit);
                    ConfigManager.load(save, false, false);
                    currentConfig = save;

                    exitProfile();
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

        if (scroll.isLocked()) {

            nameBox.keyTyped(typedChar, keyCode);
            serverIpBox.keyTyped(typedChar, keyCode);

            if (keyCode == Keyboard.KEY_ESCAPE) {
                exitProfile();
            }
        }
    }

    private boolean filter(Config p) {

        if (currentType.equals(ConfigType.FAVORITE) && !p.isFavorite()) {
            return true;
        }

        return !this.getSearchBox().getText().isEmpty() && dissimilar(p.getName(), this.getSearchBox().getText());
    }

    private void exitProfile() {
        scroll.setLocked(false);
    }
}
