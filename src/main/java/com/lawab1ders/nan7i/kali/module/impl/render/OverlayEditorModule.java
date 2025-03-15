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
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;

@IModule(
        name = "module.overlayeditor",
        description = "module.overlayeditor.desc",
        category = EModuleCategory.RENDER
)
public class OverlayEditorModule extends Module {

    public final BooleanSetting hidePumpkinMaskSetting = new BooleanSetting(
            "module.overlayeditor.opts.hidepumpkinmask", false);

    public final BooleanSetting hideFireSetting = new BooleanSetting(
            "module.overlayeditor.opts.hidefire", false);

    public final FloatSetting handXPosSetting = new FloatSetting("module.overlayeditor.opts.handxpos", 0.75F, -1, 1);
    public final FloatSetting handXScaleSetting = new FloatSetting("module.overlayeditor.opts.handxscale", 1, 0, 1);

    public final FloatSetting handYPosSetting = new FloatSetting("module.overlayeditor.opts.handypos", -0.15F, -1, 1);
    public final FloatSetting handYScaleSetting = new FloatSetting("module.overlayeditor.opts.handyscale", 1, 0, 1);

    public final FloatSetting handZPosSetting = new FloatSetting("module.overlayeditor.opts.handzpos", -1, -1, 1);
    public final FloatSetting handZScaleSetting = new FloatSetting("module.overlayeditor.opts.handzscale", 1, 0, 1);

    public final IntSetting handSwingDelaySetting = new IntSetting(
            "module.overlayeditor.opts.handswingdelay", 14, 2, 20);
}
