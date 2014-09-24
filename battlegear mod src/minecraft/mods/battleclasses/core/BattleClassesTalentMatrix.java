package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.HashMap;

import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.BattleClassesAbstractTalent;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.controlls.BattleClassesGuiButtonTalentNode;

public class BattleClassesTalentMatrix {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public BattleClassesTalentMatrix(BattleClassesPlayerHooks parPlayerHooks) {
		playerHooks = parPlayerHooks;
	}
	
	public ArrayList<BattleClassesTalentTree> talentTrees = new ArrayList<BattleClassesTalentTree>();
	public HashMap<Integer, BattleClassesAbstractTalent> talentHashMap = new HashMap<Integer, BattleClassesAbstractTalent>();

	public static final int TALENT_POINTS_TO_SPEND = 3;
	protected int talentPoints = TALENT_POINTS_TO_SPEND;
	
	public void initWithTalentTrees(ArrayList<BattleClassesTalentTree> parTalentTrees) {
		//Setting reverse references on trees and talentAbilities
		talentTrees = parTalentTrees;
		for(BattleClassesTalentTree talentTree : talentTrees) {
			talentTree.setOwnerTalentMatrix(this);
		}
		
		//Filling up talentHashMap
		talentHashMap.clear();
		for(BattleClassesTalentTree talentTree : this.talentTrees) {
        	for(BattleClassesAbstractTalent talentAbility : talentTree.talentList ) {
        		this.talentHashMap.put(talentAbility.getAbilityID(), talentAbility);
        	}
        }
	}
	
	public int getTalentPoints() {
		return talentPoints;
	}
	
	public boolean hasPointsToSpend() {
		return talentPoints > 0;
	}
	
	public void learnFullTreeAtIndex(int index) {
		//TODO
	}
	
	public void resetTalentPoints() {
		//TODO
	}
}
