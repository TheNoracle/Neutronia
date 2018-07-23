package net.hdt.neutronia.modules.world.blocks;

import net.hdt.neutronia.base.blocks.BlockNeutroniaBush;
import net.hdt.neutronia.modules.world.features.UndergroundBiomes;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockGlowshroom extends BlockNeutroniaBush {

    protected static final AxisAlignedBB MUSHROOM_AABB = new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.6, 0.8);
	
	public BlockGlowshroom() {
		super("glowshroom");
		setLightLevel(0.9375F);
		setTickRandomly(true);
		setSoundType(SoundType.PLANT);
	}

	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return MUSHROOM_AABB;
    }
	
	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() == UndergroundBiomes.glowcelium;
	}
	
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(rand.nextInt(UndergroundBiomes.glowshroomGrowthRate) == 0) {
            int i = 5;
            int j = 4;

            for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
                if(worldIn.getBlockState(blockpos).getBlock() == this) {
                    --i;

                    if(i <= 0)
                        return;
                }
            }

            BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

            for(int k = 0; k < 4; ++k) {
                if (worldIn.isAirBlock(blockpos1) && canBlockStay(worldIn, blockpos1, getDefaultState()))
                    pos = blockpos1;

                blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }

            if(worldIn.isAirBlock(blockpos1) && canBlockStay(worldIn, blockpos1, getDefaultState()))
                worldIn.setBlockState(blockpos1, getDefaultState(), 2);
        }
    }
    
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);

		if(rand.nextInt(20) == 0)
			worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX() + 0.2 + rand.nextFloat() * 0.6, pos.getY() + 0.3F, pos.getZ() + 0.2 + rand.nextFloat() * 0.6, 0, 0, 0, new int[0]);
	}

}
