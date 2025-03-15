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

import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.WorldTickedEvent;
import com.lawab1ders.nan7i.kali.injection.interfaces.ICullable;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.server.occlusionculling.OcclusionCulling;
import com.lawab1ders.nan7i.kali.module.impl.server.occlusionculling.Provider;
import com.lawab1ders.nan7i.kali.module.impl.server.occlusionculling.util.Vec3d;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

@IModule(
        name = "module.entityculling",
        description = "module.entityculling.desc",
        category = EModuleCategory.SERVER,
        activated = true
)
public class EntityCullingModule extends Module {

    public BooleanSetting renderNametagsThroughWallsSetting = new BooleanSetting(
            "module.entityculling.opts.rendernametagsthroughwalls", true);

    public BooleanSetting skipMarkerArmorStandsSetting = new BooleanSetting(
            "module.entityculling.opts.skipmarkerarmorstands", true);

    public IntSetting hitboxLimitSetting = new IntSetting(
            "module.entityculling.opts.hitboxlimit", 50, 5, 50);

    public IntSetting tracingDistanceSetting = new IntSetting(
            "module.entityculling.opts.tracingdistance", 128, 10, 150);

    public IntSetting sleepDelaySetting = new IntSetting(
            "module.entityculling.opts.sleepdelay", 10, 10, 50);

    // 会在调试界面中显示
    public static int skippedBlockEntities = 0, skippedEntities = 0;
    public static boolean requestCull = false;

    // reused preallocated vars
    private final Vec3d lastPos = new Vec3d(0, 0, 0), aabbMin = new Vec3d(0, 0, 0), aabbMax = new Vec3d(0, 0, 0);
    private final Set<String> unCullable = new HashSet<>(Collections.singletonList("tile.beacon"));
    private final OcclusionCulling cInstance = new OcclusionCulling(new Provider());

    private int lastDistance = 128;

    private final Thread thread = new Thread(() -> {
        while (true) {
            try {
                if (!isActivated()) continue;

                Thread.sleep(sleepDelaySetting.getValue());

                if (mc.theWorld == null || mc.thePlayer == null || mc.thePlayer.ticksExisted <= 10
                        || mc.getRenderViewEntity() == null) continue;

                Vec3 cameraMC = mc.getRenderViewEntity().getPositionEyes(0);

                if (!requestCull && (cameraMC.xCoord == lastPos.x && cameraMC.yCoord == lastPos.y
                        && cameraMC.zCoord == lastPos.z)) continue;

                requestCull = false;
                lastPos.set(cameraMC.xCoord, cameraMC.yCoord, cameraMC.zCoord);

                Vec3d camera = lastPos;
                boolean noCulling = mc.thePlayer.isSpectator() || mc.gameSettings.thirdPersonView != 0;

                // 重置缓存
                cInstance.resetCache();

                TileEntity entry;
                Iterator<TileEntity> iterator = mc.theWorld.loadedTileEntityList.iterator();
                while (iterator.hasNext()) {
                    try {
                        entry = iterator.next();
                    } catch (NullPointerException | ConcurrentModificationException ex) {
                        break; // We are not synced to the main thread, so NPE's/CME are allowed here and way
                        // less
                        // overhead probably than trying to sync stuff up for no really good reason
                    }

                    if (unCullable.contains(entry.getBlockType().getUnlocalizedName())) {
                        continue;
                    }

                    ICullable cullable = (ICullable) entry;
                    if (cullable.k40$notForcedVisible()) {
                        if (noCulling) {
                            cullable.k40$setCulled(false);
                            continue;
                        }

                        BlockPos pos = entry.getPos();

                        if (pos.distanceSq(cameraMC.xCoord, cameraMC.yCoord, cameraMC.zCoord) < 64 * 64) { //
                            // 64 is the fixed max tile view distance
                            aabbMin.set(pos.getX(), pos.getY(), pos.getZ());
                            aabbMax.set(pos.getX() + 1d, pos.getY() + 1d, pos.getZ() + 1d);
                            boolean visible = cInstance.isAABBVisible(aabbMin, aabbMax, camera);
                            cullable.k40$setCulled(!visible);
                        }
                    }
                }

                Entity entity;
                Iterator<Entity> iterable = mc.theWorld.getLoadedEntityList().iterator();
                while (iterable.hasNext()) {
                    try {
                        entity = iterable.next();
                    } catch (NullPointerException | ConcurrentModificationException ex) {
                        break; // We are not synced to the main thread, so NPE's/CME are allowed here and way
                        // less
                        // overhead probably than trying to sync stuff up for no really good reason
                    }

                    if (!(entity instanceof ICullable)) {
                        continue; // Not sure how this could happen outside from mixin screwing up the inject
                        // into Entity
                    }

                    ICullable cullable = (ICullable) entity;

                    if (cullable.k40$notForcedVisible()) {
                        if (noCulling || isSkippableArmorstand(entity)) {
                            cullable.k40$setCulled(false);
                            continue;
                        }

                        if (entity.getPositionVector().squareDistanceTo(cameraMC) > tracingDistanceSetting.getValue() * tracingDistanceSetting.getValue()) {
                            cullable.k40$setCulled(false); // If your entity view distance is larger than
                            // tracingDistance just render it
                            continue;
                        }

                        AxisAlignedBB boundingBox = entity.getEntityBoundingBox();

                        if (boundingBox.maxX - boundingBox.minX > hitboxLimitSetting.getValue()
                                || boundingBox.maxY - boundingBox.minY > hitboxLimitSetting.getValue()
                                || boundingBox.maxZ - boundingBox.minZ > hitboxLimitSetting.getValue()) {

                            cullable.k40$setCulled(false); // Too big to bother to cull
                            continue;
                        }

                        aabbMin.set(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
                        aabbMax.set(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);

                        boolean visible = cInstance.isAABBVisible(aabbMin, aabbMax, camera);
                        cullable.k40$setCulled(!visible);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    private boolean isSkippableArmorstand(Entity entity) {
        if (!skipMarkerArmorStandsSetting.isActivated()) return false;
        return entity instanceof EntityArmorStand && ((EntityArmorStand) entity).hasMarker();
    }

    @Override protected void onEnable() {
        if (!thread.isAlive()) thread.start();
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {
        tick();
    }

    @SubscribeEvent
    public void onWorldTicked(WorldTickedEvent event) {
        tick();
    }

    private void tick() {
        if (lastDistance != tracingDistanceSetting.getValue()) {
            lastDistance = tracingDistanceSetting.getValue();

            cInstance.setReach(lastDistance);
        }

        requestCull = true;
    }
}
