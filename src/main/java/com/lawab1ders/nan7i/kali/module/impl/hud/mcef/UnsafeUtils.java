package com.lawab1ders.nan7i.kali.module.impl.hud.mcef;

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

import org.cef.OS;
import sun.misc.Unsafe;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class UnsafeUtils {

    private static final Unsafe theUnsafe;

    private static final Field keyCode;
    private static final Field keyChar;
    private static final Field keyLocation;
    private static final Field keyScanCode;
    private static final Field keyRawCode;
    private static final Field id;
    private static final Field when;
    private static final Field modifs;

    private static final long offsetCode;
    private static final long offsetChar;
    private static final long offsetLocation;
    private static final long offsetScanCode;
    private static final long offsetRawCode;
    private static final long offsetId;
    private static final long offsetWhen;
    private static final long offsetModifs;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            theUnsafe = (Unsafe) f.get(null);

            keyCode = KeyEvent.class.getDeclaredField("keyCode");
            keyChar = KeyEvent.class.getDeclaredField("keyChar");
            keyLocation = KeyEvent.class.getDeclaredField("keyLocation");
            keyRawCode = KeyEvent.class.getDeclaredField("rawCode");
            id = AWTEvent.class.getDeclaredField("id");
            when = InputEvent.class.getDeclaredField("when");
            modifs = InputEvent.class.getDeclaredField("modifiers");

            offsetCode = theUnsafe.objectFieldOffset(keyCode);
            offsetChar = theUnsafe.objectFieldOffset(keyChar);
            offsetLocation = theUnsafe.objectFieldOffset(keyLocation);
            offsetRawCode = theUnsafe.objectFieldOffset(keyRawCode);
            offsetId = theUnsafe.objectFieldOffset(id);
            offsetWhen = theUnsafe.objectFieldOffset(when);
            offsetModifs = theUnsafe.objectFieldOffset(modifs);

            if (OS.isWindows()) {
                keyScanCode = KeyEvent.class.getDeclaredField("scancode");
                offsetScanCode = theUnsafe.objectFieldOffset(keyScanCode);
            }
            else {
                keyScanCode = null;
                offsetScanCode = 0;
            }
        } catch (Throwable e) {
            throw new RuntimeException("Unable to get theUnsafe.");
        }
    }

    public static KeyEvent makeEvent(Component src, int keyCode, char keyChar, int location, int type, long time,
                                     int modifs, long scanCode) {
        return makeEvent(src, keyCode, keyChar, location, type, time, modifs, scanCode, keyCode);
    }

    public static KeyEvent makeEvent(Component src, int keyCode, char keyChar, int location, int type, long time,
                                     int modifs, long scanCode, long raw) {

        KeyEvent event = new KeyEvent(
                src, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_E, 'e', KeyEvent.KEY_LOCATION_STANDARD);
        theUnsafe.putInt(event, offsetCode, keyCode);
        theUnsafe.putInt(event, offsetChar, keyChar);
        theUnsafe.putInt(event, offsetLocation, location);
        theUnsafe.putInt(event, offsetId, type);
        theUnsafe.putInt(event, offsetModifs, modifs);
        theUnsafe.putLong(event, offsetWhen, time);
        theUnsafe.putLong(event, offsetRawCode, raw);

        if (offsetScanCode != 0) {
            theUnsafe.putLong(event, offsetScanCode, scanCode);
        }

        return event;
    }
}