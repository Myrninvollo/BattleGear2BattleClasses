package mods.battleclasses.core;

import java.util.HashMap;

import mods.battleclasses.EnumBattleClassesPlayerClass;
import net.minecraft.entity.player.EntityPlayer;


public class BattleClassesPlayerHooks {
	
	protected EntityPlayer ownerPlayer;
	
	public BattleClassesPlayerClass playerClass;
	public BattleClassesWeaponHitHandler weaponHitHandler;
	public HashMap<Integer, ICooldownHolder> mainCooldownMap;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
		mainCooldownMap = new HashMap<Integer, ICooldownHolder>();
		
		playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.PlayerClass_NONE);
		weaponHitHandler = new BattleClassesWeaponHitHandler(this);
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
}
