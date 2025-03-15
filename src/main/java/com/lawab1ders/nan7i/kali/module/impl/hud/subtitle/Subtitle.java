package com.lawab1ders.nan7i.kali.module.impl.hud.subtitle;

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

import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

@Getter
public class Subtitle {

    @Setter
    private boolean remove;

    @Setter
    private boolean done;

    private final boolean fake;
    private final String subtitle;
    private final Animation animation = new Animation(0.0F);

    private long startTime;
    private Vec3 location;

    public Subtitle(String subtitleIn) {
        this(subtitleIn, new Vec3(0.0D, 0.0D, 0.0D), true);
    }

    public Subtitle(String subtitleIn, Vec3 locationIn) {
        this(subtitleIn, locationIn, false);
    }

    private Subtitle(String subtitleIn, Vec3 locationIn, boolean fake) {
        this.subtitle = subtitleIn;
        this.location = locationIn;
        this.startTime = Minecraft.getSystemTime();
        this.remove = false;
        this.done = false;
        this.fake = fake;

        if (fake) animation.setValue(1);
    }

    public String getString() {
        return this.subtitle;
    }

    public void refresh(Vec3 locationIn) {
        this.location = locationIn;
        this.startTime = Minecraft.getSystemTime();
    }
}