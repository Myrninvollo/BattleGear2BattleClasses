package mods.battleclasses;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battlegear2.api.weapons.CommandWeaponWield;

//BattleClasses registration goes here
public class BattleClassesMain {
	
	public static void init() {
		BattleClassesUtils.Log("Starting Battle Classes initizalization!", LogType.INIT);
		
	}
	
	public static void serverStart(FMLServerStartingEvent event){
		BattleClassesUtils.Log("FMLServerStartingEvent!", LogType.INIT);
        BattleClassesCommand.registerCommands();
    }
	
}
