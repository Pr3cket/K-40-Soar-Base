package com.lawab1ders.nan7i.kali.utils.config;

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

import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum ConfigIcon {

    COMMAND(0, "command_block"),
    CRAFTING_TABLE(1, "crafting_table_front"),
    FURNACE(2, "furnace_front_on"),
    GRASS(3, "grass_side"),
    HAY(4, "hay_block_side"),
    PUMPKIN(5, "pumpkin_face_off"),
    TNT(6, "tnt_side");

    private final Animation animation = new Animation();

    private final int id;
    private final ResourceLocation icon;

    ConfigIcon(int id, String name) {
        this.id = id;
        this.icon = new ResourceLocation("k40", "textures/blocks/" + name + ".png");
    }

    public static ConfigIcon getIconById(int id) {
        // 将 ConfigIcon 枚举的所有值转换为 Stream 流，然后通过 filter 筛选出符合条件的元素
        Optional<ConfigIcon> iconOptional = Stream.of(ConfigIcon.values())
                .filter(pi -> pi.getId() == id)
                .findFirst();

        return iconOptional.orElse(ConfigIcon.COMMAND);
    }
}
