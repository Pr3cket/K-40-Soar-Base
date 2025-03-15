package com.lawab1ders.nan7i.kali.music;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.gui.GuiLogin;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class NecCache implements InstanceAccess {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // 登录网易云账号
    private NecLoginData necLoginData;
    private boolean fetched, read = false;

    // 线程
    private Thread necHookThread;
//    private Thread getRecommendsThread;

    public NecCache() {
        updateNec(false);
    }

    public void updateNec() {
        updateNec(true);
    }

    private void updateNec(boolean post) {

        if (!post) {
            // 加载登录状态
            File file = new File(rootFile, "nec_login_status.json");

            try (FileReader reader = new FileReader(file)) {
                final JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                if (jsonObject.has("userId") && jsonObject.has("cookie")) {
                    necLoginData = new NecLoginData(jsonObject.get("userId").getAsLong(),
                            jsonObject.get("cookie").getAsString());

                    read = true;
                }
            } catch (Exception e) {
                //                e.printStackTrace();
            }
        }

        // 如果上次已经登录，那么就刷新登录
        if (read) {

            necHookThread = new Thread(() -> {
                try {
                    // 登录成功
                    NecAPI.updateUserInfo();

                    if (mc.currentScreen instanceof GuiLogin) {
                        ((GuiLogin) mc.currentScreen).getIntroAnimation().setDirection(Direction.BACKWARDS);
                    }

                    ntf.post(
                            TranslateComponent.i18n("categories.login"),
                            TranslateComponent.i18n("login.noti.login")
                                    + " "
                                    + necLoginData.getNickname(),
                            NotificationType.SUCCESS
                    );

                    fetched = true;
                    read = false;
                } catch (Exception e) {
                    ntf.post(
                            TranslateComponent.i18n("categories.login"),
                            TranslateComponent.i18n("login.noti.fail.login"),
                            NotificationType.ERROR
                    );

                    e.printStackTrace();
                }
            });

            necHookThread.start();
        }

        if (necHookThread != null && necHookThread.isAlive()) {
            return;
        }

        necHookThread = new Thread(() -> {
            // 生成二维码 & 退出登录
            try {
                if (fetched) {
                    NecAPI.logout();
                    ntf.post(
                            TranslateComponent.i18n("categories.login"),
                            TranslateComponent.i18n("login.noti.logout"),
                            NotificationType.SUCCESS
                    );
                }

                if (necLoginData != null) {
                    nvg.getAssetsManager().getGlTextureCache().remove(necLoginData.getTextureId());
                }

                necLoginData = NecAPI.generateQRCode();
            } catch (Exception e) {
                ntf.post(
                        TranslateComponent.i18n("categories.login"),
                        TranslateComponent.i18n(post ? "misc.noti.fail.connect" : "login.noti.fail.generate"),
                        post ? NotificationType.ERROR : NotificationType.WARNING
                );

                e.printStackTrace();
                return;
            }

            if (post && !fetched) {
                ntf.post(
                        TranslateComponent.i18n("categories.login"),
                        TranslateComponent.i18n("login.noti.refresh"),
                        NotificationType.SUCCESS
                );
            }

            fetched = false;

            // 登录状态监听
            try {
                MillisTimer millisTimer = new MillisTimer();

                while (true) {
                    JsonObject loginResponse = NecAPI.getLoginResponse(necLoginData.getKey());
                    necLoginData.setResponse(loginResponse);

                    switch (loginResponse.get("code").getAsInt()) {
                        case 801:
                            // 等待扫描
                            break;

                        case 802:
                            // 等待登录
                            break;

                        case 803:
                            // 登录成功
                            NecAPI.updateUserInfo();

                            if (mc.currentScreen instanceof GuiLogin) {
                                ((GuiLogin) mc.currentScreen).getIntroAnimation().setDirection(Direction.BACKWARDS);
                            }

                            ntf.post(
                                    TranslateComponent.i18n("categories.login"),
                                    TranslateComponent.i18n("login.noti.login")
                                            + " "
                                            + necLoginData.getNickname(),
                                    NotificationType.SUCCESS
                            );

                            fetched = true;
                            return;

                        default:
                            // 二维码过期
                            return;
                    }

                    while (millisTimer.delay(1000, true)) ;
                }
            } catch (Exception e) {
            }
        });

        necHookThread.start();
    }

    public void saveNec() {
        // 保存登录状态
        File file = new File(rootFile, "nec_login_status.json");
        Path path = file.toPath();

        if (!file.exists()) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("userId", necLoginData.getUserId());
            jsonObject.addProperty("cookie", necLoginData.getCookie());

            gson.toJson(jsonObject, JsonObject.class, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void updateRecommends() {
//
//        if (getRecommendsThread != null && getRecommendsThread.isAlive()) {
//            return;
//        }
//
//        getRecommendsThread = new Thread(() -> {
//
//            try {
//                mus.setRecommends(NecAPI.getRecommends());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
}
