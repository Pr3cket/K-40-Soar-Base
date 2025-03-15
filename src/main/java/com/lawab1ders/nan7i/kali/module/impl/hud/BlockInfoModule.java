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
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.Direction;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.InterpolatableAnimation;
import com.lawab1ders.nan7i.kali.utils.animation.interpolatable.impl.EaseBackInAnimation;
import com.lawab1ders.nan7i.kali.utils.buffers.ScreenAnimation;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

@IModule(
        name = "module.blockinfo",
        description = "module.blockinfo.desc",
        category = EModuleCategory.HUD
)
public class BlockInfoModule extends HUDModule {

    private final InterpolatableAnimation introAnimation;
    private final ScreenAnimation screenAnimation = new ScreenAnimation();

    private Block lastObj;

    public BlockInfoModule() {
        introAnimation = new EaseBackInAnimation(1.0F, 320, 2.0F);
        introAnimation.setDirection(Direction.BACKWARDS);
    }

    @Override
    public void onOverlayRendered(OverlayRenderedEvent event) {
        val isBlock = mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK;

        if (isBlock) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState state = mc.theWorld.getBlockState(pos);
            lastObj = state.getBlock();
        }

        if (lastObj == null) lastObj = Blocks.grass;

        introAnimation.setDirection(isBlock || isEditing() ? Direction.FORWARDS : Direction.BACKWARDS);

        if (introAnimation.isDone(Direction.BACKWARDS)
                || lastObj.equals(Blocks.portal) || lastObj.equals(Blocks.end_portal)) return;

        val _80 = from(80 * scale);

        screenAnimation.wrap(
                () -> {if (introAnimation.getValue() < 1) drawBlock(event);},
                this::drawNanoVG, x, y, _80, _80, 2 - introAnimation.getValue(),
                introAnimation.getValue()
        );

        if (introAnimation.getValue() >= 1) drawBlock(event);
    }

    private void drawBlock(OverlayRenderedEvent event) {
        val _80 = from(80 * scale);

        event.startScale(x, y, _80, _80, from(2F * scale));
        event.drawItemStack(
                new ItemStack(lastObj.getItem(mc.theWorld, null)),
                (int) (x + _80 / 2 - 11F / (sr == null ? new ScaledResolution(mc) : sr).getScaleFactor()),
                (int) (y + _80 / 2 - 11F / (sr == null ? new ScaledResolution(mc) : sr).getScaleFactor())
        );
        event.stopScale();
    }

    private void drawNanoVG() {
        this.drawBackground(80, 80);

        this.drawCenteredText(
                nvg.getLimitText(lastObj.getLocalizedName(), 9, Fonts.REGULAR, 60), 40, 6, 9,
                Fonts.REGULAR
        );
    }
}
