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

import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;

import java.util.Calendar;

@IModule(
        name = "module.calendar",
        description = "module.calendar.desc",
        category = EModuleCategory.HUD
)
public class CalendarModule extends HUDModule {

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        nvg.setupAndDraw(this::drawNanoVG);
    }

    private void drawNanoVG() {
        Calendar calendar = Calendar.getInstance();
        AccentColor currentColor = col.getCurrentColor();

        String[] dayOfWeek = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar firstDayCalendar = (Calendar) calendar.clone();
        firstDayCalendar.set(year, month, 1);

        int weekDay = firstDayCalendar.get(Calendar.DAY_OF_WEEK);

        // 计算从该月第一天开始，到该月结束总共跨越的天数
        int totalDays = weekDay + maxDay - 1;
        // 计算完整的星期数
        int fullWeeks = (totalDays - 1) / 7;

        // 处理最后一天刚好是星期日的情况，如果是则减去 1
        if (totalDays % 7 == 0) {
            fullWeeks--;
        }

        this.drawBackground(100, fullWeeks < 5 ? 97 : 110);

        float offsetX = 0, offsetY = 0;

        this.drawText(getMonthByNumber(month) + " " + year, 6, 6, 11, Fonts.MEDIUM);

        for (String s : dayOfWeek) {
            this.drawText(s, 6 + offsetX, 22, 6.5F, Fonts.MEDIUM);

            offsetX += 13.4F;
        }

        offsetX = (weekDay - 1) * 13.4F;

        for (int i = 1; i <= maxDay; i++) {
            if (i == day) {
                float scale = 10.5F;

                this.drawRoundedRect(10 + offsetX - scale / 2, 30 + offsetY, scale, scale, scale / 2);
            }

            this.drawCenteredText(String.valueOf(i), 10 + offsetX, 33 + offsetY, 6, Fonts.REGULAR, i == day ?
                    currentColor.getInterpolateColor() : this.getFontColor());

            offsetX += 13.4F;

            if (weekDay % 7 == 0 && i != maxDay) {
                offsetY += 13.4F;
                offsetX = 0;
            }

            weekDay++;
        }
    }

    private String getMonthByNumber(int month) {

        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }

        return "Whatt HELL";
    }
}
