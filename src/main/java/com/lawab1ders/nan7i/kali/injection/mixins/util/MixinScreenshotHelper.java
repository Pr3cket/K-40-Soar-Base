package com.lawab1ders.nan7i.kali.injection.mixins.util;

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

import com.lawab1ders.nan7i.kali.utils.screenshot.ScreenshotManager;
import com.lawab1ders.nan7i.kali.utils.screenshot.ScreenshotThread;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.IntBuffer;

@Mixin(ScreenShotHelper.class)
public class MixinScreenshotHelper {

    @Shadow
    private static IntBuffer pixelBuffer;

    @Shadow
    private static int[] pixelValues;

    @Inject(
            method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
            at = @At("HEAD"), cancellable = true
    )
    private static void k40$asyncScreenshotModule(File gameDirectory, String screenshotName, int width, int height,
                                                  Framebuffer buffer, CallbackInfoReturnable<IChatComponent> cir) {

        if (OpenGlHelper.isFramebufferEnabled()) {
            width = buffer.framebufferTextureWidth;
            height = buffer.framebufferTextureHeight;
        }

        int scale = width * height;

        if (pixelBuffer == null || pixelBuffer.capacity() < scale) {
            pixelBuffer = BufferUtils.createIntBuffer(scale);
            pixelValues = new int[scale];
        }

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture(buffer.framebufferTexture);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        }
        else {
            GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        }

        pixelBuffer.get(pixelValues);

        ScreenshotManager.getRoot().mkdir();
        File file = ScreenshotThread.getTimestampedPNGFileForDirectory(ScreenshotManager.getRoot());

        new ScreenshotThread(width, height, pixelValues, file).start();

        IChatComponent iChatComponents = new ChatComponentText(file.getName());
        iChatComponents.getChatStyle().setChatClickEvent(new ClickEvent(
                ClickEvent.Action.OPEN_FILE,
                file.getAbsolutePath()
        ));
        iChatComponents.getChatStyle().setUnderlined(true);

        cir.setReturnValue(new ChatComponentTranslation("screenshot.success", iChatComponents));
    }
}
