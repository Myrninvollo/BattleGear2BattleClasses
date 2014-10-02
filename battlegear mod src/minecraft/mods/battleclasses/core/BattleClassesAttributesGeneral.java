package mods.battleclasses.core;

public class BattleClassesAttributesGeneral implements IAttribute {

	//Primary attributes (in points)
	public float stamina = 0;
	public float strength = 0;
	public float agility = 0;
	public float spellpower_arcane = 0;
	public float spellpower_fire = 0;
	public float spellpower_frost = 0;
	public float spellpower_holy = 0;
	public float spellpower_shadow = 0;
	
	//Secondary attributes (in percentage)
	public float haste = 0;
	public float crit = 0;
	public float armor_pen = 0;
	
	public boolean multiplier = false;
	@Override
	public boolean isMultiplier() {
		return multiplier;
	}
}
