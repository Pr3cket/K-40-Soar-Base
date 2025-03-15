package com.lawab1ders.nan7i.kali.injection.mixins.renderer;

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
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LayerHeldItem.class)
public abstract class MixinLayerHeldItem {

    @Unique
    public ItemStack simpliefied$itemStack;

    @Inject(method = "doRenderLayer", at = @At(value = "HEAD"))
    private void k40$hookHeldItem(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks,
                                  float h, float i, float j, float scale, CallbackInfo ci) {
        simpliefied$itemStack = entitylivingbaseIn.getHeldItem();
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBiped;postRenderArm(F)V"
            )
    )
    private void k40$applyOldSneaking(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks,
                                      float h, float i, float j, float scale, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (entitylivingbaseIn.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
    }

    @Redirect(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isSneaking()Z"
            )
    )
    private boolean k40$cancelNewSneaking(EntityLivingBase instance) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        return instance.isSneaking() && !module.isActivated();
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
            )
    )
    private void k40$changeItemToStick(EntityLivingBase entitylivingbaseIn, float f, float g, float partialTicks,
                                       float h, float i, float j, float scale, CallbackInfo ci) {
        if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).fishEntity != null) {
            simpliefied$itemStack = new ItemStack(Items.stick, 0);
        }
    }

    @Inject(
            method = "doRenderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V"
            ), locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void k40$thirdPersonItemPositions(EntityLivingBase entitylivingbaseIn, float f, float g,
                                              float partialTicks, float h, float i, float j, float s,
                                              CallbackInfo ci, ItemStack stack, Item item) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        if (entitylivingbaseIn instanceof AbstractClientPlayer && ((AbstractClientPlayer) entitylivingbaseIn).isBlocking()) {
            GlStateManager.translate(0.05F, 0.0F, -0.1F);
            GlStateManager.rotate(-50.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-10.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-60.0F, 0.0F, 0.0F, 1.0F);
        }

        if ((entitylivingbaseIn instanceof EntityPlayer)
                && !Minecraft.getMinecraft().getRenderItem().shouldRenderItemIn3D(
                stack) && !(stack.getItem() instanceof ItemSkull || stack.getItem() instanceof ItemBanner)) {

            float scale = 1.5F * 0.625F;
            if (item instanceof ItemBow) {
                GlStateManager.rotate(-12.0F, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-7.0F, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(10.0F, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(1.0F, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-4.5F, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-1.5F, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.022F, -0.01F, -0.108F);
                GlStateManager.scale(scale, scale, scale);
            }
            else if (item.isFull3D()) {
                if (item.shouldRotateAroundWhenRendering()) {
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                }

                GlStateManager.scale(scale / 0.85F, scale / 0.85F, scale / 0.85F);
                GlStateManager.rotate(-2.4F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(4.5F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(-0.013F, 0.01F, 0.125F);
            }
            else {
                scale = 1.5F * 0.375F;
                GlStateManager.scale(scale / 0.55, scale / 0.55, scale / 0.55);
                GlStateManager.rotate(-195.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-168.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(15.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(-0.047F, -0.28F, 0.038F);
            }
        }
    }
}
