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
import com.lawab1ders.nan7i.kali.cosmetics.Cosmetics;
import com.lawab1ders.nan7i.kali.module.impl.player.ProtectorModule;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Shadow
    private NetworkPlayerInfo playerInfo;

    @Inject(method = "getLocationSkin*", cancellable = true, at = @At("HEAD"))
    public void k40$onGetLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        val module = InstanceAccess.mod.getModule(ProtectorModule.class);

        if (module.isActivated() && module.protectSkinSetting.isActivated()) {
            if (playerInfo != null && playerInfo.getGameProfile().getId()
                                                .equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId())) {
                cir.cancel();
                cir.setReturnValue(playerInfo.getSkinType().equals("slim") ?
                                           new ResourceLocation("minecraft:textures/entity/alex.png") :
                                           new ResourceLocation("minecraft:textures/entity/steve.png"));
            }
        }
    }

    @Inject(method = "getLocationCape", cancellable = true, at = @At("HEAD"))
    public void k40$onGetLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        if (playerInfo != null && playerInfo.getGameProfile().getId()
                                            .equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId())) {
            Cosmetics cape = InstanceAccess.cos.getCurrentCosmetics();

            if (cape != null) cir.setReturnValue(cape.getModel());
        }
    }
}
