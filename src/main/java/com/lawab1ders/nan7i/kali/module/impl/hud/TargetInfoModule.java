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

import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenAnimation;
import lombok.val;
import net.minecraft.client.entity.AbstractClientPlayer;

@IModule(
        name = "module.targetinfo",
        description = "module.targetinfo.desc",
        category = EModuleCategory.HUD
)
public class TargetInfoModule extends HUDModule {

    private final Animation healthAnimation = new Animation();
    private final Animation armorAnimation = new Animation();
    private final ScreenAnimation screenAnimation = new ScreenAnimation();
    private final InterpolatableAnimation introAnimation;

    private AbstractClientPlayer lastObj;

    private float bgWidth;

    public TargetInfoModule() {
        introAnimation = new EaseBackInAnimation(1.0F, 320, 2.0F);
        introAnimation.setDirection(Direction.BACKWARDS);
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        val target = KaliAPI.INSTANCE.getAttackingTaget();

        if (target != null) lastObj = target;
        if (lastObj == null) lastObj = mc.thePlayer;

        introAnimation.setDirection(target != null || isEditing() ? Direction.FORWARDS : Direction.BACKWARDS);

        if (introAnimation.isDone(Direction.BACKWARDS)) return;

        float nameWidth = nvg.getTextWidth(lastObj.getName(), 10.2F, Fonts.MEDIUM);

        bgWidth = 140;

        if (nameWidth + 48F > bgWidth) {
            bgWidth = (int) (bgWidth + nameWidth - 89);
        }

        screenAnimation.wrap(this::drawNanoVG, x, y, from(bgWidth * scale), from(46 * scale),
                2 - introAnimation.getValue(),
                introAnimation.getValue());
    }

    private void drawNanoVG() {

        healthAnimation.setAnimation(Math.min(lastObj.getHealth(), 20), 16);
        armorAnimation.setAnimation(Math.min(lastObj.getTotalArmorValue(), 20), 16);

        this.drawBackground(bgWidth, 46);

        this.drawPlayerHead(lastObj.getLocationSkin(), 5, 5, 36, 36, 6);
        this.drawText(lastObj.getName(), 45.5F, 8F, 10.2F, Fonts.MEDIUM);

        this.drawText(TextureCode.HEART_FILL, 52, 26.5F, 9, Fonts.ICON);
        this.drawArc(56.5F, 30.5F, 9F, -90F, -90F + 360, 1.6F, this.getFontColor(120));
        this.drawArc(56.5F, 30.5F, 9F, -90F, -90F + (18 * healthAnimation.getValue()), 1.6F);

        this.drawText(TextureCode.SHIELD_FILL, 76F, 26.5F, 9, Fonts.ICON);
        this.drawArc(80.5F, 30.5F, 9F, -90F, -90F + 360, 1.6F, this.getFontColor(120));
        this.drawArc(80.5F, 30.5F, 9F, -90F, -90F + (18 * armorAnimation.getValue()), 1.6F);
    }
}
