package com.lawab1ders.nan7i.kali.module.impl.other.immanager;

import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public enum LinuxIMFramework {
    IBUS("ibus engine", "libpinyin", "xkb:us::eng"),
    FCITX5("fcitx5-remote", "-o", "-c");

    private final String setStateCommand, onArgName, offArgName;

    public void setState(boolean state) {
        String[] command = (setStateCommand + " " + (state ? onArgName : offArgName)).split(" ");

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
