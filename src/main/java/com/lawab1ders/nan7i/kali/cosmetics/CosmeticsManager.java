package com.lawab1ders.nan7i.kali.cosmetics;

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

import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.util.*;

import static com.lawab1ders.nan7i.kali.InstanceAccess.mc;
import static com.lawab1ders.nan7i.kali.InstanceAccess.nvg;

public class CosmeticsManager extends ArrayList<Cosmetics> {

    private final Map<ECosmeticsCategory, List<Cosmetics>> COSMETICS_CATEGORIES = new HashMap<>();

    @Getter
    @Setter
    private Cosmetics currentCosmetics;

    public CosmeticsManager() {

        // 导入模块并按字母顺序排序
        addCosmetics();
        sort(Comparator.comparing(c -> c.getName().getKey()));

        for (ECosmeticsCategory c : ECosmeticsCategory.values()) {
            COSMETICS_CATEGORIES.put(c, new ArrayList<>());
        }

        for (Cosmetics m : this) {
            getCosmetics(m.getCategory()).add(m);
        }

        // 加载图像
        for (Cosmetics cosmetics : this) {

            if (cosmetics.getModel() != null) {
                nvg.loadImage(cosmetics.getModel());
            }

            if (cosmetics.getModel() != null) {
                mc.getTextureManager().bindTexture(cosmetics.getModel());
            }
        }
    }

