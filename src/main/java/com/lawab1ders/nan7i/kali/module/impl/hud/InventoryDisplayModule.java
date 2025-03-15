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
import net.minecraft.item.ItemStack;

@IModule(
        name = "module.inventorydisplay",
        description = "module.inventorydisplay.desc",
        category = EModuleCategory.HUD
)
public class InventoryDisplayModule extends HUDModule {

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        float startX = x + 6;
        float startY = y + 22;
        int index = 0;

        nvg.setupAndDraw(this::drawNanoVG);

        event.startScale(x, y, from(scale));

        for (int i = 9; i < 36; i++) {
            ItemStack slot = mc.thePlayer.inventory.mainInventory[i];

            if (slot == null) {
                startX += 20;
                index += 1;

                if (index > 8) {
                    index = 0;
                    startY += 20;
                    startX = x + 4;
                }

                continue;
            }

            event.drawItemStack(slot, (int) startX, (int) startY);

            startX += 20;
            index += 1;

            if (index > 8) {
                index = 0;
                startY += 20;
                startX = x + 6;
            }
        }

        event.stopScale();
    }

    private void drawNanoVG() {
        this.drawBackground(188, 82);
        this.drawText("Inventory", 5.5F, 6F, 10.5F, Fonts.REGULAR);
        this.drawRect(0, 17.5F, 188, 1);
    }
}
