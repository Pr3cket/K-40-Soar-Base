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
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.SimpleHUDModule;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;

@IModule(
        name = "module.stopwatch",
        description = "module.stopwatch.desc",
        category = EModuleCategory.HUD
)
public class StopwatchModule extends SimpleHUDModule {

    public final KeybindSetting keybindSetting = new KeybindSetting(Keyboard.KEY_P);

    private final MillisTimer timer = new MillisTimer();
    private final DecimalFormat timeFormat = new DecimalFormat("0.00");
    private int pressCount;
    private float currentTime;

    @Override
    public void onEnable() {
        if (timer != null) timer.reset();

        currentTime = pressCount = 0;
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        switch (pressCount) {
            case 0:
                timer.reset();
                break;
            case 1:
                currentTime = (timer.getElapsedTime() / 1000F);
                break;
            case 3:
                timer.reset();
                currentTime = 0;
                pressCount = 0;
                break;
        }
    }

    @SubscribeEvent
    public void onKey(KeyEvent event) {
        if (event.getKeyCode() == keybindSetting.getKeyCode()) {
            pressCount++;
        }
    }

    @Override
    public String getText() {
        return timeFormat.format(currentTime) + " s";
    }

    @Override
    public String getIcon() {
        return TextureCode.WATCH;
    }
}
