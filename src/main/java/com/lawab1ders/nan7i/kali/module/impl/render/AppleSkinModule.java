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

import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinFoodStats;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.render.appleskin.FoodHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@IModule(
        name = "module.appleskin",
        description = "module.appleskin.desc",
        category = EModuleCategory.RENDER,
        activated = true
)
public class AppleSkinModule extends Module {

    private static final ResourceLocation modIcons = new ResourceLocation("k40", "textures/icons.png");
    protected int foodIconsOffset;
    private float flashAlpha = 0f;
    private byte alphaDir = 1;

    public static void drawSaturationOverlay(float saturationGained, float saturationLevel, Minecraft mc, int left,
                                             int top, float alpha) {
        if (saturationLevel + saturationGained < 0) return;

        int startBar = saturationGained != 0 ? Math.max(0, (int) saturationLevel / 2) : 0;
        int endBar = (int) Math.ceil(Math.min(20, saturationLevel + saturationGained) / 2f);
        int barsNeeded = endBar - startBar;
        mc.getTextureManager().bindTexture(modIcons);

        enableAlpha(alpha);
        for (int i = startBar; i < startBar + barsNeeded; ++i) {
            int x = left - i * 8 - 9;
            int y = top;
            float effectiveSaturationOfBar = (saturationLevel + saturationGained) / 2 - i;

            if (effectiveSaturationOfBar >= 1) mc.ingameGUI.drawTexturedModalRect(x, y, 27, 0, 9, 9);
            else if (effectiveSaturationOfBar > .5) mc.ingameGUI.drawTexturedModalRect(x, y, 18, 0, 9, 9);
            else if (effectiveSaturationOfBar > .25) mc.ingameGUI.drawTexturedModalRect(x, y, 9, 0, 9, 9);
            else if (effectiveSaturationOfBar > 0) mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, 9, 9);
        }

        disableAlpha(alpha);

        // rebind default icons
        mc.getTextureManager().bindTexture(Gui.icons);
    }

    public static void drawHungerOverlay(int hungerRestored, int foodLevel, Minecraft mc, int left, int top,
                                         float alpha) {
        if (hungerRestored == 0) return;

        int startBar = foodLevel / 2;
        int endBar = (int) Math.ceil(Math.min(20, foodLevel + hungerRestored) / 2f);
        int barsNeeded = endBar - startBar;
        mc.getTextureManager().bindTexture(Gui.icons);

        enableAlpha(alpha);

        for (int i = startBar; i < startBar + barsNeeded; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            int icon = 16;
            int background = 13;

            if (mc.thePlayer.isPotionActive(Potion.hunger)) {
                icon += 36;
            }

            mc.ingameGUI.drawTexturedModalRect(x, y, 16 + background * 9, 27, 9, 9);

            if (idx < foodLevel + hungerRestored) mc.ingameGUI.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
            else if (idx == foodLevel + hungerRestored) mc.ingameGUI.drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
        }
        disableAlpha(alpha);
    }

    public static void drawExhaustionOverlay(float exhaustion, Minecraft mc, int left, int top, float alpha) {
        mc.getTextureManager().bindTexture(modIcons);

        float maxExhaustion = 4F;
        // clamp between 0 and 1
        float ratio = Math.min(1, Math.max(0, exhaustion / maxExhaustion));
        int width = (int) (ratio * 81);
        int height = 9;

        enableAlpha(.75f);
        mc.ingameGUI.drawTexturedModalRect(left - width, top, 81 - width, 18, width, height);
        disableAlpha(.75f);

        // rebind default icons
        mc.getTextureManager().bindTexture(Gui.icons);
    }

    public static void enableAlpha(float alpha) {
        GlStateManager.enableBlend();

        if (alpha == 1f) return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableAlpha(float alpha) {
        GlStateManager.disableBlend();

        if (alpha == 1f) return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickedEvent event) {
        flashAlpha += alphaDir * 0.125f;

        if (flashAlpha >= 1.5f) {
            flashAlpha = 1f;
            alphaDir = -1;
        } else if (flashAlpha <= -0.5f) {
            flashAlpha = 0f;
            alphaDir = 1;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPreRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.FOOD) return;

        foodIconsOffset = GuiIngameForge.right_height;

        if (event.isCanceled()) return;

        ScaledResolution scale = event.resolution;

        int left = scale.getScaledWidth() / 2 + 91;
        int top = scale.getScaledHeight() - foodIconsOffset;

        drawExhaustionOverlay(
                ((IMixinFoodStats) mc.thePlayer.getFoodStats()).k40$getFoodExhaustionLevel(), mc, left, top, 1f);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPostRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.FOOD) return;

        if (event.isCanceled()) return;

        EntityPlayer player = mc.thePlayer;
        ItemStack heldItem = player.getHeldItem();
        FoodStats stats = player.getFoodStats();
        ScaledResolution scale = event.resolution;

        int left = scale.getScaledWidth() / 2 + 91;
        int top = scale.getScaledHeight() - foodIconsOffset;

        // saturation overlay
        drawSaturationOverlay(0, stats.getSaturationLevel(), mc, left, top, 1f);

        if (heldItem == null || !FoodHelper.isFood(heldItem)) {
            flashAlpha = 0;
            alphaDir = 1;
            return;
        }

        // restored hunger/saturation overlay while holding food
        FoodHelper.BasicFoodValues foodValues = FoodHelper.getModifiedFoodValues(heldItem, player);

        drawHungerOverlay(foodValues.hunger, stats.getFoodLevel(), mc, left, top, flashAlpha);

        int newFoodValue = stats.getFoodLevel() + foodValues.hunger;
        float newSaturationValue = stats.getSaturationLevel() + foodValues.getSaturationIncrement();
        drawSaturationOverlay(newSaturationValue > newFoodValue ? newFoodValue - stats.getSaturationLevel() :
                foodValues.getSaturationIncrement(), stats.getSaturationLevel(), mc, left, top, flashAlpha);
    }
}
