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
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.Comp;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.CompBoxes;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.*;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.*;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseSmoothStepAnimation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import com.lawab1ders.nan7i.kali.utils.mouse.Scroll;
import lombok.Setter;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class ModuleCategory extends Category {

    private final Scroll settingScroll = new Scroll();
    private final InterpolatableAnimation settingAnimation = new EaseSmoothStepAnimation(1.0, 260);
    private final ArrayList<ModuleSetting> comps = new ArrayList<>();
    private EModuleCategory currentCategory = EModuleCategory.ALL;

    @Setter
    private Module settingModule;

    public ModuleCategory(GuiClickMenu parent) {
        super(parent, "categories.module", TextureCode.ARCHIVE, true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        AccentColor accentColor = col.getCurrentColor();

        int offsetX = 0;
        float offsetY = 13;
        int index = 1;
        float scrollValue = scroll.getValue();
        float value = settingAnimation.getValue();

        settingAnimation.setDirection(scroll.isLocked() ? Direction.BACKWARDS : Direction.FORWARDS);

        if (settingAnimation.isDone(Direction.FORWARDS)) {
            this.setCanClose(true);
            settingModule = null;
        }

        nvg.save();
        nvg.translate(-(600 - (value * 600)), 0);

        // Draw mod scene

        nvg.save();
        nvg.translate(0, scrollValue);

        for (EModuleCategory c : EModuleCategory.values()) {

            float textWidth = nvg.getTextWidth(c.getName(), 9, Fonts.MEDIUM);
            boolean isCurrentCategory = c.equals(currentCategory);

            c.getBackgroundAnimation().setAnimation(isCurrentCategory ? 1.0F : 0.0F, 16);

            Color defaultColor = palette.getBackgroundColor(ColorType.DARK);
            Color color1 = ColorUtils.applyAlpha(
                    accentColor.getColor1(),
                    (int) (c.getBackgroundAnimation().getValue() * 255)
            );
            Color color2 = ColorUtils.applyAlpha(
                    accentColor.getColor2(),
                    (int) (c.getBackgroundAnimation().getValue() * 255)
            );
            Color textColor = c.getTextColorAnimation().getColor(
                    isCurrentCategory ? Color.WHITE :
                            palette.getFontColor(ColorType.DARK), 20
            );

            nvg.drawRoundedRect(
                    this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6,
                    defaultColor
            );
            nvg.drawGradientRoundedRect(
                    this.getX() + 15 + offsetX, this.getY() + offsetY - 3, textWidth + 20, 16, 6,
                    color1, color2
            );

            nvg.drawText(
                    c.getName(), this.getX() + 15 + offsetX + ((textWidth + 20) - textWidth) / 2,
                    this.getY() + offsetY + 1.5F, textColor, 9, Fonts.MEDIUM
            );

            offsetX += (int) (textWidth + 28);
        }

        offsetY = offsetY + 23;

        for (Module m : currentCategory.equals(EModuleCategory.ALL) ? mod : mod.getModules(currentCategory)) {

            if (filterMod(m)) {
                continue;
            }

            if (offsetY + scrollValue + 45 > 0 && offsetY + scrollValue < this.getHeight()) {

                nvg.drawRoundedRect(
                        this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, 40, 8,
                        palette.getBackgroundColor(ColorType.DARK)
                );
                nvg.drawRoundedRect(
                        this.getX() + 21, this.getY() + offsetY + 6, 28, 28, 6,
                        palette.getBackgroundColor(ColorType.NORMAL)
                );
                nvg.drawText(
                        m.getName(), this.getX() + 56, this.getY() + offsetY + 15F,
                        palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM
                );
                nvg.drawText(
                        m.getDescription(),
                        this.getX() + 56 + (nvg.getTextWidth(m.getName(), 13, Fonts.MEDIUM)) + 5,
                        this.getY() + offsetY + 16, palette.getFontColor(ColorType.NORMAL), 9, Fonts.REGULAR
                );

                m.getAnimation().setAnimation(m.isActivated() ? 1.0F : 0.0F, 16);

                nvg.save();
                nvg.scale(this.getX() + 21, this.getY() + offsetY + 6, 28, 28, m.getAnimation().getValue());

                nvg.drawGradientRoundedRect(
                        this.getX() + 21, this.getY() + offsetY + 6, 28, 28, 6,
                        ColorUtils.applyAlpha(accentColor.getColor1(), (int) (m.getAnimation().getValue() * 255)),
                        ColorUtils.applyAlpha(accentColor.getColor2(), (int) (m.getAnimation().getValue() * 255))
                );

                nvg.restore();

                if (!m.getSettings().isEmpty()) {
                    nvg.drawText(
                            TextureCode.SETTINGS, this.getX() + this.getWidth() - 30 - 9,
                            this.getY() + offsetY + 13.5F, palette.getFontColor(ColorType.NORMAL), 13, Fonts.ICON
                    );
                }
            }

            index++;
            offsetY += 50;
        }

        nvg.restore();
        nvg.restore();

        //Draw mod setting scene

        nvg.save();
        nvg.translate(value * 600, 0);

        if (settingModule != null) {

            int setIndex = 0;

            settingScroll.onScroll();
            settingScroll.onAnimation();

            offsetY = 15;
            offsetX = 0;

            nvg.save();

            nvg.drawRoundedRect(
                    this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, this.getHeight() - 30,
                    10, palette.getBackgroundColor(ColorType.DARK)
            );
            nvg.drawRect(
                    this.getX() + 15, this.getY() + offsetY + 27, this.getWidth() - 30, 1,
                    palette.getBackgroundColor(ColorType.NORMAL)
            );
            nvg.drawText(
                    settingModule.getName(), this.getX() + 26, this.getY() + offsetY + 9,
                    palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM
            );
            nvg.drawText(
                    TextureCode.REFRESH, this.getX() + this.getWidth() - 39, this.getY() + offsetY + 7.5F,
                    palette.getFontColor(ColorType.DARK), 13, Fonts.ICON
            );

            offsetY = 44;

            nvg.scissor(this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, this.getHeight() - 59);
            nvg.translate(0, settingScroll.getValue());

            for (ModuleSetting s : comps) {

                s.openAnimation.setAnimation(s.openY, 16);

                nvg.drawText(
                        s.setting.getName(), this.getX() + offsetX + 26,
                        this.getY() + offsetY + 15F + s.openAnimation.getValue(),
                        palette.getFontColor(ColorType.DARK), 10, Fonts.MEDIUM
                );

                if (s.comp instanceof CompButton) {

                    CompButton toggleButton = (CompButton) s.comp;

                    toggleButton.setX(this.getX() + offsetX + 168);
                    toggleButton.setY(this.getY() + offsetY + 12 + s.openAnimation.getValue());
                    toggleButton.setScale(0.85F);
                }

                if (s.comp instanceof CompSlider) {

                    CompSlider slider = (CompSlider) s.comp;

                    slider.setX(this.getX() + offsetX + 122);
                    slider.setY(this.getY() + offsetY + 17 + s.openAnimation.getValue());
                    slider.setWidth(75);
                }

                if (s.comp instanceof CompComboBox) {

                    CompComboBox comboBox = (CompComboBox) s.comp;

                    comboBox.setX(this.getX() + offsetX + 122);
                    comboBox.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
                }

                if (s.comp instanceof CompKeybind) {

                    CompKeybind keybind = (CompKeybind) s.comp;

                    keybind.setX(this.getX() + offsetX + 122);
                    keybind.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
                }

                if (s.comp instanceof CompTextBox) {

                    CompTextBox textBox = (CompTextBox) s.comp;

                    textBox.setX(this.getX() + offsetX + 122);
                    textBox.setY(this.getY() + offsetY + 11 + s.openAnimation.getValue());
                    textBox.setWidth(75);
                    textBox.setHeight(16);
                }

                if (s.comp instanceof CompColorPicker) {

                    CompColorPicker picker = (CompColorPicker) s.comp;

                    picker.setX(this.getX() + offsetX + 98);
                    picker.setY(this.getY() + offsetY + 12.5F + s.openAnimation.getValue());
                    picker.setScale(0.8F);
                }

                s.comp.draw(mouseX, (int) (mouseY - settingScroll.getValue()));

                offsetX += 194;
                setIndex++;

                if (setIndex % 2 == 0) {
                    offsetY += 29;
                    offsetX = 0;
                }
            }

            nvg.restore();

            settingScroll.setMaxScroll(this.getModuleSettingHeight());
        }

        nvg.restore();

        scroll.setMaxScroll((index - (index > 5 ? 5.18F : index)) * 50);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        int offsetX = 0;
        float offsetY = 13 + scroll.getValue();

        if (!scroll.isLocked()) {
            for (EModuleCategory c : EModuleCategory.values()) {

                float textWidth = nvg.getTextWidth(c.getName(), 9, Fonts.MEDIUM);

                if (MouseUtils.isInside(
                        mouseX, mouseY, this.getX() + 15 + offsetX, this.getY() + offsetY - 3,
                        textWidth + 20, 16
                )) {
                    currentCategory = c;
                    return;
                }

                offsetX += (int) (textWidth + 28);
            }

            offsetY = offsetY + 23;

            for (Module m : currentCategory.equals(EModuleCategory.ALL) ? mod : mod.getModules(currentCategory)) {

                if (filterMod(m)) {
                    continue;
                }

                if (!m.getSettings().isEmpty() && MouseUtils.isInside(
                        mouseX, mouseY,
                        this.getX() + this.getWidth() - 30 - 9, this.getY() + offsetY + 13.5F, 13, 13
                )) {
                    openModuleSetting(m);
                    return;
                }
                else if (MouseUtils.isInside(
                        mouseX, mouseY, this.getX() + 15, this.getY() + offsetY,
                        this.getWidth() - 30, 40
                )) {
                    m.toggle();
                    return;
                }

                offsetY += 50;
            }
        }
        else if (settingAnimation.isDone(Direction.BACKWARDS)) {

            for (ModuleSetting s : comps) {

                s.comp.mouseClicked(mouseX, (int) (mouseY - settingScroll.getValue()));

                if (s.comp instanceof CompColorPicker) {

                    CompColorPicker picker = (CompColorPicker) s.comp;
                    int openIndex = 1;

                    if (!picker.isInsideOpen(mouseX, (int) (mouseY - settingScroll.getValue()))) {
                        continue;
                    }

                    for (int i = 0; i < comps.size(); i++) {

                        if ((openIndex * 2) + (comps.indexOf(s)) < comps.size()) {

                            ModuleSetting s2 = comps.get((openIndex * 2) + (comps.indexOf(s)));
                            int add = picker.isShowAlpha() ? 100 : 85;

                            s2.openY += picker.isOpen() ? add : -add;
                        }

                        openIndex++;
                    }
                }
            }

            if (MouseUtils.isInside(
                    mouseX, mouseY, this.getX() + this.getWidth() - 41, this.getY() + 15 + 6F, 16,
                    16
            )) {

                for (ModuleSetting s : comps) {
                    s.setting.reset();
                }

//                ProfileCategory.currentConfig = null;

                ntf.post(
                        TranslateComponent.i18n("categories.module"),
                        TranslateComponent.i18n("module.noti.reset"),
                        NotificationType.SUCCESS
                );
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {

        for (ModuleSetting s : comps) {
            s.comp.mouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        CompBoxes.wasResetKeybinding = false;

        for (ModuleSetting s : comps) {
            s.comp.keyTyped(typedChar, keyCode);
        }

        if (keyCode == Keyboard.KEY_ESCAPE && !CompBoxes.wasResetKeybinding) {
            scroll.setLocked(false);
        }
    }

    private boolean filterMod(Module m) {

        return !this.getSearchBox().getText().isEmpty()
                // 比较模块的名称和键
                && dissimilar(m.getName(), this.getSearchBox().getText())
                && dissimilar(m.getNameKey(), this.getSearchBox().getText());
    }

    private int getModuleSettingHeight() {

        int oddOutput = 0;
        int evenOutput = 0;
        int oddTotal = 0;
        int evenTotal = 0;

        for (int i = 0; i < comps.size(); i++) {

            if (MathUtils.isOdd(i + 1)) {
                oddOutput += 29;
            }
            else {
                evenOutput += 29;
            }

            ModuleSetting s = comps.get(i);
            if (s.comp instanceof CompColorPicker) {
                CompColorPicker picker = (CompColorPicker) s.comp;
                if (picker.isOpen()) {
                    int add = picker.isShowAlpha() ? 100 : 85;
                    if (MathUtils.isOdd(i + 1)) {
                        oddTotal += add;
                    }
                    else {
                        evenTotal += add;
                    }
                }
            }
        }

        int output = Math.max(oddOutput, evenOutput) + Math.max(oddTotal, evenTotal);

        return Math.max(0, output - (this.getHeight() - 72));
    }

    /**
     * 打开指定模块的设置面板
     *
     * @param module 要打开设置的模块
     */
    public void openModuleSetting(Module module) {
        if (module == null || module.getSettings().isEmpty()) {
            return;
        }

        ArrayList<com.lawab1ders.nan7i.kali.module.setting.ModuleSetting> settings = module.getSettings();
        int setIndex = 0;
        int offsetX = 0;
        int offsetY = 44;

        comps.clear();

        for (com.lawab1ders.nan7i.kali.module.setting.ModuleSetting s : settings) {

            if (s instanceof BooleanSetting) {

                BooleanSetting bSetting = (BooleanSetting) s;

                CompButton toggleButton = new CompButton(bSetting);

                toggleButton.setX(this.getX() + offsetX + 168);
                toggleButton.setY(this.getY() + offsetY + 8);
                toggleButton.setScale(0.85F);

                comps.add(new ModuleSetting(s, toggleButton));
            }
            else if (s instanceof IntSetting) {

                IntSetting nSetting = (IntSetting) s;

                CompSlider<Integer> slider = new CompSlider<>(nSetting);

                slider.setX(this.getX() + offsetX + 122);
                slider.setY(this.getY() + offsetY + 13);
                slider.setWidth(75);

                comps.add(new ModuleSetting(s, slider));
            }
            else if (s instanceof FloatSetting) {

                FloatSetting nSetting = (FloatSetting) s;

                CompSlider<Float> slider = new CompSlider<>(nSetting);

                slider.setX(this.getX() + offsetX + 122);
                slider.setY(this.getY() + offsetY + 13);
                slider.setWidth(75);

                comps.add(new ModuleSetting(s, slider));
            }
            else if (s instanceof EnumSetting) {

                EnumSetting cSetting = (EnumSetting) s;

                CompComboBox comboBox = new CompComboBox(75, cSetting);

                comboBox.setX(this.getX() + offsetX + 122);
                comboBox.setY(this.getY() + offsetY + 11);

                comps.add(new ModuleSetting(s, comboBox));
            }
            else if (s instanceof KeybindSetting) {

                KeybindSetting kSetting = (KeybindSetting) s;
                CompKeybind keybind = new CompKeybind(75, kSetting);

                keybind.setX(this.getX() + offsetX + 122);
                keybind.setY(this.getY() + offsetY + 7);

                comps.add(new ModuleSetting(s, keybind));
            }
            else if (s instanceof TextSetting) {

                TextSetting tSetting = (TextSetting) s;

                CompTextBox textBox = new CompTextBox(tSetting);
                textBox.setDefaultText(
                        TranslateComponent.i18n_component(tSetting.isRandom() ? "gui.text.random" : "gui.text.type"));
                textBox.setText(tSetting.getText());

                textBox.setX(this.getX() + offsetX + 122);
                textBox.setY(this.getY() + offsetY + 7);
                textBox.setWidth(75);
                textBox.setHeight(16);

                tSetting.setBox(textBox);

                comps.add(new ModuleSetting(s, textBox));
            }
            else if (s instanceof ColorSetting) {

                ColorSetting cSetting = (ColorSetting) s;
                CompColorPicker picker = new CompColorPicker(cSetting);

                picker.setX(this.getX() + offsetX + 98);
                picker.setY(this.getY() + offsetY + 8.5F);
                picker.setScale(0.8F);

                comps.add(new ModuleSetting(s, picker));
            }

            offsetX += 194;
            setIndex++;

            if (setIndex % 2 == 0) {
                offsetY += 29;
                offsetX = 0;
            }
        }

        settingModule = module;
        module.onOpenSettings();

        scroll.setLocked(true);
        this.setCanClose(false);
    }

    private static class ModuleSetting {

        private final Animation openAnimation = new Animation();

        private final com.lawab1ders.nan7i.kali.module.setting.ModuleSetting setting;
        private final Comp comp;
        private float openY;

        public ModuleSetting(com.lawab1ders.nan7i.kali.module.setting.ModuleSetting setting, Comp comp) {
            this.setting = setting;
            this.comp = comp;
            this.openY = 0;
        }
    }
}
