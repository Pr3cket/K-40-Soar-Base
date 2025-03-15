package com.lawab1ders.nan7i.kali.module.impl.server.occlusionculling;

import com.lawab1ders.nan7i.kali.InstanceAccess;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;

public class Provider implements DataProvider, InstanceAccess {

    private WorldClient world = null;

    @Override
    public boolean prepareChunk(int chunkX, int chunkZ) {
        world = mc.theWorld;
        return world != null;
    }

    @Override
    public boolean isOpaqueFullCube(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        return world.getBlockState(pos).getBlock().isOpaqueCube();
    }

    @Override
    public void cleanup() {
        world = null;
    }
}
