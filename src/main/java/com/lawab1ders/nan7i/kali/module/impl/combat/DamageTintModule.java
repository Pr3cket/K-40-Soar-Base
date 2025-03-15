package com.lawab1ders.nan7i.kali.module.impl.combat;

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
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;

@IModule(
        name = "module.damagetint",
        description = "module.damagetint.desc",
        category = EModuleCategory.COMBAT
)
public class DamageTintModule extends Module {

    public final IntSetting healthSetting = new IntSetting("module.damagetint.opts.health", 10, 5, 16);

    private final ResourceLocation shape = new ResourceLocation("k40", "textures/misc/shape.png");
    private final Animation animation = new Animation(0.0F);

    public void render() {

        float threshold = healthSetting.getValue();

        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());

        if (playerInfo != null &&
                (playerInfo.getGameType() == WorldSettings.GameType.CREATIVE || playerInfo.getGameType() == WorldSettings.GameType.SPECTATOR)) {
            return;
        }

        animation.setAnimation(mc.thePlayer.getHealth() <= threshold ? 1.0F : 0.0F, 10);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);

        GlStateManager.color(0F, animation.getValue(), animation.getValue(), animation.getValue());
        mc.getTextureManager().bindTexture(shape);
        Tessellator tes = Tessellator.getInstance();
        WorldRenderer wr = tes.getWorldRenderer();

        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.pos(0.0D, sr.getScaledHeight_double(), -90.0D).tex(0.0D, 1.0D).endVertex();
        wr.pos(sr.getScaledWidth_double(), sr.getScaledHeight_double(), -90.0D).tex(1.0D, 1.0D).endVertex();
        wr.pos(sr.getScaledWidth_double(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        wr.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tes.draw();

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}
