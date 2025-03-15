package com.lawab1ders.nan7i.kali.module.impl.server;

/*
 * Copyright (c) 2025 EldoDebug, Nan7i.南起, 乘风.ChengF3ng（音乐播放器 API.Music Player API）
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

import com.lawab1ders.nan7i.kali.events.KeyEvent;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@IModule(
        name = "module.antighost",
        description = "module.antighost.desc",
        category = EModuleCategory.SERVER
)
public class AntiGhostModule extends Module {

    public final KeybindSetting keybindSetting = new KeybindSetting(Keyboard.KEY_G);

    @SubscribeEvent
    public void onKey(KeyEvent event) {
        if (event.getKeyCode() == keybindSetting.getKeyCode()) {
            NetHandlerPlayClient conn = mc.getNetHandler();

            if (conn == null) {
                return;
            }

            BlockPos pos = mc.thePlayer.getPosition();
            for (int dx = -4; dx <= 4; dx++) {

                for (int dy = -4; dy <= 4; dy++) {

                    for (int dz = -4; dz <= 4; dz++) {
                        C07PacketPlayerDigging packet = new C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK,
                                new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz),
                                EnumFacing.UP       // with ABORT_DESTROY_BLOCK, this value is unused
                        );

                        conn.addToSendQueue(packet);
                    }
                }
            }

            ntf.post(
                    getName(),
                    TranslateComponent.i18n("module.antighost.noti"),
                    NotificationType.SUCCESS
            );
        }
    }
}
