package com.lawab1ders.nan7i.kali.music;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lawab1ders.nan7i.kali.InstanceAccess;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class NecAPI implements InstanceAccess {
    private static final String HOST = "https://music.skidder.top";
    private static final Gson GSON = new Gson();

    public static NecLoginData generateQRCode() throws Exception {
        JsonObject keyData = fetchObject("/login/qr/key", "data");
        String key = keyData.get("unikey").getAsString();

        JsonObject codeData = fetchObject("/login/qr/create?key=" + key + "&qrimg=true", "data");
        String codeBase64 = codeData.get("qrimg").getAsString();

        String base64Image = codeBase64.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);

        bis.close();

        return new NecLoginData(key, image);
    }

    public static JsonObject getLoginResponse(String key) throws Exception {
        return fetchObject("/login/qr/check?key=" + key);
    }

    public static void updateUserInfo() throws Exception {
        NecCache necCache = mus.getNecCache();
        NecLoginData user = necCache.getNecLoginData();

        if (user.getResponse() != null) {
            user.setCookie(user.getResponse().get("cookie").getAsString());

            JsonObject profile = fetchObject("/login/status", "data", "profile");
            user.setUserId(profile.get("userId").getAsLong());
        }

        JsonObject profile = fetchObject("/user/detail?uid=" + user.getUserId()).get("profile").getAsJsonObject();
        user.setNickname(profile.get("nickname").getAsString());
        user.setAvatarImage(downloadImage(profile.get("avatarUrl").getAsString()));
    }

    public static void logout() throws Exception {
        fetch("/logout");
    }

    private static JsonObject fetchObject(String api, String... children) throws Exception {
        JsonObject object = fetchObject(api);

        for (String child : children) {
            object = object.get(child).getAsJsonObject();
        }

        return object;
    }

    private static JsonObject fetchObject(String api) throws Exception {
        String fetch = fetch(api);
        return GSON.fromJson(fetch, JsonObject.class);
    }

    private static JsonArray fetchArray(String api, String array) throws Exception {
        String fetch = fetch(api);
        return GSON.fromJson(fetch, JsonObject.class).get(array).getAsJsonArray();
    }

    private static String fetch(String api) throws Exception {
        NecLoginData necLoginData = mus.getNecCache().getNecLoginData();
        return fetch(api, necLoginData != null && necLoginData.getCookie() != null ? necLoginData.getCookie() : "");
    }

    public static String fetch(String api, String cookie) throws Exception {
        URL url = new URL(HOST + wrap(api));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        String body = "cookie=" + cookie;
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    private static String wrap(String api) {
        return api + (api.contains("?") ? "&" : "?") + "timestamp=" + System.currentTimeMillis();
    }

    private static BufferedImage downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl + "?param=300y300");

            try (InputStream inputStream = url.openStream()) {
                BufferedImage image = ImageIO.read(inputStream);

                return image == null ? ImageIO.read(new URL(imageUrl)) : image;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
