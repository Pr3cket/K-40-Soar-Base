package com.lawab1ders.nan7i.kali.module.impl.render;

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

import com.lawab1ders.nan7i.kali.events.ticks.RendererTickedEvent;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import lombok.val;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@IModule(
        name = "module.itemphysics",
        description = "module.itemphysics.desc",
        category = EModuleCategory.RENDER
)
public class ItemPhysicsModule extends Module {

    public final IntSetting rotationSpeedSetting = new IntSetting("module.itemphysics.opts.rotationspeed", 50, 0, 100);

    public static long tick;

    @Override
    protected void onEnable() {
        val overflowAnimationsModule = mod.getModule(OverflowAnimationsModule.class);
        val item2DSetting = overflowAnimationsModule.Item2DSetting;

        if (item2DSetting.isActivated()) {
            item2DSetting.toggle();
            ntf.post(
                    getName(),
                    TranslateComponent.i18n("module.noti.disable.modulesetting")
                            + " "
                            + overflowAnimationsModule.getName()
                            + " "
                            + item2DSetting.getName(),
                    NotificationType.INFO
            );
        }
    }

    @SubscribeEvent
    public void onRendererTicked(RendererTickedEvent event) {
        tick = System.nanoTime();
    }
}
