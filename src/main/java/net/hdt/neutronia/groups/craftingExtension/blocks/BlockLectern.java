package net.hdt.neutronia.groups.craftingExtension.blocks;

import net.hdt.huskylib2.block.BlockFacing;
import net.hdt.neutronia.base.blocks.INeutroniaBlock;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;

public class BlockLectern extends BlockFacing implements INeutroniaBlock {

    public BlockLectern(BlockPlanks.EnumType woodType) {
        super(String.format("%s_lectern", woodType.getName()), Material.WOOD);
    }

}