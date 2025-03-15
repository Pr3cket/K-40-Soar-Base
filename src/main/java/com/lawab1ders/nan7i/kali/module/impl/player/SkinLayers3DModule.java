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
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import lombok.val;

@IModule(
        name = "module.skinlayers3d",
        description = "module.skinlayers3d.desc",
        category = EModuleCategory.PLAYER
)
public class SkinLayers3DModule extends Module {

    public static final float BASE_VOXEL_SIZE = 1.15F;
    public static final float BODY_VOXEL_WIDTH_SIZE = 1.05F;
    public static final float HEAD_VOXEL_SIZE = 1.18F;
    public static final int RENDER_DISTANCE_LOD = 14;

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
