package com.lawab1ders.nan7i.kali.module.impl.server.projectiletrail;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.EnumParticleTypes;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProjectileTrailType {

    BLACK_SMOKE(EnumParticleTypes.SMOKE_NORMAL, "module.projectiletrail.opts.mode.blacksmoke", 0.07F, 0.0F, 2),
    FIRE(EnumParticleTypes.FLAME, "module.projectiletrail.opts.mode.fire", 0.1F, 0.0F, 1),
    GREEN_STAR(EnumParticleTypes.VILLAGER_HAPPY, "module.projectiletrail.opts.mode.greenstar", 0.0F, 0.1F, 1),
    HEARTS(EnumParticleTypes.HEART, "module.projectiletrail.opts.mode.hearts", 0.0F, 0.2F, 1),
    MAGIC(EnumParticleTypes.SPELL_WITCH, "module.projectiletrail.opts.mode.magic", 1.0F, 0.0F, 2),
    MUSIC_NOTES(EnumParticleTypes.NOTE, "module.projectiletrail.opts.mode.musicnotes", 1.0F, 1.0F, 2),
    SLIME(EnumParticleTypes.SLIME, "module.projectiletrail.opts.mode.slime", 0.5F, 0.3F, 1),
    SPARK(EnumParticleTypes.FIREWORKS_SPARK, "module.projectiletrail.opts.mode.spark", 0.05F, 0.0F, 1),
    SWIRL(EnumParticleTypes.SPELL_MOB, "module.projectiletrail.opts.mode.swirl", 1.0F, 0.0F, 1),
    WHITE_SMOKE(EnumParticleTypes.SNOW_SHOVEL, "module.projectiletrail.opts.mode.whitesmoke", 0.07F, 0.0F, 2);

    private final EnumParticleTypes particle;
    private final String nameKey;
    private final float velocity, translate;
    private final int count;

    public static ProjectileTrailType getTypeByKey(String key) {
        return Arrays.stream(values())
                     .filter(t -> t.nameKey.equals(key))
                     .findFirst()
                     .orElse(HEARTS);
    }
}
