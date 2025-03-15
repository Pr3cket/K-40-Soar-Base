package com.lawab1ders.nan7i.kali.module.impl.combat;

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

import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.events.LevelsRenderedEvent;
import com.lawab1ders.nan7i.kali.events.cancelable.PacketReceivedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.PlayerTickedEvent;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinRenderManager;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import lombok.AllArgsConstructor;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@IModule(
        name = "module.targetpredicator",
        description = "module.targetpredicator.desc",
        category = EModuleCategory.COMBAT
)
public class TargetPredicatorModule extends Module {

    public final FloatSetting alphaSetting = new FloatSetting("module.opts.alpha", 0.45F, 0.1F, 1.0F);

    private Position realTargetPosition = new Position(0, 0, 0);

    @SubscribeEvent
    public void onPlayerTicked(PlayerTickedEvent event) {
        AbstractClientPlayer target = KaliAPI.INSTANCE.getAttackingTaget();

        if (target != null) realTargetPosition = new Position(target.posX, target.posY, target.posZ);
    }

    @SubscribeEvent
    public void onPacketReceived(PacketReceivedEvent event) {
        AbstractClientPlayer target = KaliAPI.INSTANCE.getAttackingTaget();
        Packet<?> packet = event.getPacket();

        if (target == null) return;

        if (packet instanceof S14PacketEntity) {
            S14PacketEntity s14PacketEntity = ((S14PacketEntity) packet);

            if (target.getEntityId() == s14PacketEntity.getEntity(mc.theWorld).getEntityId()) {
                realTargetPosition.x += s14PacketEntity.func_149062_c() / 32D;
                realTargetPosition.y += s14PacketEntity.func_149061_d() / 32D;
                realTargetPosition.z += s14PacketEntity.func_149064_e() / 32D;
            }
        } else if (packet instanceof S18PacketEntityTeleport) {
            S18PacketEntityTeleport s18PacketEntityTeleport = (S18PacketEntityTeleport) packet;

            if (target.getEntityId() == s18PacketEntityTeleport.getEntityId())
                realTargetPosition = new Position(s18PacketEntityTeleport.getX() / 32D,
                    s18PacketEntityTeleport.getY() / 32D, s18PacketEntityTeleport.getZ() / 32D);
        }
    }

    @SubscribeEvent
    public void onLevelsRendered(LevelsRenderedEvent event) {

        AbstractClientPlayer target = KaliAPI.INSTANCE.getAttackingTaget();

        if (target == null) return;

        if (realTargetPosition.squareDistanceTo(target.posX, target.posY, target.posZ) > 0.00001) {
            event.init();

            ColorUtils.setColor(ColorUtils.applyAlpha(col.getCurrentColor().getInterpolateColor(0),
                    (int) (alphaSetting.getValue() * 255)).getRGB());

            AxisAlignedBB aa = mc.thePlayer.getEntityBoundingBox().offset(-mc.thePlayer.posX, -mc.thePlayer.posY,
                            -mc.thePlayer.posZ).
                    offset(realTargetPosition.x, realTargetPosition.y, realTargetPosition.z).expand(0.14, 0.14, 0.14);

            event.begin();
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
            event.end();

            event.begin();
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
            event.end();

            event.begin();
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
            event.end();

            event.begin();
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
            event.end();

            event.begin();
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
            event.end();

            event.begin();
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
            glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
            glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
            glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
            event.end();

            event.reset();
        }
    }

    private void glVertex3D(Vec3 vector3d) {
        GL11.glVertex3d(vector3d.xCoord, vector3d.yCoord, vector3d.zCoord);
    }

    private Vec3 getRenderPos(double x, double y, double z) {
        x -= ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosX();
        y -= ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosY();
        z -= ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosZ();

        return new Vec3(x, y, z);
    }

    @AllArgsConstructor
    private static class Position {
        private double x, y, z;

        private double squareDistanceTo(double x, double y, double z) {
            double d0 = x - this.x;
            double d1 = y - this.y;
            double d2 = z - this.z;

            return d0 * d0 + d1 * d1 + d2 * d2;
        }
    }
}
