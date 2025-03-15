package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.GUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Attack_Combo0 {
    public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
        if (data.ticksAfterPunch < 0.5F) {
            model.swordTrail.reset();
        }

        if (player.getCurrentEquippedItem() != null && data.ticksAfterPunch < 4.0F & player.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            model.swordTrail.add(model);
        }

        float attackState = data.ticksAfterPunch / 10.0F;
        float armSwing = attackState * 3.0F;
        armSwing = GUtil.max(armSwing, 1.0F);
        if (!player.isRiding()) {
            model.renderRotation.setSmoothY(30.0F, 0.7F);
        }

        Vector3f bodyRot = new Vector3f(0.0F, 0.0F, 0.0F);
        bodyRot.x = 20.0F - armSwing * 20.0F;
        bodyRot.y = -90.0F * armSwing;
        ((ModelRendererBends) model.bipedBody).rotation.setSmooth(bodyRot, 0.9F);
        ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY);
        ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX);
        ((ModelRendererBends) model.bipedHead).pre_rotation.setSmoothX(-model.bipedBody.rotateAngleX, 0.9F);
        ((ModelRendererBends) model.bipedHead).pre_rotation.setSmoothY(-model.bipedBody.rotateAngleY - 30.0F, 0.9F);
        ((ModelRendererBends) model.bipedRightArm).pre_rotation.setSmoothZ(60.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(-20.0F + armSwing * 100.0F, 3.0F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(60.0F - armSwing * 180.0F, 3.0F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothY(0.0F, 0.9F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(0.0F, 0.9F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(20.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftArm).pre_rotation.setSmoothZ(-80.0F, 0.3F);
        model.bipedRightForeArm.rotation.setSmoothX(-20.0F, 0.3F);
        model.bipedLeftForeArm.rotation.setSmoothX(-60.0F, 0.3F);
        if (data.motion.x == 0.0F & data.motion.z == 0.0F) {
            ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-30.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-30.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothY(0.0F, 0.3F);
            ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(-25.0F, 0.3F);
            ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F);
            ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F);
            model.bipedRightForeLeg.rotation.setSmoothX(30.0F, 0.3F);
            model.bipedLeftForeLeg.rotation.setSmoothX(30.0F, 0.3F);
            if (!player.isRiding()) {
                model.renderOffset.setSmoothY(-2.0F);
            }
        } else {
            ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(-70.0F * armSwing, 0.9F);
        }

        model.renderItemRotation.setSmoothX(180.0F, 0.9F);
    }
}
