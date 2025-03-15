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

public class Animation_Mining extends Animation {
    public String getName() {
        return "mining";
    }

    public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
        ModelBendsPlayer model = (ModelBendsPlayer) argModel;
        Data_Player data = (Data_Player) argData;
        EntityPlayer player = (EntityPlayer) argEntity;
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(10.0F, 0.3F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-10.0F, 0.3F);
        model.renderOffset.setSmoothY(-1.5F, 0.3F);
        if (player.isSwingInProgress) {
            float speed = 1.8F;
            float progress = (float) player.ticksExisted * speed / 20.0F % 1.0F;
            float progress2 = (float) (player.ticksExisted - 2) * speed / 20.0F % 1.0F;
            float armSwing =
                    (MathHelper.cos(progress * 3.1415927F * 2.0F) + 1.0F) / 2.0F * -60.0F - 30.0F + model.headRotationX * 0.5F - 30.0F;
            float armYRot = 30.0F + MathHelper.cos((armSwing - 90.0F) / 180.0F * 3.14F) * -5.0F;
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(armSwing, 0.7F);
            ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothY(-armYRot, 0.7F);
            model.renderItemRotation.setSmoothZ(-30.0F, 0.3F);
            ((ModelRendererBends) model.bipedBody).rotation.setSmoothY(MathHelper.sin(progress2 * 3.1415927F * 2.0F) * -20.0F);
            ((ModelRendererBends) model.bipedHead).rotation.setSmoothX(model.headRotationX - model.bipedBody.rotateAngleX);
            ((ModelRendererBends) model.bipedHead).rotation.setSmoothY(model.headRotationY - model.bipedBody.rotateAngleY);
        }

    }
}
