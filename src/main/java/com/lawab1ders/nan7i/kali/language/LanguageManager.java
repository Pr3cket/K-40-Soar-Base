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

import lombok.Getter;
import lombok.val;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

@Getter
public class LanguageManager {

    private Language currentLanguage;

    public static final HashMap<String, TranslateComponent> TRANSLATE_TEXTS = new HashMap<>();

    public LanguageManager() {
        setCurrentLanguage(Language.ENGLISH);
    }

    private void loadMap(HashMap<String, String> map, String language) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(LanguageManager.class.getClassLoader().getResourceAsStream("assets/k40/properties/" + language + ".properties")), StandardCharsets.UTF_8))) {

            String s;

            while ((s = reader.readLine()) != null) {

                if (!s.isEmpty() && !s.startsWith("#")) {
                    String[] args = s.split("=");

                    map.put(args[0], args[1]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentLanguage(Language currentLanguage) {
        if (currentLanguage == this.currentLanguage) return;

        this.currentLanguage = currentLanguage;

        val translateMap = currentLanguage.getTranslateMap();

        if (translateMap.isEmpty()) {
            this.loadMap(currentLanguage.getTranslateMap(), currentLanguage.getId());
        }

        for (TranslateComponent text : TRANSLATE_TEXTS.values()) {
            if (translateMap.containsKey(text.getKey())) {
                text.setText(translateMap.get(text.getKey()));
            }
        }
    }
}
