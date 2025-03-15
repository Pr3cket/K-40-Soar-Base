package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.Animation;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Animation_Sneak extends Animation {
    public String getName() {
        return "sneak";
    }

    public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
        ModelBendsPlayer model = (ModelBendsPlayer) argModel;
        float var = (float) ((double) (model.armSwing * 0.6662F) / 3.141592653589793D) % 2.0F;
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-5.0F + 1.1F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F) * 1.4F * model.armSwingAmount) / 3.141592653589793D * 180.0D), 1.0F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-5.0F + 1.1F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F + 3.1415927F) * 1.4F * model.armSwingAmount) / 3.141592653589793D * 180.0D), 1.0F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(-20.0F + 20.0F * MathHelper.cos(model.armSwing * 0.6662F + 3.1415927F));
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(-20.0F + 20.0F * MathHelper.cos(model.armSwing * 0.6662F));
        model.bipedLeftForeLeg.rotation.setSmoothX((float) (var > 1.0F ? 45 : 10), 0.3F);
        model.bipedRightForeLeg.rotation.setSmoothX((float) (var > 1.0F ? 10 : 45), 0.3F);
        model.bipedLeftForeArm.rotation.setSmoothX((float) (var > 1.0F ? -10 : -45), 0.01F);
        model.bipedRightForeArm.rotation.setSmoothX((float) (var > 1.0F ? -45 : -10), 0.01F);
        float var2 = 25.0F + (float) Math.cos(model.armSwing * 0.6662F * 2.0F) * 5.0F;
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(var2);
        ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX - ((ModelRendererBends) model.bipedBody).rotation.getX());
    }
}
