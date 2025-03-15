package com.lawab1ders.nan7i.kali.module.setting;

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

import com.lawab1ders.nan7i.kali.InstanceAccess;
import com.lawab1ders.nan7i.kali.KaliAPI;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.ProfileCategory;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.Module;
import lombok.Getter;
import lombok.val;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ModuleSetting<T> implements InstanceAccess {

    @Getter
    private final TranslateComponent nameTranslate;

    private BiConsumer<T, T> changeListener = (oldValue, newValue) -> {};

    protected ModuleSetting(String name) {
        this.nameTranslate = TranslateComponent.i18n_component(name);

        if (Module.module != null) {
            Module.module.getSettings().add(this);
        }
    }

    @SuppressWarnings("ALL")
    public <TA extends ModuleSetting> TA setChangeListener(BiConsumer<T, T> changeListener) {
        this.changeListener = changeListener;
        return (TA) this;
    }

    @SuppressWarnings("ALL")
    public <TA extends ModuleSetting> TA apply(Consumer<TA> consumer) {
        consumer.accept((TA) this);
        return (TA) this;
    }

    protected void handleListener(T oldValue, T newValue) {
        changeListener.accept(oldValue, newValue);

        val instance = KaliAPI.INSTANCE;

        if (this != instance.getHudThemeSetting() && this != instance.getClickGuiKeySetting()) {
            ProfileCategory.currentConfig = null;
        }
    }

    public String getName() {
        return nameTranslate.getText();
    }

    public String getNameKey() {
        return nameTranslate.getKey();
    }

    public abstract void reset();

    // 使用泛型组件类
    @Deprecated
    public T getValue() {
        System.err.println("警告：ModuleSetting.getValue() 不应直接调用，请检查代码逻辑");
        return null;
    }

    @Deprecated
    public void setValue(T value) {
        System.err.println("警告：ModuleSetting.setValue() 不应直接调用，请检查代码逻辑");
    }

    @Deprecated
    public T getMin() {
        System.err.println("警告：ModuleSetting.getMin() 不应直接调用，请检查代码逻辑");
        return null;
    }

    @Deprecated
    public T getMax() {
        System.err.println("警告：ModuleSetting.getMax() 不应直接调用，请检查代码逻辑");
        return null;
    }
}
