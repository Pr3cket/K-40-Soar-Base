package com.lawab1ders.nan7i.kali.gui;

/*
 * Copyright (c) 2025 EldoDebug, Nan7.南起
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
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompTextBox;
import com.lawab1ders.nan7i.kali.module.impl.hud.mcef.McefBrowser;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.PunycodeUtils;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiWebBrowser extends GuiScreen implements InstanceAccess {

    private int offsetY;
    private McefBrowser browser;
    private CompTextBox searchBox;

    public GuiWebBrowser(McefBrowser browser) {
        this.browser = browser;
    }

    @Override
    public void initGui() {

        offsetY = 24;

        if (browser != null) {
            resizeBrowser();
        }

        searchBox = new CompTextBox();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        nvg.setupAndDraw(() -> drawNanoVG(mouseX, mouseY, partialTicks));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawNanoVG(int mouseX, int mouseY, float partialTicks) {

        nvg.drawRect(0, 0, width, height, palette.getBackgroundColor(ColorType.NORMAL));

        if (browser != null) {

            nvg.drawImage(browser.getTexture(), 0, offsetY, width, height - offsetY);

            nvg.drawRoundedRect(6, 4, 16, 16, 8, palette.getBackgroundColor(ColorType.DARK));
            nvg.drawRoundedRect(26, 4, 16, 16, 8, palette.getBackgroundColor(ColorType.DARK));
            nvg.drawRoundedRect(46, 4, 16, 16, 8, palette.getBackgroundColor(ColorType.DARK));
            nvg.drawRoundedRect(width - 6 - 16, 4, 16, 16, 8, palette.getBackgroundColor(ColorType.DARK));

            nvg.drawText(TextureCode.ARROW_LEFT, 8.5F, 6.5F, palette.getFontColor(ColorType.DARK), 11.5F, Fonts.ICON);
            nvg.drawText(TextureCode.ARROW_RIGHT, 28.5F, 6.5F, palette.getFontColor(ColorType.DARK), 11.5F, Fonts.ICON);
            nvg.drawText(TextureCode.REFRESH, 48.5F, 6.5F, palette.getFontColor(ColorType.DARK), 11.5F, Fonts.ICON);
            nvg.drawText(TextureCode.X, width - 19.5F, 6.5F, palette.getFontColor(ColorType.DARK), 11.5F, Fonts.ICON);

            if (!searchBox.isFocused()) {
                searchBox.setText(browser.getURL());
            }

            searchBox.setPosition(68.5F, 4, width - 97, 16);
            searchBox.draw(0, 0);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0 && mouseButton != 1) {
            return;
        }

        if (MouseUtils.isInside(mouseX, mouseY, 6, 4, 16, 16) && browser.canGoBack()) {
            browser.goBack();
        }

        if (MouseUtils.isInside(mouseX, mouseY, 26, 4, 16, 16) && browser.canGoForward()) {
            browser.goForward();
        }

        if (MouseUtils.isInside(mouseX, mouseY, 46, 4, 16, 16)) {
            browser.reload();
        }

        if (MouseUtils.isInside(mouseX, mouseY, width - 6 - 16, 4, 16, 16)) {
            InstanceAccess.mc.displayGuiScreen(null);
        }

        searchBox.mouseClicked(mouseX, mouseY);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (searchBox.isFocused() && keyCode == Keyboard.KEY_RETURN) {
            searchBox.setFocused(false);
            browser.loadURL(PunycodeUtils.punycode(searchBox.getText()));
        }

        searchBox.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void handleInput() {

        while (Keyboard.next()) {

            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                InstanceAccess.mc.displayGuiScreen(null);
                return;
            }

            boolean pressed = Keyboard.getEventKeyState();
            char key = Keyboard.getEventCharacter();
            int num = Keyboard.getEventKey();

            if (browser != null) {

                if (pressed) {
                    browser.sendKeyPressed(num, key, 0);
                }
                else {
                    browser.sendKeyReleased(num, key, 0);
                }

                if (key != 0) {
                    browser.sendKeyTyped(key, 0);
                }
            }

            if (pressed) {
                try {
                    keyTyped(key, num);
                } catch (IOException e) {}
            }
        }

        while (Mouse.next()) {
            int button = Mouse.getEventButton();
            boolean pressed = Mouse.getEventButtonState();
            int sx = Mouse.getEventX();
            int sy = Mouse.getEventY();
            int wheel = Mouse.getEventDWheel();

            if (browser != null) {
                int y = InstanceAccess.mc.displayHeight - sy - scaleY(offsetY);

                if (wheel != 0) {
                    browser.sendMouseWheel(sx, y, 0, 1, wheel);
                }
                else if (button == -1) {
                    browser.sendMouseMove(sx, y, 0);
                }
                else {
                    browser.sendMouseButton(sx, y, 0, button + 1, pressed, 1);
                }
            }

            if (pressed) {

                int x = sx * width / InstanceAccess.mc.displayWidth;
                int y = height - (sy * height / InstanceAccess.mc.displayHeight) - 1;

                try {
                    mouseClicked(x, y, button);
                } catch (Exception e) {}
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        browser.resize(1920, 1080);
    }

    private void resizeBrowser() {
        if (width > 100 && height > 100) {
            browser.resize(InstanceAccess.mc.displayWidth, InstanceAccess.mc.displayHeight - scaleY(offsetY));
        }
    }

    public int scaleY(int y) {

        double sy = ((double) y) / ((double) height) * ((double) InstanceAccess.mc.displayHeight);

        return (int) sy;
    }
}
