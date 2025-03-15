package com.lawab1ders.nan7i.kali.module.impl.render;

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

import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldSettings;
import org.lwjgl.opengl.GL11;

@IModule(
        name = "module.blockoverlay",
        description = "module.blockoverlay.desc",
        category = EModuleCategory.RENDER
)
public class BlockOverlayModule extends Module {

    public final BooleanSetting animationSetting = new BooleanSetting("module.blockoverlay.opts.animation", false);
    public final BooleanSetting fillSetting = new BooleanSetting("module.blockoverlay.opts.fill", true);
    public final BooleanSetting outlineSetting = new BooleanSetting("module.blockoverlay.opts.outline", true);
    public final BooleanSetting depthSetting = new BooleanSetting("module.blockoverlay.opts.depth", false);

    public final FloatSetting outlineAlphaSetting = new FloatSetting(
            "module.blockoverlay.opts.outlinealpha", 0.15F, 0.1F, 1.0F);

    public final FloatSetting outlineWidthSetting = new FloatSetting(
            "module.blockoverlay.opts.outlinewidth", 4, 1, 10);

    public final FloatSetting fillAlphaSetting = new FloatSetting(
            "module.blockoverlay.opts.fillalpha", 0.15F, 0.1F, 1.0F);

    private final Animation[] simpleAnimation = {
            new Animation(0.0F), new Animation(0.0F), new Animation(0.0F),
            new Animation(0.0F), new Animation(0.0F), new Animation(0.0F)
    };

    protected AxisAlignedBB currentBB, slideBB;
    protected MillisTimer timer = new MillisTimer();

    public void render(MovingObjectPosition objectMouseOver, float partialTicks) {

        AccentColor currentColor = col.getCurrentColor();

        if (!canRender(objectMouseOver)) {
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        if (depthSetting.isActivated()) {
            GlStateManager.disableDepth();
        }

        GlStateManager.disableTexture2D();

        GlStateManager.depthMask(false);
        BlockPos blockpos = objectMouseOver.getBlockPos();
        Block block = mc.theWorld.getBlockState(blockpos).getBlock();

        if (block.getMaterial() != Material.air && mc.theWorld.getWorldBorder().contains(blockpos)) {

            block.setBlockBoundsBasedOnState(mc.theWorld, blockpos);

            double x = mc.getRenderViewEntity().lastTickPosX
                    + (mc.getRenderViewEntity().posX - mc.getRenderViewEntity().lastTickPosX) * (double) partialTicks;
            double y = mc.getRenderViewEntity().lastTickPosY
                    + (mc.getRenderViewEntity().posY - mc.getRenderViewEntity().lastTickPosY) * (double) partialTicks;
            double z = mc.getRenderViewEntity().lastTickPosZ
                    + (mc.getRenderViewEntity().posZ - mc.getRenderViewEntity().lastTickPosZ) * (double) partialTicks;

            AxisAlignedBB selectedBox = block.getSelectedBoundingBox(mc.theWorld, blockpos);

            if (animationSetting.isActivated()) {

                if (!selectedBox.equals(currentBB)) {
                    slideBB = currentBB;
                    currentBB = selectedBox;
                }

                AxisAlignedBB slide;

                if ((slide = slideBB) != null) {

                    simpleAnimation[0].setAnimation((float) (slide.minX + (selectedBox.minX - slide.minX)), 24);
                    simpleAnimation[1].setAnimation((float) (slide.minY + (selectedBox.minY - slide.minY)), 24);
                    simpleAnimation[2].setAnimation((float) (slide.minZ + (selectedBox.minZ - slide.minZ)), 24);
                    simpleAnimation[3].setAnimation((float) (slide.maxX + (selectedBox.maxX - slide.maxX)), 24);
                    simpleAnimation[4].setAnimation((float) (slide.maxY + (selectedBox.maxY - slide.maxY)), 24);
                    simpleAnimation[5].setAnimation((float) (slide.maxZ + (selectedBox.maxZ - slide.maxZ)), 24);

                    AxisAlignedBB renderBB = new AxisAlignedBB(
                            simpleAnimation[0].getValue() - 0.01,
                            simpleAnimation[1].getValue() - 0.01,
                            simpleAnimation[2].getValue() - 0.01,
                            simpleAnimation[3].getValue() + 0.01,
                            simpleAnimation[4].getValue() + 0.01,
                            simpleAnimation[5].getValue() + 0.01
                    );

                    if (fillSetting.isActivated()) {
                        ColorUtils.setColor(currentColor.getInterpolateColor().getRGB(), fillAlphaSetting.getValue());
                        drawFillBox(interpolateAxis(renderBB));
                    }

                    if (outlineSetting.isActivated()) {
                        ColorUtils.setColor(currentColor.getInterpolateColor().getRGB(),
                                outlineAlphaSetting.getValue());
                        GL11.glLineWidth(outlineWidthSetting.getValue());
                        RenderGlobal.drawSelectionBoundingBox(interpolateAxis(renderBB));
                    }
                }
            } else {

                selectedBox = selectedBox.expand(0.0020000000949949026D, 0.0020000000949949026D,
                        0.0020000000949949026D).offset(-x, -y, -z);

                if (fillSetting.isActivated()) {
                    ColorUtils.setColor(currentColor.getInterpolateColor().getRGB(), fillAlphaSetting.getValue());
                    drawFillBox(selectedBox);
                }

                if (outlineSetting.isActivated()) {
                    ColorUtils.setColor(currentColor.getInterpolateColor().getRGB(), outlineAlphaSetting.getValue());
                    GL11.glLineWidth(outlineWidthSetting.getValue());
                    RenderGlobal.drawSelectionBoundingBox(selectedBox);
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();

        GlStateManager.disableBlend();

        if (depthSetting.isActivated()) {
            GlStateManager.enableDepth();
        }

        GL11.glLineWidth(2);
    }

    private boolean canRender(MovingObjectPosition movingObjectPositionIn) {

        Entity entity = mc.getRenderViewEntity();
        boolean result = entity instanceof EntityPlayer && !mc.gameSettings.hideGUI;

        if (result && !((EntityPlayer) entity).capabilities.allowEdit) {
            ItemStack itemstack = ((EntityPlayer) entity).getCurrentEquippedItem();

            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos selectedBlock = mc.objectMouseOver.getBlockPos();
                Block block = mc.theWorld.getBlockState(selectedBlock).getBlock();

                if (mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
                    result =
                            block.hasTileEntity(block.getDefaultState()) && mc.theWorld.getTileEntity(selectedBlock) instanceof IInventory;
                } else {
                    result = itemstack != null && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block));
                }
            }
        }

        result = result && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;

        return result;
    }

    private AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(
                bb.minX - mc.getRenderManager().viewerPosX,
                bb.minY - mc.getRenderManager().viewerPosY,
                bb.minZ - mc.getRenderManager().viewerPosZ,
                bb.maxX - mc.getRenderManager().viewerPosX,
                bb.maxY - mc.getRenderManager().viewerPosY,
                bb.maxZ - mc.getRenderManager().viewerPosZ);
    }

    private void drawFillBox(AxisAlignedBB box) {

        GlStateManager.disableCull();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.maxY, box.minZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.minZ).endVertex();
        worldrenderer.pos(box.maxX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.maxZ).endVertex();
        worldrenderer.pos(box.minX, box.minY, box.minZ).endVertex();
        tessellator.draw();

        GlStateManager.enableCull();
    }
}
