package team.hdt.neutronia_rewrite.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.hdt.neutronia.base.blocks.BlockNeutroniaFence;

public class BlockPalisade extends BlockNeutroniaFence {

    public static final PropertyBool POST = PropertyBool.create("post");
    public static final PropertyBool POST_TOP = PropertyBool.create("post_top");

    public BlockPalisade(String name, IBlockState state) {
        super(name, state);
        setDefaultState(this.blockState.getBaseState().withProperty(POST, false).withProperty(POST_TOP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean north = canFenceConnectTo(worldIn, pos, EnumFacing.NORTH);
        boolean east = canFenceConnectTo(worldIn, pos, EnumFacing.EAST);
        boolean south = canFenceConnectTo(worldIn, pos, EnumFacing.SOUTH);
        boolean west = canFenceConnectTo(worldIn, pos, EnumFacing.WEST);
        boolean post = north && !east && south && !west || !north && east && !south && west;
        boolean postTop = !post && worldIn.isAirBlock(pos.up());
        return state.withProperty(POST, !post).withProperty(POST_TOP, postTop).withProperty(NORTH, north).withProperty(EAST, east).withProperty(SOUTH, south).withProperty(WEST, west);
    }

    private boolean canFenceConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.offset(facing);
        Block block = world.getBlockState(other).getBlock();
        return block.canBeConnectedTo(world, other, facing.getOpposite()) || canConnectTo(world, other, facing.getOpposite());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POST, POST_TOP, NORTH, EAST, WEST, SOUTH);
    }

}