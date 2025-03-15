package com.lawab1ders.nan7i.kali.module.setting.impl;

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
import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;
import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class EnumSetting extends ModuleSetting<String> {

    @Getter
    private final List<TranslateComponent> entries = new ArrayList<>();

    @Getter
    private int selected = 0, lastSelected = 0;

    private boolean reset = false;

    public EnumSetting(String name, String... entries) {
        super(name);

        for (String entry : entries) {
            this.entries.add(TranslateComponent.i18n_component(entry));
        }
    }

    public EnumSetting(String name, ArrayList<String> entries) {
        super(name);

        for (String entry : entries) {
            this.entries.add(TranslateComponent.i18n_component(entry));
        }
    }

    public String getDefEntryKey() {
        return entries.get(0).getKey();
    }

    public String getSelectedEntryKey() {
        return entries.get(selected).getKey();
    }

    public String getSelectedEntryText() {
        return entries.get(selected).getText();
    }

    public void setSelectedEntry(String entry) {
        if (getSelectedEntryKey().equals(entry))
            return; // if the entry is already selected, return (prevent infinite loop)

        val oldValue = getSelectedEntryKey();
        IntStream.range(0, entries.size())
                 .filter(i -> Objects.equals(entries.get(i).getKey(), entry))
                 .findFirst()
                 .ifPresent(i -> selected = i);

        handleListener(oldValue, getSelectedEntryKey());
    }

    public void next() {
        val oldValue = getSelectedEntryKey();

        selected++;
        if (selected >= entries.size()) selected = 0;

        handleListener(oldValue, getSelectedEntryKey());
    }

    public void last() {
        val oldValue = getSelectedEntryKey();

        selected--;
        if (selected < 0) selected = entries.size() - 1;

        handleListener(oldValue, getSelectedEntryKey());
    }

    @Override
    public void reset() {
        if (selected == 0) return;

        val oldValue = getSelectedEntryKey();

        lastSelected = selected;
        selected = 0;
        reset = true;

        handleListener(oldValue, getSelectedEntryKey());
    }

    public boolean isReset() {
        return reset = false;
    }
}
