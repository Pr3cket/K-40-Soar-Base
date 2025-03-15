package com.lawab1ders.nan7i.kali.module.impl.hud.mcef;

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
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;

import java.io.File;

public final class Mcef {

    public static boolean active = false;
    public static CefApp cefAppInstance;
    public static CefClient cefClientInstance;

    public static final File MCEF_DIR = new File(InstanceAccess.rootFile, "mcef");
    public static final File NATIVES_DIR = new File(Mcef.MCEF_DIR, "natives");

    public static void install() {
        String[] cefSwitches = new String[] {
                "--autoplay-policy=no-user-gesture-required",
                "--disable-web-security"
        };

        if (CefApp.startup(cefSwitches)) {
            CefSettings cefSettings = new CefSettings();
            cefSettings.windowless_rendering_enabled = true;
            cefSettings.background_color = cefSettings.new ColorType(0, 255, 255, 255);
            cefSettings.cache_path = MCEF_DIR.getAbsolutePath();

            cefAppInstance = CefApp.getInstance(cefSwitches, cefSettings);
            cefClientInstance = cefAppInstance.createClient();

            Mcef.active = true;
        }
    }

    public static McefBrowser createBrowser(String url, boolean transparent) {
        if (!Mcef.active) return null;

        McefBrowser browser = new McefBrowser(cefClientInstance, url, transparent, null);
        browser.setCloseAllowed();
        browser.createImmediately();

        return browser;
    }

    public static void destory() {
        if (!Mcef.active) return;

        cefClientInstance.dispose();
        cefAppInstance.dispose();

        Mcef.active = false;
    }
}
