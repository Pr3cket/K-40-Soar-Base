package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import net.minecraft.entity.player.EntityPlayer;

public class Animation_Attack_PunchStance {
    public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
        if (data.motion.x == 0.0F & data.motion.z == 0.0F) {
            model.renderRotation.setSmoothY(20.0F);
            model.renderOffset.setSmoothY(-2.0F);
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(-90.0F, 0.3F);
            model.bipedRightForeArm.rotation.setSmoothX(-80.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(-90.0F, 0.3F);
            model.bipedLeftForeArm.rotation.setSmoothX(-80.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(20.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(-20.0F, 0.3F);
            ((ModelRendererBends) model.bipedBody).rotation.setSmoothX(10.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-30.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-30.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(-25.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F);
            ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F);
            model.bipedRightForeLeg.rotation.setSmoothX(30.0F, 0.3F);
            model.bipedLeftForeLeg.rotation.setSmoothX(30.0F, 0.3F);
            ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY - 20.0F);
            ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX - 10.0F);
        }
    }
}
