package com.lawab1ders.nan7i.kali.module.impl.player.mobends.util;

import org.lwjgl.util.vector.Vector3f;

public class SmoothVector3f {
    public Vector3f vOld = new Vector3f(0.0F, 0.0F, 0.0F);
    public Vector3f vSmooth = new Vector3f(0.0F, 0.0F, 0.0F);
    public Vector3f vFinal = new Vector3f(0.0F, 0.0F, 0.0F);
    public Vector3f smoothness = new Vector3f(1.0F, 1.0F, 1.0F);
    public Vector3f completion = new Vector3f(0.0F, 0.0F, 0.0F);

    public void setSmooth(Vector3f argRotation, float argSmooth) {
        if (this.vFinal != argRotation) {
            this.vFinal = argRotation;
            this.vOld = this.vSmooth;
            this.completion = new Vector3f(0.0F, 0.0F, 0.0F);
            this.smoothness = new Vector3f(argSmooth, argSmooth, argSmooth);
        }

    }

    public void setSmoothZero(float argSmooth) {
        this.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), argSmooth);
    }

    public void setSmooth(EnumAxis argAxis, float argRot, float argSmooth) {
        if ((argAxis == EnumAxis.X ? this.vFinal.x : (argAxis == EnumAxis.Y ? this.vFinal.y : this.vFinal.z)) != argRot) {
            if (argAxis == EnumAxis.X) {
                this.vFinal.x = argRot;
            }

            if (argAxis == EnumAxis.Y) {
                this.vFinal.y = argRot;
            }

            if (argAxis == EnumAxis.Z) {
                this.vFinal.z = argRot;
            }

            if (argAxis == EnumAxis.X) {
                this.vOld.x = this.vSmooth.x;
            }

            if (argAxis == EnumAxis.Y) {
                this.vOld.y = this.vSmooth.y;
            }

            if (argAxis == EnumAxis.Z) {
                this.vOld.z = this.vSmooth.z;
            }

            if (argAxis == EnumAxis.X) {
                this.completion.x = 0.0F;
            }

            if (argAxis == EnumAxis.Y) {
                this.completion.y = 0.0F;
            }

            if (argAxis == EnumAxis.Z) {
                this.completion.z = 0.0F;
            }
        }

        if (argAxis == EnumAxis.X) {
            this.smoothness.x = argSmooth;
        }

        if (argAxis == EnumAxis.Y) {
            this.smoothness.y = argSmooth;
        }

        if (argAxis == EnumAxis.Z) {
            this.smoothness.z = argSmooth;
        }

    }

    public void setSmoothX(float argX, float argSmooth) {
        if (this.vFinal.x != argX) {
            this.vFinal.x = argX;
            this.vOld.x = this.vSmooth.x;
            this.completion.x = 0.0F;
        }

        this.smoothness.x = argSmooth;
    }

    public void setSmoothY(float argY, float argSmooth) {
        if (this.vFinal.y != argY) {
            this.vFinal.y = argY;
            this.vOld.y = this.vSmooth.y;
            this.completion.y = 0.0F;
        }

        this.smoothness.y = argSmooth;
    }

    public void setSmoothZ(float argZ, float argSmooth) {
        if (this.vFinal.z != argZ) {
            this.vFinal.z = argZ;
            this.vOld.z = this.vSmooth.z;
            this.completion.z = 0.0F;
        }

        this.smoothness.z = argSmooth;
    }

    public void setSmoothX(float argX) {
        this.setSmoothX(argX, 0.6F);
    }

    public void setSmoothY(float argY) {
        this.setSmoothY(argY, 0.6F);
    }

    public void setSmoothZ(float argZ) {
        this.setSmoothZ(argZ, 0.6F);
    }

    public void set(SmoothVector3f argV) {
        this.completion = argV.completion;
        this.smoothness = argV.smoothness;
        this.vFinal = argV.vFinal;
        this.vOld = argV.vOld;
        this.vSmooth = argV.vSmooth;
    }

    public float getX() {
        return this.vSmooth.x;
    }

    public void setX(float argX) {
        this.vOld.x = argX;
        this.vSmooth.x = argX;
        this.vFinal.x = argX;
        this.completion.x = 1.0F;
    }

    public float getY() {
        return this.vSmooth.y;
    }

    public void setY(float argY) {
        this.vOld.y = argY;
        this.vSmooth.y = argY;
        this.vFinal.y = argY;
        this.completion.y = 1.0F;
    }

    public float getZ() {
        return this.vSmooth.z;
    }

    public void setZ(float argZ) {
        this.vOld.z = argZ;
        this.vSmooth.z = argZ;
        this.vFinal.z = argZ;
        this.completion.z = 1.0F;
    }

    public void update(float argTicksPerFrame) {
        Vector3f var10000 = this.completion;
        var10000.x += argTicksPerFrame * this.smoothness.x;
        var10000.y += argTicksPerFrame * this.smoothness.y;
        var10000.z += argTicksPerFrame * this.smoothness.z;
        GUtil.max(this.completion, 1.0F);
        if (this.completion.x >= 1.0F) {
            this.vOld.x = this.vSmooth.x = this.vFinal.x;
        } else {
            this.vSmooth.x = this.vOld.x + (this.vFinal.x - this.vOld.x) * this.completion.x;
        }

        if (this.completion.y >= 1.0F) {
            this.vOld.y = this.vSmooth.y = this.vFinal.y;
        } else {
            this.vSmooth.y = this.vOld.y + (this.vFinal.y - this.vOld.y) * this.completion.y;
        }

        if (this.completion.z >= 1.0F) {
            this.vOld.z = this.vSmooth.z = this.vFinal.z;
        } else {
            this.vSmooth.z = this.vOld.z + (this.vFinal.z - this.vOld.z) * this.completion.z;
        }

        this.setSmooth(new Vector3f(0.0F, 0.0F, 0.0F), 0.5F);
    }
}
