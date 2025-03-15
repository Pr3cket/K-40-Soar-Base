package com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.layers;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.RenderBendsPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerBendsCape implements LayerRenderer<AbstractClientPlayer> {

    private final RenderBendsPlayer playerRenderer;

    public LayerBendsCape(RenderBendsPlayer p_i46123_1_) {
        this.playerRenderer = p_i46123_1_;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer p_177166_1_, float p_177166_2_, float p_177166_3_,
                              float p_177166_4_, float p_177166_5_, float p_177166_6_, float p_177166_7_,
                              float p_177166_8_) {
        if (p_177166_1_.hasPlayerInfo() && !p_177166_1_.isInvisible() && p_177166_1_.isWearing(EnumPlayerModelParts.CAPE) && p_177166_1_.getLocationCape() != null) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.playerRenderer.bindTexture(p_177166_1_.getLocationCape());
            GlStateManager.pushMatrix();
            this.playerRenderer.getMainModel().bipedBody.postRender(0.0625F);
            GlStateManager.translate(0.0F, -0.75F, 0.125F);
            double d0 =
                    p_177166_1_.prevChasingPosX + (p_177166_1_.chasingPosX - p_177166_1_.prevChasingPosX) * (double) p_177166_4_ - (p_177166_1_.prevPosX + (p_177166_1_.posX - p_177166_1_.prevPosX) * (double) p_177166_4_);
            double d1 =
                    p_177166_1_.prevChasingPosY + (p_177166_1_.chasingPosY - p_177166_1_.prevChasingPosY) * (double) p_177166_4_ - (p_177166_1_.prevPosY + (p_177166_1_.posY - p_177166_1_.prevPosY) * (double) p_177166_4_);
            double d2 =
                    p_177166_1_.prevChasingPosZ + (p_177166_1_.chasingPosZ - p_177166_1_.prevChasingPosZ) * (double) p_177166_4_ - (p_177166_1_.prevPosZ + (p_177166_1_.posZ - p_177166_1_.prevPosZ) * (double) p_177166_4_);
            float f7 =
                    p_177166_1_.prevRenderYawOffset + (p_177166_1_.renderYawOffset - p_177166_1_.prevRenderYawOffset) * p_177166_4_;
            double d3 = MathHelper.sin(f7 * 3.1415927F / 180.0F);
            double d4 = -MathHelper.cos(f7 * 3.1415927F / 180.0F);
            float f8 = (float) d1 * 10.0F;
            f8 = MathHelper.clamp_float(f8, -6.0F, 32.0F);
            float f9 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            float f10 = (float) (d0 * d4 - d2 * d3) * 100.0F;
            if (f9 < 0.0F) {
                f9 = 0.0F;
            }

            float f11 = p_177166_1_.prevCameraYaw + (p_177166_1_.cameraYaw - p_177166_1_.prevCameraYaw) * p_177166_4_;
            f8 += MathHelper.sin((p_177166_1_.prevDistanceWalkedModified + (p_177166_1_.distanceWalkedModified - p_177166_1_.prevDistanceWalkedModified) * p_177166_4_) * 6.0F) * 32.0F * f11;
            if (p_177166_1_.isSneaking()) {
                f8 += 25.0F;
            }

            GlStateManager.rotate(6.0F + f9 / 2.0F + f8, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f10 / 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-f10 / 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            this.playerRenderer.getMainModel().renderCape(0.0625F);
            GlStateManager.popMatrix();
            GL11.glEnable(3553);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
