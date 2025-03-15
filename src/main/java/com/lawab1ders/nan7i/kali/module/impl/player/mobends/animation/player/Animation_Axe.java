package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.Animation;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class Animation_Axe extends Animation {
    public String getName() {
        return "axe";
    }

    public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
        ModelBendsPlayer model = (ModelBendsPlayer) argModel;
        Data_Player data = (Data_Player) argData;
        EntityPlayer player = (EntityPlayer) argEntity;
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZero(0.3F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZero(0.3F);
        model.bipedRightForeArm.rotation.setSmoothZero(0.3F);
        model.bipedLeftForeArm.rotation.setSmoothZero(0.3F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F, 0.3F);
        model.renderOffset.setSmoothY(-1.5F, 0.3F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothY(90.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightArm).pre_rotation.setSmoothY(0.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightArm).pre_rotation.setSmoothX(-80.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothY(90.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftArm).pre_rotation.setSmoothY(-40.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftArm).pre_rotation.setSmoothX(-70.0F, 0.3F);
        if (player.isSwingInProgress) {
            float speed = 1.8F;
            float progress = (float) player.ticksExisted * speed / 20.0F % 1.0F;
            float progress2 = (float) (player.ticksExisted - 2) * speed / 20.0F % 1.0F;
            float armSwing = (MathHelper.cos(progress * 3.1415927F * 2.0F) + 1.0F) / 2.0F * 2.0F;
            if (armSwing > 1.0F) {
                armSwing = 1.0F;
            }

            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothY(90.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightArm).pre_rotation.setSmoothY(40.0F - 90.0F * armSwing, 0.7F);
            ((ModelRendererBends) model.bipedRightArm).pre_rotation.setSmoothX(-80.0F, 0.7F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothY(90.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftArm).pre_rotation.setSmoothY(0.0F - 70.0F * armSwing, 0.7F);
            ((ModelRendererBends) model.bipedLeftArm).pre_rotation.setSmoothX(-70.0F, 0.7F);
            ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(armSwing * 20.0F);
            ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX - armSwing * 20.0F);
            ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY - armSwing * 20.0F);
        }

    }
}
