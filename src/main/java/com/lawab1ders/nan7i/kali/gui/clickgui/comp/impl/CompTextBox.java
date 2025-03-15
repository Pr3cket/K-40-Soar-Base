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

import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.Comp;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.CompBoxes;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.TextSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.IOUtils;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@Getter
public class CompTextBox extends Comp {

    protected final int maxStringLength;
    private final MillisTimer timer = new MillisTimer();
    private final Animation animation = new Animation();

    private final ModuleSetting setting;

    // 主体
    @Setter
    protected float width = 0, height = 0;
    protected String text;
    protected boolean focused;

    @Setter
    protected boolean lock = false;
    protected int cursorPosition;
    protected int selectionEnd;

    // 渲染
    @Getter
    @Setter
    private TranslateComponent defaultText = null;

    @Getter
    @Setter
    private String icon = null;

    @Setter
    private Color bgColor;

    public CompTextBox() {
        this(null);
    }

    public CompTextBox(ModuleSetting setting) {
        super(0, 0);

        this.setting = setting;
        this.focused = false;
        this.text = "";
        this.maxStringLength = 256;
        CompBoxes.boxes.add(this);
    }

    @Override
    public void draw(int mouseX, int mouseY) {

        float height = this.getHeight();
        int selectionEnd = this.getSelectionEnd();
        int cursorPosition = this.getCursorPosition();
        String text = this.getText();
        boolean focused = this.isFocused();

        float addX = 0;
        float halfHeight = height / 2F;

        int outTextSize = 0;
        String resultText = "";

        for (char c : this.getText().toCharArray()) {

            resultText = resultText + c;

            if (nvg.getTextWidth(
                    resultText, halfHeight, Fonts.REGULAR) + halfHeight + (icon == null ? 5 : 16) > this.getWidth()) {
                outTextSize++;

                addX = this.getWidth() - nvg.getTextWidth(
                        resultText, halfHeight, Fonts.REGULAR) - halfHeight - (icon == null ? 5 : 16);
            }
        }

        if (selectionEnd < outTextSize) {
            StringBuilder reversedText = new StringBuilder(this.getText()).reverse();

            addX = this.getWidth() - nvg.getTextWidth(
                    reversedText.substring(outTextSize - selectionEnd), halfHeight,
                    Fonts.REGULAR
            ) - halfHeight - (icon == null ? 5 : 16);
        }

        if (bgColor == null) {
            bgColor = palette.getBackgroundColor(ColorType.NORMAL);
        }

        nvg.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 6, bgColor);

        if (icon != null) {
            nvg.drawText(
                    icon, this.getX() + 5, this.getY() + (this.getHeight() / 2) - (nvg.getTextHeight(
                            text,
                            halfHeight, Fonts.REGULAR
                    ) / 2), palette.getFontColor(ColorType.NORMAL), halfHeight, Fonts.ICON
            );
        }

        nvg.save();
        nvg.scissor(
                this.getX() + (icon == null ? 0 : 16), this.getY(), this.getWidth() - (icon == null ? 0 : 16),
                this.getHeight()
        );

        if (cursorPosition != selectionEnd) {

            int start = Math.min(selectionEnd, cursorPosition);
            int end = Math.max(selectionEnd, cursorPosition);

            float selectionWidth = nvg.getTextWidth(this.getText().substring(start, end), halfHeight, Fonts.REGULAR);
            float offset = nvg.getTextWidth(this.getText().substring(0, start), halfHeight, Fonts.REGULAR);

            if (selectionWidth != 0) {
                nvg.drawRect(
                        this.getX() + 4 + offset + addX,
                        this.getY() + (this.getHeight() / 2) - (nvg.getTextHeight(text, halfHeight, Fonts.REGULAR) / 2),
                        selectionWidth + 2, nvg.getTextHeight(text, halfHeight, Fonts.REGULAR),
                        new Color(0, 135, 247, 85)
                );
            }
        }

        animation.setAnimation(!focused && this.getText().isEmpty() ? 1.0F : 0.0F, 16);

        if (this.getText().isEmpty() && defaultText != null) {
            nvg.save();
            nvg.translate(8 - (animation.getValue() * 8), 0);
            nvg.drawText(
                    defaultText.getText(), this.getX() + (icon == null ? 5 : 16),
                    this.getY() + (this.getHeight() / 2) - (nvg.getTextHeight(text, halfHeight, Fonts.REGULAR) / 2) + 1,
                    palette.getFontColor(ColorType.DARK, (int) (animation.getValue() * 255)), halfHeight, Fonts.REGULAR
            );
            nvg.restore();
        }

        nvg.drawText(
                this.getText(), this.getX() + (icon == null ? 5 : 16) + addX,
                this.getY() + (this.getHeight() / 2) - (nvg.getTextHeight(text, halfHeight, Fonts.REGULAR) / 2) + 1,
                palette.getFontColor(ColorType.DARK), halfHeight, Fonts.REGULAR
        );

        if (timer.delay(600)) {

            float position =
                    nvg.getTextWidth(this.getText(), halfHeight, Fonts.REGULAR) - nvg.getTextWidth(
                            this.getText().substring(cursorPosition), halfHeight, Fonts.REGULAR);

            if (focused && cursorPosition == selectionEnd) {
                nvg.drawRect(
                        this.getX() + (icon == null ? 5 : 16) + addX + position,
                        this.getY() + (this.getHeight() / 2) - (nvg.getTextHeight(text, halfHeight, Fonts.REGULAR) / 2),
                        0.7F, 10, palette.getFontColor(ColorType.DARK)
                );
            }

            if (timer.delay(1200)) {
                timer.reset();
            }
        }

