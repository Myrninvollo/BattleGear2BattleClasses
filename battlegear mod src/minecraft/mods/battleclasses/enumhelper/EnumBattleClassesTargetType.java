package mods.battleclasses.enumhelper;

public enum EnumBattleClassesTargetType {
	
	TargetType_REQUIRED_SUPPORTIVE, //very rare! examples: Ritual of Summoning
	TargetType_REQUIRED_OFFENSIVE, //common examples: Judgement, Charge (basicly all offesnive instant abilities)
	
	TargetType_OPTIONAL_UNIVERSAL, //examples: Penance, Holy Shock
	TargetType_OPTIONAL_SUPPORTIVE, //common examples: Holy Light, Cleanse
	TargetType_OPTIONAL_OFFENSIVE, //common examples: Fireball, Frostbolt
	
	TargetType_UNNECESSARY_UNIVERSAL, //acts kinda like "unknown" examples: hunter shots
	
	TargetType_AREAEFFECT_UNIVERSAL, //rare! examples: Mass Dispell
	TargetType_AREAEFFECT_SUPPORTIVE, //examples: Circle of Healing
	TargetType_AREAEFFECT_OFFENSIVE //example: Arcane Explosion
}
