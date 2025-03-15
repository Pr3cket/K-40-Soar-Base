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
import com.lawab1ders.nan7i.kali.nanovg.Fonts;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import org.lwjgl.input.Keyboard;

@IModule(
        name = "module.keystrokes",
        description = "module.keystrokes.desc",
        category = EModuleCategory.HUD
)
public class KeystrokesModule extends HUDModule {

    private final Animation[] animations = new Animation[5];

    public KeystrokesModule() {
        super();

        for (int i = 0; i < 5; i++) {
            animations[i] = new Animation();
        }
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        this.setSize(92, 86);
        nvg.setupAndDraw(this::drawNanoVG);
    }

    private void drawNanoVG() {
        boolean openGui = mc.currentScreen != null;

        animations[0].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) ?
                1.0F : 0.0F, 16);
        animations[1].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) ?
                1.0F : 0.0F, 16);
        animations[2].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) ?
                1.0F : 0.0F, 16);
        animations[3].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) ?
                1.0F : 0.0F, 16);
        animations[4].setAnimation(!openGui && Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) ?
                1.0F : 0.0F, 16);

        // W
        this.drawBackground(32, 0, 28, 28);

        // A
        this.drawBackground(0, 32, 28, 28);

        // S
        this.drawBackground(32, 32, 28, 28);

        // D
        this.drawBackground(64, 32, 28, 28);

        // W
        nvg.save();
        scale(32, 0, 28, 28, animations[0].getValue());
        this.drawRoundedRect(32, 0, 28, 28, 6, this.getFontColor((int) (120 * animations[0].getValue())));
        nvg.restore();

        // A
        nvg.save();
        scale(0, 32, 28, 28, animations[1].getValue());
        this.drawRoundedRect(0, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[1].getValue())));
        nvg.restore();

        // S
        nvg.save();
        scale(32, 32, 28, 28, animations[2].getValue());
        this.drawRoundedRect(32, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[2].getValue())));
        nvg.restore();

        // D
        nvg.save();
        scale(64, 32, 28, 28, animations[3].getValue());
        this.drawRoundedRect(64, 32, 28, 28, 6, this.getFontColor((int) (120 * animations[3].getValue())));
        nvg.restore();

        this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()),
                32 + ((float) 28 / 2), ((float) 28 / 2) - 4, 12, Fonts.REGULAR);
        this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()),
                ((float) 28 / 2), 32 + ((float) 28 / 2) - 4, 12, Fonts.REGULAR);
        this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()),
                32 + ((float) 28 / 2), 32 + ((float) 28 / 2) - 4, 12, Fonts.REGULAR);
        this.drawCenteredText(Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()),
                64 + ((float) 28 / 2), 32 + ((float) 28 / 2) - 4, 12, Fonts.REGULAR);

        this.drawBackground(0, 64, (28 * 3) + 8, 22);

        nvg.save();
        scale(0, 64, (28 * 3) + 8, 22, animations[4].getValue());
        this.drawRoundedRect(0, 64, (28 * 3) + 8, 22, 6, this.getFontColor((int) (120 * animations[4].getValue())));
        nvg.restore();

        this.drawRoundedRect(10, 74F, (26 * 3) - 6, 2, 1);
    }
}
