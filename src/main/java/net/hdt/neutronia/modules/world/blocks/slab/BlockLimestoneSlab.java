package net.hdt.neutronia.modules.world.blocks.slab;

import net.hdt.neutronia.base.blocks.BlockNeutroniaSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockLimestoneSlab extends BlockNeutroniaSlab {

	public BlockLimestoneSlab(boolean doubleSlab) {
		super("stone_limestone_slab", Material.ROCK, doubleSlab);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

}
