package com.lawab1ders.nan7i.kali.injection.mixins.entity;

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
import com.lawab1ders.nan7i.kali.module.impl.player.DamageTiltModule;
import com.lawab1ders.nan7i.kali.module.impl.render.ItemPhysicsModule;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    public abstract void setPosition(double p_setPosition_1_, double p_setPosition_3_, double p_setPosition_3_2);

    @Inject(method = "setVelocity", at = @At("HEAD"))
    public void k40$preSetVelocity(double x, double y, double z, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(DamageTiltModule.class);

        if (!module.isActivated()) return;

        // This is run before motion is locally set

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player == null) return;

        if ((Object) this == player) {

            // Set the value
            float result =
                    (float) (Math.atan2(
                            player.motionZ - z, player.motionX - x) * (180D / Math.PI) - (double) player.rotationYaw);

            if (Float.isFinite(result)) {
                player.attackedAtYaw = result;
            }
        }
    }

    @Inject(method = "setPositionAndRotation2", at = @At("HEAD"), cancellable = true)
    public void k40$setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int partialTicks,
                                            boolean value, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(ItemPhysicsModule.class);

        if (module.isActivated() && (Object) this instanceof EntityItem) {
            setPosition(x, y, z);
            ci.cancel();
        }
    }
}
