package com.lawab1ders.nan7i.kali.module;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.ProfileCategory;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.*;
import com.lawab1ders.nan7i.kali.notification.NotificationType;
import com.lawab1ders.nan7i.kali.utils.ColorUtils;
import com.lawab1ders.nan7i.kali.utils.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

@Getter
public class Module implements InstanceAccess {

    public static Module module;
    public static int hudLayer = 0;

    private final IModule info;

    private final EModuleCategory category;
    private final boolean flash;
    private final ArrayList<ModuleSetting> settings = new ArrayList<>();
    private final TranslateComponent nameTranslate, descriptionTranslate;
    private final Animation animation = new Animation();

    // 防止当模块激活时未完全初始化
    private final boolean preActivated;
    private boolean activated;

    @Setter
    private KeybindSetting keybindSetting;

    @Setter
    private boolean notified = false;

    protected Module() {
        if (getClass().isAnnotationPresent(IModule.class)) {
            info = getClass().getAnnotation(IModule.class);

            preActivated = info.activated();
            this.nameTranslate = TranslateComponent.i18n_component(info.name());
            this.descriptionTranslate = TranslateComponent.i18n_component(info.description());
            this.category = info.category();
            this.flash = info.flash();

            Module.module = this;

            if (info.keyBind() != -114514) {
                keybindSetting = new KeybindSetting(info.keyBind());
                ModuleManager.HAS_KEYBIND.add(this);

                if (flash) {
                    ModuleManager.FLASH.add(this);
                }
            }

            if (this instanceof HUDModule) {
                HUDModule hudModule = (HUDModule) this;

                ModuleManager.HUD.add(hudModule);

                if (getClass().isAnnotationPresent(IHUDModuleScaleLimiter.class)) {
                    IHUDModuleScaleLimiter lmt = getClass().getAnnotation(IHUDModuleScaleLimiter.class);

                    hudModule.setScale(lmt.min() + (lmt.max() - lmt.min()) / 2f);
                    hudModule.setMinScale(lmt.min());
                    hudModule.setMaxScale(lmt.max());
                }
            }
        }
        else {
            throw new RuntimeException("The module "
                                               + getClass().getName()
                                               + " without IModule annotation cannot be imported");
        }
    }

    protected void postNotificationAndDisableModule(String message) {
        ntf.post(this.getName(), TranslateComponent.i18n(message), NotificationType.ERROR);
        setActivated(false);
    }

    protected void postNotificationOnlyOnce(String message, NotificationType type) {
        if (!notified) ntf.post(this.getName(), TranslateComponent.i18n(message), type);
        notified = true;
    }

