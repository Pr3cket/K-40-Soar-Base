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

import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.events.LevelsRenderedEvent;
import com.lawab1ders.nan7i.kali.events.MotionEvent;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinRenderManager;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

@IModule(
        name = "module.breadcrumbs",
        description = "module.breadcrumbs.desc",
        category = EModuleCategory.RENDER
)
public class BreadcrumbsModule extends Module {

    public final IntSetting timeoutSetting = new IntSetting("module.opts.timeout", 15, 15, 500);

    private final List<Vec3> path = new ArrayList<>();

    @SubscribeEvent
    public void onLevelsRendered(LevelsRenderedEvent event) {
        AccentColor currentColor = col.getCurrentColor();

        renderBreadCrumbs(path, currentColor.getInterpolateColor());
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {
        if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
            path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }

        while (path.size() > timeoutSetting.getValue()) {
            path.remove(0);
        }
    }

    private void renderBreadCrumbs(final List<Vec3> vec3s, Color color) {
        GlStateManager.disableDepth();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int i = 0;
        try {
            for (final Vec3 v : vec3s) {
                i++;

                boolean draw = true;

                final double x = v.xCoord - ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosX();
                final double y = v.yCoord - ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosY();
                final double z = v.zCoord - ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosZ();

                final double distanceFromPlayer = mc.thePlayer.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350) quality = 350;

                if (i % 10 != 0 && distanceFromPlayer > 25) {
                    draw = false;
                }

                if (i % 3 == 0 && distanceFromPlayer > 15) {
                    draw = false;
                }

                if (draw) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);

                    final float scale = 0.04f;
                    GL11.glScalef(-scale, -scale, -scale);

                    GL11.glRotated(-(mc.getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);
                    GL11.glRotated((mc.getRenderManager()).playerViewX, 1.0D, 0.0D, 0.0D);

                    drawFilledCircleNoGL(0, 0, 0.7,
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 100).hashCode(), quality);

                    if (distanceFromPlayer < 4) {
                        drawFilledCircleNoGL(0, 0, 1.4, new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                50).hashCode(), quality);
                    }

                    if (distanceFromPlayer < 20) {
                        drawFilledCircleNoGL(0, 0, 2.3, new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                30).hashCode(), quality);
                    }

                    GL11.glScalef(0.8f, 0.8f, 0.8f);
                    GL11.glPopMatrix();
                }
            }
        } catch (final ConcurrentModificationException ignored) {
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableDepth();

        GL11.glColor3d(255, 255, 255);
    }

    private void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
    }
}
