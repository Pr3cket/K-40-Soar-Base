package com.lawab1ders.nan7i.kali.module;

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
import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.events.OverlayRenderedEvent;
import com.lawab1ders.nan7i.kali.gui.GuiEditHUD;
import com.lawab1ders.nan7i.kali.KaliProcessor;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.nanovg.NVGFont;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@Getter
public abstract class HUDModule extends Module {

    private final EnumSetting gravityHorizontalSetting = new EnumSetting(
            "gravity.horizontal",
            "gravity.center",
            "gravity.horizontal.left", "gravity.horizontal.right"
    ).setChangeListener((oldValue, newValue) -> this.updatePosAndSize());

    private final EnumSetting gravityVerticalSetting = new EnumSetting(
            "gravity.vertical",
            "gravity.center",
            "gravity.vertical.top", "gravity.vertical.bottom"
    ).setChangeListener((oldValue, newValue) -> this.updatePosAndSize());

    public static ScaledResolution sr;

    @Setter
    protected boolean dragging = false;

    @Setter
    protected float draggingX, draggingY;
    protected float x, y, width, height;
    protected float scale = 3;

    @Setter
    private float minScale = 1.5F, maxScale = 3.5F;

    // 虚拟的
    @Getter
    protected float vX, vY, vWidth, vHeight;

    private static final int TOTAL_C = 1024;
    private int screenWidth, screenHeight;
    private double ratio;

    @Setter
    private int layer;

    protected HUDModule() {
        super();

        this.layer = Module.hudLayer;
        Module.hudLayer++;
    }

    public void updatePosAndSize() {
        screenWidth = sr.getScaledWidth();
        screenHeight = sr.getScaledHeight();
        ratio = Math.min(sr.getScaledWidth_double(), sr.getScaledHeight_double()) / TOTAL_C;

        setSize(vWidth, vHeight);
        setVX(vX);
        setVY(vY);
    }

    public abstract void onOverlayRendered(OverlayRenderedEvent event);

    //---------------------------------------------------------------------------
    // Renderers
    //---------------------------------------------------------------------------

    /// Image

    protected void drawPlayerHead(ResourceLocation location, float addX, float addY, float width, float height,
                                  float radius) {

        // 第一遍只是走个形式，把宽高还有变量什么的先算出来
        if (width == 0 || height == 0) return;

        nvg.drawPlayerHead(
                location, x + from(addX * scale), y + from(addY * scale),
                from(width * scale), from(height * scale), from(radius * scale)
        );
    }

    /// Arc

    protected void drawArc(float addX, float addY, float radius, float startAngle, float endAngle, float strokeWidth) {
        drawArc(addX, addY, radius, startAngle, endAngle, strokeWidth, this.getFontColor());
    }

    protected void drawArc(float addX, float addY, float radius, float startAngle, float endAngle, float strokeWidth,
                           Color color) {
        if (width == 0 || height == 0) return;

        nvg.drawArc(
                x + from(addX * scale), y + from(addY * scale),
                from(radius * scale), startAngle, endAngle, from(strokeWidth * scale), color
        );
    }

    /// Rect

    protected void drawRect(float addX, float addY, float width, float height) {
        drawRect(addX, addY, width, height, this.getFontColor());
    }

    protected void drawRect(float addX, float addY, float width, float height, Color color) {
        if (width == 0 || height == 0) return;

        nvg.drawRect(
                x + from(addX * scale), y + from(addY * scale),
                from(width * scale), from(height * scale), color
        );
    }

    /// Rounded Rect

    protected void drawRoundedRect(float addX, float addY, float width, float height, float radius) {
        drawRoundedRect(addX, addY, width, height, radius, this.getFontColor());
    }

    protected void drawRoundedRect(float addX, float addY, float width, float height, float radius, Color color) {
        if (width == 0 || height == 0) return;

        nvg.drawRoundedRect(
                x + from(addX * scale), y + from(addY * scale),
                from(width * scale), from(height * scale), from(radius * scale), color
        );
    }

    /// Background

    protected void drawBackground(float width, float height) {
        setSize(width, height);

        drawBackground(width, height, 6 * scale);
    }

    protected void drawBackground(float width, float height, float radius) {
        drawBackground(0, 0, width, height, radius);
    }

    protected void drawBackground(float addX, float addY, float width, float height) {
        drawBackground(addX, addY, width, height, 6 * scale);
    }

