package net.hdt.neutronia.base;

import net.hdt.neutronia.base.lib.LibMisc;
import net.hdt.neutronia.base.proxy.CommonProxy;
import net.hdt.neutronia.base.util.Reference;
import net.hdt.neutronia.events.ILifeCycleHandler;
import net.hdt.neutronia.events.handlers.EventHandlers;
import net.hdt.neutronia.events.handlers.RecipeHandlers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static net.hdt.neutronia.base.util.Reference.*;

@Mod(modid = MOD_ID, name = NAME, version = VERSION, dependencies = DEPENDENCIES, guiFactory = LibMisc.GUI_FACTORY, updateJSON = UPDATE_JSON)
public class NeutroniaMain {

    public static Logger LOGGER;

    @Mod.Instance
    public static NeutroniaMain instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy proxy;

    private List<ILifeCycleHandler> handlers = new ArrayList<ILifeCycleHandler>(){{
        add(new EventHandlers());
        add(new RecipeHandlers());
    }};

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        handlers.forEach(handler -> handler.preInit(event));
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        handlers.forEach(handler -> handler.init(event));
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        handlers.forEach(handler -> handler.postInit(event));
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event){
        handlers.forEach(handler -> handler.loadComplete(event));
    }

    @Mod.EventHandler
    public void serverInit(FMLServerStartingEvent event) {
        handlers.forEach(handler -> handler.serverInit(event));
    }

}