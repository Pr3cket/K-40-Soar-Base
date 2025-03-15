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
import com.lawab1ders.nan7i.kali.events.cancelable.RotateHeadEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@IModule(
        name = "module.mousestrokes",
        description = "module.mousestrokes.desc",
        category = EModuleCategory.HUD
)
public class MouseStrokesModule extends HUDModule {

    private float mouseX, mouseY, lastMouseX, lastMouseY;

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        nvg.setupAndDraw(() -> {
            float calculatedMouseX = (lastMouseX + ((mouseX - lastMouseX) * event.getPartialTicks()));
            float calculatedMouseY = (lastMouseY + ((mouseY - lastMouseY) * event.getPartialTicks()));

            this.drawBackground(58, 58);
            this.drawRoundedRect(calculatedMouseX + 28 - 3.5F, calculatedMouseY + 28 - 3.5F, 9, 9, (float) 9 / 2);
        });
    }

    @SubscribeEvent
    public void onRotateHead(RotateHeadEvent event) {
        mouseX += event.getYaw() / 40F;
        mouseY -= event.getPitch() / 40F;
        mouseX = MathHelper.clamp_float(mouseX, -18, 18);
        mouseY = MathHelper.clamp_float(mouseY, -18, 18);
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        mouseX *= 0.75F;
        mouseY *= 0.75F;
    }
}