    protected void drawBackground(float addX, float addY, float width, float height, float rd) {
        if (width == 0 || height == 0) return;

        AccentColor currentColor = col.getCurrentColor();
        EnumSetting theme = KaliAPI.INSTANCE.getHudThemeSetting();

        boolean isNormal = theme.getSelectedEntryKey().equals("gui.appearance.opts.hudtheme.normal");
        boolean isVanilla = theme.getSelectedEntryKey().equals("gui.appearance.opts.hudtheme.vanilla");
        boolean isGlow = theme.getSelectedEntryKey().equals("gui.appearance.opts.hudtheme.glow");
        boolean isOutline = theme.getSelectedEntryKey().equals("gui.appearance.opts.hudtheme.outline");
        boolean isShadow = theme.getSelectedEntryKey().equals("gui.appearance.opts.hudtheme.shadow");

        float lastWidth = from(width * scale);
        float lastHeight = from(height * scale);
        float x = this.x + from(addX * scale);
        float y = this.y + from(addY * scale);
        float radius = from(rd);

        val _1 = from(1 * scale);
        val _2 = from(2 * scale);
        val _4 = from(4 * scale);

        //        System.out.println("X: " + x + ", Y: " + y + ", Width: " + lastWidth + ", Height: " + lastHeight);

        if (KaliProcessor.BLUR_TICK) {
            // 由于 NanoVG 暂时没有高斯模糊的方法，所以只能用传统的着色器渲染方法
            sha.getBlurRunnables()
               .add(() -> sha.getRqShader().round(x, y, lastWidth, lastHeight, radius, 0, 0, 0, 255));
        }
        else {
            if (isNormal || isVanilla || isShadow) {
                nvg.drawShadow(x, y, lastWidth, lastHeight, radius, _1);

            }
            else if (isGlow) {
                nvg.drawGradientShadow(
                        x, y, lastWidth, lastHeight, radius, _1, currentColor.getColor1(),
                        currentColor.getColor2()
                );

            }
            else if (isOutline) {
                nvg.drawShadow(x - _2, y - _2, lastWidth + _4, lastHeight + _4, radius + _2, _1);
                nvg.drawGradientOutlineRoundedRect(
                        x - _1, y - _1, lastWidth + _2, lastHeight + _2, radius + _1, from(1.5F * scale),
                        currentColor.getColor1(), currentColor.getColor2()
                );
            }

            if (isVanilla || isOutline) {
                nvg.drawRoundedRect(x, y, lastWidth, lastHeight, radius, new Color(0, 0, 0, 100));

            }
            else if (isNormal || isGlow) {
                nvg.drawGradientRoundedRect(
                        x, y, lastWidth, lastHeight, radius,
                        ColorUtils.applyAlpha(currentColor.getColor1(), 220),
                        ColorUtils.applyAlpha(currentColor.getColor2(), 220)
                );
            }
        }
    }

    /// Text

    protected void drawText(String text, float addX, float addY, float size, NVGFont NVGFont) {
        drawText(text, addX, addY, size, NVGFont, getFontColor());
    }

    protected void drawText(String text, float addX, float addY, float size, NVGFont NVGFont, Color color) {
        if (width == 0 || height == 0) return;

        nvg.drawText(text, x + from(addX * scale), y + from(addY * scale), color, from(size * scale), NVGFont);
    }

    protected void drawCenteredText(String text, float addX, float addY, float size, NVGFont NVGFont) {
        drawCenteredText(text, addX, addY, size, NVGFont, getFontColor());
    }

    protected void drawCenteredText(String text, float addX, float addY, float size, NVGFont NVGFont, Color color) {
        if (width == 0 || height == 0) return;

        nvg.drawCenteredText(text, x + from(addX * scale), y + from(addY * scale), color, from(size * scale), NVGFont);
    }

    protected void scale(float addX, float addY, float width, float height, float nvgScale) {
        nvg.scale(
                x + from(addX * scale), y + from(addY * scale),
                from(width * scale), from(height * scale), nvgScale
        );
    }

    //---------------------------------------------------------------------------
    // Utils
    //---------------------------------------------------------------------------

    protected boolean isEditing() {
        return mc.currentScreen instanceof GuiEditHUD;
    }

    protected Color getFontColor() {
        return getFontColor(255);
    }

    protected Color getFontColor(int alpha) {
        return new Color(255, 255, 255, alpha);
    }

    private HUDModuleGravity getGravityHor() {
        switch (gravityHorizontalSetting.getSelectedEntryKey()) {
            case "gravity.horizontal.left":
                return HUDModuleGravity.LEFT;

            case "gravity.horizontal.right":
                return HUDModuleGravity.RIGHT;

            default:
                return HUDModuleGravity.CENTER;
        }
    }

    private HUDModuleGravity getGravityVer() {
        switch (gravityVerticalSetting.getSelectedEntryKey()) {
            case "gravity.vertical.top":
                return HUDModuleGravity.TOP;

            case "gravity.vertical.bottom":
                return HUDModuleGravity.BOTTOM;

            default:
                return HUDModuleGravity.CENTER;
        }
    }

    //---------------------------------------------------------------------------
    // Position & size
    //---------------------------------------------------------------------------

    public void setX(float x) {
        this.x = x;

        switch (getGravityHor()) {
            case LEFT:
                break;
            case RIGHT:
                x = screenWidth - x;
                break;
            case CENTER:
                x -= screenWidth / 2F;
        }

        this.vX = to(x);
    }

    public void setY(float y) {
        this.y = y;

        switch (getGravityHor()) {
            case LEFT:
                break;
            case RIGHT:
                y = screenHeight - y;
                break;
            case CENTER:
                y -= screenHeight / 2F;
        }

        this.vY = to(y);
    }

    public void setVX(float x) {
        this.vX = x;

        x = from(x);

        switch (getGravityHor()) {
            case LEFT:
                break;
            case RIGHT:
                x = screenWidth - x;
                break;
            case CENTER:
                x += screenWidth / 2F;
        }

        this.x = x;
    }

    public void setVY(float y) {
        this.vY = y;

        y = from(y);

        switch (getGravityVer()) {
            case TOP:
                break;
            case BOTTOM:
                y = screenHeight - y;
                break;
            case CENTER:
                y += screenHeight / 2F;
        }

        this.y = y;
    }

    protected void setSize(float width, float height) {
        this.vWidth = width;
        this.vHeight = height;

        this.width = from(width * scale);
        this.height = from(height * scale);
    }

    public void setScale(float scale) {
        this.scale = Math.max(minScale, Math.min(maxScale, scale));

//        System.out.println(scale);
    }

    public float from(float num) {
        return (float) (num * ratio);
    }

    public float to(float num) {
        return (float) (num / ratio);
    }
}
