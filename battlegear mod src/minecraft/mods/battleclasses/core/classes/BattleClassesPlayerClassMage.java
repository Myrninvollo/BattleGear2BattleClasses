package mods.battleclasses.core.classes;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.BattleClassesTalentTree;

public class BattleClassesPlayerClassMage extends BattleClassesPlayerClass {

	public BattleClassesPlayerClassMage(BattleClassesPlayerHooks parPlayerHooks) {
		super(parPlayerHooks, EnumBattleClassesPlayerClass.PlayerClass_MAGE);
	}

	public LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> getClassAbilities() {
		LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> abilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityActive>();
		abilities.put(100, new BattleClassesAbilityTest(100));
		abilities.put(101, new BattleClassesAbilityTest(101));
		abilities.put(102, new BattleClassesAbilityTest(102));
		abilities.put(110, new BattleClassesAbilityTest(110));
		abilities.put(111, new BattleClassesAbilityTest(111));
		abilities.put(112, new BattleClassesAbilityTest(112));
		abilities.put(ABILITY_ID_FROSTNOVA, new FrostNovaTestAbility());
		return abilities;
	}
	
	public ArrayList<BattleClassesTalentTree> getClassTalents() {
		ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
		//TODO
		return talentTrees;
	}
	
	//--------------------------------------------------------------------------------------------------------
	//										CLASS SPECIFIC ABILITIES
	//--------------------------------------------------------------------------------------------------------
	public static final int ABILITY_ID_FROSTNOVA = 121;
	class FrostNovaTestAbility extends BattleClassesAbilityTest {
		public FrostNovaTestAbility() {
			super(ABILITY_ID_FROSTNOVA);
		}
	}
}