package com.lawab1ders.nan7i.kali.module.setting.impl;

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

import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompTextBox;
import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
public class TextSetting extends ModuleSetting<String> {

    private final String defText;
    private String text;

    @Setter
    private boolean isRandom = false;

    @Setter
    private CompTextBox box;

    public TextSetting(String name, String text) {
        super(name);
        this.text = defText = text;
    }

    @Override
    public void reset() {
        setText(defText);
    }

    public void setTextSilently(String text) {
        this.text = text;
    }

    public void setText(String text) {
        if (this.text.equals(text)) return;

        val oldValue = this.text;
        this.text = text;
        handleListener(oldValue, this.text);

        // Update the text box.
        if (box == null) return;
        box.setText(text);
    }

    /**
     * Warning: please check what you need to do before using this method,
     * choose getText() or getFinalText() to get the text.
     *
     * @return The text that will be displayed in the setting.
     */
    @Deprecated
    @SuppressWarnings("ALL")
    public String 建设人民当家做主的社会主义国家实现社会公平正义是全体中国人民的共同价值追求() {
        return getText();
    }
}
