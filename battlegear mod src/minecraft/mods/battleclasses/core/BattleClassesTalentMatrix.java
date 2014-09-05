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
	
	public ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();

	public static final int TALENT_POINTS_TO_SPEND = 3;
	protected int talentPoints = TALENT_POINTS_TO_SPEND;
	
	public void initWithTalentTrees(ArrayList<BattleClassesTalentTree> parTalentTrees) {
		talentTrees = parTalentTrees;
		for(BattleClassesTalentTree talentTree : talentTrees) {
			talentTree.setOwnerTalentMatrix(this);
		}
	}
	
	public int getTalentPoints() {
		return talentPoints;
	}
	
	public boolean hasPointsToSpend() {
		return talentPoints > 0;
	}
}
