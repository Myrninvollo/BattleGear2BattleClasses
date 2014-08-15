package mods.battleclasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BattleClassesUtils {
	
public static Logger battleClassesLogger = LogManager.getLogger("Battle Classes");
	
	public enum LogType {
		INIT,
    	GUI,
    	CORE,
    	PACKET,
    	ABILITY,
    	ATTRIBUTE,
    	AMPLIFIER,
    	COMMAND
    }
    
    public static void Log(String message, LogType logType) {
    	//Side side = FMLCommonHandler.instance().getEffectiveSide();
    	String logString = new String("[" + /*side.toString() + "/" + */  logType.toString() + "] " + ": " + message);
    	battleClassesLogger.info(logString);
    }
	
	public static float getCurrentTimeInSeconds() {
		return ((float) Minecraft.getSystemTime()) / 1000;
	}
	
	public static float getCooldownPercentage(ICooldownHolder coolDownHolder) {
		return coolDownHolder.getCooldownRemaining() / coolDownHolder.getCooldownDuration();
	}
	
	public static void setEntityPlayerItemInUseInSeconds(EntityPlayer entityPlayer, ItemStack itemStack, float time) {
		int countDownTickStart = (int) (72000 + time*20);
		entityPlayer.setItemInUse(itemStack, countDownTickStart);
	}
	
	public static BattleClassesPlayerHooks getPlayerHooks(EntityPlayer entityplayer) throws NullPointerException {
		if(entityplayer.inventory instanceof InventoryPlayerBattle) {
			InventoryPlayerBattle battleInventory = (InventoryPlayerBattle)entityplayer.inventory;
			return battleInventory.battleClassesPlayerHooks;
		}
		else {
			battleClassesLogger.error("Couldn't find InventoryPlayerBattle of player:" + entityplayer.getDisplayName());
			throw(new NullPointerException("Error when looking for InventoryPlayerBattle!"));
			//return null;
		}
	}
		
}