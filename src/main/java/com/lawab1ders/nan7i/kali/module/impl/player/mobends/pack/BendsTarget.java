package com.lawab1ders.nan7i.kali.module.impl.player.mobends.pack;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.EnumAxis;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.SmoothVector3f;

import java.util.ArrayList;
import java.util.List;

public class BendsTarget {
    public final String mob;
    public final List<BendsAction> actions = new ArrayList<>();
    public final float visual_DeletePopUp;

    public BendsTarget(String argMob) {
        this.mob = argMob;
        this.visual_DeletePopUp = 0.0F;
    }

    public void applyToModel(ModelRendererBends box, String anim, String model) {
        for (BendsAction action : this.actions) {
            if ((action.anim.equalsIgnoreCase(anim) | action.anim.equalsIgnoreCase("all")) & action.model.equalsIgnoreCase(model)) {
                if (action.prop == BendsAction.EnumBoxProperty.ROT) {
                    box.rotation.setSmooth(action.axis, action.getNumber(action.axis == EnumAxis.X ?
                            box.rotation.vFinal.x : (action.axis == EnumAxis.Y ? box.rotation.vFinal.y :
                            box.rotation.vFinal.z)), action.smooth);
                } else if (action.prop == BendsAction.EnumBoxProperty.PREROT) {
                    box.pre_rotation.setSmooth(action.axis, action.getNumber(action.axis == EnumAxis.X ?
                            box.pre_rotation.vFinal.x : (action.axis == EnumAxis.Y ? box.pre_rotation.vFinal.y :
                            box.pre_rotation.vFinal.z)), action.smooth);
                } else if (action.prop == BendsAction.EnumBoxProperty.SCALE) {
                    if (action.axis == null | action.axis == EnumAxis.X) {
                        box.scaleX = action.getNumber(box.scaleX);
                    }

                    if (action.axis == null | action.axis == EnumAxis.Y) {
                        box.scaleY = action.getNumber(box.scaleY);
                    }

                    if (action.axis == null | action.axis == EnumAxis.Z) {
                        box.scaleZ = action.getNumber(box.scaleZ);
                    }
                }
            }
        }

    }

    public void applyToModel(SmoothVector3f box, String anim, String model) {
        for (BendsAction action : this.actions) {
            if ((action.anim.equalsIgnoreCase(anim) | action.anim.equalsIgnoreCase("all")) & action.model.equalsIgnoreCase(model) && action.prop == BendsAction.EnumBoxProperty.ROT) {
                box.setSmooth(action.axis, action.getNumber(action.axis == EnumAxis.X ? box.vFinal.x :
                        (action.axis == EnumAxis.Y ? box.vFinal.y : box.vFinal.z)), action.smooth);
            }
        }
    }
}
