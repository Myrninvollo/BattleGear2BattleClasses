package mods.battleclasses.ability;

import java.util.HashMap;

import mods.battleclasses.core.BattleClassesPlayerHooks;

public class BattleClassesAbstractAbilityPassive extends BattleClassesAbstractAbility {

	public BattleClassesAbstractAbilityPassive(int parAbilityID) {
		super(parAbilityID);
	}
	
	public static HashMap<Integer, BattleClassesAbstractAbilityActive> passiveAbilityFactoryHashSet = new HashMap<Integer, BattleClassesAbstractAbilityActive>();
	static {
		//activeAbilityFactoryHashSet.put(BattleClassesAbilityTest.TEST_SPELL_ID, new BattleClassesAbilityTest());
		passiveAbilityFactoryHashSet.put(1000, new BattleClassesAbilityTest(100));
	}
	
	protected String getAbilityIconName() {
		return "talent_" + this.abilityID;
	}

}
