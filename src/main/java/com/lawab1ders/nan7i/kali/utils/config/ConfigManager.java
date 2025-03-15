package com.lawab1ders.nan7i.kali.utils.config;

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
import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.color.Theme;
import com.lawab1ders.nan7i.kali.cosmetics.Cosmetics;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinMinecraft;
import com.lawab1ders.nan7i.kali.language.Language;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.util.Session;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@UtilityClass
public class ConfigManager implements InstanceAccess {

    @Getter
    private static final ArrayList<Config> configs = new ArrayList<>();

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final File settingsFile = new File(rootFile, "settings.json");
    private static final File mccFile = new File(rootFile, "mcc.json");

    public static void update() {
        FilenameFilter filter = (dir, name) -> name.endsWith("json");
        File[] files = getConfig(null, false).getParentFile().listFiles(filter);

        if (files == null || files.length == 0) {
            configs.clear();
            return;
        }

        for (File file : files) {
            Config c = null;

            // 使用迭代器来遍历查找匹配的元素
            Iterator<Config> iterator = configs.iterator();

            while (iterator.hasNext()) {
                Config config = iterator.next();

                if (config.getFile().getName().equals(file.getName())) {
                    c = config;
                    break;
                }
            }

            if (c != null) {
                if (file.lastModified() != c.getLastModified()) {
                    // 使用迭代器的remove方法安全移除元素
                    iterator.remove();

                    add(file);
                    c.setLastModified(file.lastModified());
                }
            }
            else {
                add(file);
            }
        }

        if (files.length != configs.size()) {
            // 使用迭代器遍历并删除不存在的文件对应的元素

            configs.removeIf(c -> !c.getFile().exists());
        }
    }

