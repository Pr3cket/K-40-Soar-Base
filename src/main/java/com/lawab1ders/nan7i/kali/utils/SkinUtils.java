package com.lawab1ders.nan7i.kali.utils;

import com.lawab1ders.nan7i.kali.injection.interfaces.IEntityPlayer;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.opengl.NativeImage;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.render.CustomizableModelPart;
import com.lawab1ders.nan7i.kali.module.impl.player.skin3d.render.SolidPixelWrapper;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

@UtilityClass
public class SkinUtils {

    public static boolean noCustomSkin(AbstractClientPlayer player) {
        return DefaultPlayerSkin.getDefaultSkin((player).getUniqueID()).equals((player).getLocationSkin());
    }

    private static NativeImage getSkinTexture(AbstractClientPlayer player) {
        return getTexture(player.getLocationSkin());
    }

    private static NativeImage getTexture(ResourceLocation resource) {

        NativeImage skin = new NativeImage(64, 64);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject abstractTexture = textureManager.getTexture(resource);

        if (abstractTexture == null) {
            return null;
        }

        GlStateManager.bindTexture(abstractTexture.getGlTextureId());
        skin.downloadTexture(0, false);
        return skin;
    }

    public static boolean setup3dLayers(AbstractClientPlayer abstractClientPlayerEntity, IEntityPlayer settings,
                                        boolean thinArms, ModelPlayer model) {

        if (noCustomSkin(abstractClientPlayerEntity)) {
            return false;
        }

        NativeImage skin = getSkinTexture(abstractClientPlayerEntity);

        if (skin == null) {
            return false;
        }

        CustomizableModelPart[] layers = new CustomizableModelPart[5];
        layers[0] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 0, 48, true, 0f);
        layers[1] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 0, 32, true, 0f);

        if (thinArms) {
            layers[2] = SolidPixelWrapper.wrapBox(skin, 3, 12, 4, 48, 48, true, -2.5f);
            layers[3] = SolidPixelWrapper.wrapBox(skin, 3, 12, 4, 40, 32, true, -2.5f);
        }
        else {
            layers[2] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 48, 48, true, -2.5f);
            layers[3] = SolidPixelWrapper.wrapBox(skin, 4, 12, 4, 40, 32, true, -2.5f);
        }

        layers[4] = SolidPixelWrapper.wrapBox(skin, 8, 12, 4, 16, 32, true, -0.8f);
        settings.k40$setSkinLayers(layers);
        settings.k40$setHeadLayers(SolidPixelWrapper.wrapBox(skin, 8, 8, 8, 32, 0, false, 0.6f));
        skin.close();

        return true;
    }
}
