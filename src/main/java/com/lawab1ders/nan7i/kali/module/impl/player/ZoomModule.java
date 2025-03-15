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

import com.lawab1ders.nan7i.kali.events.ZoomFovEvent;
import com.lawab1ders.nan7i.kali.events.cancelable.ScrollMouseEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.val;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

@IModule(
        name = "module.zoom",
        description = "module.zoom.desc",
        category = EModuleCategory.PLAYER,
        activated = true
)
public class ZoomModule extends Module {

    private static final Map<Integer, Float> MODIFIER_BY_TICK = new HashMap<>();

    public final KeybindSetting keybindSetting = new KeybindSetting(Keyboard.KEY_C);
    public final BooleanSetting smoothCameraSetting = new BooleanSetting("module.zoom.opts.smoothcamera", true);
    public final BooleanSetting smoothZoomSetting = new BooleanSetting("module.opts.smoothzoom", false);
    public final BooleanSetting bowZoomSetting = new BooleanSetting("module.zoom.opts.bowzoom", false);
    public final FloatSetting zoomSpeedSetting = new FloatSetting("module.opts.zoomspeed", 14, 5, 40);
    public final FloatSetting bowFactorSetting = new FloatSetting("module.zoom.opts.bowfactor", 5, 1, 6);

    private final Animation zoomAnimation = new Animation();
    private boolean wasCinematic, isZoomActive;
    private float factor = 1;

    static {
        // These values are taken from Minecraft's code.
        // It's a bit hard to understand, but it's not that hard to calculate.

        MODIFIER_BY_TICK.put(0, 0.0F);
        MODIFIER_BY_TICK.put(1, 0.00037497282f);
        MODIFIER_BY_TICK.put(2, 0.0015000105f);
        MODIFIER_BY_TICK.put(3, 0.0033749938f);
        MODIFIER_BY_TICK.put(4, 0.0059999824f);
        MODIFIER_BY_TICK.put(5, 0.009374976f);
        MODIFIER_BY_TICK.put(6, 0.013499975f);
        MODIFIER_BY_TICK.put(7, 0.01837498f);
        MODIFIER_BY_TICK.put(8, 0.023999989f);
        MODIFIER_BY_TICK.put(9, 0.030375004f);
        MODIFIER_BY_TICK.put(10, 0.037500024f);
        MODIFIER_BY_TICK.put(11, 0.04537499f);
        MODIFIER_BY_TICK.put(12, 0.05400002f);
        MODIFIER_BY_TICK.put(13, 0.063374996f);
        MODIFIER_BY_TICK.put(14, 0.07349998f);
        MODIFIER_BY_TICK.put(15, 0.084375024f);
        MODIFIER_BY_TICK.put(16, 0.096000016f);
        MODIFIER_BY_TICK.put(17, 0.10837501f);
        MODIFIER_BY_TICK.put(18, 0.121500015f);
        MODIFIER_BY_TICK.put(19, 0.13537502f);
        MODIFIER_BY_TICK.put(20, 0.14999998f);
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        val wtf = isBowZooming();

        if (keybindSetting.isKeyDown() || wtf) {
            // Shitty code, but it works.
            if (isZoomActive) return;

            isZoomActive = true;
            wasCinematic = mc.gameSettings.smoothCamera;

            // 设置电影视角
            if (!wtf) {
                factor = 0.25F;
                mc.gameSettings.smoothCamera = smoothCameraSetting.isActivated();
            }
        }
        else if (isZoomActive) {
            isZoomActive = false;
            factor = 1;

            // 重置
            mc.gameSettings.smoothCamera = wasCinematic;
        }
    }

    @SubscribeEvent
    public void onZoomFov(ZoomFovEvent event) {
        val wtf = isBowZooming();

        if (wtf) {
            val min = Math.min(mc.thePlayer.getItemInUseDuration(), 20);
            val modifier = MODIFIER_BY_TICK.get(min);

            factor = 1 - modifier * bowFactorSetting.getValue();
        }

        zoomAnimation.setAnimation(factor, zoomSpeedSetting.getValue());
        event.setFov(event.getFov() * (wtf || smoothZoomSetting.isActivated() ? zoomAnimation.getValue() : factor));

        mc.renderGlobal.setDisplayListEntitiesDirty();
    }

    @SubscribeEvent
    public void onScrollMouse(ScrollMouseEvent event) {
        // 唐纳德特朗普是美国从辉煌到落魄的引领者，笑梗不笑人，建国真男人

        if (isZoomActive && !isBowZooming()) {
            event.setCanceled(true);

            // 中国恒大，不负众望
            if (event.getAmount() < 0) {
                if (factor < 0.98) factor += 0.03F;
            }
            // Donald Trump is the leader of the United States from greatness to poverty,
            // and he doesn't joke at people.
            else if (event.getAmount() > 0) {
                if (factor > 0.06) factor -= 0.03F;
            }
        }
    }

    private boolean isBowZooming() {
        return bowZoomSetting.isActivated() && mc.thePlayer.getItemInUse() != null && mc.thePlayer.getItemInUse()
                                                                                                  .getItem() == Items.bow;
    }
}
