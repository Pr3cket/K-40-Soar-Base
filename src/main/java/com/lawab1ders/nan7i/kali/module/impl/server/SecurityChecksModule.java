package com.lawab1ders.nan7i.kali.module.impl.server;

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

import com.lawab1ders.nan7i.kali.events.cancelable.PacketReceivedEvent;
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import net.minecraft.network.play.server.*;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@IModule(
        name = "module.securitychecks",
        description = "module.securitychecks.desc",
        category = EModuleCategory.SERVER,
        activated = true
)
public class SecurityChecksModule extends Module {

    private final Pattern pattern = Pattern.compile(".*\\$\\{[^}]*}.*");
    private int particles;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void demo(PacketReceivedEvent event) {
        if (event.getPacket() instanceof S2BPacketChangeGameState) {

            S2BPacketChangeGameState state = ((S2BPacketChangeGameState) event.getPacket());

            if (state.getGameState() == 5 && state.func_149137_d() == 0) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void explosion(PacketReceivedEvent event) {
        if (event.getPacket() instanceof S27PacketExplosion) {

            S27PacketExplosion explosion = ((S27PacketExplosion) event.getPacket());

            if (explosion.func_149149_c() >= Byte.MAX_VALUE || explosion.func_149144_d() >= Byte.MAX_VALUE || explosion.func_149149_c() >= Byte.MAX_VALUE) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void log4j(PacketReceivedEvent event) {

        if (event.getPacket() instanceof S29PacketSoundEffect) {

            S29PacketSoundEffect sound = (S29PacketSoundEffect) event.getPacket();
            String name = sound.getSoundName();

            if (pattern.matcher(name).matches()) {
                event.setCanceled(true);
            }
        }

        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat chat = ((S02PacketChat) event.getPacket());
            IChatComponent component = chat.getChatComponent();

            if (pattern.matcher(component.getUnformattedText()).matches() || pattern.matcher(component.getFormattedText()).matches()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void particle(PacketReceivedEvent event) {
        if (event.getPacket() instanceof S2APacketParticles) {
            S2APacketParticles particle = ((S2APacketParticles) event.getPacket());

            particles += particle.getParticleCount();
            particles -= 6;
            particles = Math.min(particles, 150);

            if (particles > 100 || particle.getParticleCount() < 1 || Math.abs(particle.getParticleCount()) > 20 || particle.getParticleSpeed() < 0 || particle.getParticleSpeed() > 1000) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void resourcePack(PacketReceivedEvent event) {
        if (event.getPacket() instanceof S48PacketResourcePackSend) {

            S48PacketResourcePackSend pack = ((S48PacketResourcePackSend) event.getPacket());
            String url = pack.getURL();
            boolean invalid = false;

            try {
                URI uri = new URI(url);

                String scheme = uri.getScheme();
                boolean isLevelProtocol = "level".equals(scheme);

                if (!("http".equals(scheme) || "https".equals(scheme) || isLevelProtocol)) {
                    throw new URISyntaxException(url, "Wrong protocol");
                }

                url = URLDecoder.decode(url.substring("level://".length()), StandardCharsets.UTF_8.toString());

                if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
                    throw new URISyntaxException(url, "Invalid levelstorage resource pack path");
                }

            } catch (Exception e) {
                invalid = true;
            }

            if (url.toLowerCase().startsWith("level://") && invalid) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void teleport(PacketReceivedEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {

            S08PacketPlayerPosLook pos = ((S08PacketPlayerPosLook) event.getPacket());

            if (Math.abs(pos.getX()) > 1E+9 || Math.abs(pos.getY()) > 1E+9 || Math.abs(pos.getZ()) > 1E+9) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void clickGuiClose(PacketReceivedEvent event) {
        if (event.getPacket() instanceof S2EPacketCloseWindow && mc.currentScreen instanceof GuiClickMenu) {
            event.setCanceled(true);
        }
    }
}
