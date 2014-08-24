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
		if(!this.isAvailable(itemStack, entityPlayer)) {
			return;
		}
		this.getChosenAbility().onCastStart(itemStack, world, entityPlayer);
	}
	
	public void CastTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.isAvailable(itemStack, entityPlayer)) {
			return;
		}
		this.getChosenAbility().onCastTick(itemStack, entityPlayer, tickCount);
	}
	
	public void CastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.isAvailable(itemStack, entityPlayer)) {
			return;
		}
		this.getChosenAbility().onCastRelease(itemStack, entityPlayer, tickCount);
	}
	
	public boolean isAvailable(ItemStack itemStack, EntityPlayer entityPlayer) {
		boolean cooldownFreeClass = !playerHooks.playerClass.isOnCooldown();
		if(!cooldownFreeClass) {
			//HUD WARNING MESSAGE
			return false;
		}
		return cooldownFreeClass;
	}
	
	public void setGlobalCooldown() {
		
	}
}
