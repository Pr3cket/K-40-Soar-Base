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

import com.google.gson.JsonObject;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Objects;

@Getter @Setter
public class Config {

    // 加载配置的轮廓
    private Animation animation = new Animation();
    private Animation starAnimation = new Animation();

    private File file;
    private long lastModified;

    private JsonObject jsonObject;
    private String name = "", serverIp = "";
    private ConfigIcon icon = ConfigIcon.COMMAND;
    private boolean favorite = false;

    public Config(File file, JsonObject jsonObject, String serverIp, ConfigIcon icon, boolean favorite) {
        this.file = file;
        this.lastModified = file.lastModified();

        this.jsonObject = jsonObject;
        this.name = file.getName().replace(".json", "");
        this.serverIp = serverIp;
        this.icon = icon;
        this.favorite = favorite;
    }

    public Config() {
        // empty lol
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        Config c = (Config) obj;

        return Objects.equals(c.name, name) && Objects.equals(c.serverIp, serverIp) && c.icon.getId() == icon.getId() && c.favorite == favorite;
    }
}
