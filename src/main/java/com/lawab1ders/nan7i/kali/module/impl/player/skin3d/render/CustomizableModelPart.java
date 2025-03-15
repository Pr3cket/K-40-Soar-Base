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

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import java.util.List;

public class CustomizableModelPart {

    private final List<CustomizableCube> cubes;
    public float x;
    public float y;
    public float z;
    public boolean visible = true;

    public CustomizableModelPart(List<CustomizableCube> list) {
        this.cubes = list;
    }

    public void copyFrom(ModelBox modelPart) {
        this.x = modelPart.posX1;
        this.y = modelPart.posY1;
        this.z = modelPart.posZ1;
    }

    public void setPos(float f, float g, float h) {
        this.x = f;
        this.y = g;
        this.z = h;
    }

    public void render(boolean redTint) {
        if (!this.visible)
            return;
        GlStateManager.pushMatrix();
        translateAndRotate();
        compile(redTint);
        GlStateManager.popMatrix();
    }

    public void translateAndRotate() {
        GlStateManager.translate((this.x / 16.0F), (this.y / 16.0F), (this.z / 16.0F));
    }

    private void compile(boolean redTint) {
        for (CustomizableCube cube : this.cubes) {
            cube.render(Tessellator.getInstance().getWorldRenderer(), redTint);
        }
    }
}