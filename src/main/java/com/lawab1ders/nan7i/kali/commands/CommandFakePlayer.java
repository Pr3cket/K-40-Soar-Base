package com.lawab1ders.nan7i.kali.commands;

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

import com.lawab1ders.nan7i.kali.commands.faker.FakeEntityPlayerMP;
import com.lawab1ders.nan7i.kali.commands.faker.FakeNetworkManager;
import com.mojang.authlib.GameProfile;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;

import java.util.UUID;

@AllArgsConstructor
public class CommandFakePlayer extends CommandBase {

    private final MinecraftServer server;

    @Override
    public String getCommandName() {
        return "fakeplayer";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        Vec3 vec3d = sender.getPositionVector();
        double d0 = vec3d.xCoord;
        double d1 = vec3d.yCoord;
        double d2 = vec3d.zCoord;
        double yaw = 0.0D;
        double pitch = 0.0D;

        World world = sender.getEntityWorld();
        int dimension = world.provider.getDimensionId();
        int gamemode = server.getGameType().getID();

        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP entity = getCommandSenderAsPlayer(sender);
            yaw = entity.rotationYaw;
            pitch = entity.rotationPitch;
            gamemode = entity.theItemInWorldManager.getGameType().getID();
        }

        createFakePlayer("K-" + Minecraft.getSystemTime() % 100L, server, d0, d1, d2, yaw, pitch, dimension, gamemode);
    }

    private void createFakePlayer(String username, MinecraftServer server, double x, double y, double z, double yaw,
                                  double pitch, int dimension, int gamemode) {
        WorldServer worldIn = server.worldServerForDimension(dimension);
        ItemInWorldManager interactionManagerIn = new ItemInWorldManager(worldIn);
        GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(username);

        if (gameprofile == null) {
            UUID uuid = EntityPlayer.getUUID(new GameProfile(null, username));
            gameprofile = new GameProfile(uuid, username);

        } else if (!gameprofile.getProperties().containsKey("texture")) {
            gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
        }

        FakeEntityPlayerMP instance = new FakeEntityPlayerMP(server, worldIn, gameprofile, interactionManagerIn);
        instance.setPositionAndRotation(x, y, z, (float) yaw, (float) pitch);

        NetworkManager networkManager = new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND);

        server.getConfigurationManager().initializeConnectionToPlayer(networkManager, instance,
                new NetHandlerPlayServer(server, networkManager, instance));

        if (instance.dimension != dimension) {
            WorldServer old_world = server.worldServerForDimension(instance.dimension);

            instance.dimension = dimension;
            old_world.removeEntity(instance);
            instance.isDead = false;
            worldIn.spawnEntityInWorld(instance);
            instance.setWorld(worldIn);
            server.getConfigurationManager().preparePlayer(instance, old_world);
            instance.setPositionAndRotation(x, y, z, (float) yaw, (float) pitch);
            instance.theItemInWorldManager.setWorld(worldIn);
        }

        instance.setHealth(20.0F);
        instance.isDead = false;
        instance.stepHeight = 0.6F;
        interactionManagerIn.setGameType(WorldSettings.GameType.getByID(gamemode));
        server.getConfigurationManager().sendPacketToAllPlayersInDimension(new S19PacketEntityHeadLook(instance,
                (byte) (instance.rotationYawHead * 256 / 360)), instance.dimension);
        server.getConfigurationManager().sendPacketToAllPlayersInDimension(new S18PacketEntityTeleport(instance),
                instance.dimension);
        server.getConfigurationManager().serverUpdateMountedMovingPlayer(instance);
        instance.getDataWatcher().updateObject(10, (byte) 0x7f); // show all model layers (incl. capes)

        // Create and add fake player to team bot
        Scoreboard scoreboard = instance.mcServer.worldServerForDimension(0).getScoreboard();

        if (!scoreboard.getTeamNames().contains("Bots")) {
            scoreboard.createTeam("Bots");
            ScorePlayerTeam scoreplayerteam = scoreboard.getTeam("Bots");
            EnumChatFormatting textformatting = EnumChatFormatting.getValueByName("dark_green");
            scoreplayerteam.setChatFormat(textformatting);
            scoreplayerteam.setNamePrefix(textformatting.toString());
            scoreplayerteam.setNameSuffix(EnumChatFormatting.RESET.toString());
        }

        scoreboard.addPlayerToTeam(instance.getName(), "Bots");
    }
}
