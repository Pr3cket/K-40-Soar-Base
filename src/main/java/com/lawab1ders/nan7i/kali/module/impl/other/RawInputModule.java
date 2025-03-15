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
import lombok.Getter;
import lombok.Setter;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Mouse;

import java.util.ArrayList;

@IModule(
        name = "module.rawinput",
        description = "module.rawinput.desc",
        category = EModuleCategory.OTHER
)
public class RawInputModule extends Module {

    private final ArrayList<Mouse> mouseList = new ArrayList<>();

    @Getter
    private boolean available;

    @Getter @Setter
    private volatile float dx, dy;
    private volatile boolean running;

    public RawInputModule() {
        super();

        try {
            ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
            available = env.isSupported() && env.getControllers().length > 0;

            if (!available) return;

            for (Controller controller : env.getControllers()) {
                if (controller instanceof Mouse) mouseList.add((Mouse) controller);
            }
        } catch (Exception e) {
            available = false;
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        if (!available) {
            postNotificationAndDisableModule("module.rawinput.noti");
            return;
        }

        running = true;

        MouseThread thread = new MouseThread();
        thread.setDaemon(true);
        thread.start();

        setNotified(false);
    }

    @Override
    public void onDisable() {
        running = false;
    }

    private class MouseThread extends Thread {

        @Override
        public void run() {
            while (running) {
                available = !mouseList.isEmpty();

                mouseList.stream().filter(Mouse::poll).forEach(mouse -> {
                    if (org.lwjgl.input.Mouse.isGrabbed()) {
                        dx += mouse.getX().getPollData();
                        dy += mouse.getY().getPollData();
                    }
                });
            }
        }
    }
}
