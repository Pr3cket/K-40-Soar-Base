package com.lawab1ders.nan7i.kali.module.impl.player.skin3d;

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

import lombok.AllArgsConstructor;
import net.minecraft.util.Vec3i;
import org.lwjgl.util.vector.Vector3f;

@AllArgsConstructor
public enum SkinDirection {

    DOWN(new Vec3i(0, -1, 0)), UP(new Vec3i(0, 1, 0)), NORTH(new Vec3i(0, 0, -1)), SOUTH(new Vec3i(0, 0, 1)),
    WEST(new Vec3i(-1, 0, 0)), EAST(new Vec3i(1, 0, 0));

    private final Vec3i normal;

    public int getStepX() {
        return this.normal.getX();
    }

    public int getStepY() {
        return this.normal.getY();
    }

    public int getStepZ() {
        return this.normal.getZ();
    }

    public Vector3f step() {
        return new Vector3f(getStepX(), getStepY(), getStepZ());
    }
}
