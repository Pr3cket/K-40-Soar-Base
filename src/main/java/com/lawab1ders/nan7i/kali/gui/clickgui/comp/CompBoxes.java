package com.lawab1ders.nan7i.kali.gui.clickgui.comp;

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

import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompTextBox;

import java.util.ArrayList;

/**
 * 当一个输入框 focused 的时候，就需要退出其他输入框的输入
 * 创建一个列表储存所有实例，当某个输入框 focused 时遍历
 */
public class CompBoxes {

    public static final ArrayList<CompTextBox> boxes = new ArrayList<>();

    public static boolean wasResetKeybinding = false;

//    public static final ArrayList<CompKeybind> keys = new ArrayList<>();
//
//    public static boolean isKeybinding() {
//        for (CompKeybind key : keys) {
//            if (key.isBinding()) return true;
//        }
//
//        return false;
//    }
}
