package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Attack_Stance {
    public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
        if (data.isOnGround()) {
            if (data.motion.x == 0.0F & data.motion.z == 0.0F) {
                model.renderRotation.setSmoothY(30.0F, 0.3F);
                Vector3f bodyRot = new Vector3f(0.0F, 0.0F, 0.0F);
                bodyRot.x = 20.0F;
                ((ModelRendererBends) model.bipedBody).rotation.setSmooth(bodyRot, 0.3F);
                ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY - 30.0F);
                ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX);
                ((ModelRendererBends) model.bipedHead).pre_rotation.setSmoothX(-bodyRot.x, 0.3F);
                ((ModelRendererBends) model.bipedHead).pre_rotation.setSmoothY(-bodyRot.y, 0.3F);
                ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-30.0F, 0.3F);
                ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-30.0F, 0.3F);
                ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(-25.0F, 0.3F);
                ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F);
                ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F);
                model.bipedRightForeLeg.rotation.setSmoothX(30.0F, 0.3F);
                model.bipedLeftForeLeg.rotation.setSmoothX(30.0F, 0.3F);
                ((ModelRendererBends) model.bipedRightArm).pre_rotation.setSmoothZ(60.0F, 0.3F);
                ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(60.0F, 0.3F);
                ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(20.0F, 0.3F);
                ((ModelRendererBends) model.bipedLeftArm).pre_rotation.setSmoothZ(-80.0F, 0.3F);
                model.bipedRightForeArm.rotation.setSmoothX(-20.0F, 0.3F);
                model.bipedLeftForeArm.rotation.setSmoothX(-60.0F, 0.3F);
                model.renderItemRotation.setSmoothX(65.0F, 0.3F);
                model.renderOffset.setSmoothY(-2.0F);
            } else if (player.isSprinting()) {
                ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(20.0F, 0.3F);
                ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY - 20.0F);
                ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX - 15.0F);
                ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothY(0.0F);
                ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(0.0F);
                ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(60.0F, 0.3F);
                model.renderItemRotation.setSmoothX(90.0F, 0.3F);
            }

        }
    }
}
