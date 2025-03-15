package com.lawab1ders.nan7i.kali.module.impl.combat;

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
import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.events.LevelsRenderedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@IModule(
        name = "module.reachcircle",
        description = "module.reachcircle.desc",
        category = EModuleCategory.COMBAT
)
public class ReachCircleModule extends Module {

    public final EnumSetting modeSetting = new EnumSetting(
            "module.opts.mode",
            "module.reachcircle.opts.mode.self", "module.reachcircle.opts.mode.enemy"
    );

    public final FloatSetting lineWidthSetting = new FloatSetting("module.reachcircle.opts.linewidth", 5, 1, 10);

    @SubscribeEvent
    public void onLevelsRendered(LevelsRenderedEvent event) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);

        switch (modeSetting.getSelectedEntryKey()) {
            case "module.reachcircle.opts.mode.self":

                double selfPosX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX)
                        * (double) event.getPartialTicks() - mc.getRenderManager().viewerPosX;

                double selfPosY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY)
                        * (double) event.getPartialTicks() - mc.getRenderManager().viewerPosY;

                double selfPosZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ)
                        * (double) event.getPartialTicks() - mc.getRenderManager().viewerPosZ;

                drawCircleAtPos(selfPosX, selfPosY, selfPosZ, mc.playerController.isInCreativeMode() ? 4.7D : 3.4D);
                break;

            case "module.reachcircle.opts.mode.enemy":
                AbstractClientPlayer target = KaliAPI.INSTANCE.getAttackingTaget();

                if (target != null) {
                    double posX = target.lastTickPosX + (target.posX - target.lastTickPosX)
                            * (double) event.getPartialTicks() - mc.getRenderManager().viewerPosX;

                    double posY = target.lastTickPosY + (target.posY - target.lastTickPosY)
                            * (double) event.getPartialTicks() - mc.getRenderManager().viewerPosY;

                    double posZ = target.lastTickPosZ + (target.posZ - target.lastTickPosZ)
                            * (double) event.getPartialTicks() - mc.getRenderManager().viewerPosZ;

                    this.drawCircleAtPos(posX, posY, posZ, mc.playerController.isInCreativeMode() ? 4.7D : 3.4D);
                }

                break;
        }

        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    private void drawCircleAtPos(double x, double y, double z, double rad) {
        AccentColor currentColor = col.getCurrentColor();

        GL11.glPushMatrix();
        Color color = ColorUtils.applyAlpha(currentColor.getInterpolateColor(), 120);

        GL11.glLineWidth(lineWidthSetting.getValue());
        ColorUtils.setColor(color.getRGB());
        GL11.glBegin(1);

        for (int i = 0; i <= 90; ++i) {
            ColorUtils.setColor(color.getRGB(), 0.4F);
            GL11.glVertex3d(
                    x + rad * Math.cos((double) i * 6.283185307179586D / 45.0D), y,
                    z + rad * Math.sin((double) i * 6.283185307179586D / 45.0D)
            );
        }

        GL11.glEnd();
        GL11.glPopMatrix();
    }
}
