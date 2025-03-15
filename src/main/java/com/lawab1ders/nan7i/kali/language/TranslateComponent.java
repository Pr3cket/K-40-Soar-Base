package com.lawab1ders.nan7i.kali.language;

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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;

@Getter @Setter
@RequiredArgsConstructor
public class TranslateComponent implements InstanceAccess {

    private final String key;
    private String text = "i18n.text.none";

    public static TranslateComponent i18n_component(String key) {

        if (LanguageManager.TRANSLATE_TEXTS.containsKey(key)) {
            return LanguageManager.TRANSLATE_TEXTS.get(key);
        }
        else {
            val cmpt = new TranslateComponent(key);

            if (lag != null && lag.getCurrentLanguage().getTranslateMap().containsKey(key)) {
                cmpt.setText(lag.getCurrentLanguage().getTranslateMap().get(key));
            }

            LanguageManager.TRANSLATE_TEXTS.put(key, cmpt);

            return cmpt;
        }
    }

    public static String i18n(String key) {
        return i18n_component(key).getText();
    }
}
