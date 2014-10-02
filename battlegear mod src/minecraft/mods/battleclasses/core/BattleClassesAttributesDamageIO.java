package mods.battleclasses.core;

public class BattleClassesAttributesDamageIO implements IAttribute {
	
	public float totalInput = 1.0F;
	public float totalOutput = 1.0F;
	
	@Override
	public boolean isMultiplier() {
		return true;
	}
}
