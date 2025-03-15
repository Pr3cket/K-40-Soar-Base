package com.lawab1ders.nan7i.kali.module;

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

import com.lawab1ders.nan7i.kali.module.impl.combat.*;
import com.lawab1ders.nan7i.kali.module.impl.hud.*;
import com.lawab1ders.nan7i.kali.module.impl.other.*;
import com.lawab1ders.nan7i.kali.module.impl.player.*;
import com.lawab1ders.nan7i.kali.module.impl.render.*;
import com.lawab1ders.nan7i.kali.module.impl.server.*;
import com.lawab1ders.nan7i.kali.module.setting.ModuleSetting;

import java.util.*;

public class ModuleManager extends ArrayList<Module> {

    public static final List<HUDModule> HUD = new ArrayList<>();
    public static final List<Module> HAS_KEYBIND = new ArrayList<>();
    public static final List<Module> FLASH = new ArrayList<>();

    private final Map<EModuleCategory, List<Module>> MODULE_CATEGORIES = new HashMap<>();
    private final Map<Class<? extends Module>, Module> MODULE_CACHE = new HashMap<>();

    public ModuleManager() {

        // 导入模块并按字母顺序排序
        addModules();
        sort(Comparator.comparing(Module::getNameKey));

        ModuleManager.HUD.sort(Comparator.comparing(HUDModule::getLayer));
        Collections.reverse(ModuleManager.HUD);

        Module.module = null;

        for (EModuleCategory c : EModuleCategory.values()) {
            MODULE_CATEGORIES.put(c, new ArrayList<>());
        }

        for (Module m : this) {
            getModules(m.getCategory()).add(m);

            // 构建缓存
            MODULE_CACHE.put(m.getClass(), m);
        }
    }

    private void addModules() {
        add(new AutoInsultsModule());
        add(new TargetPredicatorModule());
        add(new BlockInfoModule());
        add(new CalendarModule());
        add(new CPSDisplayModule());
        add(new ClockModule());
        add(new ComboCounterModule());
        add(new CoordsModule());
        add(new CompassModule());
        add(new FPSDisplayModule());
        add(new HorseStatsModule());
        add(new InventoryDisplayModule());
        add(new KeystrokesModule());
        add(new MemoryUsageDisplayModule());
        add(new ArmorStatusModule());
        add(new AutoNaviMapModule());
        add(new MouseStrokesModule());
        add(new PaperDollModule());
        add(new PingDisplayModule());
        add(new PotionStatusModule());
        add(new ReachDisplayModule());
        add(new RearviewModule());
        add(new ServerIPDisplayModule());
        add(new SoundSubtitlesModule());
        add(new SpeedometerModule());
        add(new SessionStatsModule());
        add(new StopwatchModule());
        add(new TargetInfoModule());
        add(new WebBrowserModule());
        add(new AntiBotModule());
        add(new AntiSpammingModule());
        add(new ChatTranslateModule());
        add(new DDoSModule());
        add(new EntityCullingModule());
        add(new IMBlockerModule());
        add(new MechvibesModule());
        add(new MouseTweaksModule());
        add(new RawInputModule());
        add(new SecurityChecksModule());
        add(new DamageTiltModule());
        add(new FreelookModule());
        add(new KeepSprintModule());
        add(new MoBendsModule());
        add(new OverflowAnimationsModule());
        add(new ProtectorModule());
        add(new WaveyCapesModule());
        add(new ZoomModule());
        add(new AppleSkinModule());
        add(new BlockOverlayModule());
        add(new BreadcrumbsModule());
        add(new ChatHeadModule());
        add(new ColorSaturationModule());
        add(new DamageTintModule());
        add(new FrameInterpolatorModule());
        add(new GammaOverrideModule());
        add(new HitColorModule());
        add(new ItemPhysicsModule());
        add(new JumpCircleModule());
        add(new KillEffectsModule());
        add(new MotionBlurModule());
        add(new NoHurtCameraModule());
        add(new OverlayEditorModule());
        add(new ProjectileTrailModule());
        add(new ReachCircleModule());
        add(new SkinLayers3DModule());
        add(new TargetIndicatorModule());
        add(new UHCOverlayModule());
        add(new AmbienceModule());
        add(new RadarModule());
        add(new AntiGhostModule());
        add(new MotionCameraModule());

        // ViaForge 可以替代
//        add(new PacketFixModule());
    }

    public void reset() {
        forEach(m -> {
            m.setActivated(false);

            for (ModuleSetting setting : m.getSettings()) {
                setting.reset();
            }
        });
    }

    public void active() {
        for (Module m : this) {
            if (m.isPreActivated()) m.setActivated(true);
        }
    }

    @SuppressWarnings("ALL")
    public <T extends Module> T getModule(Class<T> clazz) {
        // 先从缓存中获取
        return (T) MODULE_CACHE.get(clazz);
    }

    public List<Module> getModules(EModuleCategory moduleCategory) {
        return MODULE_CATEGORIES.get(moduleCategory);
    }
}
