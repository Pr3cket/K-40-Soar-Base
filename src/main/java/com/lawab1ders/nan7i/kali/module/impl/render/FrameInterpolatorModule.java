package com.lawab1ders.nan7i.kali.module.impl.render;

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
import com.lawab1ders.nan7i.kali.injection.interfaces.IMinecraft;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.EnumSetting;
import lombok.val;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@IModule(
        name = "module.frameinterpolator",
        description = "module.frameinterpolator.desc",
        category = EModuleCategory.RENDER
)
public class FrameInterpolatorModule extends Module {

    public static final File OPENCV_DIR = new File(InstanceAccess.rootFile, "opencv");

    private static final int INTERPOLATION_FREQUENCY = 2;

    public static boolean isOpenCVLoaded = false;

    public EnumSetting modeSetting = new EnumSetting(
            "module.opts.mode",
            "module.frameinterpolator.opts.mode.timelinear",
            "module.frameinterpolator.opts.mode.opticflow",
            "module.frameinterpolator.opts.mode.memc"
    );

    private BufferedImage prevFrame, interpolatedFrame;
    private Future<BufferedImage> interpolationFuture;
    private ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            100, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
    );

    private int prevTextureID = -1, interpolatedTextureID = -1;

    @Override
    public void onOpenSettings() {
        if (isOpenCVLoaded || isNotified()) return;

        val dir = FrameInterpolatorModule.OPENCV_DIR;

        try {
            if (!dir.exists()) Files.createDirectories(dir.toPath());
            Desktop.getDesktop().open(dir);
            setNotified(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        if (!isOpenCVLoaded) {
            postNotificationAndDisableModule("module.frameinterpolator.noti");
            return;
        }

        // 初始化线程池并添加监控
        if (executorService.isShutdown()) {
            executorService = new ThreadPoolExecutor(
                    1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                    r -> {
                        Thread t = new Thread(r);
                        t.setUncaughtExceptionHandler((thread, e) -> {
                            System.err.println("Frame interpolation thread crashed: " + e.getMessage());
                        });

                        return t;
                    }
            );
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        // 释放纹理资源
        if (prevTextureID != -1) {
            deleteTexture(prevTextureID);
            prevTextureID = -1;
        }

        if (interpolatedTextureID != -1) {
            deleteTexture(interpolatedTextureID);
            interpolatedTextureID = -1;
        }
    }

    // Call at: EntityRenderer/renderWorld
    public void interpolateFrame() {
        try {
            // 处理插值帧
            if (interpolatedFrame != null) {
                if (interpolatedTextureID == -1) {
                    interpolatedTextureID = this.createTexture(interpolatedFrame);
                }

                renderTexture(interpolatedTextureID);
            }

            // 捕获当前帧
            BufferedImage currFrame = this.captureFrame();

            // 执行插值计算
            if (prevFrame != null && ((IMinecraft) mc).k40$getFPS() % INTERPOLATION_FREQUENCY == 0) {
                if (interpolationFuture == null || interpolationFuture.isDone()) {
                    interpolationFuture = executorService.submit(() -> {
                        switch (modeSetting.getSelectedEntryKey()) {

                            // Motion Compensation
                            case "module.frameinterpolator.opts.mode.memc":
                                return motionCompensation(prevFrame, currFrame);

                            // Optic Flow
                            case "module.frameinterpolator.opts.mode.opticflow":
                                return opticalFlow(prevFrame, currFrame);

                            // Time Linear
                            default:
                                return linearInterpolation(prevFrame, currFrame);
                        }
                    });
                }

                if (interpolationFuture.isDone()) {
                    try {
                        interpolatedFrame = interpolationFuture.get();
                        if (interpolatedTextureID != -1) {
                            updateTexture(interpolatedTextureID, interpolatedFrame);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 渲染当前帧
            if (prevTextureID != -1) {
                renderTexture(prevTextureID);
                deleteTexture(prevTextureID);
            }

            prevTextureID = createTexture(currFrame);

            // 更新帧状态
            prevFrame = currFrame;
            ((IMinecraft) mc).k40$increaseFPS();

        } catch (Exception e) {
            e.printStackTrace();

            // 清理资源
            if (prevTextureID != -1) {
                deleteTexture(prevTextureID);
                prevTextureID = -1;
            }

            if (interpolatedTextureID != -1) {
                deleteTexture(interpolatedTextureID);
                interpolatedTextureID = -1;
            }
        }
    }

    //---------------------------------------------------------------------------
    // 风起长安
    //---------------------------------------------------------------------------

    private BufferedImage opticalFlow(BufferedImage prevFrame, BufferedImage currFrame) {
        Mat prevMat = bufferedImage2Mat(prevFrame);
        Mat currMat = bufferedImage2Mat(currFrame);

        // 创建光流对象
        Mat flowMat = new Mat();
        Video.calcOpticalFlowFarneback(
                prevMat, currMat, flowMat,
                0.5, 3, 15, 3, 5, 1.2, 0
        );

        // 创建插值帧
        Mat interpolatedMat = new Mat();
        Core.addWeighted(prevMat, 0.5, currMat, 0.5, 0, interpolatedMat);

        return mat2BufferedImage(interpolatedMat);
    }

    private BufferedImage linearInterpolation(BufferedImage prevFrame, BufferedImage currFrame) {
        Mat prevMat = bufferedImage2Mat(prevFrame);
        Mat currMat = bufferedImage2Mat(currFrame);

        Mat result = new Mat();
        Core.addWeighted(prevMat, 0.5, currMat, 0.5, 0, result);

        return mat2BufferedImage(result);
    }

    private BufferedImage motionCompensation(BufferedImage prevFrame, BufferedImage currFrame) {
        Mat prevMat = bufferedImage2Mat(prevFrame);
        Mat currMat = bufferedImage2Mat(currFrame);

        // 计算光流
        Mat flow = new Mat();
        Video.calcOpticalFlowFarneback(
                prevMat, currMat, flow,
                0.5, 3, 15, 3, 5, 1.2, 0
        );

        // 创建补偿后的帧
        Mat compensatedMat = new Mat(prevMat.size(), prevMat.type());

        // 将光流转换为映射矩阵
        Mat mapX = new Mat(flow.size(), CvType.CV_32F);
        Mat mapY = new Mat(flow.size(), CvType.CV_32F);

        for (int y = 0; y < flow.rows(); y++) {
            for (int x = 0; x < flow.cols(); x++) {
                double[] vec = flow.get(y, x);
                mapX.put(y, x, x + vec[0]);
                mapY.put(y, x, y + vec[1]);
            }
        }

        // 使用 Imgproc.remap 进行运动补偿
        Imgproc.remap(prevMat, compensatedMat, mapX, mapY, Imgproc.INTER_LINEAR);

        return mat2BufferedImage(compensatedMat);
    }

    //---------------------------------------------------------------------------
    // 哺乳室
    //---------------------------------------------------------------------------

    private Mat bufferedImage2Mat(BufferedImage image) {
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    private BufferedImage mat2BufferedImage(Mat mat) {
        byte[] data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];
        mat.get(0, 0, data);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }

    private BufferedImage captureFrame() {
        int width = mc.displayWidth;
        int height = mc.displayHeight;

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        buffer.clear();

        GL11.glReadBuffer(GL11.GL_FRONT);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1); // 设置像素存储模式

        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        buffer.rewind(); // 重置缓冲区位置

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = (y * width + x) * 4;
                int r = buffer.get(index) & 0xFF;
                int g = buffer.get(index + 1) & 0xFF;
                int b = buffer.get(index + 2) & 0xFF;
                int a = buffer.get(index + 3) & 0xFF;
                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, height - y - 1, argb);
            }
        }

        return image;
    }

    private int createTexture(BufferedImage image) {
        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        ByteBuffer buffer = convertImageToByteBuffer(image);

//        System.out.println("Texture data: " + buffer.remaining() + " bytes");

        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE, buffer
        );

        return textureID;
    }

    private ByteBuffer convertImageToByteBuffer(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        // 修改为每个像素 4 字节（RGBA）
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));  // R
                buffer.put((byte) ((pixel >> 8) & 0xFF));   // G
                buffer.put((byte) (pixel & 0xFF));          // B
                buffer.put((byte) ((pixel >> 24) & 0xFF));  // A
            }
        }

        buffer.flip();
        return buffer;
    }

    private void updateTexture(int textureID, BufferedImage image) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        ByteBuffer buffer = convertImageToByteBuffer(image);

        // 修改为使用 GL_RGBA 格式
        GL11.glTexSubImage2D(
                GL11.GL_TEXTURE_2D, 0, 0, 0, image.getWidth(), image.getHeight(), GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE, buffer
        );
    }

    private void renderTexture(int textureID) {
        // 保存当前 OpenGL 状态
        boolean texture2DEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);

        // 检查纹理 ID 有效性
        if (textureID <= 0) {
            System.err.println("Invalid texture ID: " + textureID);
            return;
        }

        // 确保正确的 OpenGL 状态
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        // 设置正确的矩阵模式
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, mc.displayWidth, mc.displayHeight, 0, -1, 1);

        // 渲染四边形
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(mc.displayWidth, 0);
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(mc.displayWidth, mc.displayHeight);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(0, mc.displayHeight);
        GL11.glEnd();

        // 恢复矩阵状态
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();

        // 恢复纹理状态
        if (!texture2DEnabled) GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private void deleteTexture(int textureID) {
        GL11.glDeleteTextures(textureID);
    }
}
