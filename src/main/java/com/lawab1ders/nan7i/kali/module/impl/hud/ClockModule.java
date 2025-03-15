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
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@IModule(
        name = "module.clock",
        description = "module.clock.desc",
        category = EModuleCategory.HUD
)
public class ClockModule extends SimpleHUDModule {

    public final EnumSetting modeSetting = new EnumSetting(
            "module.opts.design",
            "module.opts.design.simple", "module.opts.design.fancy"
    );

    private final DateFormat df = new SimpleDateFormat("HH:mm a", Locale.US);

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        if (modeSetting.getSelectedEntryKey().equals("module.opts.design.fancy")) {
            this.setSize(128, 128);
            nvg.setupAndDraw(this::drawNanoVG);
        }
        else super.onOverlayRendered(event);
    }

    @Override
    public String getText() {
        return df.format(Calendar.getInstance().getTime());
    }

    @Override
    public String getIcon() {
        return TextureCode.CLOCK;
    }

    private void drawNanoVG() {
        float center = (float) 128 / 2;
        float scaledSize = from(128 * scale);
        float radius = center * scale;

        float lineLength = 4 * scale;
        int index = 0;

        String[] numbers = { "3", "6", "9", "12" };
        Calendar c = Calendar.getInstance();

        this.drawBackground(128, 128, radius);

        nvg.drawCircle(x + (scaledSize / 2), y + (scaledSize / 2), from(2 * scale), this.getFontColor());

        for (int i = 0; i < 4; i++) {
            float angle = (float) Math.toRadians(360.0 / 4.0 * i);

            float textX = center + ((radius / scale) - 6F) * (float) Math.cos(angle);
            float textY = center + ((radius / scale) - 6F) * (float) Math.sin(angle);

            this.drawCenteredText(numbers[i], textX + 0.5F, textY - 3, 8, Fonts.MEDIUM);
        }

        NVGColor nvgColor = nvg.getColor(this.getFontColor());
        NVGColor nvgColor2 = nvg.getColor(this.getFontColor(180));

        for (int i = 0; i < 12; i++) {
            if (i == index * 3) {
                index++;
                continue;
            }

            float angle = (float) Math.toRadians(360.0 / 12.0 * i);

            float startX = from(center * scale) + from(radius - lineLength - 4) * (float) Math.cos(angle);
            float startY = from(center * scale) + from(radius - lineLength - 4) * (float) Math.sin(angle);
            float endX = from(center * scale) + from(radius - 4) * (float) Math.cos(angle);
            float endY = from(center * scale) + from(radius - 4) * (float) Math.sin(angle);

            NanoVG.nvgBeginPath(nvg.getContext());
            NanoVG.nvgMoveTo(nvg.getContext(), x + startX, y + startY);
            NanoVG.nvgLineTo(nvg.getContext(), x + endX, y + endY);
            NanoVG.nvgStrokeColor(nvg.getContext(), nvgColor);
            NanoVG.nvgStrokeWidth(nvg.getContext(), from(scale));
            NanoVG.nvgStroke(nvg.getContext());
        }

        index = 0;
        lineLength = 2 * scale;

        for (int i = 0; i < 60; i++) {
            if (i == index * 5) {
                index++;
                continue;
            }

            float angle = (float) Math.toRadians(360.0 / 60.0 * i);

            float startX = from(center * scale) + from(radius - lineLength - 6) * (float) Math.cos(angle);
            float startY = from(center * scale) + from(radius - lineLength - 6) * (float) Math.sin(angle);
            float endX = from(center * scale) + from(radius - 6) * (float) Math.cos(angle);
            float endY = from(center * scale) + from(radius - 6) * (float) Math.sin(angle);

            NanoVG.nvgBeginPath(nvg.getContext());
            NanoVG.nvgMoveTo(nvg.getContext(), x + startX, y + startY);
            NanoVG.nvgLineTo(nvg.getContext(), x + endX, y + endY);
            NanoVG.nvgStrokeColor(nvg.getContext(), nvgColor2);
            NanoVG.nvgStrokeWidth(nvg.getContext(), from(0.5F * scale));
            NanoVG.nvgStroke(nvg.getContext());
        }

        float secondAngle = (float) Math.toRadians(360.0 / 60.0 * c.get(Calendar.SECOND)) - (float) Math.toRadians(90);

        float secondX = from(center * scale) + from(radius - (14F * scale)) * (float) Math.cos(secondAngle);
        float secondY = from(center * scale) + from(radius - (14F * scale)) * (float) Math.sin(secondAngle);

        float minuteAngle = (float) Math.toRadians(360.0 / 60.0 * c.get(Calendar.MINUTE));
        float hourAngle = (float) Math.toRadians(360.0 / 12.0 * (c.get(Calendar.HOUR) + c.get(Calendar.MINUTE) / 60.0));

        nvg.drawCircle(x + secondX, y + secondY, from(1.3F * scale), this.getFontColor());

        nvg.save();
        nvg.rotate(x, y, scaledSize, scaledSize, minuteAngle - (float) Math.toRadians(90));
        nvg.drawRoundedRect(
                x + (scaledSize / 2) - from(6 * scale), y + (scaledSize / 2) - from(scale),
                from(48 * scale), from(2 * scale), from(scale), this.getFontColor()
        );
        nvg.restore();

        nvg.save();
        nvg.rotate(x, y, scaledSize, scaledSize, hourAngle - (float) Math.toRadians(90));
        nvg.drawRoundedRect(
                x + (scaledSize / 2) - from(6 * scale), y + (scaledSize / 2) - from(scale),
                from(38 * scale), from(2 * scale), from(scale), this.getFontColor()
        );
        nvg.restore();
    }
}