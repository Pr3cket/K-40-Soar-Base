package com.lawab1ders.nan7i.kali.gui.clickgui;

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
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompTextBox;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.animation.ColorAnimation;
import com.lawab1ders.nan7i.kali.utils.mouse.Scroll;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@Getter
public class Category implements InstanceAccess {

    public final Scroll scroll;

    private final TranslateComponent nameTranslate;
    private final GuiClickMenu parent;
    private final String icon;

    private final Animation textAnimation = new Animation(), categoryAnimation = new Animation();
    private final ColorAnimation textColorAnimation = new ColorAnimation();

    private final boolean needSearch;
    private final CompTextBox searchBox;

    @Setter
    private boolean canClose = true;

    public Category(GuiClickMenu parent, String nameTranslate, String icon, boolean needSearch) {
        this.nameTranslate = TranslateComponent.i18n_component(nameTranslate);
        this.parent = parent;
        this.icon = icon;
        this.scroll = new Scroll();

        this.searchBox = new CompTextBox();
        this.searchBox.setDefaultText(TranslateComponent.i18n_component("gui.text.search"));
        this.searchBox.setIcon(TextureCode.SEARCH);

        this.needSearch = needSearch;
    }

    /**
     * 用于搜索
     *
     * @return 返回为真则不相似，可以 continue;
     */
    public static boolean dissimilar(String s1, String s2) {
        s1 = s1.toLowerCase(Locale.ENGLISH);
        s2 = s2.toLowerCase(Locale.ENGLISH);

        if (s1.length() <= 1) {
            return !s1.contains(s2);
        }

        boolean similar = false;

        for (String a : StringUtils.split(s1)) {
            similar = a.contains(s2) || StringUtils.getLevenshteinDistance(a, s2) <= 1;

            if (similar) break;
        }

        return !similar && !s1.contains(s2) && StringUtils.getLevenshteinDistance(s1, s2) > 1;
    }

    public String getName() {
        return nameTranslate.getText();
    }

    public int getX() {
        return parent.getX() + 32;
    }

    public int getY() {
        return parent.getY() + 31;
    }

    public int getWidth() {
        return parent.getWidth() - 32;
    }

    public int getHeight() {
        return parent.getHeight() - 31;
    }

    //---------------------------------------------------------------------------
    // Events
    //---------------------------------------------------------------------------

    public void initCategory() {}

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {}

    // 仅在左键和右键时调用
    public void mouseClicked(int mouseX, int mouseY) {}

    public void mouseReleased(int mouseX, int mouseY) {}

    public void keyTyped(char typedChar, int keyCode) {}
}
