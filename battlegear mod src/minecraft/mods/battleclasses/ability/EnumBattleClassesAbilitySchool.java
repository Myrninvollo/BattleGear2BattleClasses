package mods.battleclasses.ability;

public enum EnumBattleClassesAbilitySchool {
	PHYSICAL_MELEE,
	PHYSICAL_RANGED,
	
	SPELL_ARCANE,
	SPELL_FIRE,
	SPELL_FROST,
	SPELL_HOLY,
	SPELL_SHADOW;
	
	public float getCriticalStrikeChanceBase() {
		switch (this) {
		case PHYSICAL_MELEE:
			return 0.10F;
		case PHYSICAL_RANGED:
			return 0.10F;
		case SPELL_ARCANE:
			return 0.10F;
		case SPELL_FIRE:
			return 0.10F;
		case SPELL_FROST:
			return 0.10F;
		case SPELL_HOLY:
			return 0.10F;
		case SPELL_SHADOW:
			return 0.10F;
		default:
			break;
		}
		return 0;
	}
	
	public float getCriticalStrikeBonusBase() {
		switch (this) {
		case PHYSICAL_MELEE:
			return 1.0F;
		case PHYSICAL_RANGED:
			return 1.0F;
		case SPELL_ARCANE:
			return 1.0F;
		case SPELL_FIRE:
			return 1.0F;
		case SPELL_FROST:
			return 1.0F;
		case SPELL_HOLY:
			return 1.0F;
		case SPELL_SHADOW:
			return 1.0F;
		default:
			break;
		}
		return 0;
	}

}



