package com.lawab1ders.nan7i.kali.module.impl.other;

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

import com.lawab1ders.nan7i.kali.events.ticks.RendererTickedEvent;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinGuiContainer;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@IModule(
        name = "module.mousetweaks",
        description = "module.mousetweaks.desc",
        category = EModuleCategory.OTHER,
        activated = true
)
public class MouseTweaksModule extends Module {

    private Container container;
    private GuiContainer gui;

    private Slot oldSelectedSlot = null;
    private Slot firstSlot = null;
    private boolean firstSlotClicked = false;

    private GuiScreen lastGui;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        GuiScreen g = event.gui;

        if (!(g instanceof GuiContainer) || g instanceof GuiContainerCreative) {
            container = null;
            gui = null;

            if (lastGui != null && g == null && lastGui instanceof GuiContainerCreative) {
                postNotificationOnlyOnce("module.mousetweaks.noti", NotificationType.WARNING);
            }

            lastGui = g;

        } else {
            gui = (GuiContainer) g;
            container = gui.inventorySlots;
        }
    }

    @SubscribeEvent
    public void onRendererTicked(RendererTickedEvent event) {

        if (gui == null || container == null) return;

        int slotCount = container.inventorySlots.size();
        int wheel = Mouse.getDWheel() / 120;

        if (!Mouse.isButtonDown(1)) {
            firstSlotClicked = false;
            firstSlot = null;
        }

        Slot selectedSlot = null;

        for (int i = 0; i < slotCount; i++) {
            Slot s = container.getSlot(i);
            if (((IMixinGuiContainer) gui).k40$invokeIsMouseOverSlot(s, getRequiredMouseX(), getRequiredMouseY())) {
                selectedSlot = s;
                break;
            }
        }

        if (selectedSlot == null) {
            return;
        }

        // Copy the stacks, so that they don't change while we do our stuff.
        ItemStack stackOnMouse = mc.thePlayer.inventory.getItemStack();
        ItemStack targetStack = selectedSlot.getStack();

        if (oldSelectedSlot != selectedSlot) {
            // ...and some more conditions.
            if (Mouse.isButtonDown(1) && !firstSlotClicked && (firstSlot == null) && (oldSelectedSlot != null)) {
                firstSlot = oldSelectedSlot;
            }

            boolean shiftIsDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

            if (Mouse.isButtonDown(0)) { // Left mouse button
                if (stackOnMouse != null) {
                    if ((targetStack != null) && areStacksCompatible(stackOnMouse, targetStack)) {

                        if (shiftIsDown) { // If shift is down, we just shift-click the slot and the item gets moved
                            // into another inventory.
                            clickSlot(selectedSlot, 0, true);
                        } else { // If shift is not down, we need to merge the item stack on the mouse with the one
                            // in the slot.
                            if ((getItemStackSize(stackOnMouse) + getItemStackSize(targetStack)) <= getMaxItemStackSize(stackOnMouse)) {
                                // We need to click on the slot so that our item stack gets merged with it,
                                // and then click again to return the stack to the mouse.
                                // However, if the slot is crafting output, then the item is added to the mouse stack
                                // on the first click, and we don't need to click the second time.
                                clickSlot(selectedSlot, 0, false);
                            }
                        }
                    }
                } else if (targetStack != null && shiftIsDown) {
                    clickSlot(selectedSlot, 0, true);
                }
            }

            oldSelectedSlot = selectedSlot;
        }

        if (wheel != 0) {
            int numItemsToMove = Math.abs(wheel);

            if (slotCount > 36) {
                ItemStack originalStack = getSlotStack(selectedSlot);
                boolean isCraftingOutput = true;

                if ((originalStack != null)
                        && ((stackOnMouse == null) || (isCraftingOutput == areStacksCompatible(originalStack,
                        stackOnMouse)))) {
                    do {
                        Slot applicableSlot = null;

                        int slotCounter = 0;
                        int countUntil = slotCount - 36;
                        if (getSlotNumber(selectedSlot) < countUntil) {
                            slotCounter = countUntil;
                            countUntil = slotCount;
                        }

                        if (wheel < 0) {
                            for (int i = slotCounter; i < countUntil; i++) {
                                Slot sl = getSlotWithID(i);
                                ItemStack stackSl = getSlotStack(sl);

                                if (stackSl == null) {
                                    sl.isItemValid(originalStack);
                                } else if (areStacksCompatible(originalStack, stackSl)) {
                                    if (stackSl.stackSize < stackSl.getMaxStackSize()) {
                                        applicableSlot = sl;
                                        break;
                                    }
                                }
                            }
                        } else {
                            for (int i = countUntil - 1; i >= slotCounter; i--) {
                                Slot sl = getSlotWithID(i);
                                ItemStack stackSl = getSlotStack(sl);

                                if (areStacksCompatible(originalStack, stackSl)) {
                                    applicableSlot = sl;
                                    break;
                                }
                            }
                        }

                        if (wheel < 0) {
                            boolean mouseWasEmpty = stackOnMouse == null;

                            for (int i = 0; i < numItemsToMove; i++) {
                                clickSlot(selectedSlot, 0, false);
                            }

                            if ((applicableSlot != null)
                                    && mouseWasEmpty) {
                                clickSlot(applicableSlot, 0, false);
                            }
                        }

                        break;

                    }
                    while (numItemsToMove != 0);
                }
            }
        }
    }

    private int getRequiredMouseX() {
        ScaledResolution var8 = new ScaledResolution(mc);
        int var9 = var8.getScaledWidth();
        return (Mouse.getX() * var9) / mc.displayWidth;
    }

    private int getRequiredMouseY() {
        ScaledResolution var8 = new ScaledResolution(mc);
        int var10 = var8.getScaledHeight();
        return var10 - ((Mouse.getY() * var10) / mc.displayHeight) - 1;
    }

    private void clickSlot(Slot targetSlot, int mouseButton, boolean shiftPressed) {
        mc.playerController.windowClick(container.windowId, targetSlot.slotNumber, mouseButton, shiftPressed ? 1 : 0,
                mc.thePlayer);
    }

    private int getMaxItemStackSize(ItemStack itemStack) {
        return itemStack.getMaxStackSize();
    }

    private int getItemStackSize(ItemStack itemStack) {
        return itemStack.stackSize;
    }

    private Slot getSlotWithID(int slotNumber) {
        return container.inventorySlots.get(slotNumber);
    }

    private ItemStack getSlotStack(Slot slot) {
        return (slot == null) ? null : slot.getStack();
    }

    private boolean areStacksCompatible(ItemStack itemStack1, ItemStack itemStack2) {
        return (itemStack1 == null || itemStack2 == null) || itemStack1.isItemEqual(itemStack2);
    }

    private int getSlotNumber(Slot slot) {
        return slot.slotNumber;
    }
}
