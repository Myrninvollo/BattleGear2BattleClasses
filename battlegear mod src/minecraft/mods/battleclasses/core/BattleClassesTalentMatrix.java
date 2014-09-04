package mods.battleclasses.core;

import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;

public class BattleClassesTalentMatrix {
	
	
	
	public void setTalentTreesByClass(EnumBattleClassesPlayerClass playerClass) {
		//activeAbilities.clear();
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
