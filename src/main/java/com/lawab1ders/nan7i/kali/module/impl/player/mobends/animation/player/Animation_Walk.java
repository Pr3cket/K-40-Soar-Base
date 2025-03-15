package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.Animation;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.EntityData;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.GUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Animation_Walk extends Animation {
    public String getName() {
        return "walk";
    }

    public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
        ModelBendsPlayer model = (ModelBendsPlayer) argModel;
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(0.5F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F + 3.1415927F) * 2.0F * model.armSwingAmount * 0.5F) / 3.141592653589793D * 180.0D));
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(0.5F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F) * 2.0F * model.armSwingAmount * 0.5F) / 3.141592653589793D * 180.0D));
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(5.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(-5.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-5.0F + 0.5F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F) * 1.4F * model.armSwingAmount) / 3.141592653589793D * 180.0D), 1.0F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-5.0F + 0.5F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F + 3.1415927F) * 1.4F * model.armSwingAmount) / 3.141592653589793D * 180.0D), 1.0F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothY(0.0F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(0.0F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(2.0F, 0.2F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-2.0F, 0.2F);
        float var = (float) ((double) (model.armSwing * 0.6662F) / 3.141592653589793D) % 2.0F;
        model.bipedLeftForeLeg.rotation.setSmoothX((float) (var > 1.0F ? 45 : 0), 0.3F);
        model.bipedRightForeLeg.rotation.setSmoothX((float) (var > 1.0F ? 0 : 45), 0.3F);
        model.bipedLeftForeArm.rotation.setSmoothX((float) (Math.cos((double) (model.armSwing * 0.6662F) + 1.5707963267948966D) + 1.0D) / 2.0F * -20.0F, 1.0F);
        model.bipedRightForeArm.rotation.setSmoothX((float) (Math.cos(model.armSwing * 0.6662F) + 1.0D) / 2.0F * -20.0F, 0.3F);
        float var2 = (float) Math.cos(model.armSwing * 0.6662F) * -20.0F;
        float var3 = (float) (Math.cos(model.armSwing * 0.6662F * 2.0F) * 0.5D + 0.5D) * 10.0F - 2.0F;
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(var2, 0.5F);
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(var3);
        ((ModelRendererBends) model.bipedHead).rotation.setSmoothY(model.headRotationY - var2, 0.5F);
        ((ModelRendererBends) model.bipedHead).rotation.setSmoothX(model.headRotationX - var3);
        float var10 = model.headRotationY * 0.1F;
        var10 = GUtil.max(var10, 10.0F);
        var10 = GUtil.min(var10, -10.0F);
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothZ(-var10, 0.3F);
    }
}
