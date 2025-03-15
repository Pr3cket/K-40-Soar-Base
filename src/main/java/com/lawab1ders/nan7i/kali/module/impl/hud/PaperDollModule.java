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
import com.lawab1ders.nan7i.kali.module.setting.impl.IntSetting;
import lombok.val;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;

@IModule(
        name = "module.paperdoll",
        description = "module.paperdoll.desc",
        category = EModuleCategory.HUD
)
public class PaperDollModule extends HUDModule {

    public final IntSetting yawOffsetSetting = new IntSetting("module.paperdoll.opts.yawoffset", 0, -90, 120);

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        this.setSize(30, 60);

        val _scale = from(scale);

        GlStateManager.enableColorMaterial();
        GlStateManager.enableDepth();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + from(15 * scale), y + from(58 * scale), -500.0F);
        GlStateManager.scale(-_scale * 30, _scale * 30, _scale * 30);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(mc.thePlayer.rotationYaw + yawOffsetSetting.getValue(), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        RenderManager rendermanager = mc.getRenderManager();

        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, event.getPartialTicks(), true);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }
}
