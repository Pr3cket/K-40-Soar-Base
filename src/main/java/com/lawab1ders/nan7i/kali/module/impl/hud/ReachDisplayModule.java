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

import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.SimpleHUDModule;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

@IModule(
        name = "module.reachdisplay",
        description = "module.reachdisplay.desc",
        category = EModuleCategory.HUD
)
public class ReachDisplayModule extends SimpleHUDModule {

    private final DecimalFormat df = new DecimalFormat("0.##");

    private double distance;
    private long hitTime = -1;

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        if (System.currentTimeMillis() - hitTime > 2000) {
            distance = 0;
        }
    }

    @SubscribeEvent
    public void onAttack(LivingHurtEvent event) {
        if (mc.objectMouseOver != null
                && mc.objectMouseOver.hitVec != null
                && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {

            distance = mc.objectMouseOver.hitVec.distanceTo(mc.thePlayer.getPositionEyes(1.0F));
        }

        hitTime = System.currentTimeMillis();
    }

    @Override
    public String getText() {
        return df.format(distance) + " Blocks";
    }

    @Override
    public String getIcon() {
        return TextureCode.ACTIVITY;
    }
}
