package com.lawab1ders.nan7i.kali.nanovg;

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

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.KaliProcessor;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.IOUtils;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class NanoVGManager implements InstanceAccess {

    private final HashMap<Integer, NVGColor> colorCache = new HashMap<>();

    @Getter
    private final long context;

    @Getter
    private final AssetsManager assetsManager;

    public NanoVGManager() {
        context = NanoVGGL2.nvgCreate(NanoVGGL2.NVG_ANTIALIAS);

        if (context == 0) {
            throw new RuntimeException("Failed to create NanoVG context");
        }

        for (NVGFont font : Fonts.getFonts()) {
            if (font.isLoaded()) continue;

            int loaded = -1;

            try {
                ByteBuffer buffer = IOUtils.resourceToByteBuffer(font.getResourceLocation());

                if (buffer == null) {
                    throw new RuntimeException("Failed to resource font to ByteBuffer " + font.getName());
                }

                loaded = NanoVG.nvgCreateFontMem(context, font.getName(), buffer, false);
                font.setBuffer(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (loaded == -1) {
                throw new RuntimeException("Failed to init font " + font.getName());
            }
            else {
                font.setLoaded(true);
            }
        }

        assetsManager = new AssetsManager();
    }

    //---------------------------------------------------------------------------
    // Setup
    //---------------------------------------------------------------------------

    public void setupAndDraw(Runnable task) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        NanoVG.nvgBeginFrame(context, mc.displayWidth, mc.displayHeight, 1);

        task.run();

        NanoVG.nvgEndFrame(context);
        GL11.glPopAttrib();
    }

    //---------------------------------------------------------------------------
    // Draw Rect
    //---------------------------------------------------------------------------

    public void drawRect(float x, float y, float width, float height, Color color) {
        drawRoundedRect(x, y, width, height, 0, color);
    }

    public void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        drawRoundedRect(x, y, width, height, radius, radius, radius, radius, color);
    }

    public void drawRoundedRect(float x, float y, float width, float height,
                                float topLeftRadius, float topRightRadius,
                                float bottomLeftRadius, float bottomRightRadius,
                                Color color) {

        if (KaliProcessor.BLUR_TICK) return;

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgRoundedRectVarying(
                context, x, y, width, height, topLeftRadius, topRightRadius, bottomRightRadius,
                bottomLeftRadius
        );
        NanoVG.nvgFillColor(context, getColor(color));
        NanoVG.nvgFill(context);
    }

    public void drawGradientRoundedRect(float x, float y,
                                        float width, float height, float radius,
                                        Color color1, Color color2) {
        drawGradientOutlineRoundedRect(x, y, width, height, radius, 0, color1, color2);
    }

    public void drawGradientOutlineRoundedRect(float x, float y,
                                               float width, float height, float radius, float strokeWidth,
                                               Color color1, Color color2) {

        if (KaliProcessor.BLUR_TICK) return;

        NVGPaint bg = NVGPaint.create();

        float tick = ((System.currentTimeMillis() % 3600) / 570F);
        float max = Math.max(width, height);

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgRoundedRect(context, x, y, width, height, radius);

        NVGColor nvgColor1 = getColor(color1);
        NVGColor nvgColor2 = getColor(color2);

        NanoVG.nvgFillColor(context, nvgColor1);
        NanoVG.nvgFillColor(context, nvgColor2);

        NVGPaint nvgPaint = NanoVG.nvgLinearGradient(
                context,
                x + width / 2 - (max / 2) * MathUtils.cos(tick),
                y + height / 2 - (max / 2) * MathUtils.sin(tick),
                x + width / 2 + (max / 2) * MathUtils.cos(tick),
                y + height / 2 + (max / 2f) * MathUtils.sin(tick),
                nvgColor1,
                nvgColor2,
                bg
        );

        if (strokeWidth == 0) {
            NanoVG.nvgFillPaint(context, nvgPaint);
            NanoVG.nvgFill(context);
        }
        else {
            NanoVG.nvgStrokeWidth(context, strokeWidth);
            NanoVG.nvgStrokePaint(context, nvgPaint);
            NanoVG.nvgStroke(context);
        }
    }

    public void drawOutlineRoundedRect(float x, float y,
                                       float width, float height, float radius, float strokeWidth,
                                       Color color) {

        if (KaliProcessor.BLUR_TICK) return;

        NVGColor nvgColor = getColor(color);

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
        NanoVG.nvgStrokeWidth(context, strokeWidth);
        NanoVG.nvgStrokeColor(context, nvgColor);
        NanoVG.nvgStroke(context);
    }

    //---------------------------------------------------------------------------
    // Draw Shadow
    //---------------------------------------------------------------------------

    public void drawShadow(float x, float y, float width, float height, float radius) {
        drawShadow(x, y, width, height, radius, 1);
    }

    public void drawShadow(float x, float y,
                           float width, float height, float radius, float length) {

        if (KaliProcessor.BLUR_TICK) return;

        int alpha = 1;

        for (float f = length * 7; f > 0; f -= length) {
            drawOutlineRoundedRect(
                    x - (f / 2), y - (f / 2), width + f, height + f, radius + length * 2, f,
                    new Color(0, 0, 0, alpha)
            );

            alpha += 2;
        }
    }

    public void drawGradientShadow(float x, float y,
                                   float width, float height, float radius, float length,
                                   Color color1, Color color2) {

        if (KaliProcessor.BLUR_TICK) return;

        int alpha = 1;

        for (float f = length * 10; f > 0; f -= length) {
            drawGradientOutlineRoundedRect(
                    x - (f / 2),
                    y - (f / 2),
                    width + f,
                    height + f,
                    radius + length * 2,
                    f,
                    ColorUtils.applyAlpha(color1, alpha), ColorUtils.applyAlpha(color2, alpha)
            );

            alpha += 3;
        }
    }

    //---------------------------------------------------------------------------
    // Draw Circle or Arc
    //---------------------------------------------------------------------------

    public void drawCircle(float x, float y, float radius, Color color) {
        if (KaliProcessor.BLUR_TICK) return;

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgCircle(context, x, y, radius);
        NanoVG.nvgFillColor(context, getColor(color));
        NanoVG.nvgFill(context);
    }

    public void drawArc(float x, float y,
                        float radius, float startAngle, float endAngle, float strokeWidth,
                        Color color) {

        if (KaliProcessor.BLUR_TICK) return;

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgArc(
                context, x, y, radius, (float) Math.toRadians(startAngle), (float) Math.toRadians(endAngle),
                NanoVG.NVG_CW
        );
        NanoVG.nvgStrokeWidth(context, strokeWidth);
        NanoVG.nvgStrokeColor(context, getColor(color));
        NanoVG.nvgStroke(context);
    }

    //---------------------------------------------------------------------------
    // Draw Text
    //---------------------------------------------------------------------------

    public void drawText(String text, float x, float y, Color color, float size, NVGFont NVGFont) {
        if (KaliProcessor.BLUR_TICK) return;

        y += size / 2;

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgFontSize(context, size);
        NanoVG.nvgFontFace(context, NVGFont.getName());
        NanoVG.nvgTextAlign(context, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE);
        NanoVG.nvgFillColor(context, getColor(color));
        NanoVG.nvgText(context, x, y, text);
    }

    public void drawCenteredText(String text, float x, float y, Color color, float size, NVGFont NVGFont) {
        drawText(text, x - ((int) getTextWidth(text, size, NVGFont) >> 1), y, color, size, NVGFont);
    }

    public String getLimitText(String inputText, float fontSize, NVGFont font, float width) {
        return getLimitText(inputText, fontSize, font, width, false);
    }

    public String getLimitText(String inputText, float fontSize, NVGFont font, float width, boolean reverse) {
        String text = inputText;
        boolean isInRange = false, isRemoved = false;

        while (!isInRange) {
            if (getTextWidth(text, fontSize, font) > width) {
                if (reverse) {
                    text = text.substring(1);
                }
                else {
                    text = text.substring(0, text.length() - 1);
                }

                isRemoved = true;
            }
            else {
                isInRange = true;
            }
        }

        String s = (isRemoved ? "..." : "");

        return reverse ? s + text : text + s;
    }

    public float getTextWidth(String text, float size, NVGFont NVGFont) {
        float[] bounds = new float[4];

        NanoVG.nvgFontSize(context, size);
        NanoVG.nvgFontFace(context, NVGFont.getName());
        NanoVG.nvgTextBounds(context, 0, 0, text, bounds);
        NanoVG.nvgTextAlign(context, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE);

        return bounds[2] - bounds[0];
    }

    public float getTextHeight(String text, float size, NVGFont NVGFont) {
        float[] bounds = new float[4];

        NanoVG.nvgFontSize(context, size);
        NanoVG.nvgFontFace(context, NVGFont.getName());
        NanoVG.nvgTextBounds(context, 0, 0, text, bounds);

        return bounds[3] - bounds[1];
    }

    //---------------------------------------------------------------------------
    // Draw Image
    //---------------------------------------------------------------------------

    public void drawSVG(ResourceLocation location,
                        float x, float y, float width, float height, Color color) {

        if (KaliProcessor.BLUR_TICK) return;

        if (assetsManager.loadSVG(context, location, width, height)) {
            NVGPaint imagePaint = NVGPaint.calloc();

            NanoVG.nvgBeginPath(context);
            NanoVG.nvgImagePattern(
                    context, x, y, width, height, 0, assetsManager.getSVG(location, width, height), 1, imagePaint
            );

            imagePaint.innerColor(getColor(color));
            imagePaint.outerColor(getColor(color));

            NanoVG.nvgRect(context, x, y, width, height);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);

            imagePaint.free();
        }
    }

    public void drawImage(File file, float x, float y, float width, float height) {
        if (KaliProcessor.BLUR_TICK) return;

        if (assetsManager.loadImage(context, file)) {
            NVGPaint imagePaint = NVGPaint.calloc();

            NanoVG.nvgBeginPath(context);
            NanoVG.nvgImagePattern(context, x, y, width, height, 0, assetsManager.getImage(file), 1, imagePaint);

            NanoVG.nvgRect(context, x, y, width, height);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);

            imagePaint.free();
        }
    }

    public void drawImage(int texture, float x, float y, float width, float height) {

        if (KaliProcessor.BLUR_TICK) return;

        if (assetsManager.loadImage(context, texture, width, height)) {
            NVGPaint imagePaint = NVGPaint.calloc();
            int image = assetsManager.getImage(texture);

            NanoVG.nvgImageSize(context, image, new int[] { (int) width }, new int[] { -(int) height });

            NanoVG.nvgImagePattern(context, x, y, width, height, 0, image, 1, imagePaint);
            NanoVG.nvgBeginPath(context);
            NanoVG.nvgRect(context, x, y, width, height);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);
            NanoVG.nvgClosePath(context);

            imagePaint.free();
        }
    }

    /**
     * 绘制玩家头像
     *
     * @param location 皮肤资源地址
     * @param x        偏移 x
     * @param y        偏移 y
     * @param width    宽
     * @param height   高
     * @param radius   圆角度数
     */
    public void drawPlayerHead(ResourceLocation location, float x, float y, float width, float height, float radius) {
        if (KaliProcessor.BLUR_TICK || location == null || mc.getTextureManager().getTexture(location) == null) return;

        int texture = mc.getTextureManager().getTexture(location).getGlTextureId();

        if (assetsManager.loadImage(context, texture, width, height)) {
            NVGPaint imagePaint = NVGPaint.calloc();
            int image = assetsManager.getImage(texture);

            NanoVG.nvgImageSize(context, image, new int[] { (int) width }, new int[] { (int) height });

            NanoVG.nvgImagePattern(context, x - width, y - height, width * 8, height * 8, 0, image, 1, imagePaint);
            NanoVG.nvgBeginPath(context);
            NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);
            NanoVG.nvgClosePath(context);

            imagePaint.free();
        }
    }

    /**
     * 绘制玩家披风
     *
     * @param location 皮肤资源地址
     * @param x        偏移 x
     * @param y        偏移 y
     * @param width    宽
     * @param height   高
     * @param radius   圆角度数
     */
    public void drawPlayerCape(ResourceLocation location, float x, float y, float width, float height, float radius) {
        if (KaliProcessor.BLUR_TICK || location == null || mc.getTextureManager().getTexture(location) == null) return;

        int texture = mc.getTextureManager().getTexture(location).getGlTextureId();

        if (assetsManager.loadImage(context, texture, width, height)) {
            NVGPaint imagePaint = NVGPaint.calloc();
            float totalWidth = width * 6.4F;
            float onePixel = totalWidth / 64;
            int image = assetsManager.getImage(texture);

            NanoVG.nvgImageSize(context, image, new int[] { (int) width }, new int[] { -(int) height });

            NanoVG.nvgImagePattern(
                    context, x - onePixel,
                    // 减去两像素，披风是 10x16 大小，但是我们绘制的披风由于布局限制只能是 10x15
                    y - onePixel * 1.5F, totalWidth, totalWidth / 2, 0, image, 1, imagePaint
            );
            NanoVG.nvgBeginPath(context);
            NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);
            NanoVG.nvgClosePath(context);

            imagePaint.free();
        }
    }

    public void drawRoundedImage(int texture, float x, float y, float width, float height, float radius) {
        drawRoundedImage(texture, x, y, width, height, radius, 1.0F);
    }

    public void drawRoundedImage(int texture, float x, float y, float width, float height, float radius, float alpha) {
        if (KaliProcessor.BLUR_TICK) return;

        if (assetsManager.loadImage(context, texture, width, height)) {
            NVGPaint imagePaint = NVGPaint.calloc();
            int image = assetsManager.getImage(texture);

            NanoVG.nvgImageSize(context, image, new int[] { (int) width }, new int[] { -(int) height });

            NanoVG.nvgImagePattern(context, x, y, width, height, 0, image, alpha, imagePaint);
            NanoVG.nvgBeginPath(context);
            NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);
            NanoVG.nvgClosePath(context);

            imagePaint.free();
        }
    }

    public void drawRoundedImage(ResourceLocation location, float x, float y, float width, float height, float radius) {
        if (KaliProcessor.BLUR_TICK) return;

        if (assetsManager.loadImage(context, location)) {
            NVGPaint imagePaint = NVGPaint.calloc();
            int image = assetsManager.getImage(location);

            NanoVG.nvgBeginPath(context);
            NanoVG.nvgImagePattern(context, x, y, width, height, 0, image, 1, imagePaint);
            NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);

            imagePaint.free();
        }
    }

    public void drawRoundedImage(File file, float x, float y, float width, float height, float radius, float alpha) {
        if (KaliProcessor.BLUR_TICK) return;

        if (assetsManager.loadImage(context, file)) {
            NVGPaint imagePaint = NVGPaint.calloc();
            int image = assetsManager.getImage(file);

            NanoVG.nvgBeginPath(context);
            NanoVG.nvgImagePattern(context, x, y, width, height, 0, image, alpha, imagePaint);
            NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
            NanoVG.nvgFillPaint(context, imagePaint);
            NanoVG.nvgFill(context);

            imagePaint.free();
        }
    }

    public void loadImage(Object obj) {
        if (obj instanceof File) assetsManager.loadImage(context, (File) obj);
        else if (obj instanceof ResourceLocation) assetsManager.loadImage(context, (ResourceLocation) obj);
    }

    //---------------------------------------------------------------------------
    // Graph and Layout
    //---------------------------------------------------------------------------

    public void scale(float x, float y, float scale) {
        NanoVG.nvgTranslate(context, x, y);
        NanoVG.nvgScale(context, scale, scale);
        NanoVG.nvgTranslate(context, -x, -y);
    }

    public void scale(float x, float y, float width, float height, float scale) {
        NanoVG.nvgTranslate(context, (x + (x + width)) / 2, (y + (y + height)) / 2);
        NanoVG.nvgScale(context, scale, scale);
        NanoVG.nvgTranslate(context, -(x + (x + width)) / 2, -(y + (y + height)) / 2);
    }

    public void rotate(float x, float y, float width, float height, float angle) {
        NanoVG.nvgTranslate(context, (x + (x + width)) / 2, (y + (y + height)) / 2);
        NanoVG.nvgRotate(context, angle);
        NanoVG.nvgTranslate(context, -(x + (x + width)) / 2, -(y + (y + height)) / 2);
    }

    public void translate(float x, float y) {
        NanoVG.nvgTranslate(context, x, y);
    }

    public void scissor(float x, float y, float width, float height) {
        NanoVG.nvgScissor(context, x, y, width, height);
    }

    public void save() {
        NanoVG.nvgSave(context);
    }

    public void restore() {
        NanoVG.nvgRestore(context);
    }

    public void setAlpha(float alpha) {
        NanoVG.nvgGlobalAlpha(context, alpha);
    }

    public NVGColor getColor(Color color) {
        if (color == null) color = Color.RED;
        if (colorCache.containsKey(color.getRGB())) return colorCache.get(color.getRGB());

        NVGColor nvgColor = NVGColor.create();

        NanoVG.nvgRGBA(
                (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(),
                (byte) color.getAlpha(), nvgColor
        );

        colorCache.put(color.getRGB(), nvgColor);
        return nvgColor;
    }

    //---------------------------------------------------------------------------
    // Misc
    //---------------------------------------------------------------------------

    public void drawAlphaBar(float x, float y, float width, float height, float radius, Color color) {
        if (KaliProcessor.BLUR_TICK) return;

        NVGPaint bg = NVGPaint.create();
        NVGColor nvgColor = getColor(color);
        NVGColor nvgColor2 = getColor(new Color(0, 0, 0, 0));

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
        NanoVG.nvgFillPaint(context, NanoVG.nvgLinearGradient(context, x, y, x + width, y, nvgColor2, nvgColor, bg));
        NanoVG.nvgFill(context);
    }

    public void drawHSBBox(float x, float y, float width, float height, float radius, Color color) {
        if (KaliProcessor.BLUR_TICK) return;

        drawRoundedRect(x, y, width, height, radius, color);

        NVGPaint bg = NVGPaint.create();
        NVGColor nvgColor = getColor(Color.WHITE);
        NVGColor nvgColor2 = getColor(new Color(0, 0, 0, 0));

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
        NanoVG.nvgFillPaint(
                context, NanoVG.nvgLinearGradient(
                        context, x + 8, y + 8, x + width, y, nvgColor,
                        nvgColor2, bg
                )
        );
        NanoVG.nvgFill(context);

        NVGPaint bg2 = NVGPaint.create();
        NVGColor nvgColor3 = getColor(new Color(0, 0, 0, 0));
        NVGColor nvgColor4 = getColor(Color.BLACK);

        NanoVG.nvgBeginPath(context);
        NanoVG.nvgRoundedRect(context, x, y, width, height, radius);
        NanoVG.nvgFillPaint(
                context, NanoVG.nvgLinearGradient(
                        context, x + 8, y + 8, x, y + height, nvgColor3,
                        nvgColor4, bg2
                )
        );
        NanoVG.nvgFill(context);
    }
}
