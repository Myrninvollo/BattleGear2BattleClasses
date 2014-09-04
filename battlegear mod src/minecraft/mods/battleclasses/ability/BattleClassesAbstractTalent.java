package mods.battleclasses.ability;

import mods.battleclasses.core.BattleClassesTalentTree;

public class BattleClassesAbstractTalent extends BattleClassesAbstractAbilityPassive {

	public BattleClassesAbstractTalent(int parAbilityID) {
		super(parAbilityID);
	}
	
	int currentState = 0;
	
	public int getMaximalState() {
		return 1;
	}
	
	public boolean incrementState() {
		if(this.currentState < this.getMaximalState() ) {
			currentState++;
			this.onStateChanged();
			return true;
		}
		return false;
	}
	
	public void resetState() {
		currentState = 0;
		this.onStateChanged();
	}
	
	public void onStateChanged() {
		//TODO
		//ADD OR REMOVE THIS AMPLIFY/MODIFY PROVIDER FROM CENTRAL HASHTABLE
		//ADD OR REMOVE CONTAINED ABILITY TO SPELLBOOK
	}
	
	public boolean isAccessAbleInTalentTree(BattleClassesTalentTree talentTree) {
		return true;
	}

}
