package com.lawab1ders.nan7i.kali.module.impl.player.mobends.data;

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.module.impl.player.MoBendsModule;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.SwordTrail;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.SmoothVector3f;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class Data_Player extends EntityData {
    public static final List<Data_Player> dataList = new ArrayList<>();
    public final SmoothVector3f renderOffset = new SmoothVector3f();
    public final SmoothVector3f renderRotation = new SmoothVector3f();
    public final SmoothVector3f renderItemRotation = new SmoothVector3f();
    public ModelRendererBends head;
    public ModelRendererBends headwear;
    public ModelRendererBends body;
    public ModelRendererBends rightArm;
    public ModelRendererBends leftArm;
    public ModelRendererBends rightLeg;
    public ModelRendererBends leftLeg;
    public ModelRendererBends rightForeArm;
    public ModelRendererBends leftForeArm;
    public ModelRendererBends rightForeLeg;
    public ModelRendererBends leftForeLeg;
    public SwordTrail swordTrail = new SwordTrail();
    public boolean sprintJumpLeg = false;
    public boolean fistPunchArm = false;
    public int currentAttack = 0;

    public Data_Player(int argEntityID) {
        super(argEntityID);
    }

    public static void add(Data_Player argData) {
        dataList.add(argData);
    }

    public static Data_Player get(int argEntityID) {
        for (Data_Player dataPlayer : dataList) {
            if (dataPlayer.entityID == argEntityID) {
                return dataPlayer;
            }
        }

        Data_Player newData = new Data_Player(argEntityID);
        if (Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID) != null) {
            dataList.add(newData);
        }

        return newData;
    }

    public void syncModelInfo(ModelBendsPlayer argModel) {
        if (this.head == null) {
            this.head = new ModelRendererBends(argModel);
        }

        this.head.sync((ModelRendererBends) argModel.bipedHead);
        if (this.headwear == null) {
            this.headwear = new ModelRendererBends(argModel);
        }

        this.headwear.sync((ModelRendererBends) argModel.bipedHeadwear);
        if (this.body == null) {
            this.body = new ModelRendererBends(argModel);
        }

        this.body.sync((ModelRendererBends) argModel.bipedBody);
        if (this.rightArm == null) {
            this.rightArm = new ModelRendererBends(argModel);
        }

        this.rightArm.sync((ModelRendererBends) argModel.bipedRightArm);
        if (this.leftArm == null) {
            this.leftArm = new ModelRendererBends(argModel);
        }

        this.leftArm.sync((ModelRendererBends) argModel.bipedLeftArm);
        if (this.rightLeg == null) {
            this.rightLeg = new ModelRendererBends(argModel);
        }

        this.rightLeg.sync((ModelRendererBends) argModel.bipedRightLeg);
        if (this.leftLeg == null) {
            this.leftLeg = new ModelRendererBends(argModel);
        }

        this.leftLeg.sync((ModelRendererBends) argModel.bipedLeftLeg);
        if (this.rightForeArm == null) {
            this.rightForeArm = new ModelRendererBends(argModel);
        }

        this.rightForeArm.sync(argModel.bipedRightForeArm);
        if (this.leftForeArm == null) {
            this.leftForeArm = new ModelRendererBends(argModel);
        }

        this.leftForeArm.sync(argModel.bipedLeftForeArm);
        if (this.rightForeLeg == null) {
            this.rightForeLeg = new ModelRendererBends(argModel);
        }

        this.rightForeLeg.sync(argModel.bipedRightForeLeg);
        if (this.leftForeLeg == null) {
            this.leftForeLeg = new ModelRendererBends(argModel);
        }

        this.leftForeLeg.sync(argModel.bipedLeftForeLeg);
        this.renderOffset.set(argModel.renderOffset);
        this.renderRotation.set(argModel.renderRotation);
        this.renderItemRotation.set(argModel.renderItemRotation);
        this.swordTrail = argModel.swordTrail;
    }

    public void update(float argPartialTicks) {
        super.update(argPartialTicks);
        if (this.ticksAfterPunch > 20.0F) {
            this.currentAttack = 0;
        }

        if (this.ticksAfterThrowup - this.ticksPerFrame == 0.0F) {
            this.sprintJumpLeg = !this.sprintJumpLeg;
        }

    }

    public void onLiftoff() {
        super.onLiftoff();
    }

    public void onPunch() {
        if (this.getEntity().getHeldItem() != null) {
            if (this.ticksAfterPunch > 6.0F) {
                if (this.currentAttack == 0) {
                    this.currentAttack = 1;
                    this.ticksAfterPunch = 0.0F;
                } else if (this.ticksAfterPunch < 15.0F) {
                    if (this.currentAttack == 1) {
                        this.currentAttack = 2;
                    } else if (this.currentAttack != 2) {
                        if (this.currentAttack == 3) {
                            this.currentAttack = 1;
                        }
                    } else {
                        this.currentAttack =
                                InstanceAccess.mod.getModule(MoBendsModule.class).spinAttackSetting.isActivated() && !this.getEntity().isRiding() ? 3 : 1;
                    }

                    this.ticksAfterPunch = 0.0F;
                }
            }
        } else {
            this.fistPunchArm = !this.fistPunchArm;
            this.ticksAfterPunch = 0.0F;
        }

    }
}
