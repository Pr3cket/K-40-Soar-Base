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
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.SimpleHUDModule;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

@IModule(
        name = "module.speedometer",
        description = "module.speedometer.desc",
        category = EModuleCategory.HUD
)
public class SpeedometerModule extends SimpleHUDModule {

    public final EnumSetting designSetting = new EnumSetting(
            "module.opts.design", "module.opts.design.simple", "module.opts.design.fancy");

    private final int speedCount = 200;
    private final double[] speeds = new double[speedCount];
    private final DecimalFormat speedFormat = new DecimalFormat("0.00");
    private long lastUpdate;

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        if (designSetting.getSelectedEntryKey().equals("module.opts.design.fancy")) {
            nvg.setupAndDraw(this::drawNanoVG);

            event.startTranslate(
                    x - 1.5F / (sr == null ? new ScaledResolution(mc) : sr).getScaleFactor(), y - from(10 * scale));

            GL11.glLineWidth(1.5F / (sr == null ? new ScaledResolution(mc) : sr).getScaleFactor() * scale);

            if (!mc.isGamePaused() && (lastUpdate == -1 || (System.currentTimeMillis() - lastUpdate) > 30)) {
                addSpeed(getSpeed() / 5);
                lastUpdate = System.currentTimeMillis();
            }

            GlStateManager.enableBlend();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glShadeModel(GL11.GL_SMOOTH);

            GL11.glBegin(GL11.GL_LINE_STRIP);

            ColorUtils.setColor(this.getFontColor().getRGB());

            for (int i = 0; i < speedCount; i++) {
                GL11.glVertex2d(
                        from((float) ((155 * scale + 1) * i / (double) speedCount + 3)),
                        from((float) (100 * scale - (speeds[i] * (16)) - 10))
                );
            }

            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            ColorUtils.resetColor();

            event.stopTranslate();
        }
        else super.onOverlayRendered(event);
    }

    private void drawNanoVG() {
        this.drawBackground(155, 100);
        this.drawRect(0, 17.5F, 155, 1);
        this.drawText("Speed: " + speedFormat.format(getSpeed()) + " m/s", 5.5F, 6F, 10.5F, Fonts.MEDIUM);
    }

    @Override
    public String getText() {
        return "Speed: " + speedFormat.format(getSpeed()) + " m/s";
    }

    @Override
    public String getIcon() {
        return TextureCode.ACTIVITY;
    }

    private void addSpeed(double speed) {

        if (speed > 3.8) {
            speed = 3.8;
        }

        System.arraycopy(speeds, 1, speeds, 0, speedCount - 1);
        speeds[speedCount - 1] = speed;
    }

    public float getSpeed() {
        double distTraveledLastTickX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double distTraveledLastTickZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        double currentSpeed = MathHelper.sqrt_double(distTraveledLastTickX * distTraveledLastTickX
                                                             + distTraveledLastTickZ * distTraveledLastTickZ);

        return (float) (currentSpeed / 0.05);
    }
}
