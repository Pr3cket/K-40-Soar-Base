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
import com.lawab1ders.nan7i.kali.module.impl.render.ItemPhysicsModule;
import com.lawab1ders.nan7i.kali.module.impl.render.UHCOverlayModule;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(RenderEntityItem.class)
public abstract class MixinRenderEntityItem extends Render<EntityItem> {

    @Shadow
    private Random field_177079_e;

    @Unique
    private boolean k40$isGui3d;

    protected MixinRenderEntityItem(RenderManager renderManager) {
        super(renderManager);
    }

    @Unique
    private static Fluid k40$getFluid(EntityItem item) {
        return k40$getFluid(item, false);
    }

    @Unique
    private static Fluid k40$getFluid(EntityItem item, boolean below) {
        double yE = item.posY + (double) item.getEyeHeight();
        int x = MathHelper.floor_double(item.posX);
        int y = MathHelper.floor_float((float) MathHelper.floor_double(yE));
        int z = MathHelper.floor_double(item.posZ);

        if (below) y--;

        BlockPos pos = new BlockPos(x, y, z);
        Block block = item.worldObj.getBlockState(pos).getBlock();
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

        if (fluid == null && block instanceof IFluidBlock) {
            fluid = ((IFluidBlock) block).getFluid();

        }
        else if (block instanceof BlockLiquid) {
            fluid = FluidRegistry.WATER;
        }

        if (below) return fluid;

        double filled = 1.0f; // If it's not a liquid assume it's a solid block

        if (block instanceof IFluidBlock) {
            filled = ((IFluidBlock) block).getFilledPercentage(item.worldObj, pos);
        }

        if (filled < 0) {
            filled *= -1;
            if (yE > y + (1 - filled)) return fluid;

        }
        else if (yE < y + filled) return fluid;

        return null;
    }

    @Shadow
    protected abstract int func_177078_a(ItemStack itemStack);

    @Inject(method = "func_177077_a", at = @At("HEAD"), cancellable = true)
    private void k40$physics(EntityItem item, double x, double y, double z, float p_177077_8_, IBakedModel iBakedModel,
                             CallbackInfoReturnable<Integer> cir) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack itemstack = item.getEntityItem();
        ItemPhysicsModule itemPhysicsModule = InstanceAccess.mod.getModule(ItemPhysicsModule.class);

        int count = func_177078_a(itemstack);
        boolean is3D = iBakedModel.isGui3d();
        double rotation = 0;

        if (itemPhysicsModule.isActivated()) cir.setReturnValue(0);
        else return;

        if (mc.inGameHasFocus) {
            rotation =
                    (double) (System.nanoTime() - ItemPhysicsModule.tick) / 2500000 / 25 * itemPhysicsModule.rotationSpeedSetting.getValue();
        }

        field_177079_e.setSeed(itemstack.getItem() != null ?
                                       Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata() : 187);

        bindTexture(TextureMap.locationBlocksTexture);
        getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();

        GlStateManager.translate((float) x, (float) y, (float) z);
        if (is3D) GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(item.rotationYaw, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0, 0, is3D ? -0.08 : -0.04);

