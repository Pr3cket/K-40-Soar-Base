package com.lawab1ders.nan7i.kali.module.impl.server;

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

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@IModule(
        name = "module.radar",
        description = "module.radar.desc",
        category = EModuleCategory.SERVER
)
public class RadarModule extends HUDModule {

    public final BooleanSetting scannerSetting = new BooleanSetting("module.radar.opts.scanner", true);

    private final ResourceLocation beamResource = new ResourceLocation("k40", "textures/radar/beam.png");
    private final ResourceLocation dotResource = new ResourceLocation("k40", "textures/radar/dot.png");
    private final ResourceLocation scopeResource = new ResourceLocation("k40", "textures/radar/scope.png");

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        setSize(128, 128);
        nvg.setupAndDraw(() -> drawBackground(128, 128, 64 * scale));

        // Draw scope
        event.drawImage(scopeResource, x, y, width, height, Color.WHITE);

        // Moved above loop (why calc for every entity xd)
        GL11.glPushMatrix();
        GL11.glTranslated(x + width / 2.0F, y + height / 2.0F, 0);
        GL11.glRotated(-mc.thePlayer.rotationYaw + 180, 0, 0, 1);

        double playerX = mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * event.getPartialTicks();
        double playerZ = mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * event.getPartialTicks();

        // Draw entities
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (InstanceAccess.isSkippable(entity, 80)) continue;

            double entityX = entity.prevPosX + (entity.posX - entity.prevPosX) * event.getPartialTicks();
            double entityZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * event.getPartialTicks();

            // Draw dot
            event.drawImage(
                    dotResource,
                    (entityX - playerX) * 2 * 0.75 - 8, (entityZ - playerZ) * 2 * 0.75 - 8, 32, 32,
                    Color.WHITE
            );
        }

        GL11.glPopMatrix();

        // Draw beam
        if (scannerSetting.isActivated()) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + width / 2.0F, y + height / 2.0F, 0);
            GL11.glRotated(-(System.currentTimeMillis() / 10D) % 360, 0, 0, 1);
            event.drawImage(beamResource, -width / 2.0F, -height / 2.0F, width, height, Color.WHITE);
            GL11.glPopMatrix();
        }
    }
}
