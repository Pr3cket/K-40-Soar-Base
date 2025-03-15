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

import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.other.immanager.IMM;
import com.lawab1ders.nan7i.kali.module.impl.other.immanager.impl.LinuxIMM;
import com.lawab1ders.nan7i.kali.module.impl.other.immanager.impl.WindowsIMM;
import com.sun.jna.Platform;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@IModule(
        name = "module.imblocker",
        description = "module.imblocker.desc",
        category = EModuleCategory.OTHER,
        activated = true
)
public class IMBlockerModule extends Module {

    private final IMM imManager;

    public IMBlockerModule() {
        imManager = Platform.isWindows() ? new WindowsIMM() : Platform.isLinux() ? new LinuxIMM() : null;
    }

    @Override
    protected void onEnable() {
        if (imManager == null) {
            postNotificationAndDisableModule("module.imblocker.noti");
        } else {
            setNotified(false);
        }
    }

    @Override
    protected void onDisable() {
        if (imManager != null) imManager.setState(true);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (imManager != null) imManager.setState(event.gui != null);
    }
}
