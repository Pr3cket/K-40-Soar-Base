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

import com.lawab1ders.nan7i.kali.events.MotionEvent;
import com.lawab1ders.nan7i.kali.events.WorldLoadedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.PlayerTickedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.WorldTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@IModule(
        name = "module.killeffects",
        description = "module.killeffects.desc",
        category = EModuleCategory.SERVER
)
public class KillEffectsModule extends Module {

    public final EnumSetting effectSetting = new EnumSetting("module.killeffects.opts.effect",
            "module.killeffects.opts.effect.lighting",
            "module.killeffects.opts.effect.flames",
            "module.killeffects.opts.effect.smoke",
            "module.killeffects.opts.effect.blood"
    );

    public final BooleanSetting soundSetting = new BooleanSetting("module.killeffects.opts.sound", true);
    public final IntSetting multiplierSetting = new IntSetting("module.killeffects.opts.multiplier", 1, 1, 10);

    private EntityLivingBase target;
    private int entityID;

    @SubscribeEvent
    public void onPlayerTicked(PlayerTickedEvent event) {
        if (mc.objectMouseOver != null & mc.objectMouseOver.entityHit != null) {
            if (mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
                target = (EntityLivingBase) mc.objectMouseOver.entityHit;
            }
        }
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {

        if (target != null && !mc.theWorld.loadedEntityList.contains(target) && mc.thePlayer.getDistanceSq(target.posX, mc.thePlayer.posY, target.posZ) < 100) {

            if (mc.thePlayer.ticksExisted > 3) {

                if (effectSetting.getSelectedEntryKey().equals("module.killeffects.opts.effect.lighting")) {

                    EntityLightningBolt entityLightningBolt = new EntityLightningBolt(mc.theWorld,
                            target.posX,
                            target.posY,
                            target.posZ
                    );

                    mc.theWorld.addEntityToWorld(entityID--, entityLightningBolt);

                    if (soundSetting.isActivated()) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(
                                new ResourceLocation("ambient.weather.thunder"),
                                ((float) target.posX),
                                ((float) target.posY),
                                ((float) target.posZ))
                        );
                    }
                } else if (effectSetting.getSelectedEntryKey().equals("module.killeffects.opts.effect.flames")) {

                    for (int i = 0; i < multiplierSetting.getValue(); i++) {
                        mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.FLAME);
                    }

                    if (soundSetting.isActivated()) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(
                                new ResourceLocation("item.fireCharge.use"),
                                ((float) target.posX),
                                ((float) target.posY),
                                ((float) target.posZ))
                        );
                    }
                } else if (effectSetting.getSelectedEntryKey().equals("module.killeffects.opts.effect.smoke")) {

                    for (int i = 0; i < multiplierSetting.getValue(); i++) {
                        mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CLOUD);
                    }

                    if (soundSetting.isActivated()) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(
                                new ResourceLocation("fireworks.twinkle"),
                                ((float) target.posX),
                                ((float) target.posY),
                                ((float) target.posZ))
                        );
                    }
                } else if (effectSetting.getSelectedEntryKey().equals("module.killeffects.opts.effect.blood")) {

                    for (int i = 0; i < 50; i++) {
                        mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                                target.posX, target.posY + target.height - 0.75, target.posZ,
                                0, 0, 0,
                                Block.getStateId(Blocks.redstone_block.getDefaultState()));
                    }

                    if (soundSetting.isActivated()) {
                        mc.getSoundHandler().playSound(new PositionedSoundRecord(
                                new ResourceLocation("dig.stone"),
                                4.0F, 1.2F,
                                ((float) target.posX),
                                ((float) target.posY),
                                ((float) target.posZ))
                        );
                    }
                }
            }
            target = null;
        }
    }

    @SubscribeEvent
    public void onWorldTicked(WorldLoadedEvent event) {
        entityID = 0;
    }
}
