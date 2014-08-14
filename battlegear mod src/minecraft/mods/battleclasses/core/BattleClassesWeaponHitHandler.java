package mods.battleclasses.core;

import java.util.EnumSet;

import mods.battleclasses.EnumBattleClassesWieldAccess;

public class BattleClassesWeaponHitHandler {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	EnumSet<EnumBattleClassesWieldAccess> accessSet;
	
	public BattleClassesWeaponHitHandler(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
	}
	
	public void initAccessSet() {
		accessSet.clear();
	}
}
