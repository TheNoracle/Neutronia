package team.abnormal.neutronia.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;
import team.abnormal.neutronia.properties.ChestType;

import javax.annotation.Nullable;

public class TileCustomChest extends TileEntityChest {

    public ChestType chestType = ChestType.NONE;



    @Nullable
    public static VanillaDoubleChestItemHandler getDoubleChestHandler(TileCustomChest chest) {
        World world = chest.getWorld();
        BlockPos pos = chest.getPos();
        if (!world.isBlockLoaded(pos))
            return null;

        Block blockType = chest.getBlockType();

        EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
        for (int i = horizontals.length - 1; i >= 0; i--) {
            EnumFacing enumfacing = horizontals[i];
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock();

            if (block == blockType) {
                TileEntity otherTE = world.getTileEntity(blockpos);

                if (otherTE instanceof TileCustomChest) {
                    TileCustomChest otherChest = (TileCustomChest) otherTE;
                    if (otherChest.chestType.equals(chest.chestType))
                        return new VanillaDoubleChestItemHandler(chest, otherChest, enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH);
                }
            }
        }
        return VanillaDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE; // All alone
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("type", chestType.name);
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setString("type", chestType.name);
        return nbt;
    }

    public void setChestType(ItemStack stack) {
        this.chestType = ChestType.getType(stack.getItemDamage());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        chestType = ChestType.getType(tag.getString("type"));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", chestType.name);
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        chestType = ChestType.getType(pkt.getNbtCompound().getString("type"));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        chestType = ChestType.getType(nbt.getString("type"));
    }

    @SuppressWarnings("incomplete-switch")
    private void setNeighbor(TileEntityChest chestTe, EnumFacing side) {
        if (chestTe.isInvalid()) {
            adjacentChestChecked = false;
        } else if (adjacentChestChecked) {
            switch (side) {
                case NORTH:
                    if (adjacentChestZNeg != chestTe)
                        adjacentChestChecked = false;
                    break;
                case SOUTH:
                    if (adjacentChestZPos != chestTe)
                        adjacentChestChecked = false;
                    break;
                case EAST:
                    if (adjacentChestXPos != chestTe)
                        adjacentChestChecked = false;
                    break;
                case WEST:
                    if (adjacentChestXNeg != chestTe)
                        adjacentChestChecked = false;
            }
        }
    }

    @Nullable
    @Override
    protected TileEntityChest getAdjacentChest(EnumFacing side) {
        BlockPos blockpos = pos.offset(side);

        if (isChestAt(blockpos)) {
            TileEntity tileentity = getWorld().getTileEntity(blockpos);

            if (tileentity instanceof TileCustomChest) {
                TileCustomChest tileentitychest = (TileCustomChest) tileentity;
                tileentitychest.setNeighbor(this, side.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean isChestAt(BlockPos posIn) {
        Block block = getWorld().getBlockState(posIn).getBlock();
        TileEntity te = getWorld().getTileEntity(posIn);
        return block instanceof BlockChest && ((BlockChest) block).chestType == getChestType() && te instanceof TileCustomChest && ((TileCustomChest) te).chestType == chestType;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (doubleChestHandler == null || doubleChestHandler.needsRefresh())
                doubleChestHandler = getDoubleChestHandler(this);
            if (doubleChestHandler != null && doubleChestHandler != VanillaDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE)
                return (T) doubleChestHandler;
        }
        return super.getCapability(capability, facing);
    }
}