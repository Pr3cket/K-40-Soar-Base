package com.lawab1ders.nan7i.kali.injection.mixins.model;

/*
 * Copyright (c) 2025 EldoDebug, Nan7.南起
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
import com.lawab1ders.nan7i.kali.module.impl.player.WaveyCapesModule;
import lombok.val;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer {

    @Inject(method = "renderCape", at = @At("HEAD"), cancellable = true)
    public void k40$renderCloak(float p_renderCape_1_, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(WaveyCapesModule.class);

        if (module.isActivated()) ci.cancel();
    }
}
