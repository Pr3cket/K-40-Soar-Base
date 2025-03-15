package com.lawab1ders.nan7i.kali.module.impl.player.mobends;

import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.Animation;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player.*;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.animation.player.*;
import com.lawab1ders.nan7i.kali.module.impl.player.mobends.client.renderer.entity.RenderBendsPlayer;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimatedEntity {

    public static final List<AnimatedEntity> animatedEntities = new ArrayList<>();
    public static final Map<Object, Object> skinMap = Maps.newHashMap();
    public static RenderBendsPlayer playerRenderer;
    public final String id;
    public final String displayName;
    public final Entity entity;
    public final Class<? extends Entity> entityClass;
    public final Render renderer;
    public final List<Animation> animations = new ArrayList<>();
    public boolean animate = true;

    public AnimatedEntity(String argID, String argDisplayName, Entity argEntity, Class<? extends Entity> argClass,
                          Render argRenderer) {
        this.id = argID;
        this.displayName = argDisplayName;
        this.entityClass = argClass;
        this.renderer = argRenderer;
        this.entity = argEntity;
    }

    public static void register() {
        animatedEntities.clear();
        registerEntity((new AnimatedEntity(
                "player", "Player", Minecraft.getMinecraft().thePlayer, EntityPlayer.class
                , new RenderBendsPlayer(Minecraft.getMinecraft().getRenderManager())
        )).add(new Animation_Stand()).add(new Animation_Walk()).add(new Animation_Sneak()).add(new Animation_Sprint())
          .add(new Animation_Jump()).add(new Animation_Attack()).add(new Animation_Swimming()).add(new Animation_Bow())
          .add(new Animation_Riding()).add(new Animation_Mining()).add(new Animation_Axe()));

        for (AnimatedEntity animatedEntity : animatedEntities) {
            if (animatedEntity.animate) {
                RenderingRegistry.registerEntityRenderingHandler(animatedEntity.entityClass, animatedEntity.renderer);
            }
        }

        playerRenderer = new RenderBendsPlayer(Minecraft.getMinecraft().getRenderManager());
        skinMap.put("default", playerRenderer);
        skinMap.put("slim", new RenderBendsPlayer(Minecraft.getMinecraft().getRenderManager(), true));
    }

    public static void registerEntity(AnimatedEntity argEntity) {
        animatedEntities.add(argEntity);
    }

    public static AnimatedEntity getByEntity(Entity argEntity) {
        for (AnimatedEntity animatedEntity : animatedEntities) {
            if (animatedEntity.entityClass.isInstance(argEntity)) {
                return animatedEntity;
            }
        }

        return null;
    }

    public static RenderBendsPlayer getPlayerRenderer(AbstractClientPlayer player) {
        String s = player.getSkinType();
        RenderBendsPlayer renderplayer = (RenderBendsPlayer) skinMap.get(s);
        return renderplayer != null ? renderplayer : playerRenderer;
    }

    public AnimatedEntity add(Animation argGroup) {
        this.animations.add(argGroup);
        return this;
    }

    public Animation get(String argName) {
        for (Animation animation : this.animations) {
            if (animation.getName().equalsIgnoreCase(argName)) {
                return animation;
            }
        }

        return null;
    }
}
