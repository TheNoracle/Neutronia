package net.hdt.neutronia.modules.tweaks.features;

import net.hdt.neutronia.base.module.Feature;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ExtendedToolProgression extends Feature {

    private float attackDamage;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        try {
            Class<Item.ToolMaterial> toolMaterial = Item.ToolMaterial.class;
            Constructor<Item.ToolMaterial> con = toolMaterial.getDeclaredConstructor();
            con.setAccessible(true);
            con.newInstance((Object) null);
            System.out.println(con.toGenericString());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
