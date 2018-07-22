package net.hdt.neutronia.base.module;

import net.hdt.neutronia.base.lib.LibMisc;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import static net.hdt.neutronia.base.lib.LibMisc.MOD_ID;

public class Feature implements Comparable<Feature> {

	public Module module;
	
	boolean loadtimeDone;
	boolean enabledAtLoadtime;
	
	boolean enabledByDefault;
	public boolean enabled;
	boolean prevEnabled;
	public String configCategory;
	String configName;
	private String iconFile = "";
	private ResourceLocation icon;
	public Property prop;
	
	boolean forceLoad;
	
	final void setupConstantConfig() {
		String[] incompat = getIncompatibleMods();
		if(incompat != null && incompat.length > 0) {
			StringBuilder desc = new StringBuilder("This feature disables itself if any of the following mods are loaded: \n");
			for(String s : incompat)
				desc.append(" - ").append(s).append("\n");
			desc.append("This is done to prevent content overlap.\nYou can turn this on to force the feature to be loaded even if the above mods are also loaded.");
				
			ConfigHelper.needsRestart = true;
			forceLoad = loadPropBool("Force Enabled", desc.toString(), false);
		}
	}
	
	public void setupConfig() {
		// NO-OP
	}

	void onEnabled() {
		// NO-OP
	}
	
	void onDisabled() {
		// NO-OP
	}
	
	public void preInit(FMLPreInitializationEvent event) {
		// NO-OP
	}
	
	public void postPreInit(FMLPreInitializationEvent event) {
		// NO-OP
	}

	public void init(FMLInitializationEvent event) {
		// NO-OP
	}

	public void postInit(FMLPostInitializationEvent event) {
		// NO-OP
	}
	
	public void finalInit(FMLPostInitializationEvent event) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	public void initClient(FMLInitializationEvent event) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	public void postInitClient(FMLPostInitializationEvent event) {
		// NO-OP
	}

	void serverStarting(FMLServerStartingEvent event) {
		// NO-OP
	}
	
	public String[] getIncompatibleMods() {
		return null;
	}

	public boolean hasSubscriptions() {
		return false;
	}

	boolean hasTerrainSubscriptions() {
		return false;
	}

	boolean hasOreGenSubscriptions() {
		return false;
	}

	String getFeatureDescription() {
		return "";
	}
	
	public String getFeatureIngameConfigName() {
		return WordUtils.capitalizeFully(configName);
	}
	
	public boolean requiresMinecraftRestartToEnable() {
		return false;
	}

	public static void registerTile(Class<? extends TileEntity> clazz, String key) {
		GameRegistry.registerTileEntity(clazz, new ResourceLocation(LibMisc.PREFIX_MOD + key));
	}

	public final boolean isClient() {
		return FMLCommonHandler.instance().getSide().isClient();
	}

	protected final int loadPropInt(String propName, String desc, int default_) {
		return ConfigHelper.loadPropInt(propName, configCategory, desc, default_);
	}

	public final double loadPropDouble(String propName, String desc, double default_) {
		return ConfigHelper.loadPropDouble(propName, configCategory, desc, default_);
	}

	public final boolean loadPropBool(String propName, String desc, boolean default_) {
		return ConfigHelper.loadPropBool(propName, configCategory, desc, default_);
	}

	public final String loadPropString(String propName, String desc, String default_) {
		return ConfigHelper.loadPropString(propName, configCategory, desc, default_);
	}

	public final String[] loadPropStringList(String propName, String desc, String[] default_) {
		return ConfigHelper.loadPropStringList(propName, configCategory, desc, default_);
	}

	@Override
	public int compareTo(Feature o) {
		return configName.compareTo(o.configName);
	}

    public void setIconFile(String iconFile) {
        this.iconFile = iconFile;
    }

    public ResourceLocation getIcon() {
        if (icon != null) {
            return icon;
        }
        if (iconFile == null || iconFile.isEmpty()) {
            return null;
        }
        icon = new ResourceLocation(MOD_ID, iconFile);
        return icon;
    }

}
