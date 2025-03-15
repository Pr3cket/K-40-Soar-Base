package com.lawab1ders.nan7i.kali.module.impl.hud;

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

import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.potion.PotionInfo;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@IModule(
        name = "module.potionstatus",
        description = "module.potionstatus.desc",
        category = EModuleCategory.HUD
)
public class PotionStatusModule extends HUDModule {

    public final BooleanSetting compactSetting = new BooleanSetting("module.potionstatus.opts.compact", false);

    private final Animation backgroundAnimation = new Animation(0.0F);
    private final CopyOnWriteArrayList<PotionInfo> potions = new CopyOnWriteArrayList<>();
    private float maxString = 0;
    private boolean compact = compactSetting.isActivated();

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        updateList();

        nvg.setupAndDraw(this::drawNanoVG);

        if (backgroundAnimation.getValue() > 1) {

            float ySize = compactSetting.isActivated() ? 22 : 23;
            float offsetY = 16;

            for (PotionInfo info : potions) {
                int index = info.getPotion().getStatusIconIndex();

                GlStateManager.color(1.0F, 1.0F, 1.0F, info.getAnimation().getValue());
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                GlStateManager.enableBlend();

                event.startScale(x, y, from(scale));

                if (compactSetting.isActivated()) {
                    event.startScale(
                            (x + 21) - 20,
                            (y + offsetY) - 11 - offsetY - 2F,
                            18, 18, 0.72F
                    );
                    event.drawTexturedModalRect(
                            (int) ((x + 21) - 20),
                            (int) ((y + offsetY) - 11),
                            index % 8 * 18, 198 + index / 8 * 18, 18, 18
                    );
                    event.stopScale();

                } else {
                    event.drawTexturedModalRect(
                            (int) ((x + 21) - 17),
                            (int) ((y + offsetY) - 12),
                            index % 8 * 18, 198 + index / 8 * 18, 18, 18
                    );
                }

                event.stopScale();

                offsetY += ySize;
            }
        }
    }

    private void updateList() {
        if (this.isEditing() || mc.thePlayer == null) {
            potions.clear();

            potions.add(new PotionInfo(new PotionEffect(1, 0), true));
            potions.add(new PotionInfo(new PotionEffect(10, 0), true));

        } else {
            Collection<PotionEffect> activePotionEffects = mc.thePlayer.getActivePotionEffects();

            for (PotionEffect effect : activePotionEffects) {
                boolean has = false;

                for (PotionInfo potion : potions) {
                    if (potion.getEffect() == effect) {
                        has = true;
                        break;
                    }
                }

                if (!has) {
                    potions.add(new PotionInfo(effect));
                }
            }

            potions.forEach(p -> {
                boolean active = activePotionEffects.contains(p.getEffect());

                p.setActive(active);
                p.getAnimation().setAnimation(active ? 1 : 0, 17);
            });

            potions.removeIf(info -> !info.isActive() && info.getAnimation().getValue() < 0.1);
        }
    }

    private void drawNanoVG() {
        int ySize = compactSetting.isActivated() ? 16 : 23;
        int height = potions.isEmpty() ? 0 : (ySize * potions.size() + 2);
        int offsetY = 16;

        if (compact != compactSetting.isActivated()) {
            compact = compactSetting.isActivated();
            maxString = 0;
//            backgroundAnimation.setValue(height);
        }

        for (PotionInfo info : potions) {
            String name = info.getName();
            String time = info.getTime();

            if (compactSetting.isActivated()) {
                float totalWidth = nvg.getTextWidth(name + " | " + time, 9, Fonts.REGULAR);
                maxString = Math.max(totalWidth, maxString);
            } else {
                float levelWidth = nvg.getTextWidth(name, 9, Fonts.REGULAR);
                float timeWidth = nvg.getTextWidth(time, 9, Fonts.REGULAR);

                maxString = Math.max(Math.max(levelWidth, timeWidth), maxString);
            }
        }

        backgroundAnimation.setAnimation(height, 20);

        if (backgroundAnimation.getValue() <= 1) {
            maxString = 0;
            return;
        }

        this.drawBackground(maxString + (compactSetting.isActivated() ? 25 : 29), backgroundAnimation.getValue());

        for (PotionInfo info : potions) {
            PotionEffect effect = info.getEffect();
            String time = info.getTime();
            String name = info.getName();
            Color color = ColorUtils.applyAlpha(getFontColor(), (int) (info.getAnimation().getValue() * 255));

            if (effect.getAmplifier() == 1) {
                name = name + " " + I18n.format("enchantment.level.2");

            } else if (effect.getAmplifier() == 2) {
                name = name + " " + I18n.format("enchantment.level.3");

            } else if (effect.getAmplifier() == 3) {
                name = name + " " + I18n.format("enchantment.level.4");
            }

            if (compactSetting.isActivated()) {
                this.drawText(name + " | " + time, 20, offsetY - 10.5F, 9, Fonts.REGULAR, color);
            } else {
                this.drawText(name, 25, offsetY - 12, 9, Fonts.REGULAR, color);
                this.drawText(time, 25, offsetY - 1, 8, Fonts.REGULAR, color);
            }

            offsetY += ySize;
        }
    }
}
