package com.lawab1ders.nan7i.kali.module.impl.render;

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

import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.events.ticks.ShaderTickedEvent;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinShaderGroup;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@IModule(
        name = "module.colorsaturation",
        description = "module.colorsaturation.desc",
        category = EModuleCategory.RENDER
)
public class ColorSaturationModule extends Module {

    public final FloatSetting hueSetting = new FloatSetting(
            "module.colorsaturation.opts.hue", 0, 0, 1.0F);

    public final FloatSetting saturationSetting = new FloatSetting(
            "module.colorsaturation.opts.saturation", 0.5F, 0, 1.0F);

    public final FloatSetting brightnessSetting = new FloatSetting(
            "module.colorsaturation.opts.brightness", 0.5F, 0, 1.0F);

    public final FloatSetting contrastSetting = new FloatSetting(
            "module.colorsaturation.opts.contrast", 0.5F, 0, 1.0F);

    private ShaderGroup group;
    private float prevHue;
    private float prevSaturation;
    private float prevBrightness;
    private float prevContrast;
    private int prevWidth, prevHeight;

    private final ResourceLocation colorSaturation = new ResourceLocation(
            "minecraft:shaders/post/colorsaturation.json");

    @SubscribeEvent
    public void onShaderTicked(ShaderTickedEvent event) {

        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        float hue = hueSetting.getValue();
        float saturation = saturationSetting.getValue();
        float brightness = brightnessSetting.getValue();
        float contrast = contrastSetting.getValue();

        if (group == null || prevWidth != sr.getScaledWidth() || prevHeight != sr.getScaledHeight()) {

            prevWidth = sr.getScaledWidth();
            prevHeight = sr.getScaledHeight();

            // 重置
            prevHue = -1; // 一个不可能的值

            try {
                group = new ShaderGroup(
                        mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(),
                        colorSaturation
                );
                group.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (prevHue != hue || prevSaturation != saturation || prevBrightness != brightness || prevContrast != contrast) {
            ((IMixinShaderGroup) group).k40$getListShaders().forEach((shader) -> {

                ShaderUniform hueUniform = shader.getShaderManager().getShaderUniform("hue");
                ShaderUniform contrastUniform = shader.getShaderManager().getShaderUniform("Contrast");
                ShaderUniform brightnessUniform = shader.getShaderManager().getShaderUniform("Brightness");
                ShaderUniform saturationUniform = shader.getShaderManager().getShaderUniform("Saturation");

                if (hueUniform != null) {
                    hueUniform.set(hue);
                }

                if (contrastUniform != null) {
                    contrastUniform.set(contrast);
                }

                if (brightnessUniform != null) {
                    brightnessUniform.set(brightness);
                }

                if (saturationUniform != null) {
                    saturationUniform.set(saturation);
                }
            });

            prevHue = hue;
            prevSaturation = saturation;
            prevBrightness = brightness;
            prevContrast = contrast;
        }

        event.getGroups().add(group);
    }
}
