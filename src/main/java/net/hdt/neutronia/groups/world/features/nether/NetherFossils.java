package net.hdt.neutronia.groups.world.features.nether;

import net.hdt.neutronia.base.groups.Component;
import net.hdt.neutronia.groups.world.world.NetherFossilGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NetherFossils extends Component {

    public static int chance;

    @Override
    public void setupConfig() {
        chance = loadPropInt("Fossil Chance", "The rarity of a fossil in a chunk. Higher means fewer fossils.", 25);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerWorldGenerator(new NetherFossilGenerator(), 0);
    }

}
