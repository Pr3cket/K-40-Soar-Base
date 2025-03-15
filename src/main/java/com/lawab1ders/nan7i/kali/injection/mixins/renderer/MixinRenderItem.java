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
import com.lawab1ders.nan7i.kali.injection.StaticStation;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {

    @Unique
    public IBakedModel k40$model;

    @Unique
    private EntityLivingBase k40$entityLivingBase;

    @Inject(
            method = "renderModel(Lnet/minecraft/client/resources/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD")
    )
    private void k40$setModel(IBakedModel model, int color, ItemStack stack, CallbackInfo ci) {
        k40$model = model;
    }

    @Inject(method = "renderItemModelForEntity", at = @At("HEAD"))
    public void k40$getLastEntity(ItemStack stack, EntityLivingBase entityToRenderFor,
                                  ItemCameraTransforms.TransformType cameraTransformType,
                                  CallbackInfo ci) {
        k40$entityLivingBase = entityToRenderFor;
    }

    @Inject(
            method = "renderItemModelTransform",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"
            )
    )
    public void k40$modifyModelPosition(ItemStack stack, IBakedModel model,
                                        ItemCameraTransforms.TransformType cameraTransformType,
                                        CallbackInfo ci) {
        if (stack == null) return;
        if (stack.getItem() == null) return;

        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (module.isActivated() && !(stack.getItem() instanceof ItemBanner)) {
            boolean isRod = stack.getItem().shouldRotateAroundWhenRendering();
            boolean isBlock = stack.getItem() instanceof ItemBlock;
            boolean isCarpet = false;

            if (isBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                isCarpet = block instanceof BlockCarpet || block instanceof BlockSnow;
            }

            if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON) {
                if (isRod) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(50.0F, 0.0F, 0.0F, 1.0F);

                }
                else if (isCarpet) {
                    GlStateManager.translate(0.0F, -5.25F * 0.0625F, 0.0F);
                }
            }
            else if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON && (k40$entityLivingBase instanceof EntityPlayer)) {
                if (isRod) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(110.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.translate(0.002F, 0.037F, -0.003F);

                }
                else if (isCarpet) {
                    GlStateManager.translate(0.0F, -0.25F, 0.0F);
                }

                if (isBlock) {
                    if (Block.getBlockFromItem(stack.getItem()).getRenderType() != 2) {
                        GlStateManager.translate(-0.0285F, -0.0375F, 0.0285F);
                        GlStateManager.rotate(-5.0f, 1.0f, 0.0f, 0.0f);
                        GlStateManager.rotate(-5.0f, 0.0f, 0.0f, 1.0f);
                    }

                    GlStateManager.scale(-1.0F, 1.0F, -1.0F);
                }
            }
        }
    }

    @ModifyArg(
            method = "renderEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
            ),
            index = 0
    )
    private float k40$modifySpeed(float x) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (module.isActivated()) x *= 64.0F;

        return x;
    }

    @ModifyArgs(
            method = "renderEffect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"
            )
    )
    public void k40$modifyScale(Args args) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        for (int i : new int[] { 0, 1, 2 }) {
            args.set(i, 1 / (float) args.get(i));
        }
    }

    //---------------------------------------------------------------------------
    // 2D Items
    //---------------------------------------------------------------------------

    @ModifyArg(
            method = "renderModel(Lnet/minecraft/client/resources/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderQuads(Lnet/minecraft/client/renderer/WorldRenderer;Ljava/util/List;ILnet/minecraft/item/ItemStack;)V",
                    ordinal = 1
            ), index = 1
    )
    private List<BakedQuad> k40$changeToSprite(List<BakedQuad> quads) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (module.isActivated() && module.Item2DSetting.isActivated()
                && !k40$model.isGui3d()
                && StaticStation.shouldBeSprite()) {

            return quads.stream().filter(baked -> baked.getFace() == EnumFacing.SOUTH).collect(Collectors.toList());
        }

        return quads;
    }

    @ModifyArgs(
            method = "putQuadNormal",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;putNormal(FFF)V")
    )
    private void k40$modifyNormalValue(Args args) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (module.isActivated() && module.Item2DSetting.isActivated()
                && !k40$model.isGui3d() && !(InstanceAccess.mc.currentScreen instanceof GuiFlatPresets)
                && StaticStation.shouldNotHaveGlint()) {

            args.setAll(args.get(0), args.get(2), args.get(1));
        }
    }

    @Inject(method = "renderEffect", at = @At(value = "HEAD"), cancellable = true)
    public void k40$disableGlint(IBakedModel model, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (module.isActivated() && module.Item2DSetting.isActivated()
                && StaticStation.shouldNotHaveGlint()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void k40$translateSprite(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (module.isActivated() && module.Item2DSetting.isActivated()
                && !model.isGui3d() && StaticStation.shouldNotHaveGlint()) {

            GlStateManager.translate(0.0F, 0.0F, -0.0625F);
        }
    }
}
