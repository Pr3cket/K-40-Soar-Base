package com.lawab1ders.nan7i.kali.injection.mixins.gui;

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
import com.lawab1ders.nan7i.kali.injection.interfaces.IChatLine;
import com.lawab1ders.nan7i.kali.module.impl.other.AntiSpammingModule;
import com.lawab1ders.nan7i.kali.module.impl.render.ChatHeadModule;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import lombok.val;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow
    public abstract void printChatMessageWithOptionalDeletion(
            IChatComponent p_printChatMessageWithOptionalDeletion_1_, int p_printChatMessageWithOptionalDeletion_2_);

    @Unique
    private ChatLine k40$lastLine;

    @Unique
    private String k40$lastMessage;

    @Unique
    private int k40$sameMessageAmount;

    @Unique
    private int k40$line;

    @Inject(
            method = "drawChat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/ChatLine;getChatComponent()Lnet/minecraft/util/IChatComponent;"
            ), locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void k40$getChatLine(int updateCounter, CallbackInfo ci, int i, boolean bl, int j, int k, float f, float g,
                                 int l, int m, ChatLine chatLine, int n, double d, int o, int p, int q) {
        k40$lastLine = chatLine;
    }

    @Redirect(
            method = "drawChat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"
            )
    )
    private int k40$redirectText(FontRenderer instance, String text, float x, float y, int color) {
        val module = InstanceAccess.mod.getModule(ChatHeadModule.class);
        val mc = InstanceAccess.mc;

        if (module.isActivated()) {
            float actualX = x;
            NetworkPlayerInfo networkPlayerInfo = ((IChatLine) k40$lastLine).k40$getPlayerInfo();

            actualX += networkPlayerInfo != null ? 10f : 0;

            if (networkPlayerInfo != null) {
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();

                mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());

                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(1.0f, 1.0f, 1.0f, ColorUtils.getAlphaByInt(color));

                Gui.drawScaledCustomSizeModalRect((int) x, (int) (y - 0.5), 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                Gui.drawScaledCustomSizeModalRect((int) x, (int) (y - 0.5), 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);

                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }

            return mc.fontRendererObj.drawStringWithShadow(text, actualX, y, color);
        }

        return instance.drawStringWithShadow(text, x, y, color);
    }

    /**
     * 折叠消息
     *
     * @param component 消息
     * @param ci        回调信息
     * @author 南起
     */
    @Inject(method = "printChatMessage", at = @At("HEAD"), cancellable = true)
    public void k40$prePrintChatMessage(IChatComponent component, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(AntiSpammingModule.class);
        val mc = InstanceAccess.mc;

        if (!module.isActivated()) return;

        String currentMessage = component.getUnformattedText();
        String baseMessage = currentMessage;

        // 如果开启了相似消息检测
        if (module.similerSetting.isActivated()) {
            baseMessage = currentMessage.replaceAll("\\s*[^\\s]*\\d+[^\\s]*$", "").trim();
        }

        if (baseMessage.equals(k40$lastMessage)) {
            mc.ingameGUI.getChatGUI().deleteChatLine(k40$line);
            k40$sameMessageAmount++;
            k40$lastMessage = baseMessage;
            component.appendText(ChatFormatting.RESET + "§7 [x" + k40$sameMessageAmount + "]" + ChatFormatting.RESET);
        }
        else {
            k40$sameMessageAmount = 1;
            k40$lastMessage = baseMessage;
        }

        k40$line++;

        if (k40$line > 256) k40$line = 0;

        printChatMessageWithOptionalDeletion(component, k40$line);
        ci.cancel(); // 取消原方法的执行
    }
}
