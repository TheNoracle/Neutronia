package net.hdt.neutronia.groups.world.world.caves;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CaveBiomeEnd extends BasicCaveBiome {

    public CaveBiomeEnd() {
        super(Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());
    }

    @Override
    public void fillWall(World world, BlockPos pos, IBlockState state) {
        super.fillWall(world, pos, state);
    }

    @Override
    public void fillFloor(World world, BlockPos pos, IBlockState state) {
        super.fillFloor(world, pos, state);
    }

    @Override
    public void setupConfig(String category) {

    }


}
