package com.lawab1ders.nan7i.kali.module.impl.player;

/*
 * Copyright (c) 2025 EldoDebug, Nan7.南起
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
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import lombok.val;

@IModule(
        name = "module.waveycapes",
        description = "module.waveycapes.desc",
        category = EModuleCategory.PLAYER
)
public class WaveyCapesModule extends Module {

    public EnumSetting styleSetting = new EnumSetting(
            "module.waveycapes.opts.style",
            "module.waveycapes.opts.style.smooth",
            "module.waveycapes.opts.style.blocky"
    );
    public BooleanSetting flutteredSetting = new BooleanSetting("module.waveycapes.opts.fluttered", true);
    public FloatSetting gravitySetting = new FloatSetting("module.waveycapes.opts.gravity", 15, 2, 30);
    public BooleanSetting swingWithTheWindSetting = new BooleanSetting("module.waveycapes.opts.swingwiththewind", true);
    public IntSetting heightMultiplierSetting = new IntSetting("module.waveycapes.opts.heightmultiplier", 6, 2, 10);

    @Override
    protected void onEnable() {
        val moBendsModule = mod.getModule(MoBendsModule.class);

        if (moBendsModule.isActivated()) {
            moBendsModule.toggle();
            ntf.post(
                    getName(),
                    TranslateComponent.i18n("module.noti.disable.module") + " " + moBendsModule.getName(),
                    NotificationType.INFO
            );
        }
    }
}
