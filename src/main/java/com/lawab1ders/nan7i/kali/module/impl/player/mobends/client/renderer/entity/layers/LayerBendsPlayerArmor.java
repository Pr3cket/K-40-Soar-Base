package com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.layers;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.model.entity.ModelBendsPlayerArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBendsPlayerArmor extends LayerArmorBase<ModelBiped> {

    public LayerBendsPlayerArmor(RendererLivingEntity p_i46116_1_) {
        super(p_i46116_1_);
    }

    @Override
    protected void initArmor() {
        this.modelLeggings = new ModelBendsPlayerArmor(0.5F);
        this.modelArmor = new ModelBendsPlayerArmor(1.0F);
    }

    @Override
    protected void setModelPartVisible(ModelBiped p_177195_1_, int p_177195_2_) {
        p_177195_1_.setInvisible(true);
        ModelBendsPlayerArmor model = (ModelBendsPlayerArmor) p_177195_1_;
        switch (p_177195_2_) {
            case 1:
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                model.bipedRightForeLeg.showModel = true;
                model.bipedLeftForeLeg.showModel = true;
                break;
            case 2:
                model.bipedBody.showModel = true;
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                model.bipedRightForeLeg.showModel = true;
                model.bipedLeftForeLeg.showModel = true;
                break;
            case 3:
                model.bipedBody.showModel = true;
                model.bipedRightArm.showModel = true;
                model.bipedLeftArm.showModel = true;
                model.bipedRightForeArm.showModel = true;
                model.bipedLeftForeArm.showModel = true;
                break;
            case 4:
                model.bipedHead.showModel = true;
                model.bipedHeadwear.showModel = true;
        }
    }
}
