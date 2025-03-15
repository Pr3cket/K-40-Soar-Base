package com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.AnimatedEntity;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelBoxBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends_SeperatedChild;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.SwordTrail;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.pack.BendsPack;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.pack.BendsVar;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.SmoothVector3f;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class ModelBendsPlayer extends ModelPlayer {
    public final ModelRendererBends bipedRightForeArm;
    public final ModelRendererBends bipedLeftForeArm;
    public final ModelRendererBends bipedRightForeLeg;
    public final ModelRendererBends bipedLeftForeLeg;
    public final ModelRendererBends bipedRightForeArmwear;
    public final ModelRendererBends bipedLeftForeArmwear;
    public final ModelRendererBends bipedRightForeLegwear;
    public final ModelRendererBends bipedLeftForeLegwear;
    public final ModelRendererBends bipedCloak;
    public final ModelRendererBends bipedEars;
    public final boolean smallArms;
    public final SmoothVector3f renderOffset;
    public final SmoothVector3f renderRotation;
    public final SmoothVector3f renderItemRotation;
    public SwordTrail swordTrail;
    public float headRotationX;
    public float headRotationY;
    public float armSwing;
    public float armSwingAmount;

    public ModelBendsPlayer(float p_i46304_1_, boolean p_i46304_2) {
        this(p_i46304_1_, p_i46304_2, true);
    }

    public ModelBendsPlayer(float p_i46304_1_, boolean p_i46304_2_, boolean bigTexture) {
        super(p_i46304_1_, p_i46304_2_);
        this.renderOffset = new SmoothVector3f();
        this.renderRotation = new SmoothVector3f();
        this.renderItemRotation = new SmoothVector3f();
        this.swordTrail = new SwordTrail();
        this.textureWidth = 64;
        this.textureHeight = bigTexture ? 64 : 32;
        this.smallArms = p_i46304_2_;
        this.bipedEars = new ModelRendererBends(this, 24, 0);
        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i46304_1_);
        this.bipedCloak = new ModelRendererBends(this, 0, 0);
        this.bipedCloak.setTextureSize(64, 32);
        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46304_1_);
        this.bipedHeadwear = new ModelRendererBends(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46304_1_ + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRendererBends(this, 16, 16);
        this.bipedBody.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, p_i46304_1_);
        this.bipedBody.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedHead = (new ModelRendererBends(this, 0, 0)).setShowChildIfHidden(true);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46304_1_);
        this.bipedHead.setRotationPoint(0.0F, -12.0F, 0.0F);
        ModelBoxBends var10000;
        if (p_i46304_2_) {
            this.bipedLeftArm =
                    (new ModelRendererBends_SeperatedChild(this, 32, 48)).setMother((ModelRendererBends) this.bipedBody).setShowChildIfHidden(true);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 6, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0F, -9.5F, 0.0F);
            this.bipedRightArm =
                    (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends) this.bipedBody).setShowChildIfHidden(true);
            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 6, 4, p_i46304_1_);
            this.bipedRightArm.setRotationPoint(-5.0F, -9.5F, 0.0F);
            this.bipedLeftArmwear = new ModelRendererBends(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 6, 4, p_i46304_1_ + 0.25F);
            var10000 = ((ModelRendererBends) this.bipedLeftArmwear).getBox();
            var10000.resY -= 0.25F;
            ((ModelRendererBends) this.bipedLeftArmwear).getBox().updateVertexPositions(this.bipedLeftArmwear);
            this.bipedLeftArmwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedRightArmwear = new ModelRendererBends(this, 40, 32);
            this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 6, 4, p_i46304_1_ + 0.25F);
            var10000 = ((ModelRendererBends) this.bipedRightArmwear).getBox();
            var10000.resY -= 0.25F;
            ((ModelRendererBends) this.bipedRightArmwear).getBox().updateVertexPositions(this.bipedRightArmwear);
            this.bipedRightArmwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            ((ModelRendererBends) this.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(3.02F, 6.0F,
                    4.02F).updateVertices();
            ((ModelRendererBends) this.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(3.02F, 6.0F,
                    4.02F).updateVertices();
            this.bipedLeftForeArm = new ModelRendererBends(this, 32, 54);
            this.bipedLeftForeArm.addBox(-1.0F, 0.0F, -4.0F, 3, 6, 4, p_i46304_1_);
            this.bipedLeftForeArm.setRotationPoint(0.0F, 4.0F, 2.0F);
            this.bipedLeftForeArm.getBox().offsetTextureQuad(this.bipedLeftForeArm, 3, 0.0F, -6.0F);
            this.bipedRightForeArm = new ModelRendererBends(this, 40, 22);
            this.bipedRightForeArm.addBox(-2.0F, 0.0F, -4.0F, 3, 6, 4, p_i46304_1_);
            this.bipedRightForeArm.setRotationPoint(0.0F, 4.0F, 2.0F);
            this.bipedRightForeArm.getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
            this.bipedLeftForeArmwear = new ModelRendererBends(this, 48, 54);
            this.bipedLeftForeArmwear.addBox(-1.0F, 0.0F, -4.0F, 3, 6, 4, p_i46304_1_ + 0.25F);
            var10000 = this.bipedLeftForeArmwear.getBox();
            var10000.resY -= 0.25F;
            var10000 = this.bipedLeftForeArmwear.getBox();
            var10000.offsetY += 0.25F;
            this.bipedLeftForeArmwear.getBox().updateVertexPositions(this.bipedLeftForeArmwear);
            this.bipedLeftForeArmwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedLeftForeArmwear.getBox().offsetTextureQuad(this.bipedLeftForeArmwear, 3, 0.0F, -6.0F);
            this.bipedRightForeArmwear = new ModelRendererBends(this, 40, 38);
            this.bipedRightForeArmwear.addBox(-2.0F, 0.0F, -4.0F, 3, 6, 4, p_i46304_1_ + 0.25F);
            var10000 = this.bipedRightForeArmwear.getBox();
            var10000.resY -= 0.25F;
            var10000 = this.bipedRightForeArmwear.getBox();
            var10000.offsetY += 0.25F;
            this.bipedRightForeArmwear.getBox().updateVertexPositions(this.bipedRightForeArmwear);
        } else {
            this.bipedLeftArm =
                    (new ModelRendererBends_SeperatedChild(this, 32, 48)).setMother((ModelRendererBends) this.bipedBody).setShowChildIfHidden(true);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0F, -10.0F, 0.0F);
            this.bipedRightArm =
                    (new ModelRendererBends_SeperatedChild(this, 40, 16)).setMother((ModelRendererBends) this.bipedBody).setShowChildIfHidden(true);
            this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, p_i46304_1_);
            this.bipedRightArm.setRotationPoint(-5.0F, -10.0F, 0.0F);
            this.bipedLeftArmwear = new ModelRendererBends(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
            var10000 = ((ModelRendererBends) this.bipedLeftArmwear).getBox();
            var10000.resY -= 0.25F;
            ((ModelRendererBends) this.bipedLeftArmwear).getBox().updateVertexPositions(this.bipedLeftArmwear);
            this.bipedLeftArmwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedRightArmwear = new ModelRendererBends(this, 40, 32);
            this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
            this.bipedRightArmwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            ((ModelRendererBends) this.bipedRightArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F,
                    4.02F).updateVertices();
            ((ModelRendererBends) this.bipedLeftArm).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F,
                    4.02F).updateVertices();
            this.bipedLeftForeArm = new ModelRendererBends(this, 32, 54);
            this.bipedLeftForeArm.addBox(-1.0F, 0.0F, -4.0F, 4, 6, 4, p_i46304_1_);
            this.bipedLeftForeArm.setRotationPoint(0.0F, 4.0F, 2.0F);
            this.bipedLeftForeArm.getBox().offsetTextureQuad(this.bipedLeftForeArm, 3, 0.0F, -6.0F);
            this.bipedRightForeArm = new ModelRendererBends(this, 40, 22);
            this.bipedRightForeArm.addBox(-3.0F, 0.0F, -4.0F, 4, 6, 4, p_i46304_1_);
            this.bipedRightForeArm.setRotationPoint(0.0F, 4.0F, 2.0F);
            this.bipedRightForeArm.getBox().offsetTextureQuad(this.bipedRightForeArm, 3, 0.0F, -6.0F);
            this.bipedLeftForeArmwear = new ModelRendererBends(this, 48, 54);
            this.bipedLeftForeArmwear.addBox(-1.0F, 0.0F, -4.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
            var10000 = this.bipedLeftForeArmwear.getBox();
            var10000.resY -= 0.25F;
            var10000 = this.bipedLeftForeArmwear.getBox();
            var10000.offsetY += 0.25F;
            this.bipedLeftForeArmwear.getBox().updateVertexPositions(this.bipedLeftForeArmwear);
            this.bipedLeftForeArmwear.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedLeftForeArmwear.getBox().offsetTextureQuad(this.bipedLeftForeArmwear, 3, 0.0F, -6.0F);
            this.bipedRightForeArmwear = new ModelRendererBends(this, 40, 38);
            this.bipedRightForeArmwear.addBox(-3.0F, 0.0F, -4.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
        }
        this.bipedRightForeArmwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightForeArmwear.getBox().offsetTextureQuad(this.bipedRightForeArmwear, 3, 0.0F, -6.0F);

        this.bipedRightLeg = new ModelRendererBends(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46304_1_);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg = new ModelRendererBends(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46304_1_);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLegwear = new ModelRendererBends(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
        var10000 = ((ModelRendererBends) this.bipedLeftLegwear).getBox();
        var10000.resY -= 0.25F;
        ((ModelRendererBends) this.bipedLeftLegwear).getBox().updateVertexPositions(this.bipedLeftLegwear);
        this.bipedLeftLegwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightLegwear = new ModelRendererBends(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
        var10000 = ((ModelRendererBends) this.bipedRightLegwear).getBox();
        var10000.resY -= 0.25F;
        ((ModelRendererBends) this.bipedRightLegwear).getBox().updateVertexPositions(this.bipedRightLegwear);
        this.bipedRightLegwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBodyWear = new ModelRendererBends(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, p_i46304_1_ + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightForeLeg = new ModelRendererBends(this, 0, 22);
        this.bipedRightForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, p_i46304_1_);
        this.bipedRightForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.bipedRightForeLeg.getBox().offsetTextureQuad(this.bipedRightForeLeg, 3, 0.0F, -6.0F);
        this.bipedLeftForeLeg = new ModelRendererBends(this, 16, 54);
        this.bipedLeftForeLeg.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, p_i46304_1_);
        this.bipedLeftForeLeg.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.bipedLeftForeLeg.getBox().offsetTextureQuad(this.bipedLeftForeLeg, 3, 0.0F, -6.0F);
        this.bipedRightForeLegwear = new ModelRendererBends(this, 0, 38);
        this.bipedRightForeLegwear.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
        var10000 = this.bipedRightForeLegwear.getBox();
        var10000.resY -= 0.25F;
        var10000 = this.bipedRightForeLegwear.getBox();
        var10000.offsetY += 0.25F;
        this.bipedRightForeLegwear.getBox().updateVertexPositions(this.bipedRightForeLegwear);
        this.bipedRightForeLegwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightForeLegwear.getBox().offsetTextureQuad(this.bipedRightForeLegwear, 3, 0.0F, -6.0F);
        this.bipedLeftForeLegwear = new ModelRendererBends(this, 0, 54);
        this.bipedLeftForeLegwear.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, p_i46304_1_ + 0.25F);
        var10000 = this.bipedLeftForeLegwear.getBox();
        var10000.resY -= 0.25F;
        var10000 = this.bipedLeftForeLegwear.getBox();
        var10000.offsetY += 0.25F;
        this.bipedLeftForeLegwear.getBox().updateVertexPositions(this.bipedLeftForeLegwear);
        this.bipedLeftForeLegwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedLeftForeLegwear.getBox().offsetTextureQuad(this.bipedLeftForeLegwear, 3, 0.0F, -6.0F);
        this.bipedBody.addChild(this.bipedRightArm);
        this.bipedBody.addChild(this.bipedLeftArm);
        this.bipedBody.addChild(this.bipedHead);
        this.bipedBody.addChild(this.bipedBodyWear);
        this.bipedHead.addChild(this.bipedHeadwear);
        this.bipedRightArm.addChild(this.bipedRightForeArm);
        this.bipedLeftArm.addChild(this.bipedLeftForeArm);
        this.bipedRightArm.addChild(this.bipedRightArmwear);
        this.bipedLeftArm.addChild(this.bipedLeftArmwear);
        this.bipedRightForeArm.addChild(this.bipedRightForeArmwear);
        this.bipedLeftForeArm.addChild(this.bipedLeftForeArmwear);
        this.bipedRightLeg.addChild(this.bipedRightForeLeg);
        this.bipedLeftLeg.addChild(this.bipedLeftForeLeg);
        this.bipedRightLeg.addChild(this.bipedRightLegwear);
        this.bipedLeftLeg.addChild(this.bipedLeftLegwear);
        this.bipedRightForeLeg.addChild(this.bipedRightForeLegwear);
        this.bipedLeftForeLeg.addChild(this.bipedLeftForeLegwear);
        ((ModelRendererBends_SeperatedChild) this.bipedRightArm).setSeperatedPart(this.bipedRightForeArm);
        ((ModelRendererBends_SeperatedChild) this.bipedLeftArm).setSeperatedPart(this.bipedLeftForeArm);
        ((ModelRendererBends) this.bipedRightLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
        ((ModelRendererBends) this.bipedLeftLeg).offsetBox_Add(-0.01F, 0.0F, -0.01F).resizeBox(4.02F, 6.0F, 4.02F).updateVertices();
    }

    @Override
    public void render(Entity argEntity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
                       float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, argEntity);
        GL11.glPushMatrix();
        if (this.isChild) {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GL11.glTranslatef(0.0F, 16.0F * p_78088_7_, 0.0F);
            this.bipedHead.render(p_78088_7_);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.bipedBody.render(p_78088_7_);
            this.bipedRightArm.render(p_78088_7_);
            this.bipedLeftArm.render(p_78088_7_);
            this.bipedRightLeg.render(p_78088_7_);
            this.bipedLeftLeg.render(p_78088_7_);
            this.bipedHeadwear.render(p_78088_7_);
            GL11.glPopMatrix();
        } else {
            this.bipedBody.render(p_78088_7_);
            this.bipedRightLeg.render(p_78088_7_);
            this.bipedLeftLeg.render(p_78088_7_);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void setRotationAngles(float argSwingTime, float argSwingAmount, float argArmSway, float argHeadY,
                                  float argHeadX, float argNr6, Entity argEntity) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (!Minecraft.getMinecraft().theWorld.isRemote || !Minecraft.getMinecraft().isGamePaused()) {
                Data_Player data = Data_Player.get(argEntity.getEntityId());
                this.armSwing = argSwingTime;
                this.armSwingAmount = argSwingAmount;
                this.headRotationX = argHeadX;
                this.headRotationY = argHeadY;
                if (Minecraft.getMinecraft().currentScreen != null) {
                    this.headRotationY = 0.0F;
                }

                ((ModelRendererBends) this.bipedHead).sync(data.head);
                ((ModelRendererBends) this.bipedHeadwear).sync(data.headwear);
                ((ModelRendererBends) this.bipedBody).sync(data.body);
                ((ModelRendererBends) this.bipedRightArm).sync(data.rightArm);
                ((ModelRendererBends) this.bipedLeftArm).sync(data.leftArm);
                ((ModelRendererBends) this.bipedRightLeg).sync(data.rightLeg);
                ((ModelRendererBends) this.bipedLeftLeg).sync(data.leftLeg);
                this.bipedRightForeArm.sync(data.rightForeArm);
                this.bipedLeftForeArm.sync(data.leftForeArm);
                this.bipedRightForeLeg.sync(data.rightForeLeg);
                this.bipedLeftForeLeg.sync(data.leftForeLeg);
                this.renderOffset.set(data.renderOffset);
                this.renderRotation.set(data.renderRotation);
                this.renderItemRotation.set(data.renderItemRotation);
                this.swordTrail = data.swordTrail;
                if (Data_Player.get(argEntity.getEntityId()).canBeUpdated()) {
                    this.renderOffset.setSmooth(new Vector3f(0.0F, -1.0F, 0.0F), 0.5F);
                    this.renderRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
                    this.renderItemRotation.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
                    ((ModelRendererBends) this.bipedHead).resetScale();
                    ((ModelRendererBends) this.bipedHeadwear).resetScale();
                    ((ModelRendererBends) this.bipedBody).resetScale();
                    ((ModelRendererBends) this.bipedRightArm).resetScale();
                    ((ModelRendererBends) this.bipedLeftArm).resetScale();
                    ((ModelRendererBends) this.bipedRightLeg).resetScale();
                    ((ModelRendererBends) this.bipedLeftLeg).resetScale();
                    this.bipedRightForeArm.resetScale();
                    this.bipedLeftForeArm.resetScale();
                    this.bipedRightForeLeg.resetScale();
                    this.bipedLeftForeLeg.resetScale();
                    BendsVar.tempData = Data_Player.get(argEntity.getEntityId());
                    if (argEntity.isRiding()) {
                        AnimatedEntity.getByEntity(argEntity).get("riding").animate((EntityLivingBase) argEntity,
                                this, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(this, "player", "riding");
                    } else if (argEntity.isInWater()) {
                        AnimatedEntity.getByEntity(argEntity).get("swimming").animate((EntityLivingBase) argEntity,
                                this, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(this, "player", "swimming");
                    } else if (!Data_Player.get(argEntity.getEntityId()).isOnGround() | Data_Player.get(argEntity.getEntityId()).ticksAfterTouchdown < 2.0F) {
                        AnimatedEntity.getByEntity(argEntity).get("jump").animate((EntityLivingBase) argEntity, this,
                                Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(this, "player", "jump");
                    } else {
                        if (Data_Player.get(argEntity.getEntityId()).motion.x == 0.0F & Data_Player.get(argEntity.getEntityId()).motion.z == 0.0F) {
                            AnimatedEntity.getByEntity(argEntity).get("stand").animate((EntityLivingBase) argEntity,
                                    this, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(this, "player", "stand");
                        } else if (argEntity.isSprinting()) {
                            AnimatedEntity.getByEntity(argEntity).get("sprint").animate((EntityLivingBase) argEntity,
                                    this, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(this, "player", "sprint");
                        } else {
                            AnimatedEntity.getByEntity(argEntity).get("walk").animate((EntityLivingBase) argEntity,
                                    this, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(this, "player", "walk");
                        }

                        if (argEntity.isSneaking()) {
                            AnimatedEntity.getByEntity(argEntity).get("sneak").animate((EntityLivingBase) argEntity,
                                    this, Data_Player.get(argEntity.getEntityId()));
                            BendsPack.animate(this, "player", "sneak");
                        }
                    }

                    if (this.aimedBow) {
                        AnimatedEntity.getByEntity(argEntity).get("bow").animate((EntityLivingBase) argEntity, this,
                                Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(this, "player", "bow");
                    } else if (((EntityPlayer) argEntity).getCurrentEquippedItem() != null && ((EntityPlayer) argEntity).getCurrentEquippedItem().getItem() instanceof ItemPickaxe || ((EntityPlayer) argEntity).getCurrentEquippedItem() != null && Block.getBlockFromItem(((EntityPlayer) argEntity).getCurrentEquippedItem().getItem()) != null) {
                        AnimatedEntity.getByEntity(argEntity).get("mining").animate((EntityLivingBase) argEntity,
                                this, Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(this, "player", "mining");
                    } else if (((EntityPlayer) argEntity).getCurrentEquippedItem() != null && ((EntityPlayer) argEntity).getCurrentEquippedItem().getItem() instanceof ItemAxe) {
                        AnimatedEntity.getByEntity(argEntity).get("axe").animate((EntityLivingBase) argEntity, this,
                                Data_Player.get(argEntity.getEntityId()));
                        BendsPack.animate(this, "player", "axe");
                    } else {
                        AnimatedEntity.getByEntity(argEntity).get("attack").animate((EntityLivingBase) argEntity,
                                this, Data_Player.get(argEntity.getEntityId()));
                    }

                    ((ModelRendererBends) this.bipedHead).update(data.ticksPerFrame);
                    ((ModelRendererBends) this.bipedHeadwear).update(data.ticksPerFrame);
                    ((ModelRendererBends) this.bipedBody).update(data.ticksPerFrame);
                    ((ModelRendererBends) this.bipedLeftArm).update(data.ticksPerFrame);
                    ((ModelRendererBends) this.bipedRightArm).update(data.ticksPerFrame);
                    ((ModelRendererBends) this.bipedLeftLeg).update(data.ticksPerFrame);
                    ((ModelRendererBends) this.bipedRightLeg).update(data.ticksPerFrame);
                    this.bipedLeftForeArm.update(data.ticksPerFrame);
                    this.bipedRightForeArm.update(data.ticksPerFrame);
                    this.bipedLeftForeLeg.update(data.ticksPerFrame);
                    this.bipedRightForeLeg.update(data.ticksPerFrame);
                    this.renderOffset.update(data.ticksPerFrame);
                    this.renderRotation.update(data.ticksPerFrame);
                    this.renderItemRotation.update(data.ticksPerFrame);
                    this.swordTrail.update(data.ticksPerFrame);
                    data.updatedThisFrame = true;
                }

                Data_Player.get(argEntity.getEntityId()).syncModelInfo(this);
            }
        }
    }

    public void postRender(float argScale) {
        GlStateManager.translate(this.renderOffset.vSmooth.x * argScale, -this.renderOffset.vSmooth.y * argScale,
                this.renderOffset.vSmooth.z * argScale);
        GlStateManager.rotate(this.renderRotation.getX(), 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(this.renderRotation.getY(), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.renderRotation.getZ(), 0.0F, 0.0F, 1.0F);
    }

    public void postRenderTranslate(float argScale) {
        GlStateManager.translate(this.renderOffset.vSmooth.x * argScale, -this.renderOffset.vSmooth.y * argScale,
                this.renderOffset.vSmooth.z * argScale);
    }

    public void postRenderRotate(float argScale) {
        GlStateManager.rotate(this.renderRotation.getX(), 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(this.renderRotation.getY(), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.renderRotation.getZ(), 0.0F, 0.0F, 1.0F);
    }

    @Override
    public void postRenderArm(float argScale) {
        this.bipedRightArm.postRender(argScale);
        this.bipedRightForeArm.postRender(argScale);
        GL11.glTranslatef(0.0F * argScale, 4.0F * argScale, -2.0F * argScale);
        GL11.glRotatef(this.renderItemRotation.vSmooth.x, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(this.renderItemRotation.vSmooth.y, 0.0F, -1.0F, 0.0F);
        GL11.glRotatef(this.renderItemRotation.vSmooth.z, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(0.0F * argScale, -8.0F * argScale, 0.0F * argScale);
    }

    public void updateWithEntityData(AbstractClientPlayer argPlayer) {
        Data_Player data = Data_Player.get(argPlayer.getEntityId());
        if (data != null) {
            this.renderOffset.set(data.renderOffset);
            this.renderRotation.set(data.renderRotation);
            this.renderItemRotation.set(data.renderItemRotation);
        }
    }
}
