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
import com.lawab1ders.nan7i.kali.injection.interfaces.ICullable;
import com.lawab1ders.nan7i.kali.injection.interfaces.IRender;
import com.lawab1ders.nan7i.kali.module.impl.server.EntityCullingModule;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager {

    @Shadow
    public abstract <T extends Entity> Render<T> getEntityRenderObject(Entity p_getEntityRenderObject_1_);

    @Inject(at = @At("HEAD"), method = "doRenderEntity", cancellable = true)
    public void k40$doRenderEntity(Entity entity, double p_doRenderEntity_2_, double d1, double d2,
                                   float tickDelta, float p_doRenderEntity_9_, boolean p_doRenderEntity_10_,
                                   CallbackInfoReturnable<Boolean> info) {

        ICullable cullable = (ICullable) entity;

        if (cullable.k40$notForcedVisible() && cullable.k40$isCulled()) {
            IRender<Entity> entityRender = (IRender) getEntityRenderObject(entity);

            if (InstanceAccess.mod.getModule(EntityCullingModule.class).renderNametagsThroughWallsSetting.isActivated()
                    && entityRender.k40$canRenderName(entity)) {

                entityRender.k40$renderName(entity, p_doRenderEntity_2_, d1, d2);
                //                entityRender.doRender(entity, entity.posX, entity.posY, entity.posZ, tickDelta,
                //                tickDelta);
            }

            EntityCullingModule.skippedEntities++;
            info.cancel();
        }
    }
}
