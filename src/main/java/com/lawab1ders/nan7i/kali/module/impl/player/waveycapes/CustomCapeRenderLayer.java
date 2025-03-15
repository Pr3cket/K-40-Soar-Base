package com.lawab1ders.nan7i.kali.module.impl.player.waveycapes;

/*
 * Copyright (c) 2025 EldoDebug, Nan7i.南起
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.injection.interfaces.IEntityPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.WaveyCapesModule;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.matrix.AMtx4f;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.vec.AVec3f;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.vec.AVec4f;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.PoseStack;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import lombok.val;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;

public class CustomCapeRenderLayer implements LayerRenderer<AbstractClientPlayer> {

    public static final int partCount = 16;
    private ModelRenderer[] customCape = new ModelRenderer[partCount];
    private final RenderPlayer playerRenderer;

    public CustomCapeRenderLayer(RenderPlayer playerRenderer, ModelBase model) {
        this.playerRenderer = playerRenderer;
        buildMesh(model);
    }

    private void buildMesh(ModelBase model) {
        customCape = new ModelRenderer[partCount];
        for (int i = 0; i < partCount; i++) {
            ModelRenderer base = new ModelRenderer(model, 0, i);
            base.setTextureSize(64, 32);
            this.customCape[i] = base.addBox(-5.0F, (float) i, -1.0F, 10, 1, 1);
        }
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer abstractClientPlayer, float paramFloat1, float paramFloat2,
                              float deltaTick, float animationTick, float paramFloat5, float paramFloat6,
                              float paramFloat7) {

        val module = InstanceAccess.mod.getModule(WaveyCapesModule.class);

        String styleSetting = module.styleSetting.getSelectedEntryKey();

        if (abstractClientPlayer.isInvisible() || !module.isActivated()) {
            return;
        }

        if (!abstractClientPlayer.hasPlayerInfo() || abstractClientPlayer.isInvisible()
                || !abstractClientPlayer.isWearing(EnumPlayerModelParts.CAPE)
                || abstractClientPlayer.getLocationCape() == null) {
            return;
        }

        if (module.flutteredSetting.isActivated()) {
            IEntityPlayer holder = (IEntityPlayer) abstractClientPlayer;
            holder.k40$updateSimulation(abstractClientPlayer, partCount);
        }

        this.playerRenderer.bindTexture(abstractClientPlayer.getLocationCape());

        if (styleSetting.equals("module.waveycapes.opts.style.smooth")) {
            renderSmoothCape(this, abstractClientPlayer, deltaTick);
        }
        else {
            ModelRenderer[] parts = customCape;
            for (int part = 0; part < partCount; part++) {
                ModelRenderer model = parts[part];
                GlStateManager.pushMatrix();
                modifyPoseStack(abstractClientPlayer, deltaTick, part);
                model.render(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }

    private void modifyPoseStack(AbstractClientPlayer abstractClientPlayer, float h, int part) {

        val module = InstanceAccess.mod.getModule(WaveyCapesModule.class);

        if (module.flutteredSetting.isActivated()) {
            modifyPoseStackSimulation(abstractClientPlayer, h, part);
            return;
        }

        modifyPoseStackVanilla(abstractClientPlayer, h, part);
    }

    private void modifyPoseStackSimulation(AbstractClientPlayer abstractClientPlayer, float delta, int part) {

        StickSimulation simulation = ((IEntityPlayer) abstractClientPlayer).k40$getSimulation();
        GlStateManager.translate(0.0D, 0.0D, 0.125D);

        float z = simulation.points.get(part).getLerpX(delta) - simulation.points.get(0).getLerpX(delta);

        if (z > 0) z = 0;

        float y = simulation.points.get(0).getLerpY(delta) - part - simulation.points.get(part).getLerpY(delta);
        float sidewaysRotationOffset = 0;
        float height = 0;

        if (abstractClientPlayer.isSneaking()) {
            height += 25.0F;
            GlStateManager.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = getNatrualWindSwing(part);

        GlStateManager.rotate(6.0F + height + naturalWindSwing, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sidewaysRotationOffset / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-sidewaysRotationOffset / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0, y / partCount, z / partCount);
        GlStateManager.translate(0, /*-offset +*/(0.48 / 16), -(0.48 / 16));
        GlStateManager.translate(0, part * 1f / partCount, (float) (0) / partCount);
        GlStateManager.translate(0, -part * 1f / partCount, (float) (0) / partCount);
        GlStateManager.translate(0, -(0.48 / 16), (0.48 / 16));
    }

    public void modifyPoseStackVanilla(AbstractClientPlayer abstractClientPlayer, float h, int part) {

        GlStateManager.translate(0.0D, 0.0D, 0.125D);

        double d = MathUtils.lerp(
                h, abstractClientPlayer.prevChasingPosX, abstractClientPlayer.chasingPosX) - MathUtils.lerp(
                h, abstractClientPlayer.prevPosX, abstractClientPlayer.posX
        );
        double e = MathUtils.lerp(
                h, abstractClientPlayer.prevChasingPosY, abstractClientPlayer.chasingPosY) - MathUtils.lerp(
                h, abstractClientPlayer.prevPosY, abstractClientPlayer.posY
        );
        double m = MathUtils.lerp(
                h, abstractClientPlayer.prevChasingPosZ, abstractClientPlayer.chasingPosZ) - MathUtils.lerp(
                h, abstractClientPlayer.prevPosZ, abstractClientPlayer.posZ
        );

        float n = abstractClientPlayer.prevRenderYawOffset
                + abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset;

        double o = Math.sin(n * 0.017453292F), p = -Math.cos(n * 0.017453292F);

        float height = (float) e * 10.0F;
        height = MathHelper.clamp_float(height, -6.0F, 32.0F);

        float swing = (float) (d * o + m * p) * easeOutSine(1.0F / partCount * part) * 100;
        swing = MathHelper.clamp_float(swing, 0.0F, 150.0F * easeOutSine(1F / partCount * part));

        float sidewaysRotationOffset = (float) (d * p - m * o) * 100.0F;
        sidewaysRotationOffset = MathHelper.clamp_float(sidewaysRotationOffset, -20.0F, 20.0F);

        float t = MathUtils.lerp(h, abstractClientPlayer.prevCameraYaw, abstractClientPlayer.cameraYaw);

        height += (float) (Math.sin(MathUtils.lerp(
                h, abstractClientPlayer.prevDistanceWalkedModified,
                abstractClientPlayer.distanceWalkedModified
        ) * 6.0F) * 32.0F * t);

        if (abstractClientPlayer.isSneaking()) {
            height += 25.0F;
            GlStateManager.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = getNatrualWindSwing(part);

        GlStateManager.rotate(6.0F + swing / 2.0F + height + naturalWindSwing, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sidewaysRotationOffset / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-sidewaysRotationOffset / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
    }

    public float getNatrualWindSwing(int part) {
        val module = InstanceAccess.mod.getModule(WaveyCapesModule.class);

        if (module.swingWithTheWindSetting.isActivated()) {
            long highlightedPart = (System.currentTimeMillis() / 3) % 360;
            float relativePart = (float) (part + 1) / partCount;

            return (float) (Math.sin(Math.toRadians((relativePart) * 360 - (highlightedPart))) * 3);
        }

        return 0;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    //---------------------------------------------------------------------------
    // Smooth
    //---------------------------------------------------------------------------

    // 添加常量定义
    private static final float CAPE_WIDTH = 0.3F;
    private static final float CAPE_HEIGHT = 0.96F;
    private static final float CAPE_DEPTH = 0.06F;
    private static final float SNEAK_OFFSET = 0.15F;
    private static final float BASE_ROTATION = 6.0F;

    private void renderSmoothCape(CustomCapeRenderLayer layer, AbstractClientPlayer player, float delta) {
        WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();

        AMtx4f oldPositionMatrix = null;
        for (int part = 0; part < CustomCapeRenderLayer.partCount; part++) {
            modifyPoseStack(layer, poseStack, player, delta, part);

            if (oldPositionMatrix == null) {
                oldPositionMatrix = poseStack.last().pose;
            }

            float partHeight = (part + 1) * (CAPE_HEIGHT / CustomCapeRenderLayer.partCount);
            float prevPartHeight = part * (CAPE_HEIGHT / CustomCapeRenderLayer.partCount);

            if (part == 0) {
                addTopVertex(
                        worldrenderer, poseStack.last().pose, oldPositionMatrix,
                        CAPE_WIDTH, 0, 0F, -CAPE_WIDTH, 0, -CAPE_DEPTH, part
                );
            }

            if (part == CustomCapeRenderLayer.partCount - 1) {
                addBottomVertex(
                        worldrenderer, poseStack.last().pose, poseStack.last().pose,
                        CAPE_WIDTH, partHeight, 0F, -CAPE_WIDTH, partHeight, -CAPE_DEPTH, part
                );
            }

            addLeftVertex(
                    worldrenderer, poseStack.last().pose, oldPositionMatrix,
                    -CAPE_WIDTH, partHeight, 0F, -CAPE_WIDTH, prevPartHeight, -CAPE_DEPTH, part
            );

            addRightVertex(
                    worldrenderer, poseStack.last().pose, oldPositionMatrix,
                    CAPE_WIDTH, partHeight, 0F, CAPE_WIDTH, prevPartHeight, -CAPE_DEPTH, part
            );

            addBackVertex(
                    worldrenderer, poseStack.last().pose, oldPositionMatrix,
                    CAPE_WIDTH, partHeight, -CAPE_DEPTH, -CAPE_WIDTH, prevPartHeight, -CAPE_DEPTH, part
            );

            addFrontVertex(
                    worldrenderer, oldPositionMatrix, poseStack.last().pose,
                    CAPE_WIDTH, partHeight, 0F, -CAPE_WIDTH, prevPartHeight, 0F, part
            );

            oldPositionMatrix = poseStack.last().pose;
            poseStack.popPose();
        }
        Tessellator.getInstance().draw();
    }

    private void modifyPoseStack(CustomCapeRenderLayer layer, PoseStack poseStack,
                                 AbstractClientPlayer abstractClientPlayer, float h, int part) {
        val module = InstanceAccess.mod.getModule(WaveyCapesModule.class);

        if (module.flutteredSetting.isActivated()) {
            modifyPoseStackSimulation(layer, poseStack, abstractClientPlayer, h, part);
            return;
        }

        modifyPoseStackVanilla(layer, poseStack, abstractClientPlayer, h, part);
    }

    private void modifyPoseStackSimulation(CustomCapeRenderLayer layer, PoseStack poseStack,
                                           AbstractClientPlayer abstractClientPlayer, float delta, int part) {

        StickSimulation simulation = ((IEntityPlayer) abstractClientPlayer).k40$getSimulation();
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.125D);

        float z = simulation.points.get(part).getLerpX(delta) - simulation.points.get(0).getLerpX(delta);

        if (z > 0) z = 0;

        float y = simulation.points.get(0).getLerpY(delta) - part - simulation.points.get(part).getLerpY(delta);
        float sidewaysRotationOffset = 0;
        float partRotation = (float) -Math.atan2(y, z);

        partRotation = Math.max(partRotation, 0);
        if (partRotation != 0) partRotation = (float) (Math.PI - partRotation);

        partRotation *= 57.2958F;
        partRotation *= 2;

        float height = 0;

        if (abstractClientPlayer.isSneaking()) {
            height += 25.0F;
            poseStack.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = layer.getNatrualWindSwing(part);

        poseStack.mulPose(AVec3f.XP.rotationDegrees(6.0F + height + naturalWindSwing));
        poseStack.mulPose(AVec3f.ZP.rotationDegrees(sidewaysRotationOffset / 2.0F));
        poseStack.mulPose(AVec3f.YP.rotationDegrees(180.0F - sidewaysRotationOffset / 2.0F));
        poseStack.translate(0, y / CustomCapeRenderLayer.partCount, z / CustomCapeRenderLayer.partCount);
        poseStack.translate(0, (0.48 / 16), -(0.48 / 16));
        poseStack.translate(
                0, part * 1f / CustomCapeRenderLayer.partCount, part * (0) / CustomCapeRenderLayer.partCount);
        poseStack.mulPose(AVec3f.XP.rotationDegrees(-partRotation));
        poseStack.translate(
                0, -part * 1f / CustomCapeRenderLayer.partCount, -part * (0) / CustomCapeRenderLayer.partCount);
        poseStack.translate(0, -(0.48 / 16), (0.48 / 16));
    }

    private void modifyPoseStackVanilla(CustomCapeRenderLayer layer, PoseStack poseStack,
                                        AbstractClientPlayer player, float delta, int part) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.125D);

        double deltaX = MathUtils.lerp(delta, player.prevChasingPosX, player.chasingPosX) -
                MathUtils.lerp(delta, player.prevPosX, player.posX);
        double deltaY = MathUtils.lerp(delta, player.prevChasingPosY, player.chasingPosY) -
                MathUtils.lerp(delta, player.prevPosY, player.posY);
        double deltaZ = MathUtils.lerp(delta, player.prevChasingPosZ, player.chasingPosZ) -
                MathUtils.lerp(delta, player.prevPosZ, player.posZ);

        float yawOffset = player.prevRenderYawOffset +
                (player.renderYawOffset - player.prevRenderYawOffset);
        double sinYaw = Math.sin(yawOffset * 0.017453292F);
        double cosYaw = -Math.cos(yawOffset * 0.017453292F);

        float height = (float) deltaY * 10.0F;
        height = MathHelper.clamp_float(height, -6.0F, 32.0F);

        float swing = (float) (deltaX * sinYaw + deltaZ * cosYaw) *
                easeOutSine(1.0F / CustomCapeRenderLayer.partCount * part) * 100;
        swing = MathHelper.clamp_float(
                swing, 0.0F,
                150.0F * easeOutSine(1F / CustomCapeRenderLayer.partCount * part)
        );

        float sidewaysRotationOffset = (float) (deltaX * cosYaw - deltaZ * sinYaw) * 100.0F;
        sidewaysRotationOffset = MathHelper.clamp_float(sidewaysRotationOffset, -20.0F, 20.0F);

        float cameraYaw = MathUtils.lerp(delta, player.prevCameraYaw, player.cameraYaw);
        height += (float) (Math.sin(MathUtils.lerp(
                delta, player.prevDistanceWalkedModified,
                player.distanceWalkedModified
        ) * 6.0F) * 32.0F * cameraYaw);

        if (player.isSneaking()) {
            height += 25.0F;
            poseStack.translate(0, SNEAK_OFFSET, 0);
        }

        float naturalWindSwing = layer.getNatrualWindSwing(part);

        poseStack.mulPose(AVec3f.XP.rotationDegrees(BASE_ROTATION + swing / 2.0F + height + naturalWindSwing));
        poseStack.mulPose(AVec3f.ZP.rotationDegrees(sidewaysRotationOffset / 2.0F));
        poseStack.mulPose(AVec3f.YP.rotationDegrees(180.0F - sidewaysRotationOffset / 2.0F));
    }

    private static void addBackVertex(WorldRenderer worldrenderer, AMtx4f matrix, AMtx4f oldMatrix, float x1,
                                      float y1, float z1, float x2, float y2, float z2, int part) {
        float i;
        AMtx4f k;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;

            k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }

        float minU = .015625F;
        float maxU = .171875F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / CustomCapeRenderLayer.partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(maxU, minV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(minU, minV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z2).tex(maxU, maxV).normal(1, 0, 0).endVertex();
    }

    private static void addFrontVertex(WorldRenderer worldrenderer, AMtx4f matrix, AMtx4f oldMatrix, float x1,
                                       float y1, float z1, float x2, float y2, float z2, int part) {

        float i;
        AMtx4f k;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;

            k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }

        float minU = .1875F;
        float maxU = .34375F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / CustomCapeRenderLayer.partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        vertex(worldrenderer, oldMatrix, x1, y1, z1).tex(maxU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y1, z1).tex(minU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x2, y2, z2).tex(minU, minV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x1, y2, z2).tex(maxU, minV).normal(1, 0, 0).endVertex();
    }

    private static void addLeftVertex(WorldRenderer worldrenderer, AMtx4f matrix, AMtx4f oldMatrix, float x1,
                                      float y1, float z1, float x2, float y2, float z2, int part) {

        float i;

        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = 0;
        float maxU = .015625F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / CustomCapeRenderLayer.partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        vertex(worldrenderer, matrix, x2, y1, z1).tex(maxU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(maxU, minV).normal(1, 0, 0).endVertex();
    }

    private static void addRightVertex(WorldRenderer worldrenderer, AMtx4f matrix, AMtx4f oldMatrix, float x1,
                                       float y1, float z1, float x2, float y2, float z2, int part) {

        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .171875F;
        float maxU = .1875F;

        float minV = .03125F;
        float maxV = .53125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / CustomCapeRenderLayer.partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z1).tex(maxU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(maxU, minV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1, 0, 0).endVertex();
    }

    private static void addBottomVertex(WorldRenderer worldrenderer, AMtx4f matrix, AMtx4f oldMatrix, float x1,
                                        float y1, float z1, float x2, float y2, float z2, int part) {

        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .171875F;
        float maxU = .328125F;

        float minV = 0;
        float maxV = .03125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / CustomCapeRenderLayer.partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        vertex(worldrenderer, oldMatrix, x1, y2, z2).tex(maxU, minV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z1).tex(minU, maxV).normal(1, 0, 0).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z1).tex(maxU, maxV).normal(1, 0, 0).endVertex();
    }

    private static WorldRenderer vertex(WorldRenderer worldrenderer, AMtx4f AMtx4f, float f, float g, float h) {
        AVec4f AVec4f = new AVec4f(f, g, h, 1.0F);
        AVec4f.transform(AMtx4f);
        worldrenderer.pos(AVec4f.x(), AVec4f.y(), AVec4f.z());
        return worldrenderer;
    }

    private static void addTopVertex(WorldRenderer worldrenderer, AMtx4f matrix, AMtx4f oldMatrix, float x1,
                                     float y1, float z1, float x2, float y2, float z2, int part) {

        float i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }

        float minU = .015625F;
        float maxU = .171875F;

        float minV = 0;
        float maxV = .03125F;

        float deltaV = maxV - minV;
        float vPerPart = deltaV / CustomCapeRenderLayer.partCount;
        maxV = minV + (vPerPart * (part + 1));
        minV = minV + (vPerPart * part);

        vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(maxU, maxV).normal(0, 1, 0).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(minU, maxV).normal(0, 1, 0).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, minV).normal(0, 1, 0).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z2).tex(maxU, minV).normal(0, 1, 0).endVertex();
    }

    private static float easeOutSine(float x) {
        return (float) Math.sin((x * Math.PI) / 2f);
    }
}
