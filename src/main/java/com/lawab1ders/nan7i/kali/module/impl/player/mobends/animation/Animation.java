package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public abstract class Animation {

    public abstract void animate(EntityLivingBase var1, ModelBase var2, EntityData var3);

    public abstract String getName();
}
