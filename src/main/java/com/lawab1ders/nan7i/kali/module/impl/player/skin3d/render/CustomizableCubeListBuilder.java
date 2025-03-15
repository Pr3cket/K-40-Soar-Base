package com.lawab1ders.nan7i.kali.module.impl.player.skin3d.render;

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

import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.SkinDirection;
import com.google.common.collect.Lists;

import java.util.List;

public class CustomizableCubeListBuilder {

    private final List<CustomizableCube> cubes = Lists.newArrayList();
    private int xTexOffs;
    private int yTexOffs;
    private boolean mirror;

    public static CustomizableCubeListBuilder create() {
        return new CustomizableCubeListBuilder();
    }

    public CustomizableCubeListBuilder texOffs(int i, int j) {
        this.xTexOffs = i;
        this.yTexOffs = j;
        return this;
    }

    public CustomizableCubeListBuilder mirror(boolean bl) {
        this.mirror = bl;
        return this;
    }

    public List<CustomizableCube> getCubes() {
        return cubes;
    }

    public CustomizableCubeListBuilder addBox(float x, float y, float z, float pixelSize, SkinDirection[] hide) {

        int textureSize = 64;
        this.cubes.add(
                new CustomizableCube(
                        xTexOffs, yTexOffs, x, y, z, pixelSize, pixelSize, pixelSize, 0, 0, 0, this.mirror, textureSize,
                        textureSize, hide
                ));

        return this;
    }

}