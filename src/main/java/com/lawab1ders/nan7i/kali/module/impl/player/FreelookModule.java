package com.lawab1ders.nan7i.kali.module.impl.player;

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

import com.lawab1ders.nan7i.kali.events.cancelable.RotateHeadEvent;
import com.lawab1ders.nan7i.kali.events.cancelable.ScrollMouseEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@IModule(
        name = "module.freelook",
        description = "module.freelook.desc",
        category = EModuleCategory.PLAYER,
        activated = true
)
public class FreelookModule extends Module {

    public final KeybindSetting keybindSetting = new KeybindSetting(Keyboard.KEY_V);
    public final BooleanSetting smoothZoomSetting = new BooleanSetting("module.opts.smoothzoom", false);

    public final BooleanSetting invertYawSetting = new BooleanSetting("module.freelook.opts.invertyaw", false);

    public final FloatSetting zoomSpeedSetting = new FloatSetting("module.opts.zoomspeed", 14, 5, 40);

    public final BooleanSetting invertPitchSetting = new BooleanSetting("module.freelook.opts.invertpitch", false);

    public final Animation zoomAnimation = new Animation();
    public boolean active;
    public float yaw;
    public float pitch;
    public int previousPerspective;
    public int currentFactor = 4;

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {

        if (keybindSetting.isKeyDown()) {
            start();
            postNotificationOnlyOnce("module.freelook.noti", NotificationType.INFO);
        } else {
            stop();
        }
    }

    @SubscribeEvent
    public void onRotateHead(RotateHeadEvent event) {

        if (active) {
            float yaw = event.getYaw();
            float pitch = event.getPitch();
            event.setCanceled(true);
            pitch = -pitch;

            if (!invertPitchSetting.isActivated()) {
                pitch = -pitch;
            }

            if (invertYawSetting.isActivated()) {
                yaw = -yaw;
            }

            this.yaw += yaw * 0.15F;
            this.pitch = MathHelper.clamp_float(this.pitch + (pitch * 0.15F), -90, 90);
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }

    @SubscribeEvent
    public void onScrollMouse(ScrollMouseEvent event) {
        if (active) {
            event.setCanceled(true);
            if (event.getAmount() < 0) {
                if (currentFactor < 15) {
                    currentFactor += 1;
                }
            } else if (event.getAmount() > 0) {
                if (currentFactor > 4) {
                    currentFactor -= 1;
                }
            }
        }
    }

    private void start() {
        if (!active) {
            active = true;
            previousPerspective = mc.gameSettings.thirdPersonView;
            mc.gameSettings.thirdPersonView = 3;
            Entity renderView = mc.getRenderViewEntity();
            yaw = renderView.rotationYaw;
            pitch = renderView.rotationPitch;
            currentFactor = 4;
        }
    }

    private void stop() {
        if (active) {
            active = false;
            mc.gameSettings.thirdPersonView = previousPerspective;
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }
    }
}
