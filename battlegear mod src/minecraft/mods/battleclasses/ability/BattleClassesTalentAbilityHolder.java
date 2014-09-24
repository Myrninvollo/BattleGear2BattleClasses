package mods.battleclasses.ability;

public class BattleClassesTalentAbilityHolder extends BattleClassesAbstractTalent {

	public BattleClassesTalentAbilityHolder(int parAbilityID, int parTalentLevel, BattleClassesAbstractAbilityActive parAbility) {
		super(parAbilityID, parTalentLevel);
		containedAbility = parAbility;
	}
	
	BattleClassesAbstractAbilityActive containedAbility;
	

}
