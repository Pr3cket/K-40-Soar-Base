package com.lawab1ders.nan7i.kali;

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

import com.lawab1ders.nan7i.kali.color.ColorManager;
import com.lawab1ders.nan7i.kali.color.palette.ColorPalette;
import com.lawab1ders.nan7i.kali.cosmetics.CosmeticsManager;
import com.lawab1ders.nan7i.kali.events.api.AccessEvent;
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.gui.clickgui.clickeffect.ClickEffectsEngine;
import com.lawab1ders.nan7i.kali.gui.clickgui.particle.ParticlesEngine;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinMinecraft;
import com.lawab1ders.nan7i.kali.language.LanguageManager;
import com.lawab1ders.nan7i.kali.module.ModuleManager;
import com.lawab1ders.nan7i.kali.module.impl.combat.AntiBotModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.mcef.Mcef;
import com.lawab1ders.nan7i.kali.module.impl.render.FrameInterpolatorModule;
import com.lawab1ders.nan7i.kali.music.MusicManager;
import com.lawab1ders.nan7i.kali.nanovg.NanoVGManager;
import com.lawab1ders.nan7i.kali.notification.NotificationManager;
import com.lawab1ders.nan7i.kali.shader.ShaderManager;
import com.lawab1ders.nan7i.kali.utils.config.ConfigIcon;
import com.lawab1ders.nan7i.kali.utils.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import org.opencv.core.Core;

import java.io.File;
import java.net.InetAddress;

public interface InstanceAccess {

    Minecraft mc = Minecraft.getMinecraft();
    File rootFile = new File(mc.mcDataDir, KaliAPI.MODID);

    static void build() {
        // 初始化库 JCEF
        try {
            if (Mcef.NATIVES_DIR.exists()) {
                System.setProperty("jcef.path", Mcef.NATIVES_DIR.getAbsolutePath().replace("\\", "/"));
                Mcef.install();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化库 OpenCV
        try {
            if (FrameInterpolatorModule.OPENCV_DIR.exists()) {
                System.loadLibrary(
                        FrameInterpolatorModule.OPENCV_DIR.getAbsolutePath().replace(
                                "\\",
                                "/"
                        ) + "/" + Core.NATIVE_LIBRARY_NAME + ".dll");
                FrameInterpolatorModule.isOpenCVLoaded = true;
            }
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }

        // 激活默认启用的模块
        mod.active();

        ConfigManager.loadSettings();
        ConfigManager.loadMcc();
        ConfigManager.load(null, true, false);

        // 当进程结束时，保存配置
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Mcef.destory();

            mus.save();
            ConfigManager.saveSettings();
            ConfigManager.saveMcc();
            ConfigManager.save(null, false, "", ConfigIcon.COMMAND, true, false, false);
        }));
    }

    static boolean isSkippable(Entity entity, float distance) {

        if (distance != 0 && entity.getDistanceToEntity(mc.thePlayer) > distance)
            return true; // someone explain why it has to be hardcoded

        if (!(entity instanceof AbstractClientPlayer)) return true;

        if (entity == mc.thePlayer) return true;

        return mod.getModule(AntiBotModule.class).isSkippable((AbstractClientPlayer) entity);
    }

    static void safeHandleEvent(AccessEvent event) {
        if (isInGame()) MinecraftForge.EVENT_BUS.post(event);
    }

    static boolean isInGame() {
        return mc.thePlayer != null && mc.theWorld != null && mc.playerController != null;
    }

    static boolean isOnServer() {
        return !mc.isSingleplayer() && mc.getCurrentServerData() != null;
    }

    static String getServerIP() {
        return isOnServer() ? mc.getCurrentServerData().serverIP : "";
    }

    static String getAddress(String url) throws Exception {
        String processedInput = url
                .replace("https://", "")
                .replace("http://", "")
                .replace("www.", "")
                .replace(" ", "");

        if (processedInput.isEmpty()) throw new RuntimeException("Invalid URL");

        return InetAddress.getByName(processedInput).getHostAddress();
    }

    /**
     * 判断玩家是否是正版账号
     */
    static boolean isPremiumAccount() {
        Session session = ((IMixinMinecraft) mc).k40$getSession();
        return session != null &&
                !"0".equals(session.getPlayerID()) &&
                !"mojang".equals(session.getToken());
    }

    //---------------------------------------------------------------------------
    // 产房
    //---------------------------------------------------------------------------

    ParticlesEngine PARTICLES_ENGINE = new ParticlesEngine();
    ClickEffectsEngine CLICK_EFFECTS_ENGINE = new ClickEffectsEngine();

    NanoVGManager nvg = new NanoVGManager();

    ModuleManager mod = new ModuleManager();

    CosmeticsManager cos = new CosmeticsManager();

    ColorManager col = new ColorManager();
    ColorPalette palette = col.getPalette();

    NotificationManager ntf = new NotificationManager();

    MusicManager mus = new MusicManager();

    ShaderManager sha = new ShaderManager();

    GuiClickMenu gcm = new GuiClickMenu();

    LanguageManager lag = new LanguageManager();
}
