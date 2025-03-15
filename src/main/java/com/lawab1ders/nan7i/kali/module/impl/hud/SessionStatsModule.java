package com.lawab1ders.nan7i.kali.module.impl.hud;

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
import com.lawab1ders.nan7i.kali.events.JoinServerEvent;
import com.lawab1ders.nan7i.kali.events.LeaveServerEvent;
import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.events.cancelable.PacketReceivedEvent;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.setting.impl.TextSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.val;
import lombok.var;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@IModule(
        name = "module.sessionstats",
        description = "module.sessionstats.desc",
        category = EModuleCategory.HUD
)
public class SessionStatsModule extends HUDModule {

    public final TextSetting winFlagSetting = new TextSetting("module.opts.titlewinsign", "VICTORY!");
    public final TextSetting killFlagSetting = new TextSetting(
            "module.opts.chatkillsign", "by ${username}");

    private int winsCount, killsCount;
    private long startTime = -0xFF;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String url, address, port;

    @Override
    public void onEnable() {
        if (startTime == -0xFF && InstanceAccess.isOnServer()) {
            startTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketReceivedEvent event) {
        if (startTime == -0xFF) return;
        if (!InstanceAccess.isInGame()) return;

        if (event.getPacket() instanceof S02PacketChat) {
            val chatPacket = (S02PacketChat) event.getPacket();

            if (chatPacket.getChatComponent() == null) return;

            val unformatted = chatPacket.getChatComponent().getUnformattedText();
            val message = StringUtils.stripControlCodes(unformatted);
            val replaced = killFlagSetting.getText().replace("${username}", mc.thePlayer.getName()).toLowerCase();

            if (message.toLowerCase().contains(replaced) && killsCount < 1000) {
                killsCount++;
            }
        }
        else if (event.getPacket() instanceof S45PacketTitle) {
            val titlePacket = (S45PacketTitle) event.getPacket();

            if (titlePacket.getMessage() == null) return;

            val unformatted = titlePacket.getMessage().getUnformattedText();
            val message = StringUtils.stripControlCodes(unformatted);
            val replaced = winFlagSetting.getText().toLowerCase();

            if (message.toLowerCase().contains(replaced) && winsCount < 1000) {
                winsCount++;
            }
        }
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        nvg.setupAndDraw(this::drawNanoVG);
    }

    private void drawNanoVG() {
        long durationInMillis = (startTime == -0xFF ? 0 : System.currentTimeMillis() - startTime);
        String time = String.format(
                "%02d:%02d:%02d",
                (durationInMillis / (1000 * 60 * 60)) % 24,
                (durationInMillis / (1000 * 60)) % 60,
                (durationInMillis / 1000) % 60
        );

        this.drawBackground(140, 64);
        this.drawText("Session Stats", 5.5F, 6F, 10.5F, Fonts.REGULAR);
        this.drawRect(0, 17.5F, 140, 1);

        this.drawText(TextureCode.CLOCK, 5.5F, 22.5F, 10F, Fonts.ICON);
        this.drawText(time, 18, 24, 9, Fonts.REGULAR);

        this.drawText(TextureCode.ARCHIVE, 5.5F, 22.5F + 13, 10F, Fonts.ICON);
        this.drawText(winsCount + " Wins", 18, 24 + 13, 9, Fonts.REGULAR);

        this.drawText(TextureCode.USER, 5.5F, 22.5F + 26, 10F, Fonts.ICON);
        this.drawText(killsCount + " Kills", 18, 24 + 26, 9, Fonts.REGULAR);
    }

    @SubscribeEvent
    public void onJoinServer(JoinServerEvent event) {
        startTime = System.currentTimeMillis();
        winsCount = killsCount = 0;

        // 获取服务器信息
        url = event.getUrl();
        address = event.getAddress();
        port = String.valueOf(event.getPort());

        File sessionsFile = new File(rootFile, "session_stats.json");
        if (!sessionsFile.exists()) return;

        // 读取文件内容
        try (FileReader reader = new FileReader(sessionsFile)) {
            val jsonObject = gson.fromJson(reader, JsonObject.class);

            if (jsonObject == null) return;
            if (!jsonObject.has("new_session")) return;

            // 检查是否有最新的会话
            val newSessionObj = jsonObject.get("new_session");
            if (newSessionObj.isJsonNull()) return;

            // 获取最新的会话
            val newSession = newSessionObj.getAsString();
            if (newSession.isEmpty()) return;

            // 千辛万苦，就为了这个，我真的很绝望
            ntf.post(
                    getName(),
                    TranslateComponent.i18n("module.sessionstats.noti")
                                      .replace("%MODID%", KaliAPI.MODID) + " " + newSession,
                    NotificationType.INFO
            );

            // 移除 "new_session" 属性
            jsonObject.remove("new_session");

            // 保存到文件
            try (FileWriter writer = new FileWriter(sessionsFile)) {
                gson.toJson(jsonObject, JsonObject.class, writer);
            }
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLeaveServer(LeaveServerEvent event) {
        long durationInMillis = System.currentTimeMillis() - startTime;
        String time = String.format(
                "%02d:%02d:%02d",
                (durationInMillis / (1000 * 60 * 60)) % 24,
                (durationInMillis / (1000 * 60)) % 60,
                (durationInMillis / 1000) % 60
        );

        File sessionsFile = new File(rootFile, "session_stats.json");
        Path path = sessionsFile.toPath();

        // 如果文件不存在，创建文件
        if (!sessionsFile.exists()) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        // 读取文件内容
        try (FileReader reader = new FileReader(sessionsFile)) {
            var jsonObject = gson.fromJson(reader, JsonObject.class);
            if (jsonObject == null) jsonObject = new JsonObject();

            // 创建新的会话数据
            JsonObject newSession = new JsonObject();
            newSession.addProperty("time", time);
            newSession.addProperty("wins_count", winsCount);
            newSession.addProperty("kills_count", killsCount);

            // 添加服务器数据
            JsonObject serverData = new JsonObject();
            serverData.addProperty("url", url);
            serverData.addProperty("address", address);
            serverData.addProperty("port", port);
            newSession.add("server_data", serverData);

            // 添加新会话数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String timestamp = sdf.format(new Date(startTime));
            jsonObject.add(timestamp, newSession);
            jsonObject.addProperty("new_session", timestamp);

            // 保存到文件
            try (FileWriter writer = new FileWriter(sessionsFile)) {
                gson.toJson(jsonObject, JsonObject.class, writer);
            }
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) e.printStackTrace();
        }

        // 重置统计数据
        startTime = -0xFF;
        winsCount = killsCount = 0;
    }
}
