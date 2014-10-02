package mods.battleclasses.core;

public class BattleClassesAttributesAbility implements IAttribute {
	
	//Basic ability attributes
	public float cooldownDuration = 0;
	public float castTime = 0;
	public float range = 0;
	
	//Supporting modifiers
	public float criticalChance = 0;
	public float haste = 0;
	
	public boolean multiplier = false;
	@Override
	public boolean isMultiplier() {
		return multiplier;
	}
}
