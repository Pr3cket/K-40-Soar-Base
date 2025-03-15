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
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ChatLine.class)
public class MixinChatLine implements IChatLine {

    @Unique
    private NetworkPlayerInfo k40$playerInfo;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void k40$onInit(int i, IChatComponent iChatComponent, int j, CallbackInfo ci) {
        NetHandlerPlayClient netHandler = InstanceAccess.mc.getNetHandler();
        Map<String, NetworkPlayerInfo> nicknameCache = new HashMap<>();

        if (netHandler == null) return;

        try {
            for (String word : iChatComponent.getFormattedText().split("(§.)|\\W")) {
                if (word.isEmpty()) continue;

                k40$playerInfo = netHandler.getPlayerInfo(word);

                if (k40$playerInfo == null) {
                    k40$playerInfo = k40$getPlayerFromNickname(word, netHandler, nicknameCache);
                }

                if (k40$playerInfo != null) {
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Unique
    private static NetworkPlayerInfo k40$getPlayerFromNickname(String word, NetHandlerPlayClient connection,
                                                               Map<String, NetworkPlayerInfo> nicknameCache) {
        if (nicknameCache.isEmpty()) {
            for (NetworkPlayerInfo p : connection.getPlayerInfoMap()) {
                IChatComponent displayName = p.getDisplayName();

                if (displayName != null) {
                    String nickname = displayName.getUnformattedTextForChat();

                    if (word.equals(nickname)) {
                        nicknameCache.clear();
                        return p;
                    }

                    nicknameCache.put(nickname, p);
                }
            }
        }
        else {
            return nicknameCache.get(word);
        }

        return null;
    }

    @Override
    public NetworkPlayerInfo k40$getPlayerInfo() {
        return k40$playerInfo;
    }
}
