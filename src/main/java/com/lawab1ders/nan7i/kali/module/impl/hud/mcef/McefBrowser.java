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

import com.lawab1ders.nan7i.kali.utils.IOUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefBrowserOsr;
import org.cef.browser.CefRequestContext;
import org.cef.callback.CefDragData;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.nio.ByteBuffer;

public class McefBrowser extends CefBrowserOsr {

    private int lastWidth, lastHeight;

    @Getter
    private final McefRenderer renderer = new McefRenderer(true);
    private final Component dummyComp = new Component() {
        @Override
        public Point getLocationOnScreen() {
            return new Point(0, 0);
        }
    };

    public McefBrowser(CefClient client, String url, boolean transparent, CefRequestContext context) {
        super(client, url, transparent, context);
        Minecraft.getMinecraft().addScheduledTask(renderer::initialize);
    }

    @Override
    public void onPaint(CefBrowser browser,
                        boolean popup, Rectangle[] dirtyRects, ByteBuffer buffer,
                        int width, int height) {

        if (width != lastWidth || height != lastHeight) {
            renderer.onPaint(buffer, width, height);
            lastWidth = width;
            lastHeight = height;
        }
        else {

            if (renderer.getTextureID() == 0) {
                return;
            }

            GlStateManager.bindTexture(renderer.getTextureID());

            if (renderer.isTransparent()) {
                GlStateManager.enableBlend();
            }

            GL11.glPixelStoref(GL11.GL_UNPACK_ROW_LENGTH, width);

            for (Rectangle dirtyRect : dirtyRects) {
                GL11.glPixelStoref(GL11.GL_UNPACK_SKIP_PIXELS, dirtyRect.x);
                GL11.glPixelStoref(GL11.GL_UNPACK_SKIP_ROWS, dirtyRect.y);
                renderer.onPaint(buffer, dirtyRect.x, dirtyRect.y, dirtyRect.width, dirtyRect.height);
            }
        }

        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
    }

    public void sendMouseMove(int x, int y, int mods) {
        MouseEvent event = new MouseEvent(dummyComp, MouseEvent.MOUSE_MOVED, 0, mods, x, y, 0, false);
        sendMouseEvent(event);
    }

    public void sendMouseButton(int x, int y, int mods, int button, boolean pressed, int ccnt) {
        if (button == 2) {
            button = 0;
        }
        else if (button == 3) {
            button = 2;
        }

        if (button == 4 && canGoBack()) {
            goBack();
            return;
        }
        else if (button == 5 && canGoForward()) {
            goForward();
            return;
        }

        MouseEvent event = new MouseEvent(
                dummyComp, pressed ? MouseEvent.MOUSE_PRESSED : MouseEvent.MOUSE_RELEASED, 0, mods, x, y, ccnt, false,
                button
        );
        sendMouseEvent(event);
    }

    public void sendMouseWheel(int x, int y, int mods, int amount, int rot) {
        MouseWheelEvent event = new MouseWheelEvent(
                dummyComp, MouseEvent.MOUSE_WHEEL, 0, mods, x, y, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, amount,
                rot
        );
        sendMouseWheelEvent(event);
    }

    public void sendKeyPressed(int key, char c, int mods) {

        if (key == Keyboard.KEY_BACK || key == Keyboard.KEY_HOME || key == Keyboard.KEY_END ||
                key == Keyboard.KEY_UP || key == Keyboard.KEY_DOWN || key == Keyboard.KEY_LEFT || key == Keyboard.KEY_RIGHT ||
                key == Keyboard.KEY_CAPITAL || key == Keyboard.KEY_PAUSE || key == Keyboard.KEY_INSERT) {

            KeyEvent ev = UnsafeUtils.makeEvent(
                    dummyComp, remapKeyCode(key, KeyEvent.CHAR_UNDEFINED), KeyEvent.CHAR_UNDEFINED,
                    KeyEvent.KEY_LOCATION_UNKNOWN, KeyEvent.KEY_PRESSED, 0, remapModifiers(mods), mapScanCode(key)
            );
            sendKeyEvent(ev);
        }
        else {
            int raw = key;

            if (c == '\n') {
                raw = 13;
            }

            KeyEvent ev = UnsafeUtils.makeEvent(
                    dummyComp, remapKeyCode(key, c), c,
                    KeyEvent.KEY_LOCATION_UNKNOWN, KeyEvent.KEY_PRESSED, 0, remapModifiers(mods), mapScanCode(key),
                    raw
            );
            sendKeyEvent(ev);
        }
    }

