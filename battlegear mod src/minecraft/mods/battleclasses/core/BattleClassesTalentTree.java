package mods.battleclasses.core;

import java.util.ArrayList;

import mods.battleclasses.ability.BattleClassesAbstractTalent;

public class BattleClassesTalentTree {
	
	protected BattleClassesTalentMatrix ownerTalentMatrix;
	
	public void setOwnerTalentMatrix(BattleClassesTalentMatrix parTalentMatrix) {
		this.ownerTalentMatrix = parTalentMatrix;
	}
	
	public ArrayList<BattleClassesAbstractTalent> talentList;
	
	/**
	 * Returns the index of the talent tree
	 * @return
	 */
	public int getIndexOfTree() {
		if(this.ownerTalentMatrix != null) {
			return this.ownerTalentMatrix.talentTrees.indexOf(this);
		}		
		return 0;
	}
	
	/**
	 * Returns the resource path of background image of this talent tree 
	 * @return
	 */
	public String getResoureLocationString() {
		if(this.ownerTalentMatrix != null) {
			int i = getIndexOfTree() + 1;
			return "textures/talents/backgrounds/talent_background_"
					+ (this.ownerTalentMatrix.playerHooks.playerClass.getPlayerClass().toString()).toLowerCase()
					+ "_" + i + ".png";
		}
		return "";
	}

}
