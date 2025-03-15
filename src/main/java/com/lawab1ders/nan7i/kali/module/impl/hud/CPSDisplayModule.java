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

import com.lawab1ders.nan7i.kali.events.cancelable.ClickMouseEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.SimpleHUDModule;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

@IModule(
        name = "module.cpsdisplay",
        description = "module.cpsdisplay.desc",
        category = EModuleCategory.HUD
)
public class CPSDisplayModule extends SimpleHUDModule {

    private final ArrayList<Long> leftPresses = new ArrayList<>();
    private final ArrayList<Long> rightPresses = new ArrayList<>();

    @SubscribeEvent
    public void onClickMouse(ClickMouseEvent event) {

        if (Mouse.getEventButtonState()) {

            if (event.getButton() == 0) {
                leftPresses.add(System.currentTimeMillis());
            }

            if (event.getButton() == 1) {
                rightPresses.add(System.currentTimeMillis());
            }
        }
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        leftPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
        rightPresses.removeIf(t -> System.currentTimeMillis() - t > 1000);
    }

    @Override
    public String getText() {
        return leftPresses.size() + " | " + rightPresses.size() + " CPS";
    }

    @Override
    public String getIcon() {
        return TextureCode.MOUSE_POINTER;
    }
}
