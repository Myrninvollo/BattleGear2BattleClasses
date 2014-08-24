package mods.battleclasses;

import java.net.URL;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.items.BattleClassesItemTestingStaff;
import mods.battlegear2.api.core.BattlegearTranslator;
import mods.battlegear2.api.weapons.CommandWeaponWield;
import mods.battlegear2.utils.BattlegearConfig;

//BattleClasses registration goes here
public class BattleClassesMain {
	
	public static void preInit(FMLPreInitializationEvent event) {
		//Testing
		GameRegistry.registerItem(new BattleClassesItemTestingStaff(ToolMaterial.WOOD), "woodenStaff");
    }
	
	public static void init() {
		BattleClassesUtils.Log("Starting Battle Classes initizalization!", LogType.INIT);
		
	}
	
	public static void serverStart(FMLServerStartingEvent event){
		BattleClassesUtils.Log("FMLServerStartingEvent!", LogType.INIT);
        BattleClassesCommand.registerCommands();
    }
	
}
