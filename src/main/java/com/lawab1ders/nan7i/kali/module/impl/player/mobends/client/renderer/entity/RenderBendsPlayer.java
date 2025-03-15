package com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity;

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.module.impl.player.MoBendsModule;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.layers.LayerBendsCape;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.layers.LayerBendsCustomHead;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.layers.LayerBendsPlayerArmor;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBendsPlayer extends RenderPlayer {

    private final boolean smallArms;

    public RenderBendsPlayer(RenderManager renderManager) {
        super(renderManager, false);
        this.smallArms = false;
        this.mainModel = new ModelBendsPlayer(0.0F, false);
        this.layerRenderers.clear();
        this.addLayer(new LayerBendsPlayerArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBendsCape(this));
        this.addLayer(new LayerBendsCustomHead((ModelBendsPlayer) this.getMainModel()));
    }

    public RenderBendsPlayer(RenderManager renderManager, boolean useSmallArms) {
        super(renderManager, useSmallArms);
        this.smallArms = useSmallArms;
        this.mainModel = new ModelBendsPlayer(0.0F, useSmallArms);
        this.layerRenderers.clear();
        this.addLayer(new LayerBendsPlayerArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBendsCape(this));
        this.addLayer(new LayerBendsCustomHead((ModelBendsPlayer) this.getMainModel()));
    }

    @Override
    public ModelPlayer getMainModel() {
        if (!(this.mainModel instanceof ModelBendsPlayer)) {
            this.mainModel = new ModelBendsPlayer(0.0F, this.smallArms);
        }

        return (ModelBendsPlayer) this.mainModel;
    }

    @Override
    protected ResourceLocation getEntityTexture(AbstractClientPlayer p_180594_1_) {
        return p_180594_1_.getLocationSkin();
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    @Override
    protected void preRenderCallback(AbstractClientPlayer p_77041_1_, float p_77041_2_) {
        float f1 = 0.9375F;
        GlStateManager.scale(f1, f1, f1);
        ((ModelBendsPlayer) this.mainModel).updateWithEntityData(p_77041_1_);
        ((ModelBendsPlayer) this.mainModel).postRenderTranslate(0.0625F);
        Data_Player data = Data_Player.get(p_77041_1_.getEntityId());
        if (InstanceAccess.mod.getModule(MoBendsModule.class).swordTrailSetting.isActivated()) {
            GL11.glPushMatrix();
            float f5 = 0.0625F;
            GL11.glScalef(-f5, -f5, f5);
            data.swordTrail.render();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }

        ((ModelBendsPlayer) this.mainModel).postRenderRotate(0.0625F);
    }

    @Override
    public void renderRightArm(AbstractClientPlayer p_177138_1_) {
        float f = 1.0F;
        GlStateManager.color(f, f, f);
        ModelBendsPlayer modelplayer = (ModelBendsPlayer) this.getMainModel();
        this.setModelVisibilities(p_177138_1_);
        modelplayer.swingProgress = 0.0F;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, p_177138_1_);
        modelplayer.renderRightArm();
    }

    @Override
    public void renderLeftArm(AbstractClientPlayer p_177139_1_) {
        float f = 1.0F;
        GlStateManager.color(f, f, f);
        ModelBendsPlayer modelplayer = (ModelBendsPlayer) this.getMainModel();
        this.setModelVisibilities(p_177139_1_);
        modelplayer.isSneak = false;
        modelplayer.swingProgress = 0.0F;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, p_177139_1_);
        modelplayer.renderLeftArm();
    }

    private void setModelVisibilities(AbstractClientPlayer p_setModelVisibilities_1_) {
        ModelPlayer modelplayer = this.getMainModel();
        if (p_setModelVisibilities_1_.isSpectator()) {
            modelplayer.setInvisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        } else {
            ItemStack itemstack = p_setModelVisibilities_1_.inventory.getCurrentItem();
            modelplayer.setInvisible(true);
            modelplayer.bipedHeadwear.showModel = p_setModelVisibilities_1_.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = p_setModelVisibilities_1_.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel =
                    p_setModelVisibilities_1_.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel =
                    p_setModelVisibilities_1_.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel =
                    p_setModelVisibilities_1_.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel =
                    p_setModelVisibilities_1_.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.heldItemLeft = 0;
            modelplayer.aimedBow = false;
            modelplayer.isSneak = p_setModelVisibilities_1_.isSneaking();
            if (itemstack == null) {
                modelplayer.heldItemRight = 0;
            } else {
                modelplayer.heldItemRight = 1;
                if (p_setModelVisibilities_1_.getItemInUseCount() > 0) {
                    EnumAction enumaction = itemstack.getItemUseAction();
                    if (enumaction == EnumAction.BLOCK) {
                        modelplayer.heldItemRight = 3;
                    } else if (enumaction == EnumAction.BOW) {
                        modelplayer.aimedBow = true;
                    }
                }
            }
        }

    }

    @Override
    protected void renderLivingAt(AbstractClientPlayer p_77039_1_, double p_77039_2_, double p_77039_4_,
                                  double p_77039_6_) {
        super.renderLivingAt(p_77039_1_, p_77039_2_, p_77039_4_, p_77039_6_);
    }
}
