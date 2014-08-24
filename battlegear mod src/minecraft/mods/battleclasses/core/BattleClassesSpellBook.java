package mods.battleclasses.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.items.BattleClassesItemWeapon;

public class BattleClassesSpellBook {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public BattleClassesSpellBook(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		protoSpell = new BattleClassesAbilityTest();
	}
	
	public BattleClassesAbstractAbilityActive protoSpell;
	
	public BattleClassesAbstractAbilityActive getChosenAbility() {
		//TODO
		return protoSpell;
	}
	
	public void CastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		this.getChosenAbility().onCastStart(itemStack, world, entityPlayer);
	}
	
	public void CastTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		this.getChosenAbility().onCastTick(itemStack, entityPlayer, tickCount);
	}
	
	public void CastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		this.getChosenAbility().onCastRelease(itemStack, entityPlayer, tickCount);
	}
	
	public void isAbleToCast(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		
	}
	
	public void setGlobalCooldown() {
		
	}
}
