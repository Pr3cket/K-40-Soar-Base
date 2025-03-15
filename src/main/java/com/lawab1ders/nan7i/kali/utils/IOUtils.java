package com.lawab1ders.nan7i.kali.utils;

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
import lombok.experimental.UtilityClass;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.Objects;

@UtilityClass
public class IOUtils implements InstanceAccess {

    public static void copyStringToClipboard(String s) {
        StringSelection stringSelection = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

        if (Objects.equals(getStringFromClipboard(), s)) {
            ntf.post(
                    TranslateComponent.i18n("categories.copy"),
                    TranslateComponent.i18n("copy.noti.copy"),
                    NotificationType.SUCCESS
            );
        }
    }

    public static String getStringFromClipboard() {
        try {
            return Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static ByteBuffer resourceToByteBuffer(ResourceLocation location) {
        try {
            InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();
            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(inputStream);
            return bytesToByteBuffer(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ByteBuffer resourceToByteBuffer(File file) {
        try {
            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(Files.newInputStream(file.toPath()));
            return bytesToByteBuffer(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 提取公共逻辑，将字节数组转换为 ByteBuffer
    private static ByteBuffer bytesToByteBuffer(byte[] bytes) {
        ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
        data.flip();
        return data;
    }
}
