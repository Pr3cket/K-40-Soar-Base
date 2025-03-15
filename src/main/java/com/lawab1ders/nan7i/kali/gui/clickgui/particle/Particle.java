package com.lawab1ders.nan7i.kali.gui.clickgui.particle;

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

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.KaliProcessor;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@Getter
@AllArgsConstructor
public class Particle implements InstanceAccess {

    private final float size = genRandom() + 0.4F;

    private final float ySpeed = new Random().nextInt(5), xSpeed = new Random().nextInt(5);
    private final MillisTimer timer = new MillisTimer();

    private float x, y;

    public void fall() {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        y = (y + ySpeed);
        x = (x + xSpeed);

        if (y > mc.displayHeight) {
            y = 1;
        }

        if (x > mc.displayWidth) {
            x = 1;
        }

        if (x < 1) {
            x = sr.getScaledWidth();
        }

        if (y < 1) {
            y = sr.getScaledHeight();
        }
    }

    public void inter() {
        for (int n = 0; n <= 64; ++n) {
            final float f = n / 64.0f;
            final float p1 = lint1(f);
            final float p2 = lint2(f);

            if (p1 != p2) {
                y -= f;
                x -= f;
            }
        }
    }

    public void connect(float x, float y, float alpha) {
        if (KaliProcessor.BLUR_TICK) return;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F * alpha);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(0.5F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(getX(), getY());
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private float lint1(float f) {
        return ((float) 1.02 * (1.0f - f)) + (f);
    }

    private float lint2(float f) {
        return (float) 1.02 + f * ((float) 1.0 - (float) 1.02);
    }

    private float genRandom() {
        return (float) (0.3f + Math.random() * (0.6f - 0.3f + 1.0F));
    }
}
