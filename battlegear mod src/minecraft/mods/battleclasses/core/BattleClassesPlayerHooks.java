package mods.battleclasses.core;

import mods.battleclasses.EnumBattleClassesPlayerClass;
import net.minecraft.entity.player.EntityPlayer;


public class BattleClassesPlayerHooks {
	
	protected EntityPlayer ownerPlayer;
	
	public BattleClassesPlayerClass playerClass;
	
	public BattleClassesWeaponHitHandler weaponHitHandler;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
		playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.PlayerClass_NONE);
		weaponHitHandler = new BattleClassesWeaponHitHandler(this);
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
}
