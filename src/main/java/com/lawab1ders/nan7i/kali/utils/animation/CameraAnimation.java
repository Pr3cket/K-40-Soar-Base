package com.lawab1ders.nan7i.kali.utils.animation;

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

public class CameraAnimation {

    public final double[] currents = new double[3];
    public final double[] targets = new double[3];

    public void setTargets(double x, double y, double z) {
        targets[0] = x;
        targets[1] = y;
        targets[2] = z;
    }

    public double[] animate(double speed) {
        currents[0] = animate(targets[0], currents[0], speed);
        currents[1] = animate(targets[1], currents[1], speed);
        currents[2] = animate(targets[2], currents[2], speed);

        // Set to target if finished
        if (this.isFinished(0)) currents[0] = targets[0];
        if (this.isFinished(1)) currents[1] = targets[1];
        if (this.isFinished(2)) currents[2] = targets[2];

        return new double[] { currents[0], currents[1], currents[2] };
    }

    private boolean isFinished(int index) {
        return Math.abs(currents[index] - targets[index]) < 0.01f;
    }

    private static double animate(double target, double current, double speed) {
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * Math.min(Math.max(speed, 0), 1.0);

        return target > current ? current + factor : current - factor;
    }
}
