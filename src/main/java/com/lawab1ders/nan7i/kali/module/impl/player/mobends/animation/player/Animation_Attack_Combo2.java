package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.GUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Attack_Combo2 {
    public static void animate(EntityPlayer player, ModelBendsPlayer model, Data_Player data) {
        if (data.ticksAfterPunch < 0.5F) {
            model.swordTrail.reset();
        }

        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            model.swordTrail.add(model);
        }

        float attackState = data.ticksAfterPunch / 10.0F;
        float armSwing = attackState * 2.0F;
        armSwing = GUtil.max(armSwing, 1.0F);
        float var5 = attackState * 1.6F;
        var5 = GUtil.max(var5, 1.0F);
        float var = 50.0F + 360.0F * var5;

        float var2;
        for (var2 = 50.0F + 360.0F * var5; var2 > 360.0F; var2 -= 360.0F) {
        }

        if (var > 360.0F) {
            float var3 = (attackState - data.ticksPerFrame / 10.0F) * 2.0F;
            var3 = GUtil.max(var3, 1.0F);
            model.renderRotation.vOld.y = var2;
            model.renderRotation.vFinal.y = var2;
            model.renderRotation.completion.y = 0.0F;
        } else {
            model.renderRotation.setSmoothY(var, 0.7F);
        }

        Vector3f bodyRot = new Vector3f(0.0F, 0.0F, 0.0F);
        bodyRot.x = 20.0F - attackState * 20.0F;
        bodyRot.y = -40.0F * attackState;
        ((ModelRendererBends) model.bipedBody).rotation.setSmooth(bodyRot, 0.9F);
        ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY);
        ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX);
        ((ModelRendererBends) model.bipedHead).pre_rotation.setSmoothX(-bodyRot.x, 0.9F);
        ((ModelRendererBends) model.bipedHead).pre_rotation.setSmoothY(-bodyRot.y, 0.9F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(-30.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(-30.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F);
        model.bipedRightForeLeg.rotation.setSmoothX(30.0F, 0.3F);
        model.bipedLeftForeLeg.rotation.setSmoothX(30.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightArm).pre_rotation.setSmoothZ(-(-60.0F - var5 * 80.0F), 0.3F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(-20.0F + armSwing * 70.0F, 3.0F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothY(0.0F, 0.3F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(0.0F, 0.9F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(20.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftArm).pre_rotation.setSmoothZ(-80.0F, 0.3F);
        model.bipedRightForeArm.rotation.setSmoothX(-20.0F, 0.3F);
        model.bipedLeftForeArm.rotation.setSmoothX(-60.0F, 0.3F);
        model.renderItemRotation.setSmoothX(90.0F * attackState, 0.9F);
        float var61 = data.ticksAfterPunch * 5.0F;
        float var62 = data.ticksAfterPunch * 5.0F;
        var61 = (MathHelper.cos(var61 * 0.0625F) + 1.0F) / 2.0F * 20.0F;
        var62 = (MathHelper.cos(var62 * 0.0625F) + 1.0F) / 2.0F * 20.0F;
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothY(0.0F, 0.9F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(-25.0F, 0.9F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(var61);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-var61);
        model.renderOffset.setSmoothY(-2.0F);
    }
}
