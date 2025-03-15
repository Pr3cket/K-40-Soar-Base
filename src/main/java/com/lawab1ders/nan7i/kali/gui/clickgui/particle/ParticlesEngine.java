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
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticlesEngine implements InstanceAccess {

    // 看久了真的不舒服，未来可能会写开关
    public static final boolean FUCK_OUTTT = true;

    private final List<Particle> particles = new ArrayList<>();

    private int amount;
    private int prevWidth, prevHeight;

    public void draw(int mouseX, int mouseY, float alpha) {
        if (FUCK_OUTTT) return;

        if (particles.isEmpty() || prevWidth != mc.displayWidth || prevHeight != mc.displayHeight) {
            particles.clear();
            amount = (mc.displayWidth + mc.displayHeight) / 8;
            create();
        }

        prevWidth = mc.displayWidth;
        prevHeight = mc.displayHeight;

        for (Particle particle : particles) {
            if (particle.getTimer().delay(1000 / 60)) {
                particle.fall();
                particle.inter();

                particle.getTimer().reset();
            }

            int range = 50;
            final boolean mouseOver = (mouseX >= particle.getX() - range) &&
                    (mouseY >= particle.getY() - range) &&
                    (mouseX <= particle.getX() + range) &&
                    (mouseY <= particle.getY() + range);

            if (mouseOver) {
                particles.stream()
                        .filter(part -> (part.getX() > particle.getX() && part.getX() - particle.getX() < range
                                && particle.getX() - part.getX() < range)
                                && (part.getY() > particle.getY() && part.getY() - particle.getY() < range
                                || particle.getY() > part.getY() && particle.getY() - part.getY() < range))
                        .forEach(connectable -> particle.connect(connectable.getX(), connectable.getY(), alpha));
            }

            Gui.drawRect((int) particle.getX(), (int) particle.getY(), (int) particle.getSize(),
                    (int) particle.getSize(), new Color(1, 1, 1, Math.min(1, alpha)).getRGB());
        }
    }

    private void create() {
        if (FUCK_OUTTT) return;

        Random random = new Random();

        for (int i = 0; i < amount; i++) {
            particles.add(new Particle(random.nextInt(mc.displayWidth), random.nextInt(mc.displayHeight)));
        }
    }
}
