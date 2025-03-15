package com.lawab1ders.nan7i.kali.module.impl.hud.rearview;

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
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinMinecraft;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

public class RearviewCamera implements InstanceAccess {

    private final int mirrorFBO;
    private final int mirrorTex;
    private final int mirrorDepth;
    private final RenderGlobalHelper mirrorRenderGlobal;
    private long renderEndNanoTime;

    @Setter
    @Getter
    private boolean firstUpdate, recording, lockCamera;

    public RearviewCamera() {
        mirrorFBO = ARBFramebufferObject.glGenFramebuffers();
        mirrorTex = GL11.glGenTextures();
        mirrorDepth = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorTex);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, 800, 600, 0, GL11.GL_RGBA, GL11.GL_INT,
                (IntBuffer) null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorDepth);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, 800, 600, 0, GL11.GL_DEPTH_COMPONENT,
                GL11.GL_INT, (IntBuffer) null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        mirrorRenderGlobal = new RenderGlobalHelper();
        lockCamera = true;
    }

    public void updateMirror() {

        int w, h;
        float y, py, p, pp;
        boolean hide;
        int view, limit;
        long endTime = 0;

        GuiScreen currentScreen;

        if (!this.firstUpdate) {
            mc.renderGlobal.loadRenderers();
            this.firstUpdate = true;
        }

        w = mc.displayWidth;
        h = mc.displayHeight;
        y = ((IMixinMinecraft) mc).k40$getRenderViewEntity().rotationYaw;
        py = ((IMixinMinecraft) mc).k40$getRenderViewEntity().prevRotationYaw;
        p = ((IMixinMinecraft) mc).k40$getRenderViewEntity().rotationPitch;
        pp = ((IMixinMinecraft) mc).k40$getRenderViewEntity().prevRotationPitch;
        hide = mc.gameSettings.hideGUI;
        view = mc.gameSettings.thirdPersonView;
        limit = mc.gameSettings.limitFramerate;
        currentScreen = mc.currentScreen;

        switchToFB();

        if (limit != 0) {
            endTime = renderEndNanoTime;
        }

        mc.currentScreen = null;
        mc.displayHeight = 600;
        mc.displayWidth = 800;
        mc.gameSettings.hideGUI = true;
        mc.gameSettings.thirdPersonView = 0;
        mc.gameSettings.limitFramerate = 0;

        ((IMixinMinecraft) mc).k40$getRenderViewEntity().rotationYaw += 180;
        ((IMixinMinecraft) mc).k40$getRenderViewEntity().prevRotationYaw += 180;

        if (lockCamera) {
            ((IMixinMinecraft) mc).k40$getRenderViewEntity().rotationPitch = 0;
            ((IMixinMinecraft) mc).k40$getRenderViewEntity().prevRotationPitch = 0;
        } else {
            ((IMixinMinecraft) mc).k40$getRenderViewEntity().rotationPitch = -p + 18;
            ((IMixinMinecraft) mc).k40$getRenderViewEntity().prevRotationPitch = -pp + 18;
        }

        recording = true;
        mirrorRenderGlobal.switchTo();

        GL11.glPushAttrib(272393);

        mc.entityRenderer.renderWorld(((IMixinMinecraft) mc).k40$getTimer().renderPartialTicks, System.nanoTime());
        mc.entityRenderer.setupOverlayRendering();

        if (limit != 0) {
            renderEndNanoTime = endTime;
        }

        GL11.glPopAttrib();

        mirrorRenderGlobal.switchFrom();
        recording = false;

        mc.currentScreen = currentScreen;
        ((IMixinMinecraft) mc).k40$getRenderViewEntity().rotationYaw = y;
        ((IMixinMinecraft) mc).k40$getRenderViewEntity().prevRotationYaw = py;
        ((IMixinMinecraft) mc).k40$getRenderViewEntity().rotationPitch = p;
        ((IMixinMinecraft) mc).k40$getRenderViewEntity().prevRotationPitch = pp;
        mc.gameSettings.limitFramerate = limit;
        mc.gameSettings.thirdPersonView = view;
        mc.gameSettings.hideGUI = hide;
        mc.displayWidth = w;
        mc.displayHeight = h;

        switchFromFB();
    }

    private void switchToFB() {

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();

        OpenGlHelper.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, mirrorFBO);
        OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER,
                OpenGlHelper.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D,
                mirrorTex, 0);
        OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER,
                OpenGlHelper.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D,
                mirrorDepth, 0);
    }

    private void switchFromFB() {
        OpenGlHelper.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, 0);
    }

    public int getTexture() {
        return mirrorTex;
    }

}
