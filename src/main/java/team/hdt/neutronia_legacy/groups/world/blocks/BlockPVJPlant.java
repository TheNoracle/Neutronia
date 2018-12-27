package team.hdt.neutronia_legacy.groups.world.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.hdt.neutronia_legacy.blocks.base.BlockModBush;

public class BlockPVJPlant extends BlockModBush {
    public BlockPVJPlant(String name) {
        super(name, Material.PLANTS);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return Blocks.SAPLING.getFlammability(world, pos, face);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return Blocks.SAPLING.getFireSpreadSpeed(world, pos, face);
    }
}