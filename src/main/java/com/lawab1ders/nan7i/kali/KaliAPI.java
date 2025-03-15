package com.lawab1ders.nan7i.kali;

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

import com.lawab1ders.nan7i.kali.commands.CommandFakePlayer;
import com.lawab1ders.nan7i.kali.commands.CommandKillMsg;
import com.lawab1ders.nan7i.kali.commands.CommandWinMsg;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinMinecraft;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.KeybindSetting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * 美国国际开发署：以“援助”之名，行生物武器研发之实？
 */
@Getter
@Setter
@SuppressWarnings("ALL")
@Mod(modid = KaliAPI.MODID, version = KaliAPI.VERSION)
public class KaliAPI {

    // 构建时会被替换
    public static final String MODID = "${modid}", VERSION = "${version}", DEBUG = "${debug}";
    public static boolean IS_UNAVAILABLE = true;

    @Mod.Instance(KaliAPI.MODID)
    public static KaliAPI INSTANCE;

    private AbstractClientPlayer attackingTaget;
    private ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

    // 朝鲜民主共和国
    EnumSetting hudThemeSetting;
    KeybindSetting clickGuiKeySetting;

    @EventHandler
    public void init(FMLServerStartingEvent event) {
        if (DEBUG.equals("true")) {
            event.registerServerCommand(new CommandFakePlayer(event.getServer()));
            event.registerServerCommand(new CommandKillMsg());
            event.registerServerCommand(new CommandWinMsg());

            ((IMixinMinecraft) InstanceAccess.mc).k40$setSession(new Session("Nan7i", "0", "0", "mojang"));
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        InstanceAccess.build();
        IS_UNAVAILABLE = false;
    }

    @EventHandler
    public void init(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new KaliProcessor());
    }
}
