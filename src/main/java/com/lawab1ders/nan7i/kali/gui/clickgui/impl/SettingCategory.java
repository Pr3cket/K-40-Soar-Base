package com.lawab1ders.nan7i.kali.gui.clickgui.impl;

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
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.gui.clickgui.Category;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.CompBoxes;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.scene.SettingScene;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.scene.impl.AppearanceScene;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.scene.impl.LanguageScene;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseSmoothStepAnimation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class SettingCategory extends Category {

    private final InterpolatableAnimation sceneAnimation = new EaseSmoothStepAnimation(1.0, 260);

    private final ArrayList<SettingScene> scenes = new ArrayList<>();
    private SettingScene currentScene;

    public SettingCategory(GuiClickMenu parent) {
        super(parent, "categories.settings", TextureCode.SETTINGS, false);

        scenes.add(new AppearanceScene(this));
        scenes.add(new LanguageScene(this));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        float offsetY = 15;
        float value = sceneAnimation.getValue();

        if (sceneAnimation.isDone(Direction.FORWARDS)) {
            this.setCanClose(true);
            currentScene = null;
        }

        nvg.save();
        nvg.translate(-(600 - (value * 600)), 0);

        for (SettingScene scene : scenes) {

            nvg.drawRoundedRect(
                    this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30, 40, 8,
                    palette.getBackgroundColor(ColorType.DARK)
            );
            nvg.drawText(
                    scene.getName(), this.getX() + 52, this.getY() + offsetY + 15F,
                    palette.getFontColor(ColorType.DARK), 13, Fonts.MEDIUM
            );
            nvg.drawText(
                    scene.getIcon(), this.getX() + 26, this.getY() + offsetY + 14,
                    palette.getFontColor(ColorType.DARK), 14, Fonts.ICON
            );
            nvg.drawText(
                    scene.getDescription(),
                    this.getX() + (nvg.getTextWidth(scene.getName(), 14, Fonts.MEDIUM)) + 56,
                    this.getY() + offsetY + 17, palette.getFontColor(ColorType.NORMAL), 9, Fonts.REGULAR
            );
            nvg.drawText(
                    ">", this.getX() + this.getWidth() - 32, this.getY() + offsetY + 17,
                    palette.getFontColor(ColorType.NORMAL), 10, Fonts.MEDIUM
            );

            offsetY += 50;
        }

        nvg.restore();

        nvg.save();
        nvg.translate(value * 600, 0);

        if (currentScene != null) {
            currentScene.drawScreen(mouseX, mouseY);
        }

        nvg.restore();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

        float offsetY = 15;

        if (currentScene == null) {

            for (SettingScene scene : scenes) {

                if (MouseUtils.isInside(
                        mouseX, mouseY, this.getX() + 15, this.getY() + offsetY, this.getWidth() - 30
                        , 40
                )) {
                    currentScene = scene;
                    this.setCanClose(false);
                    sceneAnimation.setDirection(Direction.BACKWARDS);
                    return;
                }

                offsetY += 50;
            }
        }
        else if (sceneAnimation.isDone(Direction.BACKWARDS)) {
            currentScene.mouseClicked(mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (currentScene != null) {
            CompBoxes.wasResetKeybinding = false;

            currentScene.keyTyped(typedChar, keyCode);

            if (keyCode == Keyboard.KEY_ESCAPE && !CompBoxes.wasResetKeybinding) {
                sceneAnimation.setDirection(Direction.FORWARDS);
            }
        }
    }

    public int getSceneX() {
        return getX() + 15;
    }

    public int getSceneY() {
        return getY() + 15;
    }

    public int getSceneWidth() {
        return getWidth() - 30;
    }

    public int getSceneHeight() {
        return getHeight() - 30;
    }
}
