package com.lawab1ders.nan7i.kali.module.setting.impl;

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

import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;
import lombok.Getter;
import lombok.val;
import org.lwjgl.input.Keyboard;

@Getter
public class KeybindSetting extends ModuleSetting<Integer> {

    private final int defKeyCode;

    private int keyCode;

    public KeybindSetting(int keyCode) {
        super("module.opts.keybind");
        setKeyCode(keyCode);
        defKeyCode = keyCode;
    }

    @Override
    public void reset() {
        setKeyCode(defKeyCode);
    }

    public boolean isKeyDown() {
        return Keyboard.isKeyDown(keyCode) && mc.currentScreen == null;
    }

    public void setKeyCode(int keyCode) {
        if (keyCode == this.keyCode) return;

        val oldValue = this.keyCode;
        this.keyCode = keyCode == Keyboard.KEY_NONE? defKeyCode : keyCode;
        handleListener(oldValue, this.keyCode);
    }
}
