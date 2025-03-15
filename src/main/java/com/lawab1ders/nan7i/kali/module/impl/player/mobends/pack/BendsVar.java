package com.lawab1ders.nan7i.kali.module.impl.player.mobends.pack;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.EntityData;

public class BendsVar {
    public static EntityData tempData;

    public static float getGlobalVar(String name) {
        if (name.equalsIgnoreCase("ticks")) {
            return tempData == null ? 0.0F : tempData.ticks;
        } else if (name.equalsIgnoreCase("ticksAfterPunch")) {
            return tempData == null ? 0.0F : tempData.ticksAfterPunch;
        } else {
            return Float.POSITIVE_INFINITY;
        }
    }
}
