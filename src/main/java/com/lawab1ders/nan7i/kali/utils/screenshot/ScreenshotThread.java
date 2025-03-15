package com.lawab1ders.nan7i.kali.utils.screenshot;

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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotThread extends Thread {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    private final int width, height;
    private final int[] pixelValues;
    private final File screenshot;

    public ScreenshotThread(int width, int height, int[] pixelValues, File screenshot) {
        this.width = width;
        this.height = height;
        this.pixelValues = pixelValues;
        this.screenshot = screenshot;
    }

    public static File getTimestampedPNGFileForDirectory(File p_getTimestampedPNGFileForDirectory_0_) {
        String lvt_2_1_ = dateFormat.format(new Date());
        int lvt_3_1_ = 1;

        while (true) {
            File lvt_1_1_ = new File(p_getTimestampedPNGFileForDirectory_0_, lvt_2_1_ + (lvt_3_1_ == 1 ? "" :
                    "_" + lvt_3_1_) + ".png");
            if (!lvt_1_1_.exists()) {
                return lvt_1_1_;
            }

            ++lvt_3_1_;
        }
    }

    @Override
    public void run() {
        processPixelValues(pixelValues, width, height);

        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, width, height, pixelValues, 0, width);
            ImageIO.write(image, "png", screenshot);
        } catch (Exception ignored) {

        }
    }

    private void processPixelValues(int[] pixels, int displayWidth, int displayHeight) {
        final int[] xValues = new int[displayWidth];

        for (int yValues = displayHeight >> 1, val = 0; val < yValues; ++val) {
            System.arraycopy(pixels, val * displayWidth, xValues, 0, displayWidth);

            System.arraycopy(
                    pixels,
                    (displayHeight - 1 - val) * displayWidth,
                    pixels,
                    val * displayWidth,
                    displayWidth
            );

            System.arraycopy(xValues, 0, pixels, (displayHeight - 1 - val) * displayWidth, displayWidth);
        }
    }
}
