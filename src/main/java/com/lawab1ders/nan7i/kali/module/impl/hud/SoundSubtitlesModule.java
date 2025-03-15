package com.lawab1ders.nan7i.kali.module.impl.hud;

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

import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.impl.hud.subtitle.Subtitle;
import com.lawab1ders.nan7i.kali.module.impl.hud.subtitle.SubtitlesReader;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@IModule(
        name = "module.soundsubtitles",
        description = "module.soundsubtitles.desc",
        category = EModuleCategory.HUD
)
public class SoundSubtitlesModule extends HUDModule {

    public final IntSetting maxSetting = new IntSetting("module.soundsubtitles.opts.max", 3, 3, 10);

    private final SubtitlesReader reader = new SubtitlesReader();
    private final Animation backgroundAnimation = new Animation(0.0F);

    private final List<Subtitle> subtitles = new ArrayList<>();

    public SoundSubtitlesModule() {
        super();

        subtitles.add(new Subtitle("ambient.weather.rain"));
        subtitles.add(new Subtitle("random.burp"));
        subtitles.add(new Subtitle("step.grass"));
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        postNotificationOnlyOnce("module.soundsubtitles.noti", NotificationType.WARNING);
        nvg.setupAndDraw(this::drawNanoVG);
    }

    private void drawNanoVG() {

        Vec3 Vec3 = new Vec3(
                mc.thePlayer.posX,
                mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(),
                mc.thePlayer.posZ
        );

        Vec3 Vec31 =
                (new Vec3(0.0D, 0.0D, -1.0D)).rotatePitch(-mc.thePlayer.rotationPitch * 0.017453292F).rotateYaw(-mc.thePlayer.rotationYaw * 0.017453292F);

        Vec3 Vec32 =
                (new Vec3(0.0D, 1.0D, 0.0D)).rotatePitch(-mc.thePlayer.rotationPitch * 0.017453292F).rotateYaw(-mc.thePlayer.rotationYaw * 0.017453292F);

        Vec3 Vec33 = Vec31.crossProduct(Vec32);

        int subtitleWidth = 120;
        int subtitleHeight = (isEditing() ? 3 : subtitles.size() - 3) * 16;

        ArrayList<Subtitle> removeList = new ArrayList<>();

        for (Subtitle subtitle : subtitles) {

            if (subtitle.isFake()) continue;

            if (subtitle.getStartTime() + 3000L <= Minecraft.getSystemTime()) {
                subtitle.setRemove(true);
            }

            if (subtitle.isRemove() && subtitle.isDone()) {
                removeList.add(subtitle);
            }
        }

        subtitles.removeAll(removeList);

        backgroundAnimation.setAnimation(subtitleHeight, 20);

        if (backgroundAnimation.getValue() > 1) {
            this.drawBackground(subtitleWidth, backgroundAnimation.getValue());

            int index = 1;

            for (Subtitle subtitle : subtitles) {

                if (isEditing() && index > 3) break;
                if (!isEditing() && subtitle.isFake()) continue;

                Vec3 Vec34 = subtitle.getLocation().subtract(Vec3).normalize();
                double d0 = -Vec33.dotProduct(Vec34);
                double d1 = -Vec31.dotProduct(Vec34);
                boolean flag = d1 > 0.5D;

                subtitle.getAnimation().setAnimation(subtitle.isRemove() ? 0 : 1, 17);

                if (subtitle.getAnimation().getValue() < 0.1 && subtitle.isRemove()) {
                    subtitle.setDone(true);
                }

                int opacity = (int) (subtitle.getAnimation().getValue() * 255);
                float animationOffsetY =
                        (((index - 2) * 16) + subtitle.getAnimation().getValue() * 16);

                if (index == 1) {
                    animationOffsetY = 0;
                }

                this.drawCenteredText(getSoundName(subtitle.getString()), (float) subtitleWidth / 2,
                        animationOffsetY + 4, 9, Fonts.REGULAR, this.getFontColor(opacity));

                if (!flag && !subtitle.isFake()) {
                    if (d0 > 0.0D) {
                        this.drawText(">", subtitleWidth - nvg.getTextWidth("<", 9, Fonts.REGULAR) - 4.5F,
                                animationOffsetY + 4.5F, 9, Fonts.REGULAR, this.getFontColor(opacity));
                    } else if (d0 < 0.0D) {
                        this.drawText("<", 4.5F, animationOffsetY + 4.5F, 9, Fonts.REGULAR, this.getFontColor(opacity));
                    }
                }

                index++;
            }
        }
    }

    public void soundPlay(ISound soundIn) {

        if (subtitles.size() - (isEditing() ? 0 : 3) >= maxSetting.getValue()) {
            return;
        }

        String resourcePath = soundIn.getSoundLocation().getResourcePath();

        if (resourcePath.contains("gui.") || resourcePath.contains("music.")) {
            return;
        }

        for (Subtitle subtitle : subtitles) {
            if (subtitle.isFake()) continue;

            if (subtitle.getString().equals(resourcePath) || getSoundName(subtitle.getString()).equals(getSoundName(resourcePath))) {
                subtitle.refresh(new Vec3(soundIn.getXPosF(), soundIn.getYPosF(), soundIn.getZPosF()));
                return;
            }
        }

        this.subtitles.add(new Subtitle(resourcePath, new Vec3(soundIn.getXPosF(), soundIn.getYPosF(),
                soundIn.getZPosF())));
    }

    private String getSoundName(String location) {
        String sound = reader.getSoundsMap().get(location);

        // 使用代码获取 Map
        val languageCode = mc.getLanguageManager().getCurrentLanguage().getLanguageCode();

        if (languageCode == null) {
            return getLimitSoundName(sound);
        }

        Map<String, String> map = reader.getSubtitlesMap().get(languageCode);

        if (map == null) {
            return getLimitSoundName(sound);
        }

        // 120 时背景宽度
        return getLimitSoundName(sound == null ? location : map.get(sound) == null ? sound : map.get(sound));
    }

    private String getLimitSoundName(String sound) {
        return nvg.getLimitText(sound, 9, Fonts.REGULAR, 120 * 0.75F);
    }
}
