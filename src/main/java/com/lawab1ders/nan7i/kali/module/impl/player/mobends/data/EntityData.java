package com.lawab1ders.nan7i.kali.module.impl.player.mobends.data;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class EntityData {
    public final int entityID;
    public final String entityType;
    public final ModelBase model;
    public final Vector3f motion_prev = new Vector3f();
    public final Vector3f motion = new Vector3f();
    public Vector3f position = new Vector3f();
    public float ticks = 0.0F;
    public float ticksPerFrame = 0.0F;
    public boolean updatedThisFrame = false;
    public float ticksAfterLiftoff = 0.0F;
    public float ticksAfterTouchdown = 0.0F;
    public float ticksAfterPunch = 0.0F;
    public float ticksAfterThrowup = 0.0F;
    public boolean alreadyPunched = false;
    @Getter
    public boolean onGround;

    public EntityData(int argEntityID) {
        this.entityID = argEntityID;
        if (Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID) != null) {
            this.entityType = Minecraft.getMinecraft().theWorld.getEntityByID(argEntityID).getName();
        } else {
            this.entityType = "NULL";
        }

        this.model = null;
    }

    public boolean canBeUpdated() {
        return !this.updatedThisFrame;
    }

    public boolean calcOnGround() {
        Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID);
        if (entity == null) {
            return false;
        } else {
            List<AxisAlignedBB> list = entity.worldObj.getCollidingBoundingBoxes(entity,
                    entity.getEntityBoundingBox().addCoord(0.0D, -0.0010000000474974513D, 0.0D));
            int i = 0;
            return i < list.size();
        }
    }

    public void update(float argPartialTicks) {
        if (this.getEntity() != null) {
            this.ticksPerFrame = (float) Minecraft.getMinecraft().thePlayer.ticksExisted + argPartialTicks - this.ticks;
            this.ticks = (float) Minecraft.getMinecraft().thePlayer.ticksExisted + argPartialTicks;
            this.updatedThisFrame = false;
            if (this.calcOnGround() & !this.onGround) {
                this.onTouchdown();
                this.onGround = true;
            }

            if (!this.calcOnGround() & this.onGround | (this.motion_prev.y <= 0.0F && this.motion.y - this.motion_prev.y > 0.4F && this.ticksAfterLiftoff > 2.0F)) {
                this.onLiftoff();
                this.onGround = false;
            }

            if (this.getEntity().swingProgress > 0.0F) {
                if (!this.alreadyPunched) {
                    this.onPunch();
                    this.alreadyPunched = true;
                }
            } else {
                this.alreadyPunched = false;
            }

            if (this.motion_prev.y <= 0.0F && this.motion.y > 0.0F) {
                this.onThrowup();
            }

            if (!this.isOnGround()) {
                this.ticksAfterLiftoff += this.ticksPerFrame;
            }

            if (this.isOnGround()) {
                this.ticksAfterTouchdown += this.ticksPerFrame;
            }

            this.ticksAfterPunch += this.ticksPerFrame;
            this.ticksAfterThrowup += this.ticksPerFrame;
        }
    }

    public EntityLivingBase getEntity() {
        return Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID) instanceof EntityLivingBase ?
                (EntityLivingBase) Minecraft.getMinecraft().theWorld.getEntityByID(this.entityID) : null;
    }

    public void onTouchdown() {
        this.ticksAfterTouchdown = 0.0F;
    }

    public void onLiftoff() {
        this.ticksAfterLiftoff = 0.0F;
    }

    public void onThrowup() {
        this.ticksAfterThrowup = 0.0F;
    }

    public void onPunch() {
        this.ticksAfterPunch = 0.0F;
    }
}
