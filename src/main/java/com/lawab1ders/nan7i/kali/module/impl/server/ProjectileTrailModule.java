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

import com.lawab1ders.nan7i.kali.events.ticks.PlayerTickedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.server.projectiletrail.ProjectileTrailType;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@IModule(
        name = "module.projectiletrail",
        description = "module.projectiletrail.desc",
        category = EModuleCategory.SERVER
)
public class ProjectileTrailModule extends Module {

    public EnumSetting mode = new EnumSetting(
            "module.opts.mode",
            new ArrayList<String>() {{
                for (ProjectileTrailType t : ProjectileTrailType.values()) {
                    add(t.getNameKey());
                }
            }}
    );

    private final ArrayList<Object> throwables = new ArrayList<>();
    private int ticks;

    @SubscribeEvent
    @SuppressWarnings("ALL")
    public void onUpdate(PlayerTickedEvent event) {
        ProjectileTrailType type = ProjectileTrailType.getTypeByKey(mode.getSelectedEntryKey());
        ticks = ticks >= 20 ? 0 : ticks + 2;

        updateThrowables();

        // 创建实体列表的副本以避免并发修改异常
        List<Entity> entities = new ArrayList<>(mc.theWorld.getLoadedEntityList());

        entities.stream()
                .filter(Objects::nonNull)
                .filter(entity -> isValidEntity(entity) || throwables.contains(entity))
                .filter(entity -> entity.getDistanceToEntity(mc.thePlayer) > 3.0F)
                .forEach(entity -> spawnParticle(type, entity.getPositionVector()));
    }

    public void spawnParticle(ProjectileTrailType trail, Vec3 vector) {
        if (shouldSpawnParticle(trail)) {
            float translate = trail.getTranslate();
            float velocity = trail.getVelocity();

            for (int i = 0; i < trail.getCount(); ++i) {
                Random random = new Random();
                double[] position = calculatePosition(random, translate, vector);
                double[] velocityArray = calculateVelocity(random, velocity);

                mc.theWorld.spawnParticle(
                        trail.getParticle(), true,
                        position[0], position[1], position[2],
                        velocityArray[0], velocityArray[1], velocityArray[2]
                );
            }
        }
    }

    private boolean shouldSpawnParticle(ProjectileTrailType trail) {
        return (trail != ProjectileTrailType.GREEN_STAR && trail != ProjectileTrailType.HEARTS || ticks % 4 == 0)
                && (trail != ProjectileTrailType.MUSIC_NOTES || ticks % 2 == 0);
    }

    private double[] calculatePosition(Random random, float translate, Vec3 vector) {
        return new double[] {
                random.nextFloat() * translate * 2.0F - translate + vector.xCoord,
                random.nextFloat() * translate * 2.0F - translate + vector.yCoord,
                random.nextFloat() * translate * 2.0F - translate + vector.zCoord
        };
    }

    private double[] calculateVelocity(Random random, float velocity) {
        return new double[] {
                random.nextFloat() * velocity * 2.0F - velocity,
                random.nextFloat() * velocity * 2.0F - velocity,
                random.nextFloat() * velocity * 2.0F - velocity
        };
    }

    public boolean isValidEntity(Entity entity) {
        if (entity.posX == entity.prevPosX && entity.posY == entity.prevPosY && entity.posZ == entity.prevPosZ) {
            return false;
        }

        if (entity instanceof EntityArrow) {
            return ((EntityArrow) entity).shootingEntity != null
                    && ((EntityArrow) entity).shootingEntity.equals(mc.thePlayer);
        }

        if (entity instanceof EntityFishHook) {
            return ((EntityFishHook) entity).angler != null
                    && ((EntityFishHook) entity).angler.equals(mc.thePlayer);
        }

        if (entity instanceof EntityThrowable && entity.ticksExisted == 1
                && entity.getDistanceSqToEntity(mc.thePlayer) <= 11.0D
                && !throwables.contains(entity)) {
            throwables.add(entity);
            return true;
        }

        return false;
    }

    public void updateThrowables() {
        throwables.removeIf(throwable -> throwable == null || ((EntityThrowable) throwable).isDead);
    }
}
