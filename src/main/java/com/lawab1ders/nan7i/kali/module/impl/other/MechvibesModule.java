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

import com.lawab1ders.nan7i.kali.events.KeyEvent;
import com.lawab1ders.nan7i.kali.events.cancelable.ClickMouseEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.language.TranslateComponent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.impl.other.mechibes.SoundKey;
import com.lawab1ders.nan7i.kali.module.impl.other.mechibes.SoundPlayer;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.FloatSetting;
import com.lawab1ders.nan7i.kali.utils.Multithreading;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

@IModule(
        name = "module.mechvibes",
        description = "module.mechvibes.desc",
        category = EModuleCategory.OTHER
)
public class MechvibesModule extends Module {

    public final EnumSetting keyboardTypeSetting = new EnumSetting(
            "module.mechvibes.opts.keyboardtype",
            "module.mechvibes.opts.keyboardtype.nkcream",
            "module.mechvibes.opts.keyboardtype.mxblue",
            "module.mechvibes.opts.keyboardtype.mxsilver",
            "module.mechvibes.opts.keyboardtype.razergreen",
            "module.mechvibes.opts.keyboardtype.hyperxaqua",
            "module.mechvibes.opts.keyboardtype.mxblack",
            "module.mechvibes.opts.keyboardtype.toprepurple"
    );

    public final BooleanSetting mouseSetting = new BooleanSetting("module.mechvibes.opts.mouse", true);

    public final FloatSetting mouseVolumeSetting = new FloatSetting(
            "module.mechvibes.opts.mousevolume", 0.3F, 0.1F, 1.0F);

    public final FloatSetting keyboardVolumeSetting = new FloatSetting(
            "module.mechvibes.opts.keyboardvolume", 1.0F, 0.1F, 1.0F);

    private final HashMap<String, HashMap<String, SoundKey>> keyMaps = new HashMap<>();
    private final SoundPlayer mouseSound = new SoundPlayer();
    private float lastMouseVolume, lastKeyboardVolume;

    public MechvibesModule() {
        super();

        Multithreading multithreading = new Multithreading();

        for (TranslateComponent entry : keyboardTypeSetting.getEntries()) {
            multithreading.runAsync(() -> {
                String type = entry.getKey().replace("module.mechvibes.opts.keyboardtype.", "");
                HashMap<String, SoundKey> keyMap = new HashMap<>();

                keyMap.put("tab", new SoundKey(type, "tab"));
                keyMap.put("backspace", new SoundKey(type, "backspace"));
                keyMap.put("capslock", new SoundKey(type, "capslock"));
                keyMap.put("enter", new SoundKey(type, "enter"));
                keyMap.put("space", new SoundKey(type, "space"));
                keyMap.put("shift", new SoundKey(type, "shift"));

                for (int keyCode = 1; keyCode <= 5; keyCode++) {
                    keyMap.put(String.valueOf(keyCode), new SoundKey(type, String.valueOf(keyCode)));
                }

                keyMaps.put(type, keyMap);
            });
        }

        multithreading.runAsync(() -> {
            try {
                mouseSound.loadClip("mouse");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @SubscribeEvent
    public void onKey(KeyEvent event) {
        int keyCode = event.getKeyCode();

        HashMap<String, SoundKey> keyMap = keyMaps.get(
                keyboardTypeSetting.getSelectedEntryKey().replace("module.mechvibes.opts.keyboardtype.", ""));

        SoundKey soundKey;

//        if (keyMap == null) {
//            throw  new RuntimeException("Audio files' keyMap was null");
//        }

        if (keyCode == Keyboard.KEY_TAB) {
            soundKey = keyMap.get("tab");
        }
        else if (keyCode == 14) {
            soundKey = keyMap.get("backspace");
        }
        else if (keyCode == 58) {
            soundKey = keyMap.get("capslock");
        }
        else if (keyCode == 28) {
            soundKey = keyMap.get("enter");
        }
        else if (keyCode == Keyboard.KEY_SPACE) {
            soundKey = keyMap.get("space");
        }
        else if (keyCode == Keyboard.KEY_LSHIFT || keyCode == Keyboard.KEY_RSHIFT) {
            soundKey = keyMap.get("shift");
        }
        else soundKey = keyMap.get(String.valueOf(RandomUtils.nextInt(1, 5)));

        soundKey.play();
    }

    @SubscribeEvent
    public void onClickMouse(ClickMouseEvent event) {
        if (!mouseSetting.isActivated()) return;

        // 鼠标音效
        if (event.getButton() == 0 || event.getButton() == 1) {
            mouseSound.play();
        }
    }

    @SubscribeEvent
    public void onClientTicked(ClientTickedEvent event) {

        // 调节音量
        if (lastKeyboardVolume != keyboardVolumeSetting.getValue()) {
            lastKeyboardVolume = keyboardVolumeSetting.getValue();

            for (String outerKey : keyMaps.keySet()) {
                HashMap<String, SoundKey> innerMap = keyMaps.get(outerKey);

                for (String innerKey : innerMap.keySet()) {
                    SoundKey soundKey = innerMap.get(innerKey);
                    soundKey.setVolume(lastKeyboardVolume);
                }
            }
        }

        if (lastMouseVolume != mouseVolumeSetting.getValue()) {
            lastMouseVolume = mouseVolumeSetting.getValue();

            mouseSound.setVolume(lastMouseVolume);
        }
    }
}
