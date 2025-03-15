package com.lawab1ders.nan7i.kali.module.impl.player.mobends.util;

import org.lwjgl.util.vector.Vector3f;

public class GUtil {
    public static float max(float argNum, float argMax) {
        return Math.min(argNum, argMax);
    }

    public static Vector3f max(Vector3f argNum, float argMax) {
        if (argNum.x > argMax) {
            argNum.x = argMax;
        }

        if (argNum.y > argMax) {
            argNum.y = argMax;
        }

        if (argNum.z > argMax) {
            argNum.z = argMax;
        }

        return argNum;
    }

    public static float min(float argNum, float argMax) {
        return Math.max(argNum, argMax);
    }

    public static Vector3f translate(Vector3f num, Vector3f move) {
        num.x += move.x;
        num.y += move.y;
        num.z += move.z;
        return num;
    }

    public static Vector3f scale(Vector3f num, Vector3f move) {
        num.x *= move.x;
        num.y *= move.y;
        num.z *= move.z;
        return num;
    }

    public static Vector3f rotateX(Vector3f num, float rotation) {
        Vector3f y = new Vector3f();
        Vector3f z = new Vector3f();
        y.y = (float) Math.cos((double) ((180.0F + rotation) / 180.0F) * 3.141592653589793D);
        y.z = (float) Math.sin((double) ((180.0F + rotation) / 180.0F) * 3.141592653589793D);
        y.normalise();
        y.y *= -num.y;
        y.z *= num.y;
        z.y = (float) Math.sin((double) ((180.0F + rotation) / 180.0F) * 3.141592653589793D);
        z.z = (float) Math.cos((double) ((180.0F + rotation) / 180.0F) * 3.141592653589793D);
        z.normalise();
        z.y *= -num.z;
        z.z *= -num.z;
        num = new Vector3f(num.x, y.y + z.y, y.z + z.z);
        return num;
    }

    public static Vector3f rotateY(Vector3f num, float rotation) {
        Vector3f x = new Vector3f();
        Vector3f z = new Vector3f();
        x.x = (float) Math.cos((double) (-rotation / 180.0F) * 3.141592653589793D);
        x.z = (float) Math.sin((double) (-rotation / 180.0F) * 3.141592653589793D);
        x.normalise();
        x.x *= -num.x;
        x.z *= num.x;
        z.x = (float) Math.sin((double) (-rotation / 180.0F) * 3.141592653589793D);
        z.z = (float) Math.cos((double) (-rotation / 180.0F) * 3.141592653589793D);
        z.normalise();
        z.x *= num.z;
        z.z *= num.z;
        num = new Vector3f(x.x + z.x, num.y, x.z + z.z);
        return num;
    }

    public static Vector3f rotateZ(Vector3f num, float rotation) {
        Vector3f x = new Vector3f();
        Vector3f y = new Vector3f();
        x.x = (float) Math.sin((double) ((rotation - 90.0F) / 180.0F) * 3.141592653589793D);
        x.y = (float) Math.cos((double) ((rotation - 90.0F) / 180.0F) * 3.141592653589793D);
        x.normalise();
        x.x *= -num.x;
        x.y *= num.x;
        y.x = (float) Math.cos((double) ((rotation - 90.0F) / 180.0F) * 3.141592653589793D);
        y.y = (float) Math.sin((double) ((rotation - 90.0F) / 180.0F) * 3.141592653589793D);
        y.normalise();
        y.x *= -num.y;
        y.y *= -num.y;
        num = new Vector3f(y.x + x.x, y.y + x.y, num.z);
        return num;
    }

    public static Vector3f[] translate(Vector3f[] nums, Vector3f move) {
        for (int i = 0; i < nums.length; ++i) {
            nums[i] = translate(nums[i], move);
        }

        return nums;
    }

    public static Vector3f[] scale(Vector3f[] nums, Vector3f move) {
        for (int i = 0; i < nums.length; ++i) {
            nums[i] = scale(nums[i], move);
        }

        return nums;
    }

    public static Vector3f[] rotateX(Vector3f[] nums, float move) {
        for (int i = 0; i < nums.length; ++i) {
            nums[i] = rotateX(nums[i], move);
        }

        return nums;
    }

    public static Vector3f[] rotateY(Vector3f[] nums, float move) {
        for (int i = 0; i < nums.length; ++i) {
            nums[i] = rotateY(nums[i], move);
        }

        return nums;
    }

    public static Vector3f[] rotateZ(Vector3f[] nums, float move) {
        for (int i = 0; i < nums.length; ++i) {
            nums[i] = rotateZ(nums[i], move);
        }

        return nums;
    }
}