        nvg.restore();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        this.setFocused(MouseUtils.isInside(
                mouseX, mouseY, this.getX(), this.getY(), this.getWidth(),
                this.getHeight()
        ));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {}

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!focused) {
            // Noting to do
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && keyCode == Keyboard.KEY_C) {
            // 复制
            IOUtils.copyStringToClipboard(this.getSelectedText());

        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && keyCode == Keyboard.KEY_V) {
            // 粘贴
            writeText(IOUtils.getStringFromClipboard());

        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && keyCode == Keyboard.KEY_X) {
            // 剪切
            IOUtils.copyStringToClipboard(this.getSelectedText());
            this.setText("");

        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && keyCode == Keyboard.KEY_A) {
            // 全选
            this.setCursorPosition(this.text.length());
            this.setSelectionPos(0);

        }
        else {
            switch (keyCode) {
                case Keyboard.KEY_ESCAPE:
                    setFocused(false);
                    return;

                case Keyboard.KEY_BACK:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        this.deleteWords(-1);

                    }
                    else {
                        this.deleteFromCursor(-1);
                    }

                    return;

                case Keyboard.KEY_HOME:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        this.setSelectionPos(0);
                    }
                    else {
                        this.setCursorPosition(0);
                    }

                    return;

                case Keyboard.KEY_LEFT:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.selectionEnd));
                        }
                        else {
                            this.setSelectionPos(this.selectionEnd - 1);
                        }
                    }
                    else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    }
                    else {
                        this.moveCursorBy(-1);
                    }

                    return;

                case Keyboard.KEY_RIGHT:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            this.setSelectionPos(this.getNthWordFromPos(1, this.selectionEnd));

                        }
                        else {
                            this.setSelectionPos(this.selectionEnd + 1);
                        }

                    }
                    else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        this.setCursorPosition(this.getNthWordFromCursor(1));

                    }
                    else {
                        this.moveCursorBy(1);
                    }

                    return;

                case Keyboard.KEY_END:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        this.setSelectionPos(this.text.length());

                    }
                    else {
                        this.setCursorPosition(this.text.length());
                    }

                    return;

                case Keyboard.KEY_DELETE:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        this.deleteWords(1);

                    }
                    else {
                        this.deleteFromCursor(1);
                    }

                    return;

                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                        this.writeText(Character.toString(typedChar));
                    }
            }
        }
    }

    private void writeText(String text) {
        String s = "";
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(text);
        int min = Math.min(this.cursorPosition, this.selectionEnd);
        int max = Math.max(this.cursorPosition, this.selectionEnd);
        int len = this.maxStringLength - this.text.length() - (min - max);
        int l;

        if (!this.text.isEmpty()) {
            s = s + this.text.substring(0, min);
        }

        if (len < s1.length()) {
            s = s + s1.substring(0, len);
            l = len;
        }
        else {
            s = s + s1;
            l = s1.length();
        }

        if (!this.text.isEmpty() && max < this.text.length()) {
            s = s + this.text.substring(max);
        }

        this.text = s;
        this.moveCursorBy(min - this.selectionEnd + l);
    }

    private void deleteWords(int num) {
        if (this.text.isEmpty()) return;

        if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");

        }
        else {
            this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
        }
    }

    private String getSelectedText() {
        int min = Math.min(this.cursorPosition, this.selectionEnd);
        int max = Math.max(this.cursorPosition, this.selectionEnd);

        return this.text.substring(min, max);
    }

    private void deleteFromCursor(int num) {

        if (this.text.isEmpty()) return;

        if (this.selectionEnd != this.cursorPosition) {
            this.writeText("");
        }
        else {

            boolean negative = num < 0;
            int i = negative ? this.cursorPosition + num : this.cursorPosition;
            int j = negative ? this.cursorPosition : this.cursorPosition + num;
            String s = "";

            if (i > 0) {
                s = this.text.substring(0, i);
            }

            if (j < this.text.length()) {
                s = s + this.text.substring(j);
            }

            this.text = s;

            if (negative) {
                this.moveCursorBy(num);
            }
        }
    }

    private int getNthWordFromCursor(int num) {
        return getNthWordFromPos(num, this.cursorPosition);
    }

    private int getNthWordFromPos(int num, int pos) {
        int i = pos;
        boolean negative = num < 0;
        int j = Math.abs(num);

        for (int k = 0; k < j; ++k) {
            if (negative) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);

                if (i == -1) {
                    i = l;

                }
                else {
                    while (i < l && this.text.charAt(i) == 32) {
                        ++i;
                    }
                }
            }
            else {
                while (i > 0 && this.text.charAt(i - 1) == 32) {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != 32) {
                    --i;
                }
            }
        }

        return i;
    }

    private void moveCursorBy(int i) {
        this.setCursorPosition(this.selectionEnd + i);
    }

    private void setCursorPosition(int i) {

        this.cursorPosition = i;

        int len = this.text.length();

        this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, len);
        this.setSelectionPos(this.cursorPosition);
    }

    private void setSelectionPos(int selectionPos) {

        int len = this.text.length();

        if (selectionPos > len) {
            selectionPos = len;
        }

        if (selectionPos < 0) {
            selectionPos = 0;
        }

        this.selectionEnd = selectionPos;
    }

    public void setPosition(float x, float y, float width, float height) {
        this.setX(x);
        this.setY(y);
        this.width = width;
        this.height = height;
    }

    public void setText(String text) {
        this.text = text;
        this.setCursorPosition(this.getText().length());
    }

    public void setFocused(boolean focused) {
        if (lock) return;

        if (focused) {
            CompBoxes.boxes.forEach(compTextBox -> compTextBox.focused = false);
        }
        else if (setting != null && this.focused) {
            ((TextSetting) setting).setText(getText());
        }

        this.focused = focused;
    }
}
