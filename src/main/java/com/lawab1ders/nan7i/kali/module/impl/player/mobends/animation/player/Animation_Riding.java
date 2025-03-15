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

public class Animation_Riding extends Animation {
    public String getName() {
        return "riding";
    }

    public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
        ModelBendsPlayer model = (ModelBendsPlayer) argModel;
        Data_Player data = (Data_Player) argData;
        EntityPlayer player = (EntityPlayer) argEntity;
        model.renderOffset.setSmoothY(1.5F, 0.3F);
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(0.0F, 0.3F);
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothZ(0.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-85.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothY(45.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-85.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(-45.0F, 0.3F);
        model.bipedRightForeLeg.rotation.setSmoothX(60.0F);
        model.bipedLeftForeLeg.rotation.setSmoothX(60.0F);
        if (argData.motion.x == 0.0F & argData.motion.z == 0.0F) {
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(-10.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(-10.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothY(-10.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothY(10.0F, 0.3F);
            ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(0.0F, 0.3F);
            model.bipedRightForeArm.rotation.setSmoothX(-20.0F, 0.3F);
            model.bipedLeftForeArm.rotation.setSmoothX(-20.0F, 0.3F);
        } else {
            float jiggle = MathHelper.cos((float) player.ticksExisted * 0.6F) * model.armSwingAmount;
            float jiggle_hard = MathHelper.cos((float) player.ticksExisted * 0.3F) * model.armSwingAmount;
            if (jiggle_hard < 0.0F) {
                jiggle_hard = -jiggle_hard;
            }

            model.renderOffset.setSmoothY(1.5F + jiggle_hard * 20.0F, 0.7F);
            ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(40.0F + jiggle * 300.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(-45.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(-45.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothY(-10.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothY(10.0F, 0.3F);
            model.bipedRightForeArm.rotation.setSmoothX(-30.0F, 0.3F);
            model.bipedLeftForeArm.rotation.setSmoothX(-30.0F, 0.3F);
        }

        ((ModelRendererBends) model.bipedHead).rotation.setSmoothY(model.headRotationY, 0.3F);
        ((ModelRendererBends) model.bipedHead).rotation.setSmoothX(model.headRotationX - model.bipedBody.rotateAngleX / 3.1415927F * 180.0F, 0.3F);
    }
}
