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

import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.RendererTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.rearview.RearviewCamera;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@IModule(
        name = "module.rearview",
        description = "module.rearview.desc",
        category = EModuleCategory.HUD
)
public class RearviewModule extends HUDModule {

    public final IntSetting widthSetting = new IntSetting("module.opts.width", 190, 50, 300);
    public final IntSetting heightSetting = new IntSetting("module.opts.height", 100, 50, 300);
    public final BooleanSetting lockCameraSetting = new BooleanSetting("module.rearview.opts.lockcamera", true);
    public final IntSetting fpsLimitSetting = new IntSetting("module.rearview.opts.fpslimit", 60, 10, 120);

    public final RearviewCamera rearviewCamera = new RearviewCamera();

    private final MillisTimer timer = new MillisTimer();

    @SubscribeEvent
    public void onRendererTicked(RendererTickedEvent event) {
        if (mc.theWorld != null && timer.delay(1000 / fpsLimitSetting.getValue())) {
            rearviewCamera.updateMirror();
            timer.reset();
        }
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        int w = widthSetting.getValue();
        int h = heightSetting.getValue();

        this.setSize(w, h);

        nvg.drawShadow(x, y, width, height, from(6 * scale));
        nvg.setupAndDraw(() -> {
            rearviewCamera.setLockCamera(lockCameraSetting.isActivated());
            nvg.drawRoundedImage(rearviewCamera.getTexture(), x, y + height, width, -height, from(6 * scale), 1);
        });
    }
}
