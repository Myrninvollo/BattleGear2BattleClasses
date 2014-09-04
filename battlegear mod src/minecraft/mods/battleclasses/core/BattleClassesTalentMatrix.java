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
	
	public void setTalentTreesByClass(EnumBattleClassesPlayerClass playerClass) {
		talentTrees.clear();
		//Load talentTrees by classes
		switch (playerClass) {
			case PlayerClass_NONE: {
			}
				break;
			case PlayerClass_MAGE: {
				
			}
				break;
			case PlayerClass_PRIEST:
				break;
			case PlayerClass_WARLOCK:
				break;
			case PlayerClass_ROGUE:
				break;
			case PlayerClass_HUNTER:
				break;
			case PlayerClass_PALADIN:
				break;
			case PlayerClass_WARRIOR:
				break;
			default:
				break;
		}
	}
}
