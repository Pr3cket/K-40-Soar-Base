package com.lawab1ders.nan7i.kali;

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

import com.lawab1ders.nan7i.kali.events.*;
import com.lawab1ders.nan7i.kali.events.cancelable.ClickMouseEvent;
import com.lawab1ders.nan7i.kali.events.cancelable.ScrollMouseEvent;
import com.lawab1ders.nan7i.kali.events.ticks.ClientTickedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.PlayerTickedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.RendererTickedEvent;
import com.lawab1ders.nan7i.kali.events.ticks.WorldTickedEvent;
import com.lawab1ders.nan7i.kali.gui.GuiClickMenu;
import com.lawab1ders.nan7i.kali.gui.GuiEditHUD;
import com.lawab1ders.nan7i.kali.gui.GuiLogin;
import com.lawab1ders.nan7i.kali.gui.clickgui.impl.ProfileCategory;
import com.lawab1ders.nan7i.kali.module.HUDModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.ModuleManager;
import com.lawab1ders.nan7i.kali.module.impl.combat.DamageTintModule;
import com.lawab1ders.nan7i.kali.utils.MillisTimer;
import com.lawab1ders.nan7i.kali.utils.config.Config;
import com.lawab1ders.nan7i.kali.utils.config.ConfigManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class KaliProcessor implements InstanceAccess {

    private static final ArrayList<Key> keys = new ArrayList<>();
    private final MillisTimer timer = new MillisTimer();

    public static boolean BLUR_TICK = false;

    static {
        for (int i = 0; i < Keyboard.KEYBOARD_SIZE; i++) {
            keys.add(new Key(i));
        }
    }

    @Getter @Setter
    @RequiredArgsConstructor
    static class Key {

        private final int key;
        private boolean pressed = false;
    }

    //---------------------------------------------------------------------------
    // Event Adapters
    //---------------------------------------------------------------------------

    /// 调用渲染事件
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPostRenderGameOverlay(RenderGameOverlayEvent.Post event) {

        // 最后执行
        if (!InstanceAccess.isInGame() || event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        DamageTintModule module = mod.getModule(DamageTintModule.class);

        if (module.isActivated()) {
            module.render();
        }

        Runnable runnable = () -> {
            val e = new OverlayRenderedEvent(event.partialTicks);

            for (int i = ModuleManager.HUD.size() - 1; i >= 0; i--) {
                val hudModule = ModuleManager.HUD.get(i);

                if (hudModule.isActivated()) hudModule.onOverlayRendered(e);
            }

            InstanceAccess.safeHandleEvent(e);
        };

        KaliProcessor.BLUR_TICK = true;
        runnable.run();
        KaliProcessor.BLUR_TICK = false;

        runnable.run();

        if (mc.currentScreen instanceof GuiClickMenu || mc.currentScreen instanceof GuiLogin || mc.currentScreen instanceof GuiEditHUD) {
            return;
        }

        ntf.render();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(RenderWorldLastEvent event) {
        InstanceAccess.safeHandleEvent(new LevelsRenderedEvent(event.partialTicks));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            InstanceAccess.safeHandleEvent(new ClientTickedEvent());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            InstanceAccess.safeHandleEvent(new RendererTickedEvent(event.renderTickTime));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            InstanceAccess.safeHandleEvent(new WorldTickedEvent(event.world));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            InstanceAccess.safeHandleEvent(new PlayerTickedEvent(event.player));
        }
    }

    /// 攻击实体方法会调用两遍
    private long pastTime = System.currentTimeMillis();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttackEntity(AttackEntityEvent event) {

        if (System.currentTimeMillis() - pastTime > 50) {
            InstanceAccess.safeHandleEvent(new AttackEvent(event.target));
        }

        pastTime = System.currentTimeMillis();
    }

    /// 调用按键事件
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTicked(ClientTickedEvent event) {
        if (mc.currentScreen != null) return;

        for (int i = 0; i < Keyboard.KEYBOARD_SIZE; i++) {
            boolean keyDown = Keyboard.isKeyDown(i);
            Key key = keys.get(i);

            if (keyDown && !key.isPressed()) {
                InstanceAccess.safeHandleEvent(new KeyEvent(i));
            }

            key.setPressed(keyDown);
        }

        val target = KaliAPI.INSTANCE.getAttackingTaget();

        if (target != null && mc.thePlayer != null) {
            if (target.getDistanceToEntity(mc.thePlayer) > 12) KaliAPI.INSTANCE.setAttackingTaget(null);

            if (target.isDead) KaliAPI.INSTANCE.setAttackingTaget(null);

            if (mc.thePlayer.isDead) KaliAPI.INSTANCE.setAttackingTaget(null);
        }

        if (timer.delay(5000, true)) {
            KaliAPI.INSTANCE.setAttackingTaget(null);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouse(MouseEvent event) {

        if (event.buttonstate) {
            val clickMouseEvent = new ClickMouseEvent(event.button);
            InstanceAccess.safeHandleEvent(clickMouseEvent);

            if (clickMouseEvent.isCanceled()) event.setCanceled(true);
        }

        if (event.dwheel != 0) {
            val scrollMouseEvent = new ScrollMouseEvent(event.dwheel);
            InstanceAccess.safeHandleEvent(scrollMouseEvent);

            if (scrollMouseEvent.isCanceled()) event.setCanceled(true);
        }
    }

    //---------------------------------------------------------------------------
    // Misc
    //---------------------------------------------------------------------------

    /// 打开主页面
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKey(KeyEvent event) {
        if (event.getKeyCode() == KaliAPI.INSTANCE.getClickGuiKeySetting().getKeyCode()) {
            mc.displayGuiScreen(gcm);
        }
    }

    /// 更新 SR
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void updateSR(OverlayRenderedEvent event) {
        val oldSR = HUDModule.sr;
        val sr = new ScaledResolution(mc);

        if (oldSR == null || oldSR.getScaledHeight() != sr.getScaledHeight() || oldSR.getScaledWidth() != sr.getScaledWidth()) {
            KaliAPI.INSTANCE.setScaledResolution(sr);
            HUDModule.sr = sr;

            ModuleManager.HUD.forEach(HUDModule::updatePosAndSize);
        }
    }

    /// 模糊渲染
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onOverlayRendered(OverlayRenderedEvent event) {
        if (KaliProcessor.BLUR_TICK) {
            sha.getGaussianBlur().blur(sha.getBlurRunnables(), 8);
            sha.getBlurRunnables().clear();
        }
    }

    /// 攻击目标
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttack(AttackEvent event) {
        Entity target = event.getTarget();

        if (!InstanceAccess.isSkippable(target, 0)) {
            KaliAPI.INSTANCE.setAttackingTaget((AbstractClientPlayer) target);
        }
    }

    /// 加载配置
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onJoinServer(JoinServerEvent event) {
        ConfigManager.update();

        for (Config c : ConfigManager.getConfigs()) {
            if (!c.getServerIp().isEmpty() &&
                    (StringUtils.containsIgnoreCase(event.getUrl(), c.getServerIp())
                            || StringUtils.containsIgnoreCase(event.getAddress(), c.getServerIp()))) {

                ConfigManager.load(c, false, true);
                ProfileCategory.currentConfig = c;
                break;
            }
        }
    }

    //---------------------------------------------------------------------------
    // Module Manager
    //---------------------------------------------------------------------------

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTicked2(ClientTickedEvent e) {
        ModuleManager.FLASH.forEach(m -> m.setActivated(m.getKeybindSetting().isKeyDown()));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKey2(KeyEvent e) {
        ModuleManager.HAS_KEYBIND.stream()
                                 .filter(m -> e.getKeyCode() == m.getKeybindSetting().getKeyCode())
                                 .forEach(Module::toggle);
    }
}