    private void addCosmetics() {

        // Minecraft 年度盛会
        add("cosmetics.minecon.2011", "2011", ECosmeticsCategory.MINECON);
        add("cosmetics.minecon.2012", "2012", ECosmeticsCategory.MINECON);
        add("cosmetics.minecon.2013", "2013", ECosmeticsCategory.MINECON);
        add("cosmetics.minecon.2015", "2015", ECosmeticsCategory.MINECON);
        add("cosmetics.minecon.2016", "2016", ECosmeticsCategory.MINECON);
        add("cosmetics.minecon.unused2011-1", "Unused2011-1", ECosmeticsCategory.MINECON);
        add("cosmetics.minecon.unused2011-2", "Unused2011-2", ECosmeticsCategory.MINECON);
        add("cosmetics.minecon.unused2011-3", "Unused2011-3", ECosmeticsCategory.MINECON);

        // 高清修复
        add("cosmetics.optifine.standard", "standard", ECosmeticsCategory.OPTIFINE);
        add("cosmetics.optifine.black", "black", ECosmeticsCategory.OPTIFINE);
        add("cosmetics.optifine.blue", "blue", ECosmeticsCategory.OPTIFINE);
        add("cosmetics.optifine.cyan", "cyan", ECosmeticsCategory.OPTIFINE);
        add("cosmetics.optifine.gray", "gray", ECosmeticsCategory.OPTIFINE);
        add("cosmetics.optifine.green", "green", ECosmeticsCategory.OPTIFINE);
        add("cosmetics.optifine.purple", "purple", ECosmeticsCategory.OPTIFINE);
        add("cosmetics.optifine.red", "red", ECosmeticsCategory.OPTIFINE);

        // 头像
        add("cosmetics.head.eldodebug", "EldoDebug", ECosmeticsCategory.HEAD);
        add("cosmetics.head.nan7i", "Nan7i", ECosmeticsCategory.HEAD);
        add("cosmetics.head.steve", "Steve", ECosmeticsCategory.HEAD);
        add("cosmetics.head.alex", "Alex", ECosmeticsCategory.HEAD);
        add("cosmetics.head.creeper", "creeper", ECosmeticsCategory.HEAD);
        add("cosmetics.head.skeleton", "skeleton", ECosmeticsCategory.HEAD);
        add("cosmetics.head.spider", "spider", ECosmeticsCategory.HEAD);
        add("cosmetics.head.zombie", "zombie", ECosmeticsCategory.HEAD);
        add("cosmetics.head.villager", "villager", ECosmeticsCategory.HEAD);

        // Java 版披风
        add("cosmetics.misc.oldmojang", "OldMojang", ECosmeticsCategory.MISC);
        add("cosmetics.misc.newmojang", "NewMojang", ECosmeticsCategory.MISC);
        add("cosmetics.misc.mojangstudios", "MojangStudios", ECosmeticsCategory.MISC);
        add("cosmetics.misc.xmas", "Xmas", ECosmeticsCategory.MISC);
        add("cosmetics.misc.newyears", "NewYears", ECosmeticsCategory.MISC);
        add("cosmetics.misc.bacon", "Bacon", ECosmeticsCategory.MISC);
        add("cosmetics.misc.1mill", "1Mill", ECosmeticsCategory.MISC);
        add("cosmetics.misc.dannybstyle", "dannyBstyle", ECosmeticsCategory.MISC);
        add("cosmetics.misc.julianclark", "JulianClark", ECosmeticsCategory.MISC);
        add("cosmetics.misc.translator_crowdin", "Translator_Crowdin", ECosmeticsCategory.MISC);
        add("cosmetics.misc.translator_japan", "Translator_Japan", ECosmeticsCategory.MISC);
        add("cosmetics.misc.scrolls", "Scrolls", ECosmeticsCategory.MISC);
        add("cosmetics.misc.bugtracker", "BugTracker", ECosmeticsCategory.MISC);
        add("cosmetics.misc.translator_chinese", "Translator_Chinese", ECosmeticsCategory.MISC);
        add("cosmetics.misc.realms", "Realms", ECosmeticsCategory.MISC);
        add("cosmetics.misc.cobalt", "Cobalt", ECosmeticsCategory.MISC);
        add("cosmetics.misc.mrmessiah", "MrMessiah", ECosmeticsCategory.MISC);
        add("cosmetics.misc.prismarine", "Prismarine", ECosmeticsCategory.MISC);
        add("cosmetics.misc.turtle", "Turtle", ECosmeticsCategory.MISC);
        add("cosmetics.misc.ms", "MS", ECosmeticsCategory.MISC);
        add("cosmetics.misc.cubed", "Cubed", ECosmeticsCategory.MISC);
        add("cosmetics.misc.gr8_escape", "Gr8_Escape", ECosmeticsCategory.MISC);
        add("cosmetics.misc.valentine", "Valentine", ECosmeticsCategory.MISC);
        add("cosmetics.misc.vanilla", "Vanilla", ECosmeticsCategory.MISC);
        add("cosmetics.misc.cherryblossom", "CherryBlossom", ECosmeticsCategory.MISC);
        add("cosmetics.misc.15year", "15Year", ECosmeticsCategory.MISC);
        add("cosmetics.misc.twitch", "Twitch", ECosmeticsCategory.MISC);
        add("cosmetics.misc.tiktok", "TikTok", ECosmeticsCategory.MISC);
        add("cosmetics.misc.sniffer", "Sniffer", ECosmeticsCategory.MISC);
        add("cosmetics.misc.snail", "Snail", ECosmeticsCategory.MISC);
        add("cosmetics.misc.frog", "Frog", ECosmeticsCategory.MISC);
        add("cosmetics.misc.mcchampionship", "MCChampionship", ECosmeticsCategory.MISC);
        add(
                "cosmetics.misc.minecraftexperiencevillagerrescue", "MinecraftExperienceVillagerRescue",
                ECosmeticsCategory.MISC
        );
        add("cosmetics.misc.eyeblossom", "Eyeblossom", ECosmeticsCategory.MISC);

        // 基岩版披风
        add("cosmetics.misc.minecraftmarketplace", "MinecraftMarketPlace", ECosmeticsCategory.MISC);
        add("cosmetics.misc.pancape", "Pancape", ECosmeticsCategory.MISC);
        add("cosmetics.misc.progresspride", "ProgressPride", ECosmeticsCategory.MISC);
        add("cosmetics.misc.xboxbirthday", "XboxBirthday", ECosmeticsCategory.MISC);
    }

    private void add(String name, String model, ECosmeticsCategory category) {
        String cosmeticPath = "textures/cosmetics/" + category.name().toLowerCase() + "/";

        add(new Cosmetics(
                TranslateComponent.i18n_component(name), category,
                new ResourceLocation("k40", cosmeticPath + model + ".png")
        ));
    }

    public List<Cosmetics> getCosmetics(ECosmeticsCategory cosmeticsCategory) {
        return COSMETICS_CATEGORIES.get(cosmeticsCategory);
    }
}
