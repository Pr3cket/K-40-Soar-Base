package com.lawab1ders.nan7i.kali.injection.mixins.gui;

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
import com.lawab1ders.nan7i.kali.module.impl.hud.RearviewModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.WebBrowserModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.mcef.Mcef;
import com.lawab1ders.nan7i.kali.module.impl.render.OverlayEditorModule;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void k40$preRenderPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo ci) {
        RearviewModule rearviewModule = InstanceAccess.mod.getModule(RearviewModule.class);
        OverlayEditorModule overlayEditorModule = InstanceAccess.mod.getModule(OverlayEditorModule.class);

        if ((rearviewModule.isActivated() && rearviewModule.rearviewCamera.isRecording())
                || (overlayEditorModule.isActivated() && overlayEditorModule.hidePumpkinMaskSetting.isActivated())) {

            ci.cancel();
        }
    }

    @Inject(method = "renderGameOverlay", at = @At("HEAD"))
    public void updateBrowser(float partialTicks, CallbackInfo callback) {
        WebBrowserModule webBrowserModule = InstanceAccess.mod.getModule(WebBrowserModule.class);

        if (webBrowserModule.isActivated() && Mcef.active) {
            Mcef.cefAppInstance.N_DoMessageLoopWork();
        }
    }
}
