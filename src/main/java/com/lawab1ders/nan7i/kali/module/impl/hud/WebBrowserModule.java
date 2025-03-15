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

import com.lawab1ders.nan7i.kali.events.KeyEvent;
import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.gui.GuiWebBrowser;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.mcef.Mcef;
import com.lawab1ders.nan7i.kali.module.impl.hud.mcef.McefBrowser;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.TextSetting;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.nio.file.Files;

@IModule(
        name = "module.webbrowser",
        description = "module.webbrowser.desc",
        category = EModuleCategory.HUD
)
public class WebBrowserModule extends HUDModule {

    public final KeybindSetting keybindSetting = new KeybindSetting(Keyboard.KEY_J);
    public final TextSetting urlSetting = new TextSetting("module.webbrowser.opts.url", "https://cn.bing.com/");
    public final IntSetting widthSetting = new IntSetting("module.opts.width", 150, 70, 180);
    public final IntSetting heightSetting = new IntSetting("module.opts.height", 70, 70, 180);

    private McefBrowser browser;

    @Override
    public void onOpenSettings() {
        if (Mcef.active || isNotified()) return;

        try {
            if (!Mcef.NATIVES_DIR.exists()) Files.createDirectories(Mcef.NATIVES_DIR.toPath());
            Desktop.getDesktop().open(Mcef.NATIVES_DIR);
            setNotified(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onEnable() {
        if (!Mcef.active) postNotificationAndDisableModule("module.webbrowser.noti");
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        if (browser != null) nvg.setupAndDraw(this::drawNanoVG);
    }

    private void drawNanoVG() {
        int w = widthSetting.getValue();
        int h = heightSetting.getValue();

        this.setSize(w, h);

        nvg.drawShadow(x, y, width, height, from(6 * scale));
        nvg.drawRoundedImage(
                browser.getTexture(), x, y, width, height, from(6 * scale), 1
        );
    }

    @SubscribeEvent
    public void onKey(KeyEvent event) {
        if (event.getKeyCode() != keybindSetting.getKeyCode()) return;

        if (browser == null && (browser = Mcef.createBrowser(urlSetting.getText(), true)) == null) {
            ntf.post(getName(), TranslateComponent.i18n("module.webbrowser.noti"), NotificationType.ERROR);
            return;
        }

        mc.displayGuiScreen(new GuiWebBrowser(browser));
    }

    @Override
    public void onDisable() {
        if (browser != null) {
            browser.close();
            browser = null;
        }
    }
}
