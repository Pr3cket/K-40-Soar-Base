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

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;

@UtilityClass
public class ScreenshotManager implements InstanceAccess {

    @Getter
    private static final ArrayList<Screenshot> screenshots = new ArrayList<>();

    @Getter
    private static final File root = new File(mc.mcDataDir, "screenshots");

    @Getter @Setter
    private static Screenshot currentScreenshot;

    public static void update() {

        FilenameFilter filter = (dir, name) -> name.endsWith("png");
        File[] files = root.listFiles(filter);

        if (files == null || files.length == 0) {
            // 使用迭代器来遍历并移除元素，避免 ConcurrentModificationException
            Iterator<Screenshot> iterator = screenshots.iterator();

            while (iterator.hasNext()) {
                Screenshot s = iterator.next();
                nvg.getAssetsManager().getImageCache().remove(s.getFile().getName());
                iterator.remove();
            }

            currentScreenshot = null;
            return;
        }

        for (File file : files) {
            Screenshot s = null;

            // 使用迭代器来遍历查找匹配的元素

            for (Screenshot screenshot : screenshots) {
                if (screenshot.getFile().getName().equals(file.getName())) {
                    s = screenshot;
                    break;
                }
            }

            if (s != null) {
                if (file.lastModified() != s.getLastModified()) {
                    nvg.getAssetsManager().getImageCache().remove(s.getFile().getName());
                    nvg.loadImage(file);
                    s.setLastModified(file.lastModified());
                }
            } else {
                screenshots.add(new Screenshot(file));
                nvg.loadImage(file);
            }
        }

        if (files.length != screenshots.size()) {
            // 使用迭代器来遍历并进行相应的删除等操作
            Iterator<Screenshot> iterator = screenshots.iterator();

            while (iterator.hasNext()) {
                Screenshot s = iterator.next();

                if (!s.getFile().exists()) {
                    int index = Math.max(0, screenshots.indexOf(currentScreenshot) - 1);

                    iterator.remove();
                    nvg.getAssetsManager().getImageCache().remove(s.getFile().getName());

                    if (currentScreenshot == s) {
                        currentScreenshot = screenshots.isEmpty() ? null : screenshots.get(index);
                    }
                }
            }
        }

        if (!screenshots.isEmpty() && currentScreenshot == null) {
            currentScreenshot = screenshots.get(0);
        }
    }

    public static void next() {
        int max = screenshots.size();
        int index = screenshots.indexOf(currentScreenshot);

        if (index < max - 1) {
            index++;
        } else {
            index = 0;
        }

        currentScreenshot = screenshots.get(index);
    }

    public static void last() {
        int max = screenshots.size();
        int index = screenshots.indexOf(currentScreenshot);

        if (index > 0) {
            index--;
        } else {
            index = max - 1;
        }

        currentScreenshot = screenshots.get(index);
    }

    public static void delete() {
        boolean delete = currentScreenshot.getFile().delete();

        if (delete) {
            ntf.post(
                    TranslateComponent.i18n("categories.screenshot"),
                    TranslateComponent.i18n("screenshot.noti.delete")
                            + " "
                            + currentScreenshot.getName()
                            + ".png",
                    NotificationType.SUCCESS
            );

            // 使用迭代器来删除元素，避免 ConcurrentModificationException
            Iterator<Screenshot> iterator = screenshots.iterator();
            int index = Math.max(0, screenshots.indexOf(currentScreenshot) - 1);

            while (iterator.hasNext()) {
                Screenshot s = iterator.next();

                if (s == currentScreenshot) {
                    iterator.remove();
                    nvg.getAssetsManager().getImageCache().remove(currentScreenshot.getFile().getName());
                    currentScreenshot = screenshots.isEmpty() ? null : screenshots.get(index);
                    break;
                }
            }
        } else {
            ntf.post(
                    TranslateComponent.i18n("categories.screenshot"),
                    TranslateComponent.i18n("misc.noti.fail"),
                    NotificationType.ERROR
            );
        }
    }
}
