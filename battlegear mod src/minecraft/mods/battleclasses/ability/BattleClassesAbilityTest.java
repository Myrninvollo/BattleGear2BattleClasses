package mods.battleclasses.ability;

import net.minecraft.entity.EntityLiving;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;

public class BattleClassesAbilityTest extends BattleClassesAbstractAbilityActive {
	
	public static final int TEST_SPELL_ID = 777;

	public BattleClassesAbilityTest() {
		super(TEST_SPELL_ID);
		this.castTime = 3;
		this.channeled = true;
		this.channelTickCount = 3;
	}

	@Override
	public boolean performEffect(EntityLiving targetEntity, int tickCount) {
		BattleClassesUtils.Log("BANG BANG Performed effect", LogType.CORE);
		return true;
	}
	
}
