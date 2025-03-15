package com.lawab1ders.nan7i.kali.module.impl.render;

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

import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.events.LevelsRenderedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.PlayerTickedEvent;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinRenderManager;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@IModule(
        name = "module.jumpcircle",
        description = "module.jumpcircle.desc",
        category = EModuleCategory.RENDER
)
public class JumpCircleModule extends Module {

    public final EnumSetting modeSetting = new EnumSetting(
            "module.opts.mode",
            "module.jumpcircle.opts.mode.jumpoff",
            "module.jumpcircle.opts.mode.land"
    );

    private final CopyOnWriteArrayList<JumpCircle> circles = new CopyOnWriteArrayList<>();
    private boolean jumping;

    private static double createAnimation(double value) {
        return Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
    }

    @SubscribeEvent
    public void onPlayerTicked(PlayerTickedEvent event) {
        if (jumping && mc.thePlayer.onGround) {
            if (Objects.equals(modeSetting.getSelectedEntryKey(), "module.jumpcircle.opts.mode.land")) {
                circles.add(new JumpCircle(event.getPlayer().getPositionVector()));
            }

            jumping = false;
        }

        circles.removeIf(JumpCircle::update);
    }

    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.entity != mc.thePlayer) return;

        if (!jumping && Objects.equals(modeSetting.getSelectedEntryKey(), "module.jumpcircle.opts.mode.jumpoff")) {
            circles.add(new JumpCircle(mc.thePlayer.getPositionVector()));
        }

        jumping = true;
    }

    @SubscribeEvent
    public void onLevelsRendered(LevelsRenderedEvent event) {
        if (circles.isEmpty()) return;

        AccentColor currentColor = col.getCurrentColor();

        float red = (float) (currentColor.getInterpolateColor().getRGB() >> 16 & 255) / 255.0F;
        float green = (float) (currentColor.getInterpolateColor().getRGB() >> 8 & 255) / 255.0F;
        float blue = (float) (currentColor.getInterpolateColor().getRGB() & 255) / 255.0F;

        event.init();

        for (JumpCircle circle : circles) {
            event.begin();

            for (int i = 0; i <= 360; i += 5) {
                Vec3 pos = circle.pos();

                double x = Math.cos(Math.toRadians(i)) * createAnimation(
                        1.0 - circle.getAnimation(event.getPartialTicks())) * 0.7;
                double z = Math.sin(Math.toRadians(i)) * createAnimation(
                        1.0 - circle.getAnimation(event.getPartialTicks())) * 0.7;

                val k = circle.getAnimation(event.getPartialTicks());
                val alpha = 0.9 * (k > 0.5 ? 1 - k : k);

                GL11.glColor4d(red, green, blue, alpha);
                GL11.glVertex3d(pos.xCoord + x, pos.yCoord + (double) 0.2f, pos.zCoord + z);
                GL11.glColor4d(red, green, blue, alpha / 3);
                GL11.glVertex3d(pos.xCoord + x * 1.4, pos.yCoord + (double) 0.2f, pos.zCoord + z * 1.4);
            }

            event.end();
        }

        event.reset();
    }

    @RequiredArgsConstructor
    private static class JumpCircle {

        private final Vec3 vector;
        private int tick = 20;
        private int prevTick = 20;

        public double getAnimation(float pt) {
            return ((float) this.prevTick + (float) (this.tick - this.prevTick) * pt) / 20.0f;
        }

        public boolean update() {
            this.prevTick = this.tick;
            return this.tick-- <= 0;
        }

        public Vec3 pos() {
            return new Vec3(
                    this.vector.xCoord - ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosX(),
                    this.vector.yCoord - ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosY(),
                    this.vector.zCoord - ((IMixinRenderManager) mc.getRenderManager()).k40$getRenderPosZ()
            );
        }
    }
}