        if (is3D || mc.getRenderManager().options != null) {
            if (is3D) {
                if (!item.onGround) {
                    Fluid fluid = k40$getFluid(item);

                    if (fluid == null) fluid = k40$getFluid(item, true);

                    if (fluid != null) {
                        rotation /= (double) fluid.getDensity() / 1000 * 10;
                    }

                    item.rotationPitch += (float) rotation;
                }
            }
            else if (!Double.isNaN(item.posX) && !Double.isNaN(item.posY) && !Double.isNaN(
                    item.posZ) && item.worldObj != null) {
                if (item.onGround) item.rotationPitch = 0;
                else {
                    Fluid fluid = k40$getFluid(item);

                    if (fluid != null) {
                        rotation /= (double) fluid.getDensity() / 1000 * 10;
                    }

                    item.rotationPitch += (float) rotation;
                }
            }

            GlStateManager.rotate(item.rotationPitch, 1, 0, 0.0F);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int k = 0; k < count; ++k) {
            if (is3D) {
                GlStateManager.pushMatrix();

                if (k > 0) {
                    float f7 = (field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f9 = (field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f6 = (field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(f7, f9, f6);
                }

                iBakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                        iBakedModel,
                        ItemCameraTransforms.TransformType.GROUND
                );
                mc.getRenderItem().renderItem(itemstack, iBakedModel);
                GlStateManager.popMatrix();
            }
            else {
                GlStateManager.pushMatrix();

                iBakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                        iBakedModel,
                        ItemCameraTransforms.TransformType.GROUND
                );

                mc.getRenderItem().renderItem(itemstack, iBakedModel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.05375F);
            }
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        bindTexture(TextureMap.locationBlocksTexture);

        getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    //---------------------------------------------------------------------------
    // 2D Items
    //---------------------------------------------------------------------------

    @Inject(method = "func_177077_a", at = @At("TAIL"))
    private void k40$zoom(EntityItem entityItem, double x, double y, double z, float p_177077_8_,
                          IBakedModel iBakedModel,
                          CallbackInfoReturnable<Integer> cir) {
        UHCOverlayModule uhcMod = InstanceAccess.mod.getModule(UHCOverlayModule.class);

        float ingotScale = uhcMod.goldIngotScaleSetting.getValue();
        float nuggetScale = uhcMod.goldNuggetScaleSetting.getValue();
        float appleScale = uhcMod.goldAppleScaleSetting.getValue();
        float oreScale = uhcMod.goldOreScaleSetting.getValue();
        float skullScale = uhcMod.skullScaleSetting.getValue();

        ItemStack stack = entityItem.getEntityItem();
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);

        int i = func_177078_a(stack);
        float f6 = -0.0F * (i - 1) * 0.5F;
        float f4 = -0.0F * (i - 1) * 0.5F;
        float f5 = -0.046875F * (i - 1) * 0.5F;

        if (uhcMod.isActivated()) {
            if (item == Items.gold_ingot) {
                GlStateManager.translate(f6, f4 + (ingotScale / 8), f5);
                GlStateManager.scale(ingotScale, ingotScale, ingotScale);

            }
            else if (item == Items.gold_nugget) {
                GlStateManager.translate(f6, f4 + (nuggetScale / 8), f5);
                GlStateManager.scale(nuggetScale, nuggetScale, nuggetScale);

            }
            else if (item == Items.golden_apple) {
                GlStateManager.translate(f6, f4 + (appleScale / 8), f5);
                GlStateManager.scale(appleScale, appleScale, appleScale);

            }
            else if (block == Blocks.gold_ore) {
                GlStateManager.translate(f6, f4 + (oreScale / 8), f5);
                GlStateManager.scale(oreScale, oreScale, oreScale);

            }
            else if (item == Items.skull) {
                GlStateManager.translate(f6, f4 + (skullScale / 8), f5);
                GlStateManager.scale(skullScale, skullScale, skullScale);
            }
        }
    }

    @ModifyVariable(method = "func_177077_a", at = @At("STORE"), ordinal = 0)
    private boolean k40$hookGui3d(boolean isGui3d) {
        k40$isGui3d = isGui3d;
        return isGui3d;
    }

    @ModifyArg(
            method = "func_177077_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"),
            index = 0
    )
    private float k40$apply2dItem(float angle) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!k40$isGui3d && module.isActivated() && module.Item2DSetting.isActivated()) {
            return 180.0F - renderManager.playerViewY;
        }

        return angle;
    }

    @Inject(
            method = "func_177077_a",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void k40$fix2dRotation(EntityItem itemIn, double p_177077_2_, double p_177077_4_,
                                   double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_,
                                   CallbackInfoReturnable<Integer> cir) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!k40$isGui3d && module.isActivated() && module.Item2DSetting.isActivated()) {
            GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        }
    }
}
