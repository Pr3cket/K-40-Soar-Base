package com.lawab1ders.nan7i.kali.gui.clickgui.impl;

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
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.gui.clickgui.Category;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import com.lawab1ders.nan7i.kali.utils.screenshot.Screenshot;
import com.lawab1ders.nan7i.kali.utils.screenshot.ScreenshotManager;

import java.awt.*;
import java.io.IOException;

public class ScreenshotCategory extends Category {

    private final Animation leftAnimation = new Animation(), rightAnimation = new Animation(), trashAnimation =
            new Animation();

    public ScreenshotCategory(GuiClickMenu parent) {
        super(parent, "categories.screenshot", TextureCode.CAMERA, false);
    }

    @Override
    public void initCategory() {
        ScreenshotManager.update();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        AccentColor accentColor = col.getCurrentColor();
        Screenshot currentScreenshot = ScreenshotManager.getCurrentScreenshot();

        int addX = 42;
        int addY = 12;
        int offsetX = 0;
        int index = 1;

        leftAnimation.setAnimation(
                MouseUtils.isInside(
                        mouseX, mouseY, this.getX(), this.getY(), 42,
                        this.getHeight()
                ) ? 1.0F : 0.0F, 16
        );
        rightAnimation.setAnimation(
                MouseUtils.isInside(
                        mouseX, mouseY, this.getX() + this.getWidth() - 42,
                        this.getY(), 42, this.getHeight()
                ) ? 1.0F : 0.0F, 16
        );

        if (!ScreenshotManager.getScreenshots().isEmpty()) {

            trashAnimation.setAnimation(
                    MouseUtils.isInside(
                            mouseX, mouseY, this.getX() + addX, this.getY() + addY,
                            this.getWidth() - (addX * 2), this.getHeight() - (addY * 2) - 38
                    ) ? 1.0F : 0.0F, 16
            );

            nvg.drawRoundedImage(
                    currentScreenshot.getFile(), this.getX() + addX, this.getY() + addY,
                    this.getWidth() - (addX * 2), this.getHeight() - (addY * 2) - 38, 6, 1
            );
            nvg.drawText(
                    TextureCode.TRASH, this.getX() + this.getWidth() - 59, this.getY() + addY + 6,
                    palette.getMaterialRed((int) (trashAnimation.getValue() * 255)), 12, Fonts.ICON
            );

            addX = 58;

            nvg.drawRoundedRect(
                    this.getX() + addX, this.getY() + this.getHeight() - 40, this.getWidth() - (addX * 2)
                    , 30, 6, palette.getBackgroundColor(ColorType.DARK)
            );

            nvg.save();
            nvg.translate(scroll.getValue(), 0);

            for (Screenshot s : ScreenshotManager.getScreenshots()) {

                int alpha = (int) (s.getSelectAnimation().getValue() * 255);

                if (offsetX + scroll.getValue() + 30 > 0 && offsetX + scroll.getValue() < this.getWidth() - 100) {

                    nvg.drawRoundedRect(
                            this.getX() + offsetX + 62, this.getY() + this.getHeight() - 36, 23, 23, 5,
                            palette.getBackgroundColor(ColorType.NORMAL)
                    );

                    nvg.save();
                    nvg.scale(this.getX() + offsetX + 62, this.getY() + this.getHeight() - 31, 0.07F);
                    nvg.drawImage(
                            s.getFile(), this.getX() + offsetX + 62, this.getY() + this.getHeight() - 31,
                            16 * 20, 9 * 20
                    );
                    nvg.restore();

                    s.getSelectAnimation().setAnimation(currentScreenshot.equals(s) ? 1.0F : 0.0F, 16);

                    nvg.drawGradientOutlineRoundedRect(
                            this.getX() + offsetX + 62,
                            this.getY() + this.getHeight() - 36, 23, 23, 5, s.getSelectAnimation().getValue() * 1.2F,
                            ColorUtils.applyAlpha(accentColor.getColor1(), alpha),
                            ColorUtils.applyAlpha(accentColor.getColor2(), alpha)
                    );
                }

                offsetX += 27;
                index++;
            }


            nvg.restore();

            nvg.drawRect(
                    this.getX(), this.getY() + this.getHeight() - 40, addX, 30,
                    palette.getBackgroundColor(ColorType.NORMAL)
            );
            nvg.drawRect(
                    this.getX() + this.getWidth() - addX, this.getY() + this.getHeight() - 40, addX - 14, 30,
                    palette.getBackgroundColor(ColorType.NORMAL)
            );

            float leftValue = leftAnimation.getValue();
            float rightValue = rightAnimation.getValue();

            if (ScreenshotManager.getScreenshots().size() > 1) {

                nvg.save();
                nvg.translate(10 - (leftValue * 10), 0);

                nvg.drawRoundedRect(
                        this.getX() + 20, this.getY() + ((float) this.getHeight() / 2) - 30.5F, 12, 24, 4
                        , palette.getBackgroundColor(ColorType.DARK, (int) (leftValue * 255))
                );
                nvg.drawText(
                        "<", this.getX() + 23F, this.getY() + ((float) this.getHeight() / 2) - 22F,
                        palette.getFontColor(ColorType.DARK, (int) (leftValue * 255)), 9, Fonts.DEMIBOLD
                );

                nvg.restore();

                nvg.save();
                nvg.translate(-10 + (rightValue * 10), 0);

                nvg.drawRoundedRect(
                        this.getX() + this.getWidth() - 32,
                        this.getY() + ((float) this.getHeight() / 2) - 30.5F, 12, 24, 4,
                        palette.getBackgroundColor(ColorType.DARK, (int) (rightValue * 255))
                );
                nvg.drawText(
                        ">", this.getX() + this.getWidth() - 29,
                        this.getY() + ((float) this.getHeight() / 2) - 22F, palette.getFontColor(
                                ColorType.DARK,
                                (int) (rightValue * 255)
                        ), 9, Fonts.DEMIBOLD
                );

                nvg.restore();
            }
        }
        else {

            nvg.drawRoundedRect(
                    this.getX() + addX, this.getY() + addY, this.getWidth() - (addX * 2),
                    this.getHeight() - (addY * 2) - 38, 6, palette.getBackgroundColor(ColorType.DARK)
            );
            nvg.drawCenteredText(
                    TextureCode.CAMERA,
                    this.getX() + addX + ((float) (this.getWidth() - (addX * 2)) / 2), this.getY() + 68,
                    palette.getFontColor(ColorType.NORMAL), 64, Fonts.ICON
            );

            addX = 58;

            nvg.drawRoundedRect(
                    this.getX() + addX, this.getY() + this.getHeight() - 40, this.getWidth() - (addX * 2)
                    , 30, 6, palette.getBackgroundColor(ColorType.DARK)
            );
        }

        scroll.setMaxScroll(index > 12 ? (index - 12) * 27 : 0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        Screenshot currentScreenshot = ScreenshotManager.getCurrentScreenshot();

        if (ScreenshotManager.getScreenshots().isEmpty()) return;

        int offsetX = (int) scroll.getValue();
        int addX;
        int addY = 12;

        if (MouseUtils.isInside(
                mouseX, mouseY, this.getX() + this.getWidth() - 61, this.getY() + addY + 4.5F, 16,
                16
        )) {
            ScreenshotManager.delete();
            return;
        }

        addX = 58;

        for (Screenshot s : ScreenshotManager.getScreenshots()) {

            if (MouseUtils.isInside(
                    mouseX, mouseY, this.getX() + offsetX + 62, this.getY() + this.getHeight() - 36,
                    23, 23
            ) &&
                    MouseUtils.isInside(
                            mouseX, mouseY, this.getX() + addX, this.getY() + this.getHeight() - 40,
                            this.getWidth() - (addX * 2), 30
                    )) {

                ScreenshotManager.setCurrentScreenshot(s);
                return;
            }

            offsetX += 27;
        }

        if (MouseUtils.isInside(
                mouseX, mouseY, this.getX() + addX, this.getY() + addY, this.getWidth() - addX * 2,
                this.getHeight() - addY * 2 - 38
        )) {
            try {
                Desktop.getDesktop().open(currentScreenshot.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (ScreenshotManager.getScreenshots().size() > 1) {

            if (MouseUtils.isInside(
                    mouseX, mouseY, this.getX() + 20,
                    this.getY() + ((double) this.getHeight() / 2) - 30.5F, 12, 24
            )) {
                ScreenshotManager.last();
            }
            else if (MouseUtils.isInside(
                    mouseX, mouseY, this.getX() + this.getWidth() - 32,
                    this.getY() + ((double) this.getHeight() / 2) - 30.5F, 12, 24
            )) {
                ScreenshotManager.next();
            }
        }
    }
}
