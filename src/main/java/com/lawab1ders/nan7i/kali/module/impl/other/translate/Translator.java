package com.lawab1ders.nan7i.kali.module.impl.other.translate;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class Translator implements InstanceAccess {

    public static final String AUTO_DETECT = "";
    private static String authCache;
    private static MillisTimer timer;

    private static String auth() throws Exception {

        if (timer == null) {
            timer = new MillisTimer();
        }

        if (timer.delay(300 * 1000) || authCache == null) {

            URL url = new URL("https://edge.microsoft.com/translate/auth");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();

            authCache = content.toString();

            return authCache;
        }

        return authCache;
    }

    public static String translate(String text) throws Exception {

        URL url =
                new URL("https://api.cognitive.microsofttranslator.com/translate?from=" + AUTO_DETECT + "&to=" + lag.getCurrentLanguage().getId() + "&api-version=3.0&includeSentenceLength=true");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("authorization", "Bearer " + auth());

        StringJoiner sj = new StringJoiner(",", "[", "]");
        sj.add("{\"Text\":\"" + text + "\"}");

        String jsonInputString = sj.toString();

        con.setDoOutput(true);
        JsonArray jsonArray = getJsonElements(con, jsonInputString);

        StringBuilder sb = new StringBuilder();
        for (JsonElement json : jsonArray) {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray translations = jsonObject.getAsJsonArray("translations");
            for (JsonElement trans : translations) {
                JsonObject translation = trans.getAsJsonObject();
                sb.append(translation.get("text").getAsString());
                sb.append(",");
            }
        }

        return sb.toString().replaceAll(",$", "");
    }

    private static JsonArray getJsonElements(HttpURLConnection con, String jsonInputString) throws IOException {
        OutputStream os = con.getOutputStream();
        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String responseLine;
        StringBuilder responseContent = new StringBuilder();
        while ((responseLine = br.readLine()) != null) {
            responseContent.append(responseLine.trim());
        }
        br.close();

        JsonParser parser = new JsonParser();
        return parser.parse(responseContent.toString()).getAsJsonArray();
    }
}
