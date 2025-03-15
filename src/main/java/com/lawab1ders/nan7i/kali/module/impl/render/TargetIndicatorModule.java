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

import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.events.LevelsRenderedEvent;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinMinecraft;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinRenderManager;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@IModule(
        name = "module.targetindicator",
        description = "module.targetindicator.desc",
        category = EModuleCategory.RENDER
)
public class TargetIndicatorModule extends Module {

    @SubscribeEvent
    public void onLevelsRendered(LevelsRenderedEvent event) {
        AbstractClientPlayer target = KaliAPI.INSTANCE.getAttackingTaget();
        Color color = col.getCurrentColor().getInterpolateColor();

        if (target == null) return;

        if (!target.equals(mc.thePlayer)) {
            event.init();
            event.begin();

            double x = target.lastTickPosX
                    + (target.posX - target.lastTickPosX) * ((IMixinMinecraft) mc).k40$getTimer().renderPartialTicks
                    - (((IMixinRenderManager) mc.getRenderManager())).k40$getRenderPosX();

            double y = (target.lastTickPosY
                    + (target.posY - target.lastTickPosY) * ((IMixinMinecraft) mc).k40$getTimer().renderPartialTicks
                    - (((IMixinRenderManager) mc.getRenderManager())).k40$getRenderPosY()) + Math.sin(System.currentTimeMillis() / 2E+2) + 1;

            double z = target.lastTickPosZ
                    + (target.posZ - target.lastTickPosZ) * ((IMixinMinecraft) mc).k40$getTimer().renderPartialTicks
                    - (((IMixinRenderManager) mc.getRenderManager())).k40$getRenderPosZ();

            for (float i = 0; i < Math.PI * 2; i += (float) (Math.PI * 2 / 64.F)) {
                double vecX = x + 0.67 * Math.cos(i);
                double vecZ = z + 0.67 * Math.sin(i);

                GL11.glColor4f(color.getRed() / 255.F,
                        color.getGreen() / 255.F,
                        color.getBlue() / 255.F,
                        0
                );
                GL11.glVertex3d(vecX, y - Math.cos(System.currentTimeMillis() / 2E+2) / 2.0F, vecZ);
                GL11.glColor4f(color.getRed() / 255.F,
                        color.getGreen() / 255.F,
                        color.getBlue() / 255.F,
                        0.40F
                );
                GL11.glVertex3d(vecX, y, vecZ);
            }

            event.end();
            event.reset();
        }
    }
}
