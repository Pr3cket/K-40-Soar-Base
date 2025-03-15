package com.lawab1ders.nan7i.kali.module.impl.render;

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

import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;

@IModule(
        name = "module.motioncamera",
        description = "module.motioncamera.desc",
        category = EModuleCategory.RENDER
)
public class MotionCameraModule extends Module {

    public final FloatSetting xSetting = new FloatSetting("module.motioncamera.opts.x", 0, -5, 5);
    public final BooleanSetting motionSetting = new BooleanSetting("module.motioncamera.opts.motion", true);
    public final FloatSetting ySetting = new FloatSetting("module.motioncamera.opts.y", 0, -5, 5);
    public final FloatSetting interpolationSetting = new FloatSetting(
            "module.motioncamera.opts.interpolation", 0.15f, 0.05f, 0.5f
    );
    public final FloatSetting zSetting = new FloatSetting("module.motioncamera.opts.z", 0, -5, 5);
}
