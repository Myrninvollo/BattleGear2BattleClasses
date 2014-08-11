package mods.battleclasses.core;

import java.util.EnumSet;

import mods.battleclasses.EnumBattleClassesWeaponAccess;

public class BattleClassesWeaponHitHandler {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	EnumSet<EnumBattleClassesWeaponAccess> accessSet;
	
	public BattleClassesWeaponHitHandler(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
	}
	
	public void initAccessSet() {
		accessSet.clear();
	}
}
