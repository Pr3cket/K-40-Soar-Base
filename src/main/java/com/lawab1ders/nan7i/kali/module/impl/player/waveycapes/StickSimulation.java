package com.lawab1ders.nan7i.kali.module.impl.player.waveycapes;

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
import com.lawab1ders.nan7i.kali.module.impl.player.WaveyCapesModule;
import com.lawab1ders.nan7i.kali.module.impl.player.waveycapes.amath.vec.AVec2f;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class StickSimulation {

    private static final float MAX_BEND = 5;

    public List<Point> points = new ArrayList<>();
    public List<Stick> sticks = new ArrayList<>();

    public float gravity = 20f;
    public int numIterations = 30;

    public void simulate() {
        gravity = InstanceAccess.mod.getModule(WaveyCapesModule.class).gravitySetting.getValue();

        float deltaTime = 50f / 1000f;

        AVec2f down = new AVec2f(0, gravity * deltaTime);
        AVec2f tmp = new AVec2f(0, 0);
        for (Point p : points) {
            if (!p.locked) {
                tmp.copyFrom(p.position);
                p.position.subtract(down);
                p.prevPosition.copyFrom(tmp);
            }
        }

        Point basePoint = points.get(0);
        for (Point p : points) {
            if (p != basePoint && p.position.x - basePoint.position.x > 0) {
                p.position.x = basePoint.position.x - 0.1f;
            }
        }

        for (int i = points.size() - 2; i >= 1; i--) {
            double angle = getAngle(points.get(i).position, points.get(i - 1).position, points.get(i + 1).position);
            angle *= 57.2958;
            if (angle > 360) {
                angle -= 360;
            }
            if (angle < -360) {
                angle += 360;
            }
            double abs = Math.abs(angle);
            if (abs < 180 - MAX_BEND) {
                points.get(i + 1).position = getReplacement(
                        points.get(i).position, points.get(i - 1).position, angle,
                        180 - MAX_BEND + 1
                );
            }
            if (abs > 180 + MAX_BEND) {
                points.get(i + 1).position = getReplacement(
                        points.get(i).position, points.get(i - 1).position, angle,
                        180 + MAX_BEND - 1
                );
            }
        }

        for (int i = 0; i < numIterations; i++) {
            for (int x = sticks.size() - 1; x >= 0; x--) {
                Stick stick = sticks.get(x);
                AVec2f stickCentre = stick.pointA.position.copy().add(stick.pointB.position).div(2);
                AVec2f stickDir = stick.pointA.position.copy().subtract(stick.pointB.position).normalize();
                if (!stick.pointA.locked) {
                    stick.pointA.position = stickCentre.copy().add(stickDir.copy().mul(stick.length / 2));
                }
                if (!stick.pointB.locked) {
                    stick.pointB.position = stickCentre.copy().subtract(stickDir.copy().mul(stick.length / 2));
                }
            }
        }

        for (Stick stick : sticks) {
            AVec2f stickDir = stick.pointA.position.copy().subtract(stick.pointB.position).normalize();
            if (!stick.pointB.locked) {
                stick.pointB.position = stick.pointA.position.copy().subtract(stickDir.mul(stick.length));
            }
        }
    }

    private AVec2f getReplacement(AVec2f middle, AVec2f prev, double angle, double target) {
        double theta = target / 57.2958;
        float x = prev.x - middle.x;
        float y = prev.y - middle.y;

        if (angle < 0) {
            theta *= -1;
        }

        double cs = Math.cos(theta);
        double sn = Math.sin(theta);
        return new AVec2f((float) ((x * cs) - (y * sn) + middle.x), (float) ((x * sn) + (y * cs) + middle.y));
    }

    private double getAngle(AVec2f middle, AVec2f prev, AVec2f next) {
        return Math.atan2(next.y - middle.y, next.x - middle.x) - Math.atan2(prev.y - middle.y, prev.x - middle.x);
    }

    public static class Point {

        public AVec2f position = new AVec2f(0, 0);
        public AVec2f prevPosition = new AVec2f(0, 0);
        public boolean locked;

        public float getLerpX(float delta) {
            return MathUtils.lerp(delta, prevPosition.x, position.x);
        }

        public float getLerpY(float delta) {
            return MathUtils.lerp(delta, prevPosition.y, position.y);
        }
    }

    @AllArgsConstructor
    public static class Stick {

        public Point pointA, pointB;
        public float length;
    }
}