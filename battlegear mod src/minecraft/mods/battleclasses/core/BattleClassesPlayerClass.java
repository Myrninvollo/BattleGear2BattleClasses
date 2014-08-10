package mods.battleclasses.core;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.EnumBattleClassesPlayerClass;

public class BattleClassesPlayerClass implements ICooldownHolder {
	
	public static final float CLASS_SWITCH_COOLDOWN = 30.0F;
	
	protected BattleClassesPlayerHooks playerHooks;
	protected EnumBattleClassesPlayerClass playerClass;
	
	public BattleClassesPlayerClass(BattleClassesPlayerHooks parPlayerHooks, EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks = parPlayerHooks;
		this.setPlayerClass(parPlayerClass);
	}
	
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.setPlayerClass(parPlayerClass);

		this.setToCooldown();
		
		BattleClassesUtils.Log(playerHooks.ownerPlayer.getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
	}
	
	protected void setPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerClass = parPlayerClass;
		
		//TO DO
		//Set SpellBook content
		//Set Talents content
		//Set Weapon skill abilities
	}
	
	public EnumBattleClassesPlayerClass getPlayerClass() {
		return this.playerClass;
	}
	
	// -------------------- ICooldownHolder implementation --------------------

	public float setTime;
	
	@Override
	public float getCooldown() {
		return CLASS_SWITCH_COOLDOWN;
	}

	@Override
	public void setToCooldown() {
		if(!isOnCooldown()) {
			setToCooldownForced();
		}
	}

	@Override
	public void setToCooldownForced() {
		this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
	}

	@Override
	public float getCooldownRemaining() {
		float timeRemaining = BattleClassesUtils.getCurrentTimeInSeconds() - getCooldown();
		if(timeRemaining < 0 ) {
			timeRemaining = 0;
		}
		return timeRemaining;
	}

	@Override
	public boolean isOnCooldown() {
		return getCooldownRemaining() > 0;
	}
	
}
