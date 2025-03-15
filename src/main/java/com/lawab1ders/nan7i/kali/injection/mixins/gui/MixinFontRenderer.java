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
import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.module.impl.player.ProtectorModule;
import lombok.val;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @ModifyVariable(method = "renderString", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String renderString(String text) {
        if (KaliAPI.IS_UNAVAILABLE) return text;

        val module = InstanceAccess.mod.getModule(ProtectorModule.class);

        if (text != null && module.isActivated() && module.protectNameSetting.isActivated()) {
            return text.replace(InstanceAccess.mc.getSession().getUsername(), module.fakeNameSetting.getText());
        }

        return text;
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String getStringWidth(String text) {
        if (KaliAPI.IS_UNAVAILABLE) return text;

        val module = InstanceAccess.mod.getModule(ProtectorModule.class);

        if (text != null && module.isActivated() && module.protectNameSetting.isActivated()) {
            return text.replace(InstanceAccess.mc.getSession().getUsername(), module.fakeNameSetting.getText());
        }

        return text;
    }
}
