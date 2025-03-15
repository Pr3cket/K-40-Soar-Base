package com.lawab1ders.nan7i.kali.injection.mixins.renderer;

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
import com.lawab1ders.nan7i.kali.events.GammaGotEvent;
import com.lawab1ders.nan7i.kali.events.ZoomFovEvent;
import com.lawab1ders.nan7i.kali.events.cancelable.RotateHeadEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ShaderTickedEvent;
import com.lawab1ders.nan7i.kali.injection.StaticStation;
import com.lawab1ders.nan7i.kali.injection.mixins.IMixinMinecraft;
import com.lawab1ders.nan7i.kali.module.ModuleManager;
import com.lawab1ders.nan7i.kali.module.impl.hud.RearviewModule;
import com.lawab1ders.nan7i.kali.module.impl.player.FreelookModule;
import com.lawab1ders.nan7i.kali.module.impl.player.OverflowAnimationsModule;
import com.lawab1ders.nan7i.kali.module.impl.render.BlockOverlayModule;
import com.lawab1ders.nan7i.kali.module.impl.render.FrameInterpolatorModule;
import com.lawab1ders.nan7i.kali.module.impl.render.MotionCameraModule;
import com.lawab1ders.nan7i.kali.module.impl.render.NoHurtCameraModule;
import com.lawab1ders.nan7i.kali.module.impl.server.AmbienceModule;
import com.lawab1ders.nan7i.kali.utils.animation.CameraAnimation;
import lombok.val;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow
    private Minecraft mc;

    @Shadow
    private float thirdPersonDistance;

    // 隐式写入

    @Unique
    private float k40$rotationYaw;

    @Unique
    private float k40$prevRotationYaw;

    @Unique
    private float k40$rotationPitch;

    @Unique
    private float k40$prevRotationPitch;

    @Unique
    private float k40$height;

    @Unique
    private float k40$previousHeight;

    @Shadow
    protected abstract boolean isDrawBlockOutline();

    @Redirect(
            method = "updateCameraAndRender",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setAngles(FF)V"
            )
    )
    public void k40$updateCameraAndRender(EntityPlayerSP entityPlayerSP, float yaw, float pitch) {
        RotateHeadEvent event = new RotateHeadEvent(yaw, pitch);
        InstanceAccess.safeHandleEvent(event);

        yaw = event.getYaw();
        pitch = event.getPitch();

        if (!event.isCanceled()) {
            entityPlayerSP.setAngles(yaw, pitch);
        }
    }

    @Redirect(
            method = "hurtCameraEffect",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"
            )
    )
    public void k40$adjustHurtCameraEffect(float angle, float x, float y, float z) {

        ModuleManager moduleManager = InstanceAccess.mod;
        RearviewModule rearviewModule = moduleManager.getModule(RearviewModule.class);
        NoHurtCameraModule noHurtCameraModule = moduleManager.getModule(NoHurtCameraModule.class);

        if (noHurtCameraModule.isActivated()) {
            angle *= noHurtCameraModule.intensitySetting.getValue() / 100F;
        }

        if (rearviewModule.isActivated()) {
            angle = 0;
        }

        GlStateManager.rotate(angle, x, y, z);
    }

    @Inject(method = "orientCamera", at = @At("HEAD"))
    public void k40$orientCamera(float partialTicks, CallbackInfo ci) {

        Entity renderViewEntity = mc.getRenderViewEntity();
        FreelookModule freelookModule = InstanceAccess.mod.getModule(FreelookModule.class);

        if (freelookModule.isActivated() && freelookModule.active) {
            k40$prevRotationYaw = freelookModule.yaw;
            k40$prevRotationPitch = freelookModule.pitch;
            k40$rotationYaw = freelookModule.yaw;
            k40$rotationPitch = freelookModule.pitch;

            freelookModule.zoomAnimation.setAnimation(
                    freelookModule.currentFactor, freelookModule.zoomSpeedSetting.getValue());
            thirdPersonDistance =
                    (freelookModule.smoothZoomSetting.isActivated() ? freelookModule.zoomAnimation.getValue() : freelookModule.currentFactor);
        }
        else {
            k40$prevRotationYaw = renderViewEntity.prevRotationYaw;
            k40$prevRotationPitch = renderViewEntity.prevRotationPitch;
            k40$rotationYaw = renderViewEntity.rotationYaw;
            k40$rotationPitch = renderViewEntity.rotationPitch;
            thirdPersonDistance = 4;
        }
    }

    @Inject(method = "addRainParticles", at = @At("HEAD"), cancellable = true)
    public void k40$preAddRainParticles(CallbackInfo ci) {

        AmbienceModule mod = InstanceAccess.mod.getModule(AmbienceModule.class);
        String weather = mod.weatherSetting.getSelectedEntryKey();

        if (mod.isActivated() && (
                weather.equals("module.ambience.opts.weather.clear")
                        || weather.equals("module.ambience.opts.weather.snow"))) {

            ci.cancel();
        }
    }

    @Redirect(
            method = "updateLightmap",
            at = @At(
                    value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;gammaSetting:F"
            )
    )
    public float k40$overrideGamma(GameSettings settings) {

        GammaGotEvent event = new GammaGotEvent(settings.gammaSetting);
        InstanceAccess.safeHandleEvent(event);

        return event.getGamma();
    }

    @Inject(method = "getFOVModifier", at = @At("RETURN"), cancellable = true)
    public void k40$onFovModifier(float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Float> cir) {

        ZoomFovEvent event = new ZoomFovEvent(cir.getReturnValue());
        InstanceAccess.safeHandleEvent(event);

        cir.setReturnValue(event.getFov());
    }

    @Inject(
            method = "updateCameraAndRender",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/shader/Framebuffer;bindFramebuffer(Z)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void k40$addShaders(float partialTicks, long nanoTime, CallbackInfo callback) {
        ShaderTickedEvent event = new ShaderTickedEvent();
        InstanceAccess.safeHandleEvent(event);

        for (ShaderGroup group : event.getGroups()) {
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            group.loadShaderGroup(((IMixinMinecraft) mc).k40$getTimer().renderPartialTicks);
            GlStateManager.popMatrix();
        }
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    public void k40$onRenderWorld(float partialTicks, long nanoTime, CallbackInfo callback) {
        val module = InstanceAccess.mod.getModule(FrameInterpolatorModule.class);

        if (module.isActivated() && InstanceAccess.isInGame()) {
            module.interpolateFrame();
        }
    }

    @Redirect(
            method = "renderWorldPass",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;isInsideOfMaterial(Lnet/minecraft/block/material/Material;)Z",
                    ordinal = 0
            )
    )
    public boolean k40$overrideWetBlockHighlight(Entity entity, Material materialIn) {

        boolean maybeWould = entity.isInsideOfMaterial(materialIn);
        boolean would = maybeWould && isDrawBlockOutline();

        BlockOverlayModule module = InstanceAccess.mod.getModule(BlockOverlayModule.class);

        if (module.isActivated()) {
            module.render(mc.objectMouseOver, ((IMixinMinecraft) mc).k40$getTimer().renderPartialTicks);
        }

        if (maybeWould && module.isActivated()) {
            return false;
        }

        return would;
    }

    @Redirect(
            method = "renderWorldPass",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;isInsideOfMaterial(Lnet/minecraft/block/material/Material;)Z",
                    ordinal = 1
            )
    )
    public boolean k40$overrideBlockHighlight(Entity entity, Material materialIn) {

        boolean totallyWouldNot = entity.isInsideOfMaterial(materialIn);
        boolean wouldNot = totallyWouldNot || !isDrawBlockOutline();

        BlockOverlayModule module = InstanceAccess.mod.getModule(BlockOverlayModule.class);

        if (module.isActivated()) {
            module.render(mc.objectMouseOver, ((IMixinMinecraft) mc).k40$getTimer().renderPartialTicks);
        }

        if (!totallyWouldNot && module.isActivated()) {
            return true;
        }

        return wouldNot;
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationYaw:F"))
    public float k40$getRotationYaw(Entity entity) {
        return k40$rotationYaw;
    }

    @Redirect(
            method = "orientCamera",
            at = @At(
                    value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationYaw:F"
            )
    )
    public float k40$getPrevRotationYaw(Entity entity) {
        return k40$prevRotationYaw;
    }

    @Redirect(
            method = "orientCamera",
            at = @At(
                    value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationPitch:F"
            )
    )
    public float k40$getRotationPitch(Entity entity) {
        return k40$rotationPitch;
    }

    @Redirect(
            method = "orientCamera",
            at = @At(
                    value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationPitch:F"
            )
    )
    public float k40$getPrevRotationPitch(Entity entity) {
        return k40$prevRotationPitch;
    }

    //---------------------------------------------------------------------------
    // Motion Camera
    //---------------------------------------------------------------------------

    @Unique
    private final CameraAnimation k40$cameraPosAnim = new CameraAnimation();

    @Unique
    private double k40$renderX;

    @Unique
    private double k40$renderY;

    @Unique
    private double k40$renderZ;

    @Unique
    private double k40$prevRenderX;

    @Unique
    private double k40$prevRenderY;

    @Unique
    private double k40$prevRenderZ;

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), ordinal = 0)
    private double k40$getRenderX(double renderX) {
        k40$renderX = renderX;
        return renderX;
    }

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), ordinal = 1)
    private double k40$getRenderY(double renderY) {
        k40$renderY = renderY;
        return renderY;
    }

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), ordinal = 2)
    private double k40$getRenderZ(double renderZ) {
        k40$renderZ = renderZ;
        return renderZ;
    }

    @Redirect(
            method = "orientCamera",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V",
                    ordinal = 2
            )
    )
    private void k40$redirectTranslate(float x, float y, float z) {
        val module = InstanceAccess.mod.getModule(MotionCameraModule.class);

        if (!module.isActivated()) {
            GlStateManager.translate(x, y, z);
            return;
        }

        k40$prevRenderX += (k40$renderX - k40$prevRenderX) * module.interpolationSetting.getValue();
        k40$prevRenderY += (k40$renderY - k40$prevRenderY) * module.interpolationSetting.getValue();
        k40$prevRenderZ += (k40$renderZ - k40$prevRenderZ) * module.interpolationSetting.getValue();

        k40$cameraPosAnim.setTargets(
                x - module.xSetting.getValue(),
                y - module.ySetting.getValue(),
                // 反向旋转坐标系后的距离，因此相加
                z + module.zSetting.getValue()
        );

        double[] animatedPosition = k40$cameraPosAnim.animate(0.1f);

        GlStateManager.translate(animatedPosition[0], animatedPosition[1], animatedPosition[2]);
    }

    @Inject(
            method = "orientCamera",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 6,
                    shift = At.Shift.AFTER
            )
    )
    private void k40$onRotate(float partialTicks, CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(MotionCameraModule.class);
        if (!module.isActivated()) return;

        Entity renderEntity = mc.getRenderViewEntity();

        GlStateManager.rotate(renderEntity.rotationYaw, 0.0F, 1.0F, 0.0F);
        if (module.motionSetting.isActivated()) GlStateManager.translate(
                k40$prevRenderX - k40$renderX,
                k40$renderY - k40$prevRenderY,
                k40$prevRenderZ - k40$renderZ
        );
        GlStateManager.rotate(-renderEntity.rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    //---------------------------------------------------------------------------
    // Overflow Animations
    //---------------------------------------------------------------------------

    @Inject(method = "setupCameraTransform", at = @At("HEAD"))
    protected void k40$getInterpolatedEyeHeight(float partialTicks, int pass, CallbackInfo ci) {
        StaticStation.sneakingHeight = k40$previousHeight + (k40$height - k40$previousHeight) * partialTicks;
    }

    @ModifyVariable(method = "orientCamera", at = @At(value = "STORE", ordinal = 0), index = 3)
    public float k40$modifyEyeHeight(float eyeHeight) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return eyeHeight;

        return StaticStation.sneakingHeight;
    }

    @ModifyArg(
            method = "renderWorldDirections",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
            ), index = 1
    )
    public float k40$syncCrossHair(float x) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return x;

        return StaticStation.sneakingHeight;
    }

    @Inject(method = "updateRenderer", at = @At("HEAD"))
    private void k40$interpolateHeight(CallbackInfo ci) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) return;

        Entity entity = mc.getRenderViewEntity();
        float eyeHeight = entity.getEyeHeight();

        k40$previousHeight = k40$height;

        if (eyeHeight < k40$height) k40$height = eyeHeight;
        else k40$height += (eyeHeight - k40$height) * 0.5f;
    }

    @Redirect(
            method = "setupViewBobbing",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V",
                    ordinal = 2
            )
    )
    public void k40$modernBobbing(float angle, float x, float y, float z) {
        val module = InstanceAccess.mod.getModule(OverflowAnimationsModule.class);

        if (!module.isActivated()) GlStateManager.rotate(angle, x, y, z);
    }
}
