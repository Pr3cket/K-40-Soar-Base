package com.lawab1ders.nan7i.kali.commands.faker;

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

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

public class FakeEntityPlayerMP extends EntityPlayerMP {

    private double lastReportedPosX;
    private double lastReportedPosY;
    private double lastReportedPosZ;

    public FakeEntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile,
                              ItemInWorldManager interactionManagerIn) {
        super(server, worldIn, profile, interactionManagerIn);
    }

    @Override
    public void onKillCommand() {
        logout();
    }

    @Override
    public void onUpdate() {
        this.onUpdateEntity();

        if (posX != lastReportedPosX || posY != lastReportedPosY || posZ != lastReportedPosZ) {
            mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
            lastReportedPosX = posX;
            lastReportedPosY = posY;
            lastReportedPosZ = posZ;
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        logout();
    }

    private void logout() {
        this.dismountEntity(this);
        mcServer.getConfigurationManager().playerLoggedOut(this);

        Scoreboard scoreboard = mcServer.worldServerForDimension(0).getScoreboard();
        scoreboard.removePlayerFromTeams(getName());
    }
}
