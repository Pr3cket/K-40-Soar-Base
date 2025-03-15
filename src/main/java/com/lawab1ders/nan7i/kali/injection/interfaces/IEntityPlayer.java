package com.lawab1ders.nan7i.kali.injection.interfaces;

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
import com.lawab1ders.nan7i.kali.module.impl.player.WaveyCapesModule;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.render.CustomizableModelPart;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.StickSimulation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public interface IEntityPlayer {

    CustomizableModelPart k40$getHeadLayers();

    CustomizableModelPart[] k40$getSkinLayers();

    void k40$setHeadLayers(CustomizableModelPart box);

    void k40$setSkinLayers(CustomizableModelPart[] box);

    /// Wavey Capes

    StickSimulation k40$getSimulation();

    default void k40$updateSimulation(EntityPlayer abstractClientPlayer, int partCount) {

        StickSimulation simulation = k40$getSimulation();
        boolean dirty = false;

        if (simulation.points.size() != partCount) {

            simulation.points.clear();
            simulation.sticks.clear();

            for (int i = 0; i < partCount; i++) {
                StickSimulation.Point point = new StickSimulation.Point();
                point.position.y = -i;
                point.locked = i == 0;
                simulation.points.add(point);
                if (i > 0) {
                    simulation.sticks.add(new StickSimulation.Stick(simulation.points.get(i - 1), point, 1f));
                }
            }
            dirty = true;
        }

        if (dirty) {
            for (int i = 0; i < 10; i++) {
                k40$simulate(abstractClientPlayer);
            }
        }
    }

    default void k40$simulate(EntityPlayer abstractClientPlayer) {
        StickSimulation simulation = k40$getSimulation();

        if (simulation.points.isEmpty()) {
            return;
        }

        simulation.points.get(0).prevPosition.copyFrom(simulation.points.get(0).position);
        double d = abstractClientPlayer.chasingPosX
                - abstractClientPlayer.posX;
        double m = abstractClientPlayer.chasingPosZ
                - abstractClientPlayer.posZ;
        float n = abstractClientPlayer.prevRenderYawOffset + abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset;
        double o = Math.sin(n * 0.017453292F);
        double p = -Math.cos(n * 0.017453292F);
        float heightMul = InstanceAccess.mod.getModule(WaveyCapesModule.class).heightMultiplierSetting.getValue();
        double fallHack = MathHelper.clamp_double(
                (simulation.points.get(0).position.y - (abstractClientPlayer.posY * heightMul)), 0d, 1d);

        simulation.points.get(0).position.x += (float) ((d * o + m * p) + fallHack);
        simulation.points.get(
                0).position.y = (float) (abstractClientPlayer.posY * heightMul + (abstractClientPlayer.isSneaking() ? -4 : 0));
        simulation.simulate();
    }
}
