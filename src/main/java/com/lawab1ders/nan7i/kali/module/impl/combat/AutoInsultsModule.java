package com.lawab1ders.nan7i.kali.module.impl.combat;

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
import com.lawab1ders.nan7i.kali.events.cancelable.PacketReceivedEvent;
import com.lawab1ders.nan7i.kali.module.EModuleCategory;
import com.lawab1ders.nan7i.kali.module.IModule;
import com.lawab1ders.nan7i.kali.module.Module;
import com.lawab1ders.nan7i.kali.module.setting.impl.BooleanSetting;
import com.lawab1ders.nan7i.kali.module.setting.impl.TextSetting;
import lombok.val;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@IModule(
        name = "module.autoinsults",
        description = "module.autoinsults.desc",
        category = EModuleCategory.COMBAT
)
public class AutoInsultsModule extends Module {

    public final TextSetting winFlagSetting = new TextSetting(
            "module.opts.titlewinsign",
            "VICTORY!"
    );
    public final TextSetting winRemarksSetting = new TextSetting(
            "module.autoinsults.opts.winremarks",
            "GG|Good Game"
    );
    public final TextSetting killFlagSetting = new TextSetting(
            "module.opts.chatkillsign",
            "by ${username}"
    );
    public final TextSetting killRemarksSetting = new TextSetting(
            "module.autoinsults.opts.killremarks",
            ""
    ).apply((TextSetting setting) -> setting.setRandom(true));

    public final BooleanSetting mentionSetting = new BooleanSetting("module.autoinsults.opts.mention", true);

    private static final String[] INSULTS = {
            "Mit Icarus wäre das nicht passiert",
            "POV: Icarus",
            "Nova is 'THE BEST CLIENT 2024', trust, no auto ban",
            "Vesper ist kein Exitscam, vertrau",
            "Spielst du Fortnite?",
            "Welcome to Meist... Meist Hacks?... Was ist den Meist Hacks?",
            "POV Icarus... Und jetzt einmal kurz... POV Augustus...",
            "Ah... Doof gelaufen für Augustus... Vielleicht nächstes Mal...",
            "IQ Zellen",
            "Bro paid for a cheat to lose against me",
            "It's only cheating when you get caught!",
            "I'm on Immaculate rn, btw",
            "I'm on AstroWare rn, btw",
            "I'm on Wurst rn, btw",
            "Klientus ist keine Rat",
            "10/10 im HAZE Rating",
            "RAT im Clientlauncher / Ich wurde geRATTED!",
            "ESound Calling",
            "Adapt ist gut",
            "Dümmer als Toastbrot",
            "Jetzt erstmal 10 Minuten Rage Stream",
            "'Nius ist eine neutrale Quelle'~Verschmxtztxcole(geht so leicht in die rechte Richtung)",
            "'Alice Weidel ist nicht rechts'~Verschmxtztxcole(geht so leicht in die rechte Richtung)",
            "foiled again",
            "I love Nekomame",
            "slurp",
            "Polar is always watching",
            "e.setYaw(RotationUtils.serverYaw)",
            "Aus Protest Vernunft wählen ~ FDP",
            "Unser Client zuerst ~ FDP",
            "Piwo",
            "Bottom Text"
    };

    @SubscribeEvent
    public void onReceivePacket(PacketReceivedEvent event) {
        if (!InstanceAccess.isInGame()) return;

        if (event.getPacket() instanceof S02PacketChat) {
            val chatPacket = (S02PacketChat) event.getPacket();
            if (chatPacket.getChatComponent() == null) return;

            val unformatted = chatPacket.getChatComponent().getUnformattedText();
            val message = StringUtils.stripControlCodes(unformatted);
            val replaced = killFlagSetting.getText().replace("${username}", mc.thePlayer.getName()).toLowerCase();

            if (message.toLowerCase().contains(replaced)) {
                // 获取原始带格式的消息
                val formattedMessage = chatPacket.getChatComponent().getFormattedText();

                // 提取被击败玩家的昵称
                String defeatedPlayerName = extractDefeatedPlayerName(formattedMessage);
                String[] remarks = killRemarksSetting.getText().split("\\|");

                mc.thePlayer.sendChatMessage(
                        (mentionSetting.isActivated() && !defeatedPlayerName.isEmpty() ? "@" + defeatedPlayerName + ", " : "")
                                + (killRemarksSetting.getText().isEmpty()

                                // 如果没有自定义的 insults 则使用内置的 insults
                                ? INSULTS[mc.thePlayer.getRNG().nextInt(INSULTS.length)]
                                : remarks[mc.thePlayer.getRNG().nextInt(remarks.length)]
                        )
                );
            }
        }
        else if (event.getPacket() instanceof S45PacketTitle && !winRemarksSetting.getText().isEmpty()) {
            val titlePacket = (S45PacketTitle) event.getPacket();
            if (titlePacket.getMessage() == null) return;

            val unformatted = titlePacket.getMessage().getUnformattedText();
            val message = StringUtils.stripControlCodes(unformatted);
            val replaced = winFlagSetting.getText().toLowerCase();

            if (message.toLowerCase().contains(replaced)) {
                String[] remarks = winRemarksSetting.getText().split("\\|");

                mc.thePlayer.sendChatMessage(remarks[mc.thePlayer.getRNG().nextInt(remarks.length)]);
            }
        }
    }

    /**
     * 从带格式的消息中提取被击败玩家的昵称
     */
    private String extractDefeatedPlayerName(String formattedMessage) {
        // 匹配颜色代码后的玩家昵称
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("§[0-9a-fk-or]([^§]+)");
        java.util.regex.Matcher matcher = pattern.matcher(formattedMessage);

        // 查找第一个匹配的玩家昵称
        if (matcher.find()) {
            return matcher.group(1);
        }

        // 如果没有找到，返回空
        return "";
    }
}
