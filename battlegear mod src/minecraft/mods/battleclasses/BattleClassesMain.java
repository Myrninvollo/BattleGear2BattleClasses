package mods.battleclasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

//BattleClasses registration goes here
public class BattleClassesMain {
	
	public static Logger battleClassesLogger = LogManager.getLogger("Battle Classes");
	
	public enum LogType {
    	GUI,
    	CORE,
    	ABILITY,
    	ATTRIBUTE,
    	AMPLIFIER,
    	COMMAND
    }
    
    public static void Log(String message, LogType logType) {
    	Side side = FMLCommonHandler.instance().getEffectiveSide();
    	String logString = new String("[" + side.toString() + "/" + logType.toString() + "] " + ": " + message);
    	battleClassesLogger.info(logString);
    }
}
