package com.lawab1ders.nan7i.kali.module.impl.player;

/*
 * Copyright (c) 2025 EldoDebug, Nan7i.南起, 乘风.ChengF3ng（音乐播放器 API.Music Player API）
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

import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.render.ItemPhysicsModule;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import lombok.val;

@IModule(
        name = "module.overflowanimations",
        description = "module.overflowanimations.desc",
        category = EModuleCategory.PLAYER
)
public class OverflowAnimationsModule extends Module {

    public final BooleanSetting Item2DSetting = new BooleanSetting(
            "module.overflowanimations.opts.2ditems", false
    ).setChangeListener(((oldValue, newValue) -> {
        val itemPhysicsModule = mod.getModule(ItemPhysicsModule.class);

        if (newValue && itemPhysicsModule.isActivated()) {
            itemPhysicsModule.toggle();
            ntf.post(
                    getName(),
                    TranslateComponent.i18n("module.noti.disable.module") + " " + itemPhysicsModule.getName(),
                    NotificationType.INFO
            );
        }
    }));
}
