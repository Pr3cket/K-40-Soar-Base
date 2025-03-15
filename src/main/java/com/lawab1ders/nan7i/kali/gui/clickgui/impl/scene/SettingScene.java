package com.lawab1ders.nan7i.kali.gui.clickgui.impl.scene;

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
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.SettingCategory;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import lombok.Getter;

public class SettingScene implements InstanceAccess {

    private final SettingCategory parent;

    @Getter
    private final String icon;
    private final TranslateComponent nameTranslate;
    private final TranslateComponent descriptionTranslate;

    public SettingScene(SettingCategory parent, String nameTranslate, String descriptionTranslate, String icon) {
        this.parent = parent;
        this.nameTranslate = TranslateComponent.i18n_component(nameTranslate);
        this.descriptionTranslate = TranslateComponent.i18n_component(descriptionTranslate);
        this.icon = icon;
    }

    public String getName() {
        return nameTranslate.getText();
    }

    public String getDescription() {
        return descriptionTranslate.getText();
    }

    public int getX() {
        return parent.getSceneX();
    }

    public int getY() {
        return parent.getSceneY();
    }

    public int getWidth() {
        return parent.getSceneWidth();
    }

    public int getHeight() {
        return parent.getSceneHeight();
    }

    public void drawScreen(int mouseX, int mouseY) { }

    public void mouseClicked(int mouseX, int mouseY) { }

    public void keyTyped(char typedChar, int keyCode) { }
}
