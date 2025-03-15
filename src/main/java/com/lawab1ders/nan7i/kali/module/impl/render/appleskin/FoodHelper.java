package com.lawab1ders.nan7i.kali.module.impl.render.appleskin;

import lombok.AllArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class FoodHelper {

    public static boolean isFood(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemFood;
    }

    public static BasicFoodValues getDefaultFoodValues(ItemStack itemStack) {
        ItemFood itemFood = (ItemFood) itemStack.getItem();
        int hunger = itemFood.getHealAmount(itemStack);
        float saturationModifier = itemFood.getSaturationModifier(itemStack);

        return new BasicFoodValues(hunger, saturationModifier);
    }

    public static BasicFoodValues getModifiedFoodValues(ItemStack itemStack, EntityPlayer player) {
        return getDefaultFoodValues(itemStack);
    }

    @AllArgsConstructor
    public static class BasicFoodValues {

        public final int hunger;
        public final float saturationModifier;

        public float getSaturationIncrement() {
            return hunger * saturationModifier * 2f;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BasicFoodValues)) return false;

            BasicFoodValues that = (BasicFoodValues) o;

            return hunger == that.hunger && Float.compare(that.saturationModifier, saturationModifier) == 0;
        }

        @Override
        public int hashCode() {
            int result = hunger;
            result = 31 * result + (saturationModifier != +0.0f ? Float.floatToIntBits(saturationModifier) : 0);
            return result;
        }
    }
}