    public void sendKeyTyped(int key, int mods) {
        char c = (char) key;
        int keyRemapped = remapKeyCode(key, (char) key);

        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_V)) {
            sendPaste();
        }

        if (key != KeyEvent.VK_UNDEFINED) {

            if (key == Keyboard.KEY_BACK || key == Keyboard.KEY_HOME || key == Keyboard.KEY_END ||
                    key == Keyboard.KEY_UP || key == Keyboard.KEY_DOWN || key == Keyboard.KEY_RIGHT ||
                    key == Keyboard.KEY_LEFT || key == Keyboard.KEY_PAUSE || key == Keyboard.KEY_INSERT) {

                KeyEvent ev = UnsafeUtils.makeEvent(
                        dummyComp, keyRemapped, KeyEvent.CHAR_UNDEFINED,
                        KeyEvent.KEY_LOCATION_UNKNOWN, KeyEvent.KEY_TYPED, 0, remapModifiers(mods),
                        mapScanCode(keyRemapped)
                );
                sendKeyEvent(ev);
            }
            else if (key == KeyEvent.VK_ENTER || key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER) {
                int raw = key;

                if (c == '\n') {
                    raw = 13;
                }

                KeyEvent ev = UnsafeUtils.makeEvent(
                        dummyComp, key, '\n',
                        KeyEvent.KEY_LOCATION_UNKNOWN, KeyEvent.KEY_TYPED, 0, remapModifiers(mods), mapScanCode(key),
                        raw
                );
                sendKeyEvent(ev);
            }
            else {
                KeyEvent ev = UnsafeUtils.makeEvent(
                        dummyComp, keyRemapped, c,
                        KeyEvent.KEY_LOCATION_UNKNOWN, KeyEvent.KEY_TYPED, 0, remapModifiers(mods),
                        mapScanCode(keyRemapped)
                );
                sendKeyEvent(ev);
            }
        }
    }

    public void sendKeyReleased(int key, char c, int mods) {
        if (key == Keyboard.KEY_BACK || key == Keyboard.KEY_HOME || key == Keyboard.KEY_END || key == Keyboard.KEY_UP ||
                key == Keyboard.KEY_DOWN || key == Keyboard.KEY_RIGHT || key == Keyboard.KEY_LEFT || key == Keyboard.KEY_PAUSE ||
                key == Keyboard.KEY_INSERT) {

            KeyEvent ev = UnsafeUtils.makeEvent(
                    dummyComp, remapKeyCode(key, KeyEvent.CHAR_UNDEFINED), c,
                    KeyEvent.KEY_LOCATION_UNKNOWN, KeyEvent.KEY_RELEASED, 0, remapModifiers(mods), mapScanCode(key)
            );
            sendKeyEvent(ev);
        }
        else {
            KeyEvent ev = UnsafeUtils.makeEvent(
                    dummyComp, remapKeyCode(key, c), c,
                    KeyEvent.KEY_LOCATION_UNKNOWN, KeyEvent.KEY_RELEASED, 0, remapModifiers(mods), mapScanCode(key)
            );
            sendKeyEvent(ev);
        }
    }

    private void sendPaste() {
        String clipboardText = IOUtils.getStringFromClipboard();

        if (clipboardText != null) {
            for (char c : clipboardText.toCharArray()) {
                KeyEvent ev = new KeyEvent(dummyComp, KeyEvent.KEY_TYPED, 0, 0, 0, c);
                sendKeyEvent(ev);
            }
        }
    }

    private int remapModifiers(int mods) {
        int vkMods = 0;

        if ((mods & Keyboard.KEY_LCONTROL) != 0) {
            vkMods |= KeyEvent.CTRL_DOWN_MASK | KeyEvent.CTRL_MASK;
        }

        if ((mods & Keyboard.KEY_LMENU) != 0) {
            vkMods |= KeyEvent.ALT_DOWN_MASK | KeyEvent.ALT_MASK;
        }

        if ((mods & Keyboard.KEY_LSHIFT) != 0) {
            vkMods |= KeyEvent.SHIFT_DOWN_MASK | KeyEvent.SHIFT_MASK;
        }

        if ((mods & Keyboard.KEY_LMETA) != 0) {
            vkMods |= KeyEvent.META_DOWN_MASK | KeyEvent.META_MASK;
        }

        return vkMods;
    }

    private int remapKeyCode(int key, char c) {
        if (key == Keyboard.KEY_ESCAPE) {
            return KeyEvent.VK_ESCAPE;
        }

        if (key == Keyboard.KEY_TAB) {
            return KeyEvent.VK_TAB;
        }

        int ck = getChar(key);

        if (ck == 0) {
            return c;
        }

        return ck;
    }

    private int getChar(int key) {

        if (key == Keyboard.KEY_LCONTROL || key == Keyboard.KEY_RCONTROL || key == Keyboard.KEY_END) {
            return '\uFFFF';
        }

        if (key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER || key == KeyEvent.VK_ENTER) {
            return '\n';
        }

        if (key == Keyboard.KEY_SPACE) {
            return 32;
        }

        if (key == Keyboard.KEY_BACK) {
            return 8;
        }

        if (key == Keyboard.KEY_DELETE) {
            return '\u007F';
        }

        return key;
    }

    private long mapScanCode(int key) {

        if (key == Keyboard.KEY_LCONTROL || key == Keyboard.KEY_RCONTROL) {
            return 29;
        }

        if (key == Keyboard.KEY_DELETE) {
            return 83;
        }

        if (key == Keyboard.KEY_LEFT) {
            return 75;
        }

        if (key == Keyboard.KEY_DOWN) {
            return 80;
        }

        if (key == Keyboard.KEY_UP) {
            return 72;
        }

        if (key == Keyboard.KEY_RIGHT) {
            return 77;
        }

        if (key == Keyboard.KEY_END) {
            return 79;
        }

        if (key == Keyboard.KEY_HOME) {
            return 71;
        }

        if (key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER || key == KeyEvent.VK_ENTER) {
            return 28;
        }

        if (key == Keyboard.KEY_BACK) {
            return 14;
        }

        return 0;
    }

    public void resize(int width, int height) {
        browser_rect_.setBounds(0, 0, width, height);
        wasResized(width, height);
    }

    public void close() {
        renderer.cleanup();
        super.close(true);
    }

    @Override
    protected void finalize() throws Throwable {
        Minecraft.getMinecraft().addScheduledTask(renderer::cleanup);
        super.finalize();
    }

    @Override
    public boolean startDragging(CefBrowser browser, CefDragData dragData, int mask, int x, int y) {
        return false;
    }

    public int getTexture() {
        return renderer.getTextureID();
    }
}