    private static void add(File file) {
        try (FileReader reader = new FileReader(getConfig(file.getName().replace(".json", ""), false))) {
            final JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            String serverIp = "";
            boolean favorite = false;
            int icon = 0;

            if (jsonObject.has("favorite")) {
                favorite = jsonObject.get("favorite").getAsBoolean();
            }

            if (jsonObject.has("server_ip")) {
                serverIp = jsonObject.get("server_ip").getAsString();
            }

            if (jsonObject.has("icon")) {
                icon = jsonObject.get("icon").getAsInt();
            }

            configs.add(new Config(file, jsonObject, serverIp, ConfigIcon.getIconById(icon), favorite));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(Config config, boolean noti) {
        boolean delete = getConfig(config.getName(), false).delete();

        if (delete) {
            // 使用迭代器安全移除元素
            Iterator<Config> iterator = configs.iterator();

            while (iterator.hasNext()) {
                Config c = iterator.next();

                if (c == config) {
                    iterator.remove();
                    break;
                }
            }

            if (noti) {
                ntf.post(
                        TranslateComponent.i18n("categories.profile"),
                        TranslateComponent.i18n("profile.noti.delete")
                                + " "
                                + config.getName()
                                + ".json",
                        NotificationType.SUCCESS
                );
            }
        }
        else if (noti) {
            ntf.post(
                    TranslateComponent.i18n("categories.profile"),
                    TranslateComponent.i18n("misc.noti.fail"),
                    NotificationType.ERROR
            );
        }
    }

    public static void load(Config config, boolean current, boolean noti) {

        String n = config == null ? "" : config.getName();

        try (FileReader reader = new FileReader(getConfig(n, current))) {
            final JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            mod.forEach(module -> {
                String name = module.getNameTranslate().getKey();

                if (jsonObject.has(name)) {
                    JsonElement jsonElement = jsonObject.get(name);
                    if (jsonElement.isJsonObject()) module.onLoadConfig(jsonElement.getAsJsonObject());
                }
            });

            if (noti) {
                ntf.post(
                        TranslateComponent.i18n("categories.profile"),
                        TranslateComponent.i18n("profile.noti.load")
                                + " "
                                + n
                                + ".json",
                        NotificationType.SUCCESS
                );
            }
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) e.printStackTrace();

            if (noti) {
                ntf.post(
                        TranslateComponent.i18n("categories.profile"),
                        TranslateComponent.i18n("misc.noti.fail"),
                        NotificationType.ERROR
                );
            }
        }
    }

    public static Config save(String configName, boolean favorite, String serverIp, ConfigIcon icon, boolean current,
                              boolean noti, boolean isCreate) {

        File file = getConfig(configName, current);
        Path path = file.toPath();

        if (!file.exists()) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        try (FileWriter writer = new FileWriter(file)) {

            JsonObject jsonObject = new JsonObject();

            mod.forEach(module -> {
                JsonObject subJsonObject = new JsonObject();
                module.onSaveConfig(subJsonObject, isCreate);
                jsonObject.add(module.getNameTranslate().getKey(), subJsonObject);
            });

            if (!current) {
                jsonObject.addProperty("favorite", favorite);
                jsonObject.addProperty("server_ip", serverIp);
                jsonObject.addProperty("icon", icon.getId());
            }

            gson.toJson(jsonObject, JsonObject.class, writer);

            Config config = new Config(file, jsonObject, serverIp, icon, favorite);

            if (!current) {
                // 使用迭代器添加元素，避免并发修改异常
                Iterator<Config> iterator = configs.iterator();

                while (iterator.hasNext()) {
                    Config c = iterator.next();

                    if (c.getFile().getName().equals(config.getFile().getName())) {
                        // 若已有同名配置，先移除旧的
                        iterator.remove();
                        break;
                    }
                }
                configs.add(config);
            }

            if (noti) {
                ntf.post(
                        TranslateComponent.i18n("categories.profile"),
                        TranslateComponent.i18n(isCreate ? "profile.noti.create" : "profile.noti.save")
                                + " "
                                + configName
                                + ".json",
                        NotificationType.SUCCESS
                );
            }

            return config;
        } catch (Exception e) {
            e.printStackTrace();

            if (noti) {
                ntf.post(
                        TranslateComponent.i18n("categories.profile"),
                        TranslateComponent.i18n("misc.noti.fail"),
                        NotificationType.ERROR
                );
            }
        }

        return null;
    }

    public static void refresh(Config config) {
        try (FileWriter writer = new FileWriter(config.getFile())) {

            JsonObject jsonObject = config.getJsonObject();

            if (jsonObject.has("favorite")) {
                jsonObject.remove("favorite");
                jsonObject.addProperty("favorite", config.isFavorite());
            }

            if (jsonObject.has("server_ip")) {
                jsonObject.remove("server_ip");
                jsonObject.addProperty("server_ip", config.getServerIp());
            }

            if (jsonObject.has("icon")) {
                jsonObject.remove("icon");
                jsonObject.addProperty("icon", config.getIcon().getId());
            }

            gson.toJson(jsonObject, JsonObject.class, writer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getConfig(String configName, boolean current) {
        File configsDir = new File(rootFile, "configs");

        File currPath = new File(rootFile, "default_config.json");
        File path = new File(configsDir, configName + ".json");

        return current ? currPath : path;
    }

    //---------------------------------------------------------------------------
    // Settings
    //---------------------------------------------------------------------------

    public static void loadSettings() {

        try (FileReader reader = new FileReader(settingsFile)) {
            final JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            if (jsonObject.has("cosmetics")) {
                String cosName = jsonObject.get("cosmetics").getAsString();

                if (!Objects.equals(cosName, "none")) {
                    cos.setCurrentCosmetics(
                            cos.stream().filter(cos -> Objects.equals(cos.getName().getKey(), cosName)).findFirst()
                               .get());
                }
            }

            if (jsonObject.has("theme")) {
                col.setTheme(Theme.getThemeById(jsonObject.get("theme").getAsInt()));
            }

            if (jsonObject.has("account_color")) {
                col.setCurrentColor(col.getColorByName(jsonObject.get("account_color").getAsString()));
            }

            if (jsonObject.has("hud_theme_setting")) {
                KaliAPI.INSTANCE.getHudThemeSetting()
                                .setSelectedEntry(jsonObject.get("hud_theme_setting").getAsString());
            }

            if (jsonObject.has("click_gui_key")) {
                KaliAPI.INSTANCE.getClickGuiKeySetting().setKeyCode(jsonObject.get("click_gui_key").getAsInt());
            }

            if (jsonObject.has("language")) {
                lag.setCurrentLanguage(Language.getLanguageById(jsonObject.get("language").getAsString()));
            }

        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) e.printStackTrace();
        }
    }

    public static void saveSettings() {

        Path path = settingsFile.toPath();

        if (!settingsFile.exists()) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        try (FileWriter writer = new FileWriter(settingsFile)) {

            JsonObject jsonObject = new JsonObject();

            Cosmetics currentCosmetics = cos.getCurrentCosmetics();
            jsonObject.addProperty(
                    "cosmetics", currentCosmetics == null ? "none" :
                            currentCosmetics.getName().getKey()
            );

            jsonObject.addProperty("theme", col.getTheme().getId());
            jsonObject.addProperty("account_color", col.getCurrentColor().getName());
            jsonObject.addProperty("hud_theme_setting", KaliAPI.INSTANCE.getHudThemeSetting().getSelectedEntryKey());
            jsonObject.addProperty("click_gui_key", KaliAPI.INSTANCE.getClickGuiKeySetting().getKeyCode());
            jsonObject.addProperty("language", lag.getCurrentLanguage().getId());

            gson.toJson(jsonObject, JsonObject.class, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------
    // MCC
    //---------------------------------------------------------------------------

    public static void loadMcc() {
        if (InstanceAccess.isPremiumAccount()) return;

        try (FileReader reader = new FileReader(mccFile)) {
            final JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            if (jsonObject.has("offline_nickname")) {
                ((IMixinMinecraft) mc).k40$setSession(new Session(
                        jsonObject.get(
                                "offline_nickname").getAsString(), "0", "0", "mojang"
                ));
            }
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) e.printStackTrace();
        }
    }

    public static void saveMcc() {

        Path path = mccFile.toPath();

        if (!mccFile.exists()) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        try (FileWriter writer = new FileWriter(mccFile)) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("offline_nickname", ((IMixinMinecraft) mc).k40$getSession().getUsername());

            gson.toJson(jsonObject, JsonObject.class, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
