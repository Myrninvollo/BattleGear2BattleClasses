package mods.battleclasses.ability;

import mods.battleclasses.core.BattleClassesPlayerHooks;

public abstract class BattleClassesAbstractAbility {

	protected int abilityID;
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public int getAbilityID() {
		return abilityID;
	}
	
	public BattleClassesAbstractAbility(int parAbilityID) {
		this.abilityID = parAbilityID;
	}
	
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
	}
	
	public BattleClassesPlayerHooks getPlayerHooks() throws NullPointerException {
		if(playerHooks == null) {
			throw(new NullPointerException("Ability requires playerHooks to be set"));
		}
		return this.playerHooks;
	}
		
}
