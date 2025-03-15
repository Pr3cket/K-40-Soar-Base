package com.lawab1ders.nan7i.kali.utils.animation.interpolatable;

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

import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public abstract class InterpolatableAnimation {

    protected final MillisTimer timer = new MillisTimer();

    @Setter
    protected double endPoint;
    protected final int duration;
    protected Direction direction;

    public InterpolatableAnimation(double endPoint, int ms) {
        this(endPoint, ms, Direction.FORWARDS);
        reset();
    }

    public boolean isDone(Direction direction) {
        return isDone() && this.direction.equals(direction);
    }

    public boolean isDone() {
        return timer.delay(duration);
    }

    public void reset() {
        timer.reset();
    }

    public float getValue() {
        if (direction == Direction.FORWARDS) {
            if (isDone()) return (float) endPoint;
            return (float) (getEquation(timer.getElapsedTime()) * endPoint);

        } else {
            if (isDone()) return 0;

            if (correctOutput()) {
                double revTime = Math.min(duration, Math.max(0, duration - timer.getElapsedTime()));
                return (float) (getEquation(revTime) * endPoint);

            } else {
                return (float) ((1 - getEquation(timer.getElapsedTime())) * endPoint);
            }
        }
    }

    public void setValue(double value) {
        if (value >= 0 && value <= 1) {
            this.endPoint = value;
            long elapsedTime = (long) ((1 - value) * duration);
            timer.setLastMs(System.currentTimeMillis() - (duration - Math.min(duration, elapsedTime)));
        }
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            timer.setLastMs(System.currentTimeMillis() - (duration - Math.min(duration, timer.getElapsedTime())));
        }
    }

    protected abstract double getEquation(double x);

    protected boolean correctOutput() {
        return false;
    }
}
