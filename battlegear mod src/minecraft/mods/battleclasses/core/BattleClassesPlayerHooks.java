package mods.battleclasses.core;

import net.minecraft.entity.player.EntityPlayer;

public class BattleClassesPlayerHooks {
	
	protected EntityPlayer ownerPlayer;
	public BattleClassesPlayerClass playerClass;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
}
