package mods.battleclasses.core;

import mods.battleclasses.EnumBattleClassesPlayerClass;
import net.minecraft.entity.player.EntityPlayer;


public class BattleClassesPlayerHooks {
	
	protected EntityPlayer ownerPlayer;
	public BattleClassesPlayerClass playerClass;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
		playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.PlayerClass_NONE);
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
}
