package com.lawab1ders.nan7i.kali.gui;

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
import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.color.AccentColor;
import com.lawab1ders.nan7i.kali.color.Theme;
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.clickgui.Category;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompTextBox;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.*;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.MathUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenAnimation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiClickMenu extends GuiScreen implements InstanceAccess {

    private final Animation headAnimation = new Animation();

    @Getter
    private final ArrayList<Category> categories = new ArrayList<>();
    private final Animation moveAnimation = new Animation();
    private final ScreenAnimation screenAnimation = new ScreenAnimation();
    private final ResourceLocation svg = new ResourceLocation("k40", "textures/kali.svg");
    private final ResourceLocation svgWhite = new ResourceLocation("k40", "textures/kali_white.svg");
    private final Animation svgAnimation = new Animation();
    private InterpolatableAnimation introAnimation;

    @Getter
    private int x, y, width, height;
    private Category currentCategory;
    private boolean toEditHUD, toLogin;

    public GuiClickMenu() {
        currentCategory = new ModuleCategory(this);

        categories.add(currentCategory);
        categories.add(new CosmeticsCategory(this));
        categories.add(new MusicCategory(this));
        categories.add(new ProfileCategory(this));
        categories.add(new ScreenshotCategory(this));
        categories.add(new SettingCategory(this));

        Theme theme = col.getTheme();
        svgAnimation.setValue(theme.isDarkTint() ? 0 : 1);
    }

    /**
     * 打开模块设置，用于指示用户修改模块设置
     *
     * @param module 模块
     */
    public void openModuleSetting(Module module) {
        categories.forEach(c -> {
            // open module setting
            if (c instanceof ModuleCategory) {
                currentCategory = c;

                InstanceAccess.mc.displayGuiScreen(this);

                ((ModuleCategory) c).openModuleSetting(module);
            }
        });
    }

    @Override
    public void initGui() {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        int addX = 225;
        int addY = 140;

        x = (sr.getScaledWidth() / 2) - addX;
        y = (sr.getScaledHeight() / 2) - addY;
        width = addX * 2;
        height = addY * 2;

        introAnimation = new EaseBackInAnimation(1.0F, 320, 2.0F);
        introAnimation.setDirection(Direction.FORWARDS);

        toEditHUD = false;
        toLogin = false;

        currentCategory.initCategory();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();
        float value = introAnimation.getValue();

        List<Runnable> runnables = new ArrayList<>();
        runnables.add(() -> GuiLogin.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK.getRGB()));
        sha.getGaussianBlur().blur(runnables, toLogin ? 8 : (int) (Math.min(value, 1) * 8));

        PARTICLES_ENGINE.draw(mouseX, mouseY, value);

        screenAnimation.wrap(
                () ->
                        nvg.drawShadow(x, y, width, height, 12), 2 - value, Math.min(value, 1)
        );

        screenAnimation.wrap(
                () ->
                        drawNanoVG(mouseX, mouseY, partialTicks),
                this.getX(), this.getY(), this.getWidth(), this.getHeight(), 2 - value,
                Math.min(value, 1), true
        );

        ntf.render();
        CLICK_EFFECTS_ENGINE.drawClickEffects();
    }

    private void drawNanoVG(int mouseX, int mouseY, float partialTicks) {
        AccentColor currentColor = col.getCurrentColor();

        if (introAnimation.isDone(Direction.BACKWARDS)) {
            InstanceAccess.mc.displayGuiScreen(
                    toEditHUD ? new GuiEditHUD() : toLogin ? new GuiLogin() : null);
        }

        nvg.drawRoundedRect(x, y, width, height, 12, palette.getBackgroundColor(ColorType.NORMAL));
        nvg.drawRoundedRect(x, y, 32, height, 12, 0, 12, 0, palette.getBackgroundColor(ColorType.DARK));
        nvg.drawRect(x + 32, y + 30, width - 32, 1, palette.getBackgroundColor(ColorType.DARK));

        //        nvg.drawGradientRoundedRect(x + 5, y + 7, 22, 22, 11, currentColor.getColor1(), currentColor
        //        .getColor2());

        svgAnimation.setAnimation(col.getTheme().isDarkTint() ? 0 : 1, 16);

        nvg.save();
        nvg.scale(x + 2, y + 4, 30 / 512F);
        nvg.drawSVG(svg, x, y, 512, 512, ColorUtils.applyAlpha(Color.WHITE, (int) (svgAnimation.getValue() * 255)));
        nvg.drawSVG(
                svgWhite, x, y, 512, 512, ColorUtils.applyAlpha(
                        Color.WHITE,
                        255 - (int) (svgAnimation.getValue() * 255)
                )
        );
        nvg.restore();

        nvg.save();
        nvg.translate(currentCategory.getTextAnimation().getValue() * 15, 0);
        nvg.drawText(
                currentCategory.getName(), x + 32, y + 10, palette.getFontColor(
                        ColorType.DARK,
                        (int) (currentCategory.getTextAnimation().getValue() * 255)
                ), 15, Fonts.MEDIUM
        );
        nvg.restore();

        int offsetY = 0;

        moveAnimation.setAnimation(categories.indexOf(currentCategory) * 30, 18);

        nvg.save();
        nvg.scissor(x, y + 30, 32, height - 100);

        nvg.drawGradientRoundedRect(
                x + 5.5F, y + 38.5F + moveAnimation.getValue(), 21, 21, 4,
                currentColor.getColor1(), currentColor.getColor2()
        );

        for (Category c : categories) {

            Color textColor = c.getTextColorAnimation().getColor(
                    MathUtils.isInRange(
                            moveAnimation.getValue(),
                            offsetY - 8, offsetY + 8
                    ) ? Color.WHITE : palette.getFontColor(ColorType.NORMAL), 18
            );

            c.getTextAnimation().setAnimation(c.equals(currentCategory) ? 1.0F : 0.0F, 14);

            nvg.drawText(c.getIcon(), x + 9F, y + 42 + offsetY, textColor, 14, Fonts.ICON);

            offsetY += 30;
        }

        nvg.restore();

        //        nvg.drawRect(x, y + height - 70, 32, 1, palette.getBackgroundColor(ColorType.NORMAL));

        nvg.drawRoundedRect(x + 5, y + height - 30, 22, 22, 11, palette.getBackgroundColor(ColorType.DARK));
        nvg.drawRoundedRect(x + 5, y + height - 30, 22, 22, 11, palette.getBackgroundColor(ColorType.NORMAL));
        nvg.drawPlayerHead(InstanceAccess.mc.thePlayer.getLocationSkin(), x + 5, y + height - 30, 22, 22, 11);

        boolean hover = MouseUtils.isInsideCircle(mouseX, mouseY, x + 5 + 11, y + height - 30 + 11, 11);
        // 勾股定理求鼠标到圆心的距离，判断鼠标是否玩家头像上
        //                Math.sqrt((mouseX - x - 5 - 11) * (mouseX - x - 5 - 11) + (mouseY - y - height + 30 - 11) *
        //                (mouseY - y - height + 30 - 11)) <= 11;

        int alpha = (int) (headAnimation.getValue() * 255);

        headAnimation.setAnimation(hover ? 1.0F : 0.0F, 16);

        nvg.drawGradientOutlineRoundedRect(
                x + 5, y + height - 30, 22, 22, 11, 1.4F * headAnimation.getValue(),
                ColorUtils.applyAlpha(currentColor.getColor1(), alpha),
                ColorUtils.applyAlpha(currentColor.getColor2(), alpha)
        );

        nvg.drawGradientRoundedRect(
                x + 5.5F, y + height - 60, 21, 21, 4, currentColor.getColor1(),
                currentColor.getColor2()
        );
        nvg.drawText(TextureCode.LAYOUT, x + 9, y + height - 56.5F, Color.WHITE, 14, Fonts.ICON);

        for (Category c : categories) {
            c.getCategoryAnimation().setAnimation(c.equals(currentCategory) ? 1.0F : 0.0F, 16);

            if (c.equals(currentCategory)) {

                nvg.save();

                if (c.isNeedSearch()) {
                    c.getSearchBox().setPosition(x + width - 175, y + 6.5F, 160, 18);
                    c.getSearchBox().setBgColor(palette.getBackgroundColor(ColorType.DARK));
                    c.getSearchBox().draw(mouseX, mouseY);
                }

                nvg.scissor(x + 32, y + 31, width - 32, height - 31);
                nvg.translate(0, 50 - (c.getCategoryAnimation().getValue() * 50));

                c.drawScreen(mouseX, mouseY, partialTicks);

                nvg.restore();

            }
        }

        currentCategory.scroll.onScroll();
        currentCategory.scroll.onAnimation();

//        if (currentCategory.isNeedSearch() && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_F)) {
//            currentCategory.getSearchBox().setFocused(true);
//        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0 && mouseButton != 1) {
            return;
        }

        CLICK_EFFECTS_ENGINE.addClickEffect(mouseX, mouseY);

        int offsetY = 0;

        for (Category c : categories) {
            if (MouseUtils.isInside(mouseX, mouseY, x + 5.5F, y + 38.5F + offsetY, 21, 21)) {
                if (c instanceof MusicCategory && !mus.getNecCache().isFetched()) {
                    ntf.post(
                            TranslateComponent.i18n("categories.music"),
                            TranslateComponent.i18n("login.noti.pleaselogin"),
                            NotificationType.INFO
                    );
                }
                else {
                    currentCategory = c;
                }

                currentCategory.initCategory();
                return;
            }

            offsetY += 30;
        }

        if (MouseUtils.isInside(mouseX, mouseY, x + 5.5F, y + height - 60, 21, 21)) {
            toEditHUD = true;
            introAnimation.setDirection(Direction.BACKWARDS);

        }
        else if (MouseUtils.isInsideCircle(mouseX, mouseY, x + 5 + 11, y + height - 30 + 11, 11)) {
            toLogin = true;
            introAnimation.setDirection(Direction.BACKWARDS);

        }
        else if (MouseUtils.isInside(mouseX, mouseY, x + 32, y + 31, width - 32, height - 31)) {
            currentCategory.mouseClicked(mouseX, mouseY);

        }
        else {
            currentCategory.getSearchBox().mouseClicked(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 || mouseButton == 1) {
            currentCategory.mouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        CompTextBox searchBox = currentCategory.getSearchBox();

        currentCategory.keyTyped(typedChar, keyCode);
        searchBox.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE && currentCategory.isCanClose()) {
            introAnimation.setDirection(Direction.BACKWARDS);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
