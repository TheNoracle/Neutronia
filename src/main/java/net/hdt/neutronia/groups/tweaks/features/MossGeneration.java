package net.hdt.neutronia.groups.tweaks.features;

import net.hdt.neutronia.base.BWMRecipes;
import net.hdt.neutronia.base.groups.Component;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MossGeneration extends Component {
    private static int RADIUS;
    private static int RATE;
    private static boolean DISABLE_VINE_RECIPES;
    private static HashMap<Block, IBlockState> CONVERTED_BLOCKS = new HashMap<>();

    private static void addBlockConversion(Block block, IBlockState mossyState) { //TODO: Could be the new block meta ingredient possibly
        CONVERTED_BLOCKS.put(block, mossyState);
    }

    private static Optional<BlockPos> randomPosition(World world, BlockPos start, BlockPos end) {
        if (world.isAreaLoaded(start, end)) {
            return Optional.of(new BlockPos(
                    randomRange(start.getX(), end.getX()),
                    randomRange(start.getY(), end.getY()),
                    randomRange(start.getZ(), end.getZ())
            ));
        }
        return Optional.empty();
    }

    private static Optional<IBlockState> getMossyVariant(IBlockState state) {
        return Optional.ofNullable(CONVERTED_BLOCKS.get(state.getBlock()));
    }

    private static int randomRange(int start, int end) {
        int d = end - start;
        return start + RandomUtils.nextInt(0, d);
    }

    @Override
    public void setupConfig() {
        RADIUS = loadPropInt("Moss radius from the mob spawner", "", 5);
        RATE = loadPropInt("Moss grow rate", "1 out of this rate will cause a moss to try to generate", 100);
        DISABLE_VINE_RECIPES = loadPropBool("Disable Vine Recipes", "Disables the mossy cobblestone and mossy brick recipes involving vines.", true);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (DISABLE_VINE_RECIPES) {
            BWMRecipes.removeRecipe("minecraft:mossy_cobblestone");
            BWMRecipes.removeRecipe("minecraft:mossy_stonebrick");
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addBlockConversion(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE.getDefaultState());
        addBlockConversion(Blocks.STONEBRICK, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY));
    }

    @SubscribeEvent
    public void generateMossNearSpawner(TickEvent.WorldTickEvent event) {
        World world = event.world;
        List<BlockPos> positions;
        if (world.isRemote || event.phase != TickEvent.Phase.END || event.side != Side.SERVER)
            return;
        if (world.rand.nextInt(RATE) != 0)
            return;
        synchronized (world.loadedTileEntityList) {
            positions = world.loadedTileEntityList
                    .stream()
                    .filter(t -> t instanceof TileEntityMobSpawner)
                    .map(TileEntity::getPos)
                    .collect(Collectors.toList());
        }
        positions.forEach(pos -> {
            BlockPos min = pos.add(-RADIUS, -RADIUS, -RADIUS), max = pos.add(RADIUS, RADIUS, RADIUS);
            randomPosition(world, min, max).ifPresent(p ->
                    getMossyVariant(world.getBlockState(p)).ifPresent(mossy -> world.setBlockState(p, mossy))
            );
        });
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String getFeatureDescription() {
        return "Cobblestone or Stonebrick within the spawning radius of a Mob Spawner will randomly grow into the Mossy version.";
    }

}