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
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@IModule(
        name = "module.armorstatus",
        description = "module.armorstatus.desc",
        category = EModuleCategory.HUD
)
public class ArmorStatusModule extends HUDModule {

    public final EnumSetting modeSetting = new EnumSetting(
            "module.opts.mode",
            "module.armorstatus.opts.mode.horizontal", "module.armorstatus.opts.mode.vertical"
    );

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        boolean vertical = modeSetting.getSelectedEntryKey().equals("module.armorstatus.opts.mode.vertical");
        ItemStack[] fakeStack = new ItemStack[4];

        setSize(vertical ? 16 : 16 * 4, vertical ? 16 * 4 : 16);

        fakeStack[3] = new ItemStack(Items.diamond_helmet);
        fakeStack[2] = new ItemStack(Items.diamond_chestplate);
        fakeStack[1] = new ItemStack(Items.diamond_leggings);
        fakeStack[0] = new ItemStack(Items.diamond_boots);

        event.startScale(x, y, from(scale));

        for (int i = 0; i < 4; i++) {
            ItemStack item = (this.isEditing() ? fakeStack : mc.thePlayer.inventory.armorInventory)[Math.abs(3 - i)];
            int addX = vertical ? 0 : 16 * i;
            int addY = vertical ? 16 * i : 0;

            if (item != null) {
                event.drawItemStack(item, (int) (x + addX), (int) (y + addY));
            }
        }

        event.stopScale();
    }
}
