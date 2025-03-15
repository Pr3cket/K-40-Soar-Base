package com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl;

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
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.Comp;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.CompBoxes;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class CompKeybind extends Comp {

    public final KeybindSetting setting;

    private final float width;
    private boolean binding;

    public CompKeybind(float width, KeybindSetting setting) {
        super(0, 0);
        this.width = width;
        this.setting = setting;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        AccentColor accentColor = col.getCurrentColor();

        String info = binding ? "Binding..." : Keyboard.getKeyName(setting.getKeyCode());

        nvg.drawGradientRoundedRect(
                this.getX(), this.getY(), width, 16, 4, accentColor.getColor1(),
                accentColor.getColor2()
        );

        nvg.drawCenteredText(
                info, this.getX() + (width / 2), this.getY() + 5F, new Color(255, 255, 255), 8,
                Fonts.REGULAR
        );
    }

    @Override
    public void mouseClicked(int mouseX, int mosueY) {
        if (MouseUtils.isInside(mouseX, mosueY, this.getX(), this.getY(), width, 16)) {
            binding = !binding;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {}

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!binding) return; // if not binding, return (don)

        if (keyCode == Keyboard.KEY_ESCAPE) {
            setting.setKeyCode(Keyboard.KEY_NONE);
            binding = false;

            CompBoxes.wasResetKeybinding = true;
            return;
        }

        setting.setKeyCode(keyCode);
        binding = false;
    }
}
