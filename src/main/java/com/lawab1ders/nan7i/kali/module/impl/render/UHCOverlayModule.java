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

import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;

@IModule(
        name = "module.uhcoverlay",
        description = "module.uhcoverlay.desc",
        category = EModuleCategory.RENDER
)
public class UHCOverlayModule extends Module {

    public final FloatSetting goldIngotScaleSetting = new FloatSetting(
            "module.uhcoverlay.opts.goldingotscale", 1.5F, 1.0F, 5.0F);

    public final FloatSetting goldNuggetScaleSetting = new FloatSetting(
            "module.uhcoverlay.opts.goldnuggetscale", 1.5F, 1.0F, 5.0F);

    public final FloatSetting goldOreScaleSetting = new FloatSetting(
            "module.uhcoverlay.opts.goldorescale", 1.5F, 1.0F, 5.0F);

    public final FloatSetting goldAppleScaleSetting = new FloatSetting(
            "module.uhcoverlay.opts.goldapplescale", 1.5F, 1.0F, 5.0F);

    public final FloatSetting skullScaleSetting = new FloatSetting(
            "module.uhcoverlay.opts.skullscale", 1.5F, 1.0F, 5.0F);
}
