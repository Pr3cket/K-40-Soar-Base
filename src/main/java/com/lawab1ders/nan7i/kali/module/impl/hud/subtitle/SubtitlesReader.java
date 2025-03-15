package com.lawab1ders.nan7i.kali.module.impl.hud.subtitle;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.language.LanguageManager;
import lombok.Getter;
import net.minecraft.client.resources.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class SubtitlesReader implements InstanceAccess {

    private final Map<String, String> soundsMap = new HashMap<>();

    /**
     * 第一个字符串以语言代码为密钥，双层夹心 Map，里面的第一个字符串代表音频代码，最后一个是翻译文本
     */
    private final Map<String, Map<String, String>> subtitlesMap = new HashMap<>();

    public SubtitlesReader() {
        try {
            JsonObject sounds = readJsonFile("soundid_adapter");

            for (Language language : mc.getLanguageManager().getLanguages()) {
                JsonObject subtitles = readJsonFile("subtitles-924/" + language.getLanguageCode());
                Map<String, String> childMap = new HashMap<>();

                for (Map.Entry<String, JsonElement> entry : subtitles.entrySet()) {
                    if (entry.getValue().isJsonPrimitive()) {
                        JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();

                        if (primitive.isString()) {
                            childMap.put(entry.getKey(), primitive.getAsString());
                        }
                    }
                }

                subtitlesMap.put(language.getLanguageCode(), childMap);
            }

            for (Map.Entry<String, JsonElement> entry : sounds.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) {
                    JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();

                    if (primitive.isString()) {
                        soundsMap.put(entry.getKey(), primitive.getAsString());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject readJsonFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(LanguageManager.class.getClassLoader().getResourceAsStream("assets/k40/" + filePath + ".json")), StandardCharsets.UTF_8));

        StringBuilder jsonContent = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }

        reader.close();
        return new JsonParser().parse(jsonContent.toString()).getAsJsonObject();
    }
}
