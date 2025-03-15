package com.lawab1ders.nan7i.kali.module.impl.hud.potion;

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

import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@Getter
public class PotionInfo {

    private final PotionEffect effect;
    private final Potion potion;
    private final Animation animation = new Animation(0.0F);

    @Setter
    private boolean active = true;

    public PotionInfo(PotionEffect effect) {
        this(effect, false);
    }

    public PotionInfo(PotionEffect effect, boolean fake) {
        this.effect = effect;
        this.potion = Potion.potionTypes[effect.getPotionID()];

        if (fake) animation.setValue(1);
    }

    public String getName() {
        return I18n.format(potion.getName());
    }

    public String getTime() {
        return Potion.getDurationString(effect);
    }
}
