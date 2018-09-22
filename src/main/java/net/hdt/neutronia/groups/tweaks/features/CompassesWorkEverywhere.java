package net.hdt.neutronia.groups.tweaks.features;

import net.hdt.neutronia.base.groups.Component;
import net.hdt.neutronia.groups.tweaks.client.item.ClockTimeGetter;
import net.hdt.neutronia.groups.tweaks.client.item.CompassAngleGetter;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class CompassesWorkEverywhere extends Component {

    public static boolean enableCompassNerf, enableClockNerf, enableNether, enableEnd;

    @Override
    public void setupConfig() {
        enableCompassNerf = loadProperty("Enable Compass Fix", true).setComment("Make compasses always point north until crafted").get();
        enableClockNerf = loadProperty("Enable Clock Fix", true).setComment("Make clocks always show day until crafted").get();
        enableNether = loadProperty("Enable Nether Compass", true).setComment("Make compasses point to where the portal you came in from when in the nether").get();
        enableEnd = loadProperty("Enable End Compass", true).setComment("Make compasses point to center of the main island when in the end").get();
    }

    @Override
    public String getDescription() {
        return "Makes the compasses work everywhere";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (enableCompassNerf || enableNether || enableEnd)
            Items.COMPASS.addPropertyOverride(new ResourceLocation("angle"), new CompassAngleGetter());

        if (enableClockNerf)
            Items.CLOCK.addPropertyOverride(new ResourceLocation("time"), new ClockTimeGetter());
    }

    @SubscribeEvent
    public void onUpdate(PlayerTickEvent event) {
        if (event.phase == Phase.START) {
            for (int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = event.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.COMPASS)
                    CompassAngleGetter.tickCompass(event.player, stack);
                else if (stack.getItem() == Items.CLOCK)
                    ClockTimeGetter.tickClock(event.player, stack);
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

}
