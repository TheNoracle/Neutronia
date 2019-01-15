/*
 * Decompiled with CFR 0.137.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.IWorldGenerator
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.SidedProxy
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  net.minecraftforge.fml.common.registry.GameRegistry
 */
package team.abnormal.neutronia.world;

import java.io.File;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xxrexraptorxx.additionalstructures.proxy.ServerProxy;
import xxrexraptorxx.additionalstructures.util.Events;
import xxrexraptorxx.additionalstructures.util.UpdateChecker;
import xxrexraptorxx.additionalstructures.world.WorldGenCustomStructures;

@Mod(modid="additionalstructures", version="1.8.0", acceptedMinecraftVersions="[1.12.2]")
public class AdditionalStructures {
    @Mod.Instance(value="additionalstructures")
    public static AdditionalStructures instance;
    @SidedProxy(clientSide="xxrexraptorxx.additionalstructures.proxy.ClientProxy", serverSide="xxrexraptorxx.additionalstructures.proxy.ServerProxy")
    public static ServerProxy proxy;
    public static boolean activateUpdateChecker;
    public static boolean activateDebugMode;
    public static boolean activateOverworldGeneration;
    public static boolean activateNetherGeneration;
    public static boolean activateEndGeneration;
    public static float generationModifier;
    public static int spawnchanceLogs;
    public static int spawnchanceSpawnAltars;
    public static int spawnchanceBiggerCacti;
    public static int spawnchanceBoats;
    public static int spawnchanceBushes;
    public static int spawnchanceCamps;
    public static int spawnchanceFallenTrees;
    public static int spawnchanceDeadTrees;
    public static int spawnchanceDesertPillars;
    public static int spawnchanceTombs;
    public static int spawnchanceFarm;
    public static int spawnchanceGrave;
    public static int spawnchanceIllagerHouses;
    public static int spawnchanceLavaFountain;
    public static int spawnchanceLogBundle;
    public static int spawnchanceMayaTemple;
    public static int spawnchanceMeteor;
    public static int spawnchanceMineEntry;
    public static int spawnchanceNetherPortals;
    public static int spawnchanceOasis;
    public static int spawnchancePalms;
    public static int spawnchanceTemple;
    public static int spawnchancePyramideDungeon;
    public static int spawnchanceRocks;
    public static int spawnchanceTotems;
    public static int spawnchanceScarecrow;
    public static int spawnchanceSnowmen;
    public static int spawnchanceSnowPiles;
    public static int spawnchanceSphinx;
    public static int spawnchanceSkulls;
    public static int spawnchanceStonePillars;
    public static int spawnchanceVillagerHouses;
    public static int spawnchanceWallRuins;
    public static int spawnchanceWheatBalls;
    public static int spawnchanceWell;
    public static int spawnchanceOthers;
    public static int spawnchanceSpecials;
    public static int spawnchanceBones;
    public static int spawnchanceFossils;
    public static int spawnchanceObsidianPillars;
    public static int spawnchanceEndTowers;
    public static int spawnchanceEndCrystals;
    public static int spawnchanceEndBuildings;
    public static int spawnchanceSpiderNests;
    public static int spawnchancePyramides;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        activateUpdateChecker = config.get("EVENTS", "Activate the Update-Checker", true, "[true/false]").getBoolean();
        activateDebugMode = config.get("EVENTS", "Activate the Debug Mode (shows all positions of generated structures in the console)", false, "[true/false]").getBoolean();
        activateOverworldGeneration = config.get("GENERATOR", "Activate the Overworld structure generation", true, "[true/false]").getBoolean();
        activateNetherGeneration = config.get("GENERATOR", "Activate the Nether structure generation", true, "[true/false]").getBoolean();
        activateEndGeneration = config.get("GENERATOR", "Activate the End structure generation", true, "[true/false]").getBoolean();
        generationModifier = config.getFloat("Modifies the chance of the dungeon generation", "GENERATION", 1.0f, 0.1f, 10.0f, "This value multiplies the spawnchance! E.g. spawnchance is 1000 and the config says 0.3   ==> 1000 x 0.3 = 300   '300' is now the new spawnchance.");
        spawnchanceLogs = config.getInt("Spawnchance of the Logs", "GENERATION", 1000, 100, 50000, "higher value = rarer");
        spawnchanceSpawnAltars = config.getInt("Spawnchance of the Spawn Altars", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceBiggerCacti = config.getInt("Spawnchance of the Bigger Cacti", "GENERATION", 1000, 100, 50000, "higher value = rarer");
        spawnchanceBoats = config.getInt("Spawnchance of the Boats", "GENERATION", 6000, 100, 50000, "higher value = rarer");
        spawnchanceBushes = config.getInt("Spawnchance of the Bushes", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceCamps = config.getInt("Spawnchance of the Camps", "GENERATION", 1500, 100, 50000, "higher value = rarer");
        spawnchanceFallenTrees = config.getInt("Spawnchance of the Fallen Trees", "GENERATION", 1500, 100, 50000, "higher value = rarer");
        spawnchanceDeadTrees = config.getInt("Spawnchance of the Dead Trees (in the desert)", "GENERATION", 1000, 100, 50000, "higher value = rarer");
        spawnchanceDesertPillars = config.getInt("Spawnchance of the Desert Pillars", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceTombs = config.getInt("Spawnchance of the Tombs", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceFarm = config.getInt("Spawnchance of the Farm", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceGrave = config.getInt("Spawnchance of the Grave", "GENERATION", 4000, 100, 50000, "higher value = rarer");
        spawnchanceIllagerHouses = config.getInt("Spawnchance of the Illager Houses", "GENERATION", 5000, 100, 50000, "higher value = rarer");
        spawnchanceLavaFountain = config.getInt("Spawnchance of the Lava Fountain", "GENERATION", 4000, 100, 50000, "higher value = rarer");
        spawnchanceLogBundle = config.getInt("Spawnchance of the Log Bundle", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceMayaTemple = config.getInt("Spawnchance of the Maya Temple", "GENERATION", 5000, 100, 50000, "higher value = rarer");
        spawnchanceMeteor = config.getInt("Spawnchance of the Meteor", "GENERATION", 5000, 100, 50000, "higher value = rarer");
        spawnchanceMineEntry = config.getInt("Spawnchance of the Mine Entry", "GENERATION", 2000, 1000, 50000, "higher value = rarer");
        spawnchanceNetherPortals = config.getInt("Spawnchance of the Nether Portals", "GENERATION", 5000, 100, 50000, "higher value = rarer");
        spawnchanceOasis = config.getInt("Spawnchance of the Oasis", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchancePalms = config.getInt("Spawnchance of the Palms", "GENERATION", 1000, 100, 50000, "higher value = rarer");
        spawnchanceTemple = config.getInt("Spawnchance of the Temple", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchancePyramideDungeon = config.getInt("Spawnchance of the Pyramide Dungeon", "GENERATION", 5000, 100, 50000, "higher value = rarer");
        spawnchanceRocks = config.getInt("Spawnchance of the Rocks", "GENERATION", 1000, 100, 50000, "higher value = rarer");
        spawnchanceTotems = config.getInt("Spawnchance of the Totems", "GENERATION", 1500, 100, 50000, "higher value = rarer");
        spawnchanceScarecrow = config.getInt("Spawnchance of the Scarecrow", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceSnowmen = config.getInt("Spawnchance of the Snowmen", "GENERATION", 1500, 100, 50000, "higher value = rarer");
        spawnchanceSnowPiles = config.getInt("Spawnchance of the Snow Piles", "GENERATION", 1000, 100, 50000, "higher value = rarer");
        spawnchanceSphinx = config.getInt("Spawnchance of the Sphinx", "GENERATION", 4000, 100, 50000, "higher value = rarer");
        spawnchanceSkulls = config.getInt("Spawnchance of the Skulls", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceStonePillars = config.getInt("Spawnchance of the Stone Pillars", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceVillagerHouses = config.getInt("Spawnchance of the single Villager Houses", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceWallRuins = config.getInt("Spawnchance of the Wall Ruins", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceWheatBalls = config.getInt("Spawnchance of the Wheat Balls", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceWell = config.getInt("Spawnchance of the Well", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceOthers = config.getInt("Spawnchance of other things", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceSpecials = config.getInt("Spawnchance of Specials!", "GENERATION", 30000, 100, 50000, "higher value = rarer");
        spawnchanceBones = config.getInt("Spawnchance of big bones", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceFossils = config.getInt("Spawnchance of Fossils", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceObsidianPillars = config.getInt("Spawnchance of Obsidian Pillars in the End", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceEndTowers = config.getInt("Spawnchance of End Towers", "GENERATION", 3000, 100, 50000, "higher value = rarer");
        spawnchanceEndCrystals = config.getInt("Spawnchance of End Crystals", "GENERATION", 2000, 100, 50000, "higher value = rarer");
        spawnchanceEndBuildings = config.getInt("Spawnchance of End Buildings", "GENERATION", 5000, 100, 50000, "higher value = rarer");
        spawnchanceSpiderNests = config.getInt("Spawnchance of the Underground Spider Nests", "GENERATION", 5000, 100, 50000, "higher value = rarer");
        spawnchancePyramides = config.getInt("Spawnchance of the Pyramides", "GENERATION", 4000, 100, 50000, "higher value = rarer");
        config.save();
        UpdateChecker.checkForUpdates();
        MinecraftForge.EVENT_BUS.register((Object)new Events());
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);
    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event) {
        proxy.registerClientStuff();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}

