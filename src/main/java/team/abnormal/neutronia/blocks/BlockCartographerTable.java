package team.abnormal.neutronia.blocks;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import team.abnormal.abnormalib.block.BlockFacing;

public class BlockCartographerTable extends BlockFacing implements INeutroniaBlock {

    public BlockCartographerTable(BlockPlanks.EnumType woodType) {
        super(String.format("%s_cartographer_table", woodType.getName()), Material.WOOD);
    }

}