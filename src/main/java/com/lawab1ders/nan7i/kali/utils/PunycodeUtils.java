package com.lawab1ders.nan7i.kali.utils;

/*
 * Copyright (c) 2025 EldoDebug, Nan7.南起
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
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class PunycodeUtils implements InstanceAccess {

    private static final Map<String, String> PUNYCODE_CACHE = new ConcurrentHashMap<>(256);

    // 定义 Punycode 算法所需的常量
    private static final int PUNYCODE_TMIN = 1;
    private static final int PUNYCODE_TMAX = 26;
    private static final int PUNYCODE_SKEW = 38;
    private static final int PUNYCODE_DAMP = 700;
    private static final int PUNYCODE_INITIAL_BIAS = 72;
    private static final int PUNYCODE_INITIAL_N = 128;

    public static String punycode(String url) {
        return PUNYCODE_CACHE.computeIfAbsent(url, PunycodeUtils::encodeUrl);
    }

    /**
     * 将 URL 中的域名部分进行 Punycode 编码
     *
     * @param url 输入的 URL
     */
    private static String encodeUrl(String url) {
        int protoEnd = url.indexOf("://");
        protoEnd = protoEnd < 0 ? 0 : protoEnd + 3;

        int hostEnd = Math.max(url.indexOf('/', protoEnd), url.length());
        String hostname = url.substring(protoEnd, hostEnd);

        // 使用流式 API 检查是否需要转换
        boolean doTransform = hostname.chars().anyMatch(c -> c >= 128);
        if (!doTransform) {
            return url;
        }

        // 分割主机名
        String[] parts = hostname.split("\\.");
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        // 添加协议部分
        sb.append(url, 0, protoEnd);

        for (String p : parts) {
            // 检查当前部分是否需要转换
            doTransform = checkIfNeedsTransform(p);

            if (!first) {
                sb.append('.');
            }

            if (doTransform) {
                sb.append(punycodeEncodeString(p.codePoints().toArray()));
            }
            else {
                sb.append(p);
            }

            first = false;
        }

        // 添加剩余部分
        sb.append(url, hostEnd, url.length());
        return sb.toString();
    }

    /**
     * 检查字符串是否包含需要转换的字符
     *
     * @param str 输入的字符串
     */
    private static boolean checkIfNeedsTransform(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 128) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对输入的代码点数组进行 Punycode 编码
     *
     * @param input 输入的代码点数组
     */
    private static String punycodeEncodeString(int[] input) {
        StringBuilder output = new StringBuilder();

        // 使用流式 API 处理 ASCII 字符
        output.append(new String(input, 0, input.length)
                              .chars()
                              .filter(c -> c < 128)
                              .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append));

        int n = PUNYCODE_INITIAL_N;
        int delta = 0;
        int bias = PUNYCODE_INITIAL_BIAS;
        int h = output.length();
        int b = h;

        if (b > 0) {
            output.append('-');
        }

        while (h < input.length) {
            int m = Integer.MAX_VALUE;

            // 找到下一个最小的非 ASCII 字符
            for (int codePoint : input) {
                if (codePoint >= n && codePoint < m) {
                    m = codePoint;
                }
            }

            delta += (m - n) * (h + 1);
            n = m;

            for (int codePoint : input) {
                if (codePoint < n) {
                    delta++;
                }

                if (codePoint == n) {
                    punycodeEncodeNumber(output, delta, bias);
                    bias = punycodeBiasAdapt(delta, h + 1, h == b);
                    delta = 0;
                    h++;
                }
            }

            delta++;
            n++;
        }

        return "xn--" + output;
    }

    /**
     * 对数字进行 Punycode 编码并追加到输出字符串中
     *
     * @param dst  输出的字符串构建器
     * @param q    要编码的数字
     * @param bias 当前的偏差值
     */
    private static void punycodeEncodeNumber(StringBuilder dst, int q, int bias) {
        boolean keepGoing = true;

        for (int k = 36; keepGoing; k += 36) {
            int t = k - bias;
            t = Math.max(PUNYCODE_TMIN, Math.min(t, PUNYCODE_TMAX));

            int digit;
            if (q < t) {
                digit = q;
                keepGoing = false;
            }
            else {
                digit = t + (q - t) % (36 - t);
                q = (q - t) / (36 - t);
            }

            if (digit < 26) {
                dst.append((char) ('a' + digit));
            }
            else {
                dst.append((char) ('0' + digit - 26));
            }
        }
    }

    /**
     * 调整偏差值
     *
     * @param delta     增量
     * @param numPoints 字符数量
     * @param firstTime 是否是第一次调整
     */
    private static int punycodeBiasAdapt(int delta, int numPoints, boolean firstTime) {
        if (firstTime) {
            delta /= PUNYCODE_DAMP;
        }
        else {
            delta /= 2;
        }

        int k = 0;
        delta += delta / numPoints;

        while (delta > ((36 - PUNYCODE_TMIN) * PUNYCODE_TMAX) / 2) {
            delta /= 36 - PUNYCODE_TMIN;
            k += 36;
        }

        return k + ((36 - PUNYCODE_TMIN + 1) * delta) / (delta + PUNYCODE_SKEW);
    }
}
