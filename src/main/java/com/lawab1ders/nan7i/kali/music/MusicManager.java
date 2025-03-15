package com.lawab1ders.nan7i.kali.music;

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
import lombok.Getter;

import java.io.File;

@Getter
public class MusicManager implements InstanceAccess {

    private final NecCache necCache;

//    @Setter
//    private List<MusicList> recommends;

    public MusicManager() {
        necCache = new NecCache();
    }

    public void save() {
        NecLoginData necLoginData = necCache.getNecLoginData();

        if (necCache.isFetched()) {
            necCache.saveNec();
        } else if (necLoginData != null && necLoginData.getUserId() != 0 && necLoginData.getCookie() != null) {
            // 账号没加载成功
        } else {
            File file = new File(rootFile, "nec_login_status.json");

            if (file.exists()) file.delete();
        }
    }
}
