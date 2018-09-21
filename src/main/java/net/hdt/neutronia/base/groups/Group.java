package net.hdt.neutronia.base.groups;

import net.hdt.neutronia.base.lib.LibMisc;
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

import java.util.*;
import java.util.function.Consumer;

public class Group implements Comparable<Group> {

    public final Map<String, Component> components = new HashMap<>();
    final List<Component> enabledComponents = new ArrayList<>();
    private List<Component> componentDependencies = new ArrayList<>();
    private List<Group> groupDependencies = new ArrayList<>();
    public String name, desc;
    public boolean enabled;
    public Property prop;
    private ItemStack iconStack;

    public Group() {
        name = getClass().getSimpleName().replaceAll("Neutronia", "").toLowerCase();
        desc = "This is a missing description text since this component does not have a description defined";
    }

    public Group(Builder builder) {
        name = builder.name;
        desc = builder.desc;
        enabled = builder.enabled;
        for (Component component : builder.components) {
            registerComponent(component, component.enabled);
        }
        groupDependencies = builder.groupDependencies;
        componentDependencies = builder.componentDependencies;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void registerComponent(Component component, boolean enabledByDefault) {
        registerComponent(component, convertName(component.getClass().getSimpleName()), enabledByDefault);
    }

    private String convertName(String origName) {
        String withSpaces = origName.replaceAll("(?<=.)([A-Z])", " $1").toLowerCase();
        return Character.toUpperCase(withSpaces.charAt(0)) + withSpaces.substring(1);
    }

    private void registerComponent(Component component, String name, boolean enabledByDefault) {
        Class<? extends Component> clazz = component.getClass();
        if (GroupLoader.componentInstances.containsKey(clazz))
            throw new IllegalArgumentException("Component " + clazz + " is already registered!");

        GroupLoader.componentInstances.put(clazz, component);
        GroupLoader.componentClassNames.put(clazz.getSimpleName(), component);
        components.put(name, component);

        component.enabledByDefault = enabledByDefault;
        component.prevEnabled = false;

        component.group = this;
        component.configName = name;
        component.configCategory = this.name + "." + name;
    }

    public void setupConfig() {
        forEachComponent(component -> {
            ConfigHelper.needsRestart = component.requiresMinecraftRestartToEnable();
            component.enabled = loadPropBool(component.configName, component.getFeatureDescription(), component.enabledByDefault) && enabled;
            component.prop = ConfigHelper.lastProp;

            component.setupConstantConfig();

            if (!component.forceLoad && GlobalConfig.enableAntiOverlap) {
                String[] incompatibilities = component.getIncompatibleMods();
                if (incompatibilities != null) {
                    List<String> failiures = new ArrayList<>();

                    for (String s : incompatibilities)
                        if (Loader.isModLoaded(s)) {
                            component.enabled = false;
                            failiures.add(s);
                        }

                    if (!failiures.isEmpty())
                        LibMisc.LOGGER.info("'" + component.configName + "' is forcefully disabled as it's incompatible with the following loaded mods: " + failiures);
                }
            }

            if (!component.loadtimeDone) {
                component.enabledAtLoadtime = component.enabled;
                component.loadtimeDone = true;
            }

            if (component.enabled && !enabledComponents.contains(component))
                enabledComponents.add(component);
            else if (!component.enabled)
                enabledComponents.remove(component);

            component.setupConfig();

            if (!component.enabled && component.prevEnabled) {
                if (component.hasSubscriptions())
                    MinecraftForge.EVENT_BUS.unregister(component);
                if (component.hasTerrainSubscriptions())
                    MinecraftForge.TERRAIN_GEN_BUS.unregister(component);
                if (component.hasOreGenSubscriptions())
                    MinecraftForge.ORE_GEN_BUS.unregister(component);
                component.onDisabled();
            } else if (component.enabled && (component.enabledAtLoadtime || !component.requiresMinecraftRestartToEnable()) && !component.prevEnabled) {
                if (component.hasSubscriptions())
                    MinecraftForge.EVENT_BUS.register(component);
                if (component.hasTerrainSubscriptions())
                    MinecraftForge.TERRAIN_GEN_BUS.register(component);
                if (component.hasOreGenSubscriptions())
                    MinecraftForge.ORE_GEN_BUS.register(component);
                component.onEnabled();
            }

            component.prevEnabled = component.enabled;
        });
    }

    public void preInit(FMLPreInitializationEvent event) {
        forEachEnabledComponent(component -> component.preInit(event));
    }

    void postPreInit(FMLPreInitializationEvent event) {
        forEachEnabledComponent(component -> component.postPreInit(event));
    }

    public void init(FMLInitializationEvent event) {
        forEachEnabledComponent(component -> component.init(event));
    }

    public void postInit(FMLPostInitializationEvent event) {
        forEachEnabledComponent(component -> component.postInit(event));
    }

    void finalInit(FMLPostInitializationEvent event) {
        forEachEnabledComponent(component -> component.finalInit(event));
    }

    @SideOnly(Side.CLIENT)
    void preInitClient(FMLPreInitializationEvent event) {
        forEachEnabledComponent(component -> component.preInitClient(event));
    }

    @SideOnly(Side.CLIENT)
    void initClient(FMLInitializationEvent event) {
        forEachEnabledComponent(component -> component.initClient(event));
    }

    @SideOnly(Side.CLIENT)
    void postInitClient(FMLPostInitializationEvent event) {
        forEachEnabledComponent(component -> component.postInitClient(event));
    }

    void serverStarting(FMLServerStartingEvent event) {
        forEachEnabledComponent(component -> component.serverStarting(event));
    }

    boolean canBeDisabled() {
        return true;
    }

    boolean isEnabledByDefault() {
        return true;
    }

    String getModuleDescription() {
        return desc;
    }

    public ItemStack getIconStack() {
        if (iconStack != null) {
            return iconStack;
        } else {
            return new ItemStack(Blocks.BARRIER);
        }
    }

    private void setIconStack(ItemStack stack) {
        this.iconStack = stack;
    }

    public void forEachComponent(Consumer<Component> consumer) {
        components.values().forEach(consumer);
    }

    private void forEachEnabledComponent(Consumer<Component> consumer) {
        enabledComponents.forEach(consumer);
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
    public int compareTo(Group group) {
        return name.compareTo(group.name);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static class Builder {

        private String name, desc;
        private ItemStack icon;
        private Group group;
        private boolean enabled;
        private List<Component> components = new ArrayList<>();
        private List<Component> componentDependencies = new ArrayList<>();
        private List<Group> groupDependencies = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder addComponent(Component component) {
            components.add(component);
            return this;
        }

        public Builder groupDependency(Group group) {
            groupDependencies.add(group);
            return this;
        }

        public Builder componentDependency(Component component) {
            componentDependencies.add(component);
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder iconStack(ItemStack icon) {
            if (!icon.isEmpty()) {
                this.icon = icon;
            } else {
                this.icon = new ItemStack(Blocks.AIR);
            }
            return this;
        }

        public Group register() {
            group = new Group(this);
            group.setIconStack(icon);
            GroupLoader.registerGroup(group);
            return group;
        }

    }

}