    public void setActivated(boolean activated) {
        if (this.activated == activated) return;

        this.activated = activated;
        ProfileCategory.currentConfig = null;

        setNotified(false);

        if (activated) {
            onEnable();
            MinecraftForge.EVENT_BUS.register(this);
        }
        else {
            onDisable();
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    public void toggle() {
        setActivated(!activated);
    }

    public String getName() {
        return nameTranslate.getText();
    }

    public String getNameKey() {
        return nameTranslate.getKey();
    }

    public String getDescription() {
        return descriptionTranslate.getText();
    }

    //---------------------------------------------------------------------------
    // #覆写事件
    //---------------------------------------------------------------------------

    protected void onEnable() {}

    protected void onDisable() {}

    public void onOpenSettings() {}

    public void onLoadConfig(JsonObject jsonObject) {
        if (jsonObject.has("activated")) setActivated(jsonObject.get("activated").getAsBoolean());
        if (jsonObject.has("key_bind") && keybindSetting != null) {
            keybindSetting.setKeyCode(jsonObject.get("key_bind").getAsInt());
        }

        if (this instanceof HUDModule) {
            HUDModule module = (HUDModule) this;

            module.setVX(0);
            module.setVY(0);
            module.setScale(3);

            if (jsonObject.has("x")) {
                module.setVX(jsonObject.get("x").getAsInt());
            }

            if (jsonObject.has("y")) {
                module.setVY(jsonObject.get("y").getAsInt());
            }

            if (jsonObject.has("scale")) {
                module.setScale(jsonObject.get("scale").getAsFloat());
            }
        }

        if (settings.isEmpty() || !jsonObject.has("settings")) return;
        JsonElement jsonElement = jsonObject.get("settings");

        if (!jsonElement.isJsonObject()) return;
        JsonObject subJsonObject = jsonElement.getAsJsonObject();

        settings.forEach(setting -> {
            String name = setting.getNameKey();

            if (subJsonObject.has(name)) {
                JsonElement subJsonElement = subJsonObject.get(name);

                if (setting instanceof BooleanSetting) {

                    ((BooleanSetting) setting).setActivated(subJsonElement.getAsBoolean());

                }
                else if (setting instanceof EnumSetting) {

                    ((EnumSetting) setting).setSelectedEntry(subJsonElement.getAsString());

                }
                else if (setting instanceof FloatSetting) {

                    setting.setValue(subJsonElement.getAsFloat());

                }
                else if (setting instanceof IntSetting) {

                    setting.setValue(subJsonElement.getAsInt());

                }
                else if (setting instanceof ColorSetting) {

                    ((ColorSetting) setting).setColor(ColorUtils.getColorByInt(subJsonElement.getAsInt()));

                }
                else if (setting instanceof TextSetting) {

                    ((TextSetting) setting).setText(subJsonElement.getAsString());

                }
                else if (setting instanceof KeybindSetting) {

                    ((KeybindSetting) setting).setKeyCode(subJsonElement.getAsInt());
                }
            }
        });
    }

    public void onSaveConfig(JsonObject jsonObject, boolean isCreate) {
        jsonObject.addProperty("activated", isCreate ? info.activated() : activated);

        if (keybindSetting != null) {
            jsonObject.addProperty("key_bind", isCreate ? info.keyBind() : keybindSetting.getKeyCode());
        }

        if (this instanceof HUDModule) {
            jsonObject.addProperty("x", ((HUDModule) this).getVX());
            jsonObject.addProperty("y", ((HUDModule) this).getVY());
            jsonObject.addProperty("scale", ((HUDModule) this).getScale());
        }

        if (settings.isEmpty()) return;
        JsonObject subJsonObject = new JsonObject();

        settings.forEach(setting -> {
            String name = setting.getNameKey();

            if (setting instanceof BooleanSetting) {

                subJsonObject.addProperty(
                        name, isCreate ? ((BooleanSetting) setting).isDefActivated() :
                                ((BooleanSetting) setting).isActivated()
                );

            }
            else if (setting instanceof EnumSetting) {

                subJsonObject.addProperty(
                        name, isCreate ? ((EnumSetting) setting).getDefEntryKey() :
                                ((EnumSetting) setting).getSelectedEntryKey()
                );

            }
            else if (setting instanceof FloatSetting) {

                subJsonObject.addProperty(
                        name, isCreate ? ((FloatSetting) setting).getDefValue() :
                                ((FloatSetting) setting).getValue()
                );

            }
            else if (setting instanceof IntSetting) {

                subJsonObject.addProperty(
                        name, isCreate ? ((IntSetting) setting).getDefValue() :
                                ((IntSetting) setting).getValue()
                );

            }
            else if (setting instanceof ColorSetting) {

                subJsonObject.addProperty(
                        name, isCreate ? ((ColorSetting) setting).getDefColor().getRGB() :
                                ((ColorSetting) setting).getColor().getRGB()
                );
            }
            else if (setting instanceof TextSetting) {

                subJsonObject.addProperty(
                        name, isCreate ? ((TextSetting) setting).getDefText() :
                                ((TextSetting) setting).getText()
                );
            }
            else if (setting instanceof KeybindSetting) {

                subJsonObject.addProperty(
                        name, isCreate ? ((KeybindSetting) setting).getDefKeyCode() :
                                ((KeybindSetting) setting).getKeyCode()
                );
            }
        });

        jsonObject.add("settings", subJsonObject);
    }
}
