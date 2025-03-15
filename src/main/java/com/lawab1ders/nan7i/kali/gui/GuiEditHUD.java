package com.lawab1ders.nan7i.kali.gui;

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
import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.ModuleManager;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import lombok.val;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;

public class GuiEditHUD extends GuiScreen implements InstanceAccess {

    private InterpolatableAnimation introAnimation;
    private boolean snapping, canSnap;

    @Override
    public void initGui() {
        for (HUDModule m : ModuleManager.HUD) {
            m.setDragging(false);
            m.getAnimation().setValue(0.0F);
        }

        introAnimation = new EaseBackInAnimation(1.0F, 320, 2.0F);
        introAnimation.setDirection(Direction.FORWARDS);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        snapping = false;

        if (introAnimation.isDone(Direction.BACKWARDS)) {
            InstanceAccess.mc.displayGuiScreen(null);
            return;
        }

        PARTICLES_ENGINE.draw(mouseX, mouseY, introAnimation.getValue());

        nvg.setupAndDraw(() -> {
            int halfScreenWidth = sr.getScaledWidth() / 2;
            int halfScreenHeight = sr.getScaledHeight() / 2;

            nvg.drawRect(0, halfScreenHeight, sr.getScaledWidth(), 0.5F, palette.getBackgroundColor(ColorType.DARK));
            nvg.drawRect(halfScreenWidth, 0, 0.5F, sr.getScaledHeight(), palette.getBackgroundColor(ColorType.DARK));

            ModuleManager.HUD.sort(Comparator.comparing(HUDModule::getLayer));
            Collections.reverse(ModuleManager.HUD);

            for (HUDModule m : ModuleManager.HUD) {
                boolean isInside = MouseUtils.isInside(mouseX, mouseY, m.getX(), m.getY(), m.getWidth(), m.getHeight());

                val isValid = m.isActivated() && (isInside || m.isDragging());

                m.getAnimation().setAnimation(isValid ? 1.0F : 0.0F, 14);

                nvg.drawRoundedRect(
                        m.getX(), m.getY(), m.getWidth(), m.getHeight(),
                        m.from(6 * m.getScale()), new Color(255, 255, 255, (int) (m.getAnimation().getValue() * 100))
                );

                if (!isValid) continue;

                int dWheel = Mouse.getDWheel();

                if (m.isDragging()) {
                    m.setX(mouseX + m.getDraggingX());
                    m.setY(mouseY + m.getDraggingY());
                }
                else if (dWheel != 0) {
                    float scaleChange = 0.1F;
                    float newScale = m.getScale();

                    if (dWheel > 1) {
                        newScale += scaleChange;

                    }
                    else if (dWheel < 1) {
                        newScale -= scaleChange;
                    }

                    val oldScale = m.getScale();

                    m.setScale(Math.round(newScale * 10.0F) / 10.0F);

                    if (oldScale != m.getScale()) {
                        m.setX(m.getX() + (mouseX - m.getX() - m.getWidth() / 2) * Math.abs(
                                oldScale - m.getScale()));
                        m.setY(m.getY() + (mouseY - m.getY() - m.getHeight() / 2) * Math.abs(
                                oldScale - m.getScale()));
                    }
                }

                float modX = m.getX();
                float modY = m.getY();
                float modWidth = m.getWidth();
                float modHeight = m.getHeight();

                int snapRange = 5;

//                m.setX(Math.max(0, Math.min(modX, sr.getScaledWidth() - modWidth)));
//                m.setY(Math.max(0, Math.min(modY, sr.getScaledHeight() - modHeight)));

                if (canSnap) {
                    if (MathUtils.isInRange(
                            modX + (modWidth / 2), halfScreenWidth - snapRange,
                            halfScreenWidth + snapRange
                    )) {
                        m.setX(halfScreenWidth - (modWidth / 2));
                    }

                    if (MathUtils.isInRange(
                            modY + (modHeight / 2), halfScreenHeight - snapRange,
                            halfScreenHeight + snapRange
                    )) {
                        m.setY(halfScreenHeight - (modHeight / 2));
                    }
                }

                Color color = new Color(217, 60, 255);

                if (!snapping && canSnap) {
                    for (HUDModule m2 : ModuleManager.HUD) {

                        if (!m2.isActivated() || !m.isDragging() || m2.equals(m)) continue;

                        float mod2X = m2.getX();
                        float mod2Y = m2.getY();
                        float mod2Width = m2.getWidth();
                        float mod2Height = m2.getHeight();

                        if (MathUtils.isInRange(mod2X, modX - snapRange, modX + snapRange)) {
                            nvg.drawRect(mod2X, 0, 0.5F, sr.getScaledHeight(), color);
                            snapping = true;
                            m.setX(mod2X);
                        }

                        if (MathUtils.isInRange(mod2Y, modY - snapRange, modY + snapRange)) {
                            nvg.drawRect(0, mod2Y, sr.getScaledWidth(), 0.5F, color);
                            snapping = true;
                            m.setY(mod2Y);
                        }

                        if (MathUtils.isInRange(mod2X + mod2Width, modX - snapRange, modX + snapRange)) {
                            nvg.drawRect(mod2X + mod2Width, 0, 0.5F, sr.getScaledHeight(), color);
                            snapping = true;
                            m.setX(mod2X + mod2Width);
                        }

                        if (MathUtils.isInRange(mod2Y + mod2Height, modY - snapRange, modY + snapRange)) {
                            nvg.drawRect(0, mod2Y + mod2Height, sr.getScaledWidth(), 0.5F, color);
                            snapping = true;
                            m.setY(mod2Y + mod2Height);
                        }

                        if (MathUtils.isInRange(mod2X, modX + modWidth - snapRange, modX + modWidth + snapRange)) {
                            nvg.drawRect(mod2X, 0, 0.5F, sr.getScaledHeight(), color);
                            snapping = true;
                            m.setX(mod2X - modWidth);
                        }

                        if (MathUtils.isInRange(mod2Y, modY + modHeight - snapRange, modY + modHeight + snapRange)) {
                            nvg.drawRect(0, mod2Y, sr.getScaledWidth(), 0.5F, color);
                            snapping = true;
                            m.setY(mod2Y - modHeight);
                        }

                        if (MathUtils.isInRange(
                                mod2X + mod2Width, modX + modWidth - snapRange,
                                modX + modWidth + snapRange
                        )) {
                            nvg.drawRect(mod2X + mod2Width, 0, 0.5F, sr.getScaledHeight(), color);
                            snapping = true;
                            m.setX(mod2X + mod2Width - modWidth);
                        }

                        if (MathUtils.isInRange(
                                mod2Y + mod2Height, modY + modHeight - snapRange,
                                modY + modHeight + snapRange
                        )) {
                            nvg.drawRect(0, mod2Y + mod2Height, sr.getScaledWidth(), 0.5F, color);
                            snapping = true;
                            m.setY(mod2Y + mod2Height - modHeight);
                        }
                    }
                }

                break;
            }
        });

        ntf.render();
        CLICK_EFFECTS_ENGINE.drawClickEffects();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0 && mouseButton != 1) {
            return;
        }

        CLICK_EFFECTS_ENGINE.addClickEffect(mouseX, mouseY);

        for (HUDModule m : ModuleManager.HUD) {
            if (!m.isActivated()) continue;

            boolean isInside = MouseUtils.isInside(mouseX, mouseY, m.getX(), m.getY(), m.getWidth(), m.getHeight());

            canSnap = true;

            if (isInside) {
                m.setDragging(true);
                m.setDraggingX(m.getX() - mouseX);
                m.setDraggingY(m.getY() - mouseY);

                for (HUDModule hudModule : ModuleManager.HUD) {
                    if (hudModule == m) break;

                    hudModule.setLayer(hudModule.getLayer() - 1);
                }

                m.setLayer(Module.hudLayer);

                return;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (HUDModule m : ModuleManager.HUD) {
            m.setDragging(false);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            InstanceAccess.mc.displayGuiScreen(gcm);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
