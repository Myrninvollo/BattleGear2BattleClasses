package mods.battleclasses.ability;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enumhelper.EnumBattleClassesTargetType;

public class BattleClassesAbilityTest extends BattleClassesAbstractAbilityActive {
	
	public static final int TEST_SPELL_ID = 777;

	public BattleClassesAbilityTest() {
		super(TEST_SPELL_ID);
		this.castTime = 3;
		this.channeled = true;
		this.channelTickCount = 3;
		initSpellSchool();
	}
	
	public BattleClassesAbilityTest(int id) {
		super(id);
		this.castTime = 3;
		//this.channeled = true;
		//this.channelTickCount = 3;
		this.cooldownDuration = 6.0F;
		initSpellSchool();
	}
	
	public void initSpellSchool() {
		int pick = new Random().nextInt(EnumBattleClassesAbilitySchool.values().length);
		this.school = EnumBattleClassesAbilitySchool.values()[pick];
		
		int pick2 = new Random().nextInt(EnumBattleClassesTargetType.values().length);
		this.targetType = EnumBattleClassesTargetType.values()[pick2];
		
		if(this.abilityID == 100) {
			this.channeled = true;
			this.channelTickCount = 3;
			this.school = EnumBattleClassesAbilitySchool.SPELL_ARCANE;
		}
	}

	@Override
	public boolean performEffect(EntityLiving targetEntity, int tickCount) {
		BattleClassesUtils.Log("BANG BANG Performed effect", LogType.CORE);
		return true;
	}
	
	public String getName() {
		return "Testing " + school + " " + targetType;
	}
}
