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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderFish.class, priority = 2000)
public class MixinRenderFish {

    @ModifyVariable(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            at = @At(value = "STORE", ordinal = 0),
            index = 23
    )
    private Vec3 k40$modifyLinePosition(Vec3 vec3) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return vec3;

        double fov = Minecraft.getMinecraft().gameSettings.fovSetting;
        double decimalFov = fov / 110;

        double xCoord = (-decimalFov + (decimalFov / 2.5) - (decimalFov / 8)) + 0.16 + 0.15D;
        double yCoord = 0.0D;
        double zCoord = 0.4D;

        return new Vec3(xCoord, yCoord, zCoord);
    }

    @Redirect(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/EntityPlayer;getEyeHeight()F"
            )
    )
    public float k40$modifyEyeHeight(EntityPlayer instance) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (module.isActivated()) return StaticStation.sneakingHeight;

        return instance.getEyeHeight();
    }

    @Inject(
            method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/WorldRenderer;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V",
                    ordinal = 1
            )
    )
    private void k40$modifyLineThickness(EntityFishHook entity, double x, double y, double z,
                                         float entityYaw, float partialTicks, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        GL11.glLineWidth(1.0f);
    }
}
