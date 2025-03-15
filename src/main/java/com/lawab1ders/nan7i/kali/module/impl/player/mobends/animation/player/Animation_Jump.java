package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.Animation;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Animation_Jump extends Animation {
    public String getName() {
        return "jump";
    }

    public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
        ModelBendsPlayer model = (ModelBendsPlayer) argModel;
        Data_Player data = (Data_Player) argData;
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(0.0F, 0.3F);
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(0.0F, 0.1F);
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothZ(0.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(45.0F, 0.05F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(-45.0F, 0.05F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(0.0F, 0.5F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(0.0F, 0.5F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F, 0.1F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F, 0.1F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-20.0F, 0.1F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-20.0F, 0.1F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-45.0F, 0.1F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-45.0F, 0.1F);
        model.bipedRightForeLeg.rotation.setSmoothX(50.0F, 0.1F);
        model.bipedLeftForeLeg.rotation.setSmoothX(50.0F, 0.1F);
        model.bipedRightForeArm.rotation.setSmoothX(0.0F, 0.3F);
        model.bipedLeftForeArm.rotation.setSmoothX(0.0F, 0.3F);
        ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY);
        ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX - model.bipedBody.rotateAngleX);
        if (data.ticksAfterLiftoff < 2.0F) {
            ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(20.0F, 2.0F);
            ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(0.0F, 2.0F);
            ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(0.0F, 2.0F);
            model.bipedRightForeLeg.rotation.setSmoothX(0.0F, 2.0F);
            model.bipedLeftForeLeg.rotation.setSmoothX(0.0F, 2.0F);
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(2.0F, 2.0F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(-2.0F, 2.0F);
            model.bipedRightForeArm.rotation.setSmoothX(-20.0F, 2.0F);
            model.bipedLeftForeArm.rotation.setSmoothX(-20.0F, 2.0F);
        }

        if (argData.motion.x != 0.0F | argData.motion.z != 0.0F) {
            float bodyRot;
            if (argEntity.isSprinting()) {
                bodyRot = 0.0F;
                float bodyLean = argData.motion.y;
                if (bodyLean < -0.2F) {
                    bodyLean = -0.2F;
                }

                if (bodyLean > 0.2F) {
                    bodyLean = 0.2F;
                }

                bodyLean = bodyLean * -100.0F + 20.0F;
                ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(bodyLean, 0.3F);
                ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(5.0F, 0.3F);
                ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-5.0F, 0.3F);
                ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(10.0F, 0.3F);
                ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(-10.0F, 0.3F);
                if (data.sprintJumpLeg) {
                    ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-45.0F, 0.4F);
                    ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(45.0F, 0.4F);
                    ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(50.0F, 0.3F);
                    ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(-50.0F, 0.3F);
                    bodyRot = 20.0F;
                } else {
                    ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(45.0F, 0.4F);
                    ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-45.0F, 0.4F);
                    ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(-50.0F, 0.3F);
                    ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(50.0F, 0.3F);
                    bodyRot = -20.0F;
                }

                ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(bodyRot, 0.3F);
                ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY - bodyRot);
                ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX - 20.0F);
                float var = model.bipedRightLeg.rotateAngleX;
                model.bipedLeftForeLeg.rotation.setSmoothX((float) (var < 0.0F ? 45 : 2), 0.3F);
                model.bipedRightForeLeg.rotation.setSmoothX((float) (var < 0.0F ? 2 : 45), 0.3F);
            } else {
                ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-5.0F + 0.5F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F) * 1.4F * model.armSwingAmount) / 3.141592653589793D * 180.0D), 1.0F);
                ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-5.0F + 0.5F * (float) ((double) (MathHelper.cos(model.armSwing * 0.6662F + 3.1415927F) * 1.4F * model.armSwingAmount) / 3.141592653589793D * 180.0D), 1.0F);
                bodyRot = (float) ((double) (model.armSwing * 0.6662F) / 3.141592653589793D) % 2.0F;
                model.bipedLeftForeLeg.rotation.setSmoothX((float) (bodyRot > 1.0F ? 45 : 0), 0.3F);
                model.bipedRightForeLeg.rotation.setSmoothX((float) (bodyRot > 1.0F ? 0 : 45), 0.3F);
                model.bipedLeftForeArm.rotation.setSmoothX((float) (Math.cos((double) (model.armSwing * 0.6662F) + 1.5707963267948966D) + 1.0D) / 2.0F * -20.0F, 1.0F);
                model.bipedRightForeArm.rotation.setSmoothX((float) (Math.cos(model.armSwing * 0.6662F) + 1.0D) / 2.0F * -20.0F, 0.3F);
            }
        }

    }
}
