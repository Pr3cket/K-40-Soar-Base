package com.lawab1ders.nan7i.kali.module.impl.player;

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

import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.RendererTickedEvent;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.AnimatedEntity;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.RenderBendsPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.data.Data_Player;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import lombok.val;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.util.vector.Vector3f;

import java.util.Objects;

@IModule(
        name = "module.mobends",
        description = "module.mobends.desc",
        category = EModuleCategory.PLAYER
)
public class MoBendsModule extends Module {

    public final BooleanSetting swordTrailSetting = new BooleanSetting("module.mobends.opts.swordtrail", true);
    public final BooleanSetting spinAttackSetting = new BooleanSetting("module.mobends.opts.spinattack", true);

    public static final ResourceLocation texture_NULL = new ResourceLocation("k40", "textures/white.png");
    public static float partialTicks;

    @Override
    protected void onEnable() {
        val skinLayers3DModule = mod.getModule(SkinLayers3DModule.class);
        val waveyCapesModule = mod.getModule(WaveyCapesModule.class);

        if (skinLayers3DModule.isActivated()) {
            skinLayers3DModule.toggle();
            ntf.post(
                    getName(),
                    TranslateComponent.i18n("module.noti.disable.module") + " " + skinLayers3DModule.getName(),
                    NotificationType.INFO
            );
        }

        if (waveyCapesModule.isActivated()) {
            waveyCapesModule.toggle();
            ntf.post(
                    getName(),
                    TranslateComponent.i18n("module.noti.disable.module") + " " + waveyCapesModule.getName(),
                    NotificationType.INFO
            );
        }
    }

    /**
     * 别问我为什么又移植了一遍
     */
    public MoBendsModule() {
        super();
        AnimatedEntity.register();
    }

    @SubscribeEvent
    public void onRendererTicked(RendererTickedEvent event) {
        if (mc.theWorld == null) return;

        partialTicks = event.getRenderTickTime();

        for (int i = 0; i < Data_Player.dataList.size(); ++i) {
            Data_Player.dataList.get(i).update(event.getRenderTickTime());
        }
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        if (mc.theWorld == null) return;

        Entity entity;

        for (int i = 0; i < Data_Player.dataList.size(); ++i) {
            Data_Player data = Data_Player.dataList.get(i);
            entity = mc.theWorld.getEntityByID(data.entityID);

            if (entity != null) {
                if (!data.entityType.equalsIgnoreCase(entity.getName())) {
                    Data_Player.dataList.remove(data);
                    Data_Player.add(new Data_Player(entity.getEntityId()));

                } else {
                    data.motion_prev.set(data.motion);
                    data.motion.x = (float) entity.posX - data.position.x;
                    data.motion.y = (float) entity.posY - data.position.y;
                    data.motion.z = (float) entity.posZ - data.position.z;
                    data.position = new Vector3f((float) entity.posX, (float) entity.posY, (float) entity.posZ);
                }
            } else {
                Data_Player.dataList.remove(data);
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (event.entity instanceof EntityPlayer && !(event.renderer instanceof RenderBendsPlayer)
                && Objects.requireNonNull(AnimatedEntity.getByEntity(event.entity)).animate) {

            AbstractClientPlayer player = (AbstractClientPlayer) event.entity;
            AnimatedEntity.getPlayerRenderer(player).doRender(player, event.x, event.y, event.z, 0.0F, partialTicks);

            event.setCanceled(true);
        }
    }
}
