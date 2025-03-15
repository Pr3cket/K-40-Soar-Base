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
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenStencil;
import net.minecraft.util.MathHelper;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import java.awt.*;

@IModule(
        name = "module.compass",
        description = "module.compass.desc",
        category = EModuleCategory.HUD
)
public class CompassModule extends SimpleHUDModule {

    public final EnumSetting designSetting = new EnumSetting(
            "module.opts.design",
            "module.opts.design.simple", "module.opts.design.fancy"
    );

    public final IntSetting widthSetting = new IntSetting("module.opts.width", 190, 50, 300);

    private final ScreenStencil stencil = new ScreenStencil();

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        if (designSetting.getSelectedEntryKey().equals("module.opts.design.fancy")) {
            nvg.setupAndDraw(() -> this.drawBackground(widthSetting.getValue(), 29));

            stencil.wrap(
                    this::drawNanoVG, x, y, from(widthSetting.getValue() * scale), from(29 * scale),
                    from(6 * scale)
            );
        }
        else super.onOverlayRendered(event);
    }

    private void drawNanoVG() {
        int width = widthSetting.getValue();

        int angle = (int) MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) * -1 - 360;
        int angle2 = (int) MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) * -1 - 360;
        int angle3 = (int) MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) * -1 - 360;
        int angle4 = (int) MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) * -1 - 360;
        int angle5 = (int) MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) * -1 - 360;
        int angle6 = (int) MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) * -1 - 360;

        this.renderMarker(
                x + from((float) width * scale / 2),
                y + from(2.5F * scale), this.getFontColor()
        );

        for (int i = 0; i <= 2; i++) {
            for (double d = 0.0D; d <= 1.5D; d += 0.5D) {

                String s = "W";

                if (d == 0.0D) {
                    s = "S";
                }

                if (d == 1.0D) {
                    s = "N";
                }

                if (d == 1.5D) {
                    s = "E";
                }

                this.drawRect(((float) width / 2) + angle - 2F, 8, 0.8F, 9);

                this.drawRect(((float) width / 2) + angle + 12F, 8, 0.8F, 6);
                this.drawRect(((float) width / 2) + angle + 26F, 8, 0.8F, 6);

                this.drawRect(((float) width / 2) + angle - 16F, 8, 0.8F, 6);
                this.drawRect(((float) width / 2) + angle - 30F, 8, 0.8F, 6);

                this.drawCenteredText(s, ((float) width / 2) + angle - 1.5F, 19, 8.5F, Fonts.MEDIUM);

                angle += 90;
            }

            for (double d = 0.0D; d <= 1.5D; d += 0.5D) {
                String s = "NW";

                if (d == 0.0D) {
                    s = "SW";
                }

                if (d == 1.0D) {
                    s = "NE";
                }

                if (d == 1.5D) {
                    s = "SE";
                }

                this.drawCenteredText(s, ((float) width / 2) + angle2 + 43F, 8.5F, 6.8F, Fonts.REGULAR);

                angle2 += 90;
            }

            for (double d = 0.0D; d <= 1.5D; d += 0.5D) {
                String s = "105";

                if (d == 0.0D) {
                    s = "15";
                }

                if (d == 1.0D) {
                    s = "195";
                }

                if (d == 1.5D) {
                    s = "285";
                }

                this.drawCenteredText(s, ((float) width / 2) + angle3 + 13F, 17, 5.4F, Fonts.REGULAR);

                angle3 += 90;
            }

            for (double d = 0.0D; d <= 1.5D; d += 0.5D) {
                String s = "120";

                if (d == 0.0D) {
                    s = "30";
                }

                if (d == 1.0D) {
                    s = "210";
                }

                if (d == 1.5D) {
                    s = "300";
                }

                this.drawCenteredText(s, ((float) width / 2) + angle4 + 27F, 17, 5.4F, Fonts.REGULAR);

                angle4 += 90;
            }

            for (double d = 0.0D; d <= 1.5D; d += 0.5D) {

                String s = "150";

                if (d == 0.0D) {
                    s = "60";
                }

                if (d == 1.0D) {
                    s = "240";
                }

                if (d == 1.5D) {
                    s = "300";
                }

                this.drawCenteredText(s, ((float) width / 2) + angle5 + 60.5F, 17, 5.4F, Fonts.REGULAR);

                angle5 += 90;
            }

            for (double d = 0.0D; d <= 1.5D; d += 0.5D) {

                String s = "165";

                if (d == 0.0D) {
                    s = "70";
                }

                if (d == 1.0D) {
                    s = "255";
                }

                if (d == 1.5D) {
                    s = "345";
                }

                this.drawCenteredText(s, ((float) width / 2) + angle6 + 74.5F, 17, 5.4F, Fonts.REGULAR);

                angle6 += 90;
            }
        }
    }

    private void renderMarker(float x, float y, Color color) {
        long vg = nvg.getContext();
        NVGColor nvgColor = nvg.getColor(color);

        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgMoveTo(vg, x, y + from(4 * scale));
        NanoVG.nvgLineTo(vg, x + from(4 * scale), y);
        NanoVG.nvgLineTo(vg, x - from(4 * scale), y);
        NanoVG.nvgClosePath(vg);

        NanoVG.nvgFillColor(vg, nvgColor);
        NanoVG.nvgFill(vg);
    }

    @Override
    public String getText() {

        String s = "Direction: ";
        double rotation = (mc.thePlayer.rotationYawHead - 90) % 360;

        if (rotation < 0) {
            rotation += 360.0;
        }

        if (0 <= rotation && rotation < 22.5) {
            return s + "W";
        }
        else if (22.5 <= rotation && rotation < 67.5) {
            return s + "NW";
        }
        else if (67.5 <= rotation && rotation < 112.5) {
            return s + "N";
        }
        else if (112.5 <= rotation && rotation < 157.5) {
            return s + "NE";
        }
        else if (157.5 <= rotation && rotation < 202.5) {
            return s + "E";
        }
        else if (202.5 <= rotation && rotation < 247.5) {
            return s + "SE";
        }
        else if (247.5 <= rotation && rotation < 292.5) {
            return s + "S";
        }
        else if (292.5 <= rotation && rotation < 337.5) {
            return s + "SW";
        }
        else if (337.5 <= rotation && rotation < 360.0) {
            return s + "W";
        }

        return s + "Whatt HELL";
    }

    @Override
    public String getIcon() {
        return TextureCode.COMPASS;
    }
}
