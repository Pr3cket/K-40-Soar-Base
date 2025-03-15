package com.lawab1ders.nan7i.kali.module.impl.server;

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
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;

@IModule(
        name = "module.ambience",
        description = "module.ambience.desc",
        category = EModuleCategory.SERVER
)
public class AmbienceModule extends Module {

    public final EnumSetting weatherSetting = new EnumSetting("module.ambience.opts.weather",
            "module.ambience.opts.weather.clear",
            "module.ambience.opts.weather.rain",
            "module.ambience.opts.weather.storm",
            "module.ambience.opts.weather.snow"
    );

    public final FloatSetting timeSetting = new FloatSetting("module.ambience.opts.time", 12, 0, 24);

    public final FloatSetting rainSnowStrength = new FloatSetting("module.ambience.opts.rainsnowstrength", 1, 0, 1);
    public final FloatSetting thunderStrength = new FloatSetting("module.ambience.opts.thunderstrength", 1, 0, 1);
}
