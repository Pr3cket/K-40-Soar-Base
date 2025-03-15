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
        name = "module.motionblur",
        description = "module.motionblur.desc",
        category = EModuleCategory.RENDER
)
public class MotionBlurModule extends Module {

    public final FloatSetting amountSetting = new FloatSetting(
            "module.motionblur.opts.amount", 0.3F, 0.2F, 0.9F);

    private final ResourceLocation motionBlur = new ResourceLocation("minecraft:shaders/post/motion_blur.json");
    private ShaderGroup group;
    private float preAmount;
    private int prevWidth, prevHeight;

    @SubscribeEvent
    public void onShaderTicked(ShaderTickedEvent event) {

        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        float amount = amountSetting.getValue();

        if (group == null || prevWidth != sr.getScaledWidth() || prevHeight != sr.getScaledHeight()) {

            prevWidth = sr.getScaledWidth();
            prevHeight = sr.getScaledHeight();

            // 重置
            preAmount = -1; // 一个不可能的值

            try {
                group = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(),
                        motionBlur);
                group.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (preAmount != amount) {
            ((IMixinShaderGroup) group).k40$getListShaders().forEach((shader) -> {
                ShaderUniform factor = shader.getShaderManager().getShaderUniform("BlurFactor");

                if (factor != null) {
                    factor.set(amount);
                }
            });

            preAmount = amount;
        }

        event.getGroups().add(group);
    }
}
