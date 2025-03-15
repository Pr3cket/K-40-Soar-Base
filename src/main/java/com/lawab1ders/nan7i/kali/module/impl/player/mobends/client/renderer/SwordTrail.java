package com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer;

import com.lawab1ders.nan7i.kali.module.impl.player.MoBendsModule;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.ModelRendererBends;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.util.GUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SwordTrail {
    public final List<SwordTrail.TrailPart> trailPartList = new ArrayList<>();

    public void reset() {
        this.trailPartList.clear();
    }

    public void render() {
        Minecraft.getMinecraft().renderEngine.bindTexture(MoBendsModule.texture_NULL);
        GL11.glDepthFunc(515);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2884);
        GL11.glDisable(2896);
        GL11.glHint(3152, 4354);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);

        for (int i = 0; i < this.trailPartList.size(); ++i) {
            SwordTrail.TrailPart part = this.trailPartList.get(i);
            float alpha = part.ticksExisted / 5.0F;
            alpha = GUtil.max(alpha, 1.0F);
            alpha = 1.0F - alpha;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            Vector3f[] point = new Vector3f[] { new Vector3f(0.0F, 0.0F, -8.0F + 8.0F * alpha), new Vector3f(0.0F,
                    0.0F, -8.0F - 8.0F * alpha) };
            GUtil.rotateX(point, part.itemRotation.getX());
            GUtil.rotateY(point, part.itemRotation.getY());
            GUtil.rotateZ(point, part.itemRotation.getZ());
            GUtil.translate(point, new Vector3f(-1.0F, -6.0F, 0.0F));
            GUtil.rotateX(point, part.foreArm.rotateAngleX / 3.1415927F * 180.0F);
            GUtil.rotateY(point, part.foreArm.rotateAngleY / 3.1415927F * 180.0F);
            GUtil.rotateZ(point, part.foreArm.rotateAngleZ / 3.1415927F * 180.0F);
            GUtil.rotateX(point, part.foreArm.pre_rotation.getX());
            GUtil.rotateY(point, part.foreArm.pre_rotation.getY());
            GUtil.rotateZ(point, -part.foreArm.pre_rotation.getZ());
            GUtil.translate(point, new Vector3f(0.0F, -4.0F, 0.0F));
            GUtil.rotateX(point, part.arm.rotateAngleX / 3.1415927F * 180.0F);
            GUtil.rotateY(point, part.arm.rotateAngleY / 3.1415927F * 180.0F);
            GUtil.rotateZ(point, part.arm.rotateAngleZ / 3.1415927F * 180.0F);
            GUtil.rotateX(point, part.arm.pre_rotation.getX());
            GUtil.rotateY(point, part.arm.pre_rotation.getY());
            GUtil.rotateZ(point, -part.arm.pre_rotation.getZ());
            GUtil.translate(point, new Vector3f(-5.0F, 10.0F, 0.0F));
            GUtil.rotateX(point, part.body.rotateAngleX / 3.1415927F * 180.0F);
            GUtil.rotateY(point, part.body.rotateAngleY / 3.1415927F * 180.0F);
            GUtil.rotateZ(point, part.body.rotateAngleZ / 3.1415927F * 180.0F);
            GUtil.rotateX(point, part.body.pre_rotation.getX());
            GUtil.rotateY(point, part.body.pre_rotation.getY());
            GUtil.rotateZ(point, part.body.pre_rotation.getZ());
            GUtil.translate(point, new Vector3f(0.0F, 12.0F, 0.0F));
            GUtil.rotateX(point, part.renderRotation.getX());
            GUtil.rotateY(point, part.renderRotation.getY());
            GUtil.translate(point, part.renderOffset);
            if (i > 0) {
                GL11.glVertex3f(point[1].x, point[1].y, point[1].z);
                GL11.glVertex3f(point[0].x, point[0].y, point[0].z);
                GL11.glVertex3f(point[0].x, point[0].y, point[0].z);
                GL11.glVertex3f(point[1].x, point[1].y, point[1].z);
            } else {
                GL11.glVertex3f(point[0].x, point[0].y, point[0].z);
                GL11.glVertex3f(point[1].x, point[1].y, point[1].z);
            }

            if (i == this.trailPartList.size() - 1) {
                GL11.glVertex3f(point[1].x, point[1].y, point[1].z);
                GL11.glVertex3f(point[0].x, point[0].y, point[0].z);
            }
        }

        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(2896);
    }

    public void add(ModelBendsPlayer argModel) {
        SwordTrail.TrailPart newPart = new TrailPart(argModel);
        newPart.body.sync((ModelRendererBends) argModel.bipedBody);
        newPart.body.setPosition(argModel.bipedBody.rotationPointX, argModel.bipedBody.rotationPointY,
                argModel.bipedBody.rotationPointZ);
        newPart.body.setOffset(argModel.bipedBody.offsetX, argModel.bipedBody.offsetY, argModel.bipedBody.offsetZ);
        newPart.arm.sync((ModelRendererBends) argModel.bipedRightArm);
        newPart.arm.setPosition(argModel.bipedRightArm.rotationPointX, argModel.bipedRightArm.rotationPointY,
                argModel.bipedRightArm.rotationPointZ);
        newPart.arm.setOffset(argModel.bipedRightArm.offsetX, argModel.bipedRightArm.offsetY,
                argModel.bipedRightArm.offsetZ);
        newPart.foreArm.sync(argModel.bipedRightForeArm);
        newPart.foreArm.setPosition(argModel.bipedRightForeArm.rotationPointX,
                argModel.bipedRightForeArm.rotationPointY, argModel.bipedRightForeArm.rotationPointZ);
        newPart.foreArm.setOffset(argModel.bipedRightForeArm.offsetX, argModel.bipedRightForeArm.offsetY,
                argModel.bipedRightForeArm.offsetZ);
        newPart.renderOffset.set(argModel.renderOffset.vSmooth);
        newPart.renderRotation.set(argModel.renderRotation.vSmooth);
        newPart.itemRotation.set(argModel.renderItemRotation.vSmooth);
        this.trailPartList.add(newPart);
    }

    public void update(float argPartialTicks) {
        int i;
        for (i = 0; i < this.trailPartList.size(); ++i) {
            SwordTrail.TrailPart var10000 = this.trailPartList.get(i);
            var10000.ticksExisted += argPartialTicks;
        }

        for (i = 0; i < this.trailPartList.size(); ++i) {
            if (this.trailPartList.get(i).ticksExisted > 20.0F) {
                this.trailPartList.remove(this.trailPartList.get(i));
            }
        }

    }

    public static class TrailPart {
        public final ModelRendererBends body;
        public final ModelRendererBends arm;
        public final ModelRendererBends foreArm;
        public final Vector3f renderRotation = new Vector3f();
        public final Vector3f renderOffset = new Vector3f();
        public final Vector3f itemRotation = new Vector3f();
        float ticksExisted;

        public TrailPart(ModelBendsPlayer argModel) {
            this.body = new ModelRendererBends(argModel);
            this.arm = new ModelRendererBends(argModel);
            this.foreArm = new ModelRendererBends(argModel);
        }
    }
}
