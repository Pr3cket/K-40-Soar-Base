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
import com.lawab1ders.nan7i.kali.color.palette.ColorType;
import com.lawab1ders.nan7i.kali.gui.clickgui.comp.impl.CompTextBox;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinMinecraft;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.music.NecCache;
import com.lawab1ders.nan7i.kali.music.NecLoginData;
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.nanovg.TextureCode;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenAnimation;
import com.lawab1ders.nan7i.kali.utils.mouse.MouseUtils;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiLogin extends GuiScreen implements InstanceAccess {

    private final ScreenAnimation screenAnimation = new ScreenAnimation();
    private final Animation qrRefresh = new Animation(), necLogout = new Animation();

    private final CompTextBox nameBox = new CompTextBox();

    @Getter
    private InterpolatableAnimation introAnimation;
    private float x, y, maxWidth;
    private float width, height;

    @Override
    public void initGui() {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();

        int addX = 200;
        int addY = 100;

        x = (sr.getScaledWidth() / 2F) - addX;
        y = (sr.getScaledHeight() / 2F) - addY;
        maxWidth = addX * 2;

        width = maxWidth / 2.2F;
        height = addY * 2;

        introAnimation = new EaseBackInAnimation(1.0F, 320, 2.0F);
        introAnimation.setDirection(Direction.FORWARDS);

        String username = ((IMixinMinecraft) InstanceAccess.mc).k40$getSession().getUsername();
        boolean changed = !Objects.equals(username, InstanceAccess.mc.thePlayer.getName());

        nameBox.setText(changed ? "" : username);

        val usernameComp = new TranslateComponent("Welcome back, %s!");
        usernameComp.setText(username);

        nameBox.setDefaultText(changed ? usernameComp : TranslateComponent.i18n_component("login.text.nickname"));
        nameBox.setLock(changed || InstanceAccess.isPremiumAccount());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = KaliAPI.INSTANCE.getScaledResolution();
        float value = introAnimation.getValue();

        if (introAnimation.isDone(Direction.BACKWARDS)) {
            InstanceAccess.mc.displayGuiScreen(gcm);
            return;
        }

        List<Runnable> runnables = new ArrayList<>();
        runnables.add(() -> Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), Color.BLACK.getRGB()));
        sha.getGaussianBlur().blur(runnables, 8);

        PARTICLES_ENGINE.draw(mouseX, mouseY, value);

        // Left
        screenAnimation.wrap(
                () -> nvg.drawShadow(x, y, width, height, 12),
                2 - value, Math.min(value, 1)
        );

        screenAnimation.wrap(
                () -> drawNanoVG_Left(mouseX, mouseY), x, y, maxWidth, height, 2 - value,
                Math.min(value, 1), false
        );

        // Right
        screenAnimation.wrap(
                () -> nvg.drawShadow(x + maxWidth - width, y, width, height, 12),
                2 - value, Math.min(value, 1)
        );

        screenAnimation.wrap(
                () -> drawNanoVG_Right(mouseX, mouseY), x, y, maxWidth, height, 2 - value,
                Math.min(value, 1), false
        );

        ntf.render();
        CLICK_EFFECTS_ENGINE.drawClickEffects();
    }

    private void drawNanoVG_Left(int mouseX, int mouseY) {
        AccentColor currentColor = col.getCurrentColor();

        nvg.drawRoundedRect(x, y, width, height, 12, palette.getBackgroundColor(ColorType.NORMAL));

        float imageSize = width / 3f;
        float offset = (height - imageSize - 15) / 3;

        nvg.drawRoundedRect(
                x + imageSize, y + offset, imageSize, imageSize, imageSize / 2,
                palette.getBackgroundColor(ColorType.DARK)
        );

        nvg.drawPlayerHead(
                InstanceAccess.mc.thePlayer.getLocationSkin(), x + imageSize, y + offset, imageSize, imageSize,
                imageSize / 2
        );

        nvg.drawGradientOutlineRoundedRect(
                x + imageSize, y + offset, imageSize, imageSize, imageSize / 2, 2,
                currentColor.getColor1(), currentColor.getColor2()
        );

        nvg.drawCenteredText(
                nvg.getLimitText(InstanceAccess.mc.thePlayer.getName(), 15, Fonts.DEMIBOLD, width / 2),
                x + width / 2, y + height - offset - 15,
                palette.getFontColor(ColorType.DARK), 15, Fonts.DEMIBOLD
        );

        nameBox.setPosition(x + (width - 130) / 2, y + height - offset - 18, 130, 18);
        nameBox.setBgColor(palette.getBackgroundColor(ColorType.DARK));
        nameBox.draw(mouseX, mouseY);

        if (introAnimation.getDirection() == Direction.BACKWARDS
                && !Objects.equals(nameBox.getText(), "")
                && !Objects.equals(nameBox.getText(), InstanceAccess.mc.thePlayer.getName())
                && !Objects.equals(
                nameBox.getText(), ((IMixinMinecraft) InstanceAccess.mc).k40$getSession().getUsername())) {

            ((IMixinMinecraft) InstanceAccess.mc).k40$setSession(new Session(nameBox.getText(), "0", "0", "mojang"));

            ntf.post(
                    TranslateComponent.i18n("categories.login"),
                    TranslateComponent.i18n("login.noti.change"),
                    NotificationType.SUCCESS
            );
        }
    }

    private void drawNanoVG_Right(int mouseX, int mouseY) {
        AccentColor currentColor = col.getCurrentColor();
        NecLoginData necLoginData = mus.getNecCache().getNecLoginData();
        Thread necHookThread = mus.getNecCache().getNecHookThread();

        nvg.drawRoundedRect(x + maxWidth - width, y, width, height, 12, palette.getBackgroundColor(ColorType.NORMAL));

        if (mus.getNecCache().isFetched()) {
            float imageSize = width / 3f;
            float offset = (height - imageSize - 15) / 3;

            nvg.drawRoundedRect(
                    x + maxWidth - width + imageSize, y + offset, imageSize, imageSize, imageSize / 2,
                    palette.getBackgroundColor(ColorType.DARK)
            );

            if (necLoginData != null && necLoginData.getAvatarImage() != null) {
                nvg.drawRoundedImage(
                        new DynamicTexture(necLoginData.getAvatarImage()).getGlTextureId(),
                        x + maxWidth - width + imageSize, y + offset, imageSize, imageSize, imageSize / 2
                );
            }

            nvg.drawGradientOutlineRoundedRect(
                    x + maxWidth - width + imageSize, y + offset, imageSize, imageSize,
                    imageSize / 2, 2, currentColor.getColor1(), currentColor.getColor2()
            );

            nvg.drawCenteredText(
                    necLoginData != null && necLoginData.getNickname() != null ?
                            nvg.getLimitText(necLoginData.getNickname(), 15, Fonts.DEMIBOLD, width / 2) :
                            TranslateComponent.i18n("login.text.nonickname"),
                    x + maxWidth - width / 2, y + height - offset - 15,
                    palette.getFontColor(ColorType.DARK), 15, Fonts.DEMIBOLD
            );

            boolean hover = MouseUtils.isInsideCircle(
                    mouseX, mouseY, x + maxWidth - width + imageSize * 1.5f,
                    y + offset + imageSize / 2, imageSize / 2
            );
            necLogout.setAnimation(hover && !(necHookThread != null && necHookThread.isAlive()) ? 1 : 0, 14);

            nvg.drawRoundedRect(
                    x + maxWidth - width + imageSize + 1, y + offset + 1, imageSize - 2, imageSize - 2,
                    imageSize / 2,
                    ColorUtils.applyAlpha(
                            palette.getBackgroundColor(ColorType.DARK),
                            (int) (necLogout.getValue() * 255 * 0.75F)
                    )
            );

            nvg.drawCenteredText(
                    TextureCode.LOGOUT,
                    x + maxWidth - width + imageSize * 1.5f,
                    y + offset + imageSize / 2 - 10,
                    ColorUtils.applyAlpha(currentColor.getInterpolateColor(), (int) (necLogout.getValue() * 255)),
                    20, Fonts.ICON
            );
        }
        else {
            float size = maxWidth / 3f;
            float reachToEdge = (width - size) / 2;
            float fontSize = 10;

            nvg.drawRoundedRect(
                    x + maxWidth - width + reachToEdge, y + reachToEdge, size, size, 12,
                    palette.getBackgroundColor(ColorType.DARK)
            );

            if (necLoginData != null && necLoginData.getQrCode() != null) {
                if (necLoginData.getTextureId() == -114514) {
                    necLoginData.setTextureId(new DynamicTexture(necLoginData.getQrCode()).getGlTextureId());
                }

                nvg.drawRoundedImage(
                        necLoginData.getTextureId(),
                        x + maxWidth - width + reachToEdge,
                        y + reachToEdge, size, size, 12
                );
            }

            nvg.drawGradientOutlineRoundedRect(
                    x + maxWidth - width + reachToEdge, y + reachToEdge, size, size,
                    12, 2, currentColor.getColor1(), currentColor.getColor2()
            );

            nvg.drawCenteredText(
                    TranslateComponent.i18n("login.text.neteasecloudaccountlogin"),
                    x + maxWidth - width / 2, y + height - reachToEdge - fontSize / 2,
                    palette.getFontColor(ColorType.DARK), fontSize, Fonts.DEMIBOLD
            );

            boolean hover = MouseUtils.isInside(
                    mouseX, mouseY, x + maxWidth - width + reachToEdge, y + reachToEdge,
                    size, size
            );
            qrRefresh.setAnimation(hover && !(necHookThread != null && necHookThread.isAlive()) ? 1 : 0, 14);

            nvg.drawRoundedRect(
                    x + maxWidth - width + reachToEdge + 1, y + reachToEdge + 1, size - 2, size - 2, 12,
                    ColorUtils.applyAlpha(
                            palette.getBackgroundColor(ColorType.DARK),
                            (int) (qrRefresh.getValue() * 255 * 0.75F)
                    )
            );
            nvg.drawText(
                    TextureCode.REFRESH,
                    x + maxWidth - width + reachToEdge + size / 2 - 25,
                    y + reachToEdge + size / 2 - 25,
                    ColorUtils.applyAlpha(currentColor.getInterpolateColor(), (int) (qrRefresh.getValue() * 255)),
                    50, Fonts.ICON
            );
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        nameBox.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE) {
            introAnimation.setDirection(Direction.BACKWARDS);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0 && mouseButton != 1) {
            return;
        }

        CLICK_EFFECTS_ENGINE.addClickEffect(mouseX, mouseY);

        float size = maxWidth / 3f;
        float reachToEdge = (width - size) / 2;
        float imageSize = width / 3f;

        NecCache necCache = mus.getNecCache();
        Thread necHookThread = necCache.getNecHookThread();

        boolean hover = MouseUtils.isInsideCircle(
                mouseX, mouseY,
                x + maxWidth - width + imageSize * 1.5f,
                y + imageSize * 1.5f, imageSize / 2
        );

        boolean hover2 = MouseUtils.isInside(
                mouseX, mouseY,
                x + maxWidth - width + reachToEdge,
                y + reachToEdge, size, size
        );

        if (!(necHookThread != null && necHookThread.isAlive())) {

            if (hover && necCache.isFetched()) {
                // 退出登录
                necCache.updateNec();

            }
            else if (hover2 && !necCache.isFetched()) {
                // 刷新二维码
                necCache.updateNec();
            }
        }

        nameBox.mouseClicked(mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
