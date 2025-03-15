package com.lawab1ders.nan7i.kali.module.impl.player.mobends.pack;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BendsPack {
    public static final List<BendsTarget> targets = new ArrayList<>();

    public static BendsTarget getTargetByID(String argID) {
        for (BendsTarget target : targets) {
            if (target.mob.equalsIgnoreCase(argID)) {
                return target;
            }
        }

        return null;
    }

    public static void animate(ModelBendsPlayer model, String target, String anim) {
        if (getTargetByID(target) != null) {
            Objects.requireNonNull(getTargetByID(target)).applyToModel((ModelRendererBends) model.bipedBody, anim,
                    "body");
            Objects.requireNonNull(getTargetByID(target)).applyToModel((ModelRendererBends) model.bipedHead, anim,
                    "head");
            Objects.requireNonNull(getTargetByID(target)).applyToModel((ModelRendererBends) model.bipedLeftArm, anim,
                    "leftArm");
            Objects.requireNonNull(getTargetByID(target)).applyToModel((ModelRendererBends) model.bipedRightArm, anim
                    , "rightArm");
            Objects.requireNonNull(getTargetByID(target)).applyToModel((ModelRendererBends) model.bipedLeftLeg, anim,
                    "leftLeg");
            Objects.requireNonNull(getTargetByID(target)).applyToModel((ModelRendererBends) model.bipedRightLeg, anim
                    , "rightLeg");
            Objects.requireNonNull(getTargetByID(target)).applyToModel(model.bipedLeftForeArm, anim, "leftForeArm");
            Objects.requireNonNull(getTargetByID(target)).applyToModel(model.bipedRightForeArm, anim, "rightForeArm");
            Objects.requireNonNull(getTargetByID(target)).applyToModel(model.bipedLeftForeLeg, anim, "leftForeLeg");
            Objects.requireNonNull(getTargetByID(target)).applyToModel(model.bipedRightForeLeg, anim, "rightForeLeg");
            Objects.requireNonNull(getTargetByID(target)).applyToModel(model.renderItemRotation, anim, "itemRotation");
            Objects.requireNonNull(getTargetByID(target)).applyToModel(model.renderRotation, anim, "playerRotation");
        }
    }
}
