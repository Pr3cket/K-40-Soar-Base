package com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath;

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

import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.matrix.AMtx3f;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.matrix.AMtx4f;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import com.google.common.collect.Queues;
import lombok.AllArgsConstructor;

import java.util.Deque;

public class PoseStack {

    private final Deque<Pose> poseStack;

    public PoseStack() {
        this.poseStack = Queues.newArrayDeque();
        AMtx4f AMtx4f = new AMtx4f();
        AMtx4f.setIdentity();
        AMtx3f AMtx3f = new AMtx3f();
        AMtx3f.setIdentity();
        poseStack.add(new Pose(AMtx4f, AMtx3f));
    }

    public void translate(double d, double e, double f) {
        Pose pose = this.poseStack.getLast();
        pose.pose.multiplyWithTranslation((float) d, (float) e, (float) f);
    }

    public void scale(float f, float g, float h) {
        Pose pose = this.poseStack.getLast();
        pose.pose.multiply(AMtx4f.createScaleMatrix(f, g, h));
        if (f == g && g == h) {
            if (f > 0.0F)
                return;
            pose.normal.mul(-1.0F);
        }
        float i = 1.0F / f;
        float j = 1.0F / g;
        float k = 1.0F / h;
        float l = MathUtils.fastInvCubeRoot(i * j * k);

        pose.normal.mul(AMtx3f.createScaleMatrix(l * i, l * j, l * k));
    }

    public void mulPose(AQuaternion quaternion) {
        Pose pose = this.poseStack.getLast();
        pose.pose.multiply(quaternion);
        pose.normal.mul(quaternion);
    }

    public void pushPose() {
        Pose pose = this.poseStack.getLast();
        this.poseStack.addLast(new Pose(pose.pose.copy(), pose.normal.copy()));
    }

    public void popPose() {
        this.poseStack.removeLast();
    }

    public Pose last() {
        return this.poseStack.getLast();
    }

    public boolean clear() {
        return (this.poseStack.size() == 1);
    }

    @AllArgsConstructor
    public static class Pose {

        public final AMtx4f pose;
        public final AMtx3f normal;
    }
}
