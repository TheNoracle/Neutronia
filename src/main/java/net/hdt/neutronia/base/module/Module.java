package net.hdt.neutronia.base.module;

import net.hdt.neutronia.base.NeutroniaMain;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Module implements Comparable<Module> {

	public final String name = makeName();
	public final Map<String, Feature> features = new HashMap<>();
	private final List<Feature> enabledFeatures = new ArrayList<>();
	public boolean enabled;
	public Property prop;

	public void addFeatures() {
		// NO-OP
	}

	protected void registerFeature(Feature feature) {
		registerFeature(feature, convertName(feature.getClass().getSimpleName()));
	}

	public void registerFeature(Feature feature, boolean enabledByDefault) {
		registerFeature(feature, convertName(feature.getClass().getSimpleName()), enabledByDefault);
	}

	private String convertName(String origName) {
		String withSpaces = origName.replaceAll("(?<=.)([A-Z])", " $1").toLowerCase();
		return Character.toUpperCase(withSpaces.charAt(0)) + withSpaces.substring(1);
	}

	private void registerFeature(Feature feature, String name) {
		registerFeature(feature, name, true);
	}

	private void registerFeature(Feature feature, String name, boolean enabledByDefault) {
		Class<? extends Feature> clazz = feature.getClass();
		if(ModuleLoader.featureInstances.containsKey(clazz))
			throw new IllegalArgumentException("Feature " + clazz + " is already registered!");
		
		ModuleLoader.featureInstances.put(clazz, feature);
		ModuleLoader.featureClassNames.put(clazz.getSimpleName(), feature);
		features.put(name, feature);

		feature.enabledByDefault = enabledByDefault;
		feature.prevEnabled = false;
		
		feature.module = this;
		feature.configName = name;
		feature.configCategory = this.name + "." + name;
	}

	public void setupConfig() {
		if(features.isEmpty())
			addFeatures();
		
		forEachFeature(feature -> {
			ConfigHelper.needsRestart = feature.requiresMinecraftRestartToEnable();
			feature.enabled = loadPropBool(feature.configName, feature.getFeatureDescription(), feature.enabledByDefault) && enabled;
			feature.prop = ConfigHelper.lastProp;
			
			feature.setupConstantConfig();
			
			if(!feature.forceLoad && GlobalConfig.enableAntiOverlap) {
				String[] incompatibilities = feature.getIncompatibleMods();
				if(incompatibilities != null) {
					List<String> failiures = new ArrayList<>();

					for(String s : incompatibilities)
						if(Loader.isModLoaded(s)) {
							feature.enabled = false;
							failiures.add(s);
						}
					
					if(!failiures.isEmpty())
						NeutroniaMain.LOGGER.info("[Neutronia] '" + feature.configName + "' is forcefully disabled as it's incompatible with the following loaded mods: " + failiures);
				}
			}
			
			if(!feature.loadtimeDone) {
				feature.enabledAtLoadtime = feature.enabled;
				feature.loadtimeDone = true;
			}
			
			if(feature.enabled && !enabledFeatures.contains(feature))
				enabledFeatures.add(feature);
			else if(!feature.enabled)
				enabledFeatures.remove(feature);
			
			feature.setupConfig();
			
			if(!feature.enabled && feature.prevEnabled) {
				if(feature.hasSubscriptions())
					MinecraftForge.EVENT_BUS.unregister(feature);
				if(feature.hasTerrainSubscriptions())
					MinecraftForge.TERRAIN_GEN_BUS.unregister(feature);
				if(feature.hasOreGenSubscriptions())
					MinecraftForge.ORE_GEN_BUS.unregister(feature);
				feature.onDisabled();
			} else if(feature.enabled && (feature.enabledAtLoadtime || !feature.requiresMinecraftRestartToEnable()) && !feature.prevEnabled) {
				if(feature.hasSubscriptions())
					MinecraftForge.EVENT_BUS.register(feature);
				if(feature.hasTerrainSubscriptions())
					MinecraftForge.TERRAIN_GEN_BUS.register(feature);
				if(feature.hasOreGenSubscriptions())
					MinecraftForge.ORE_GEN_BUS.register(feature);
				feature.onEnabled();
			}
			
			feature.prevEnabled = feature.enabled;
		});
	}

	public void preInit(FMLPreInitializationEvent event) {
		forEachEnabled(feature -> feature.preInit(event));
	}
	
	void postPreInit(FMLPreInitializationEvent event) {
		forEachEnabled(feature -> feature.postPreInit(event));
	}

	public void init(FMLInitializationEvent event) {
		forEachEnabled(feature -> feature.init(event));
	}

	public void postInit(FMLPostInitializationEvent event) {
		forEachEnabled(feature -> feature.postInit(event));
	}
	
	void finalInit(FMLPostInitializationEvent event) {
		forEachEnabled(feature -> feature.finalInit(event));
	}
	
	@SideOnly(Side.CLIENT)
	void preInitClient(FMLPreInitializationEvent event) {
		forEachEnabled(feature -> feature.preInitClient(event));
	}

	@SideOnly(Side.CLIENT)
	void initClient(FMLInitializationEvent event) {
		forEachEnabled(feature -> feature.initClient(event));
	}

	@SideOnly(Side.CLIENT)
	void postInitClient(FMLPostInitializationEvent event) {
		forEachEnabled(feature -> feature.postInitClient(event));
	}

	void serverStarting(FMLServerStartingEvent event) {
		forEachEnabled(feature -> feature.serverStarting(event));
	}

	boolean canBeDisabled() {
		return true;
	}

	boolean isEnabledByDefault() {
		return true;
	}

	private String makeName() {
		return getClass().getSimpleName().replaceAll("Neutronia", "").toLowerCase();
	}

	String getModuleDescription() {
		return "";
	}
	
	public ItemStack getIconStack() {
		return new ItemStack(Blocks.BARRIER);
	}

	public final void forEachFeature(Consumer<Feature> consumer) {
		features.values().forEach(consumer);
	}

	private void forEachEnabled(Consumer<Feature> consumer) {
		enabledFeatures.forEach(consumer);
	}

	public final int loadPropInt(String propName, String desc, int default_) {
		return ConfigHelper.loadPropInt(propName, name, desc, default_);
	}

	public final double loadPropDouble(String propName, String desc, double default_) {
		return ConfigHelper.loadPropDouble(propName, name, desc, default_);
	}

	private boolean loadPropBool(String propName, String desc, boolean default_) {
		return ConfigHelper.loadPropBool(propName, name, desc, default_);
	}

	public final String loadPropString(String propName, String desc, String default_) {
		return ConfigHelper.loadPropString(propName, name, desc, default_);
	}

	@Override
	public int compareTo(Module o) {
		return name.compareTo(o.name);
	}

}