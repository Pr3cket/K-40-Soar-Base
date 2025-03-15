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

import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

@Getter
public enum Language {

    ENGLISH("en-US", "language.en-US", new ResourceLocation("k40", "textures/flags/united_states.png")),
    CHINESE("zh-CN", "language.zh-CN", new ResourceLocation("k40", "textures/flags/china.png"));

    private final Animation animation = new Animation();

    private final String id;
    private final TranslateComponent nameTranslate;
    private final ResourceLocation flag;

    private final HashMap<String, String> translateMap = new HashMap<>();

    Language(String id, String nameTranslate, ResourceLocation flag) {
        this.id = id;
        this.nameTranslate = TranslateComponent.i18n_component(nameTranslate);
        this.flag = flag;
    }

    public static Language getLanguageById(String id) {

        for (Language lang : Language.values()) {
            if (lang.getId().equals(id)) {
                return lang;
            }
        }

        return Language.ENGLISH;
    }

    public String getName() {
        return nameTranslate.getText();
    }
}
