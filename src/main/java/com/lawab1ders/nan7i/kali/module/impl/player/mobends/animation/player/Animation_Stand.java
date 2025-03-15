package com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.Animation;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.util.vector.Vector3f;

public class Animation_Stand extends Animation {
    public String getName() {
        return "stand";
    }

    public void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData) {
        ModelBendsPlayer model = (ModelBendsPlayer) argModel;
        Data_Player data = (Data_Player) argData;
        ((ModelRendererBends) model.bipedBody).rotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothZ(2.0F, 0.2F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothZ(-2.0F, 0.2F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothX(0.0F, 0.1F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothX(0.0F, 0.1F);
        ((ModelRendererBends) model.bipedRightLeg).rotation.setSmoothY(5.0F);
        ((ModelRendererBends) model.bipedLeftLeg).rotation.setSmoothY(-5.0F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothX(0.0F, 0.1F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothX(0.0F, 0.1F);
        model.bipedRightForeLeg.rotation.setSmoothX(4.0F, 0.1F);
        model.bipedLeftForeLeg.rotation.setSmoothX(4.0F, 0.1F);
        model.bipedRightForeArm.rotation.setSmoothX(-4.0F, 0.1F);
        model.bipedLeftForeArm.rotation.setSmoothX(-4.0F, 0.1F);
        ((ModelRendererBends) model.bipedHead).rotation.setX(model.headRotationX);
        ((ModelRendererBends) model.bipedHead).rotation.setY(model.headRotationY);
        ((ModelRendererBends) model.bipedBody).rotation.setSmoothX((float) ((Math.cos(data.ticks / 10.0F) - 1.0D) / 2.0D) * -3.0F);
        ((ModelRendererBends) model.bipedLeftArm).rotation.setSmoothZ(-((float) ((Math.cos((double) (data.ticks / 10.0F) + 1.5707963267948966D) - 1.0D) / 2.0D)) * -5.0F);
        ((ModelRendererBends) model.bipedRightArm).rotation.setSmoothZ(-((float) ((Math.cos((double) (data.ticks / 10.0F) + 1.5707963267948966D) - 1.0D) / 2.0D)) * 5.0F);
    }
}
