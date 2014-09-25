package mods.battleclasses.ability;

public class BattleClassesTalentAbilityContainer extends BattleClassesAbstractTalent {

	public BattleClassesTalentAbilityContainer(int parAbilityID, int parTalentLevel, BattleClassesAbstractAbilityActive parAbility) {
		super(parAbilityID, parTalentLevel);
		containedAbility = parAbility;
	}
	
	BattleClassesAbstractAbilityActive containedAbility;
	

}
