package mods.battleclasses.core;

import java.util.ArrayList;

import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;

public class BattleClassesTalentMatrix {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public BattleClassesTalentMatrix(BattleClassesPlayerHooks parPlayerHooks) {
		playerHooks = parPlayerHooks;
	}
	
	protected ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();

	public static final int TALENT_POINTS_TO_SPEND = 3;
	protected int talentPoints = TALENT_POINTS_TO_SPEND;
	
	public void initWithTalentTrees(ArrayList<BattleClassesTalentTree> parTalentTrees) {
		talentTrees = parTalentTrees;
		//TODO
	}
	
	public boolean hasPointsToSpend() {
		return talentPoints > 0;
	}
}
