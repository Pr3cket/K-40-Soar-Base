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
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenAnimation;
import lombok.val;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;

import java.text.DecimalFormat;

@IModule(
        name = "module.horsestats",
        description = "module.horsestats.desc",
        category = EModuleCategory.HUD
)
public class HorseStatsModule extends HUDModule {

    private final DecimalFormat df = new DecimalFormat("0.0");

    private final ScreenAnimation screenAnimation = new ScreenAnimation();
    private final InterpolatableAnimation introAnimation;

    private EntityHorse lastObj;

    public HorseStatsModule() {
        introAnimation = new EaseBackInAnimation(1.0F, 320, 2.0F);
        introAnimation.setDirection(Direction.BACKWARDS);
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        val isHorse = mc.objectMouseOver.entityHit instanceof EntityHorse && !mc.thePlayer.isRidingHorse();

        if (isHorse) lastObj = (EntityHorse) mc.objectMouseOver.entityHit;

        introAnimation.setDirection(isHorse || isEditing() ? Direction.FORWARDS : Direction.BACKWARDS);

        if (introAnimation.isDone(Direction.BACKWARDS)) return;

        String jump = "Jump Strength: " + (lastObj != null ?
                df.format(lastObj.getHorseJumpStrength() * 5.5) : "2.8") + " Blocks";

        screenAnimation.wrap(this::drawNanoVG, x, y, from(11 + nvg.getTextWidth(jump, 9, Fonts.REGULAR) * scale), from(29 * scale),
                2 - introAnimation.getValue(), introAnimation.getValue());
    }

    private void drawNanoVG() {
        String speed = "Speed: " + (lastObj != null ?
                this.getHorseSpeedRounded(lastObj.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()) : "7.6") + " B/s";

        String jump = "Jump Strength: " + (lastObj != null ?
                df.format(lastObj.getHorseJumpStrength() * 5.5) : "2.8") + " Blocks";

        this.drawBackground(11 + nvg.getTextWidth(jump, 9, Fonts.REGULAR), 9 * 2 + 11);

        this.drawText(speed, 5.5F, 5.5F, 9, Fonts.REGULAR);
        this.drawText(jump, 5.5F, 15.5F, 9, Fonts.REGULAR);
    }

    private String getHorseSpeedRounded(double baseSpeed) {
        final float factor = 43.1703703704f;

        float speed = (float) (baseSpeed * factor);

        return df.format(speed);
    }
}
