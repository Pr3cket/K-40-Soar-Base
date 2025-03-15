package com.lawab1ders.nan7i.kali.injection.core;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OptiFineCompatible {
    private static Class<?> OPTIFINE_LANG_CLASS;
    private static Method OPTIFINE_LANG_RELOADED_METHOD;

    static {
        try {
            OPTIFINE_LANG_CLASS = Class.forName("net.optifine.Lang");
            try {
                OPTIFINE_LANG_RELOADED_METHOD = OPTIFINE_LANG_CLASS.getDeclaredMethod("resourcesReloaded");
                OPTIFINE_LANG_RELOADED_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                OPTIFINE_LANG_RELOADED_METHOD = null;
            }
        } catch (ClassNotFoundException e) {
            OPTIFINE_LANG_CLASS = null;
        }
    }

    public static void callOptiFineReload() {
        if (OPTIFINE_LANG_CLASS != null && OPTIFINE_LANG_RELOADED_METHOD != null) {
            try {
                OPTIFINE_LANG_RELOADED_METHOD.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
