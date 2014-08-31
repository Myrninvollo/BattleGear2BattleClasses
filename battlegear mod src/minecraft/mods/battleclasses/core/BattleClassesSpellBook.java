package mods.battleclasses.core;

import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battlegear2.api.core.IBattlePlayer;

public class BattleClassesSpellBook {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public static final int SPELLBOOK_CAPACITY = 7;
	
	public LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> abilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityActive>();
	protected int chosenAbilityIndex = 0;
	public int chosenAbilityID = 0;
	
	public BattleClassesSpellBook(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		protoSpell = new BattleClassesAbilityTest(100);
	}
	
	public BattleClassesAbstractAbilityActive protoSpell;
	
	public BattleClassesAbstractAbilityActive getChosenAbility() {
		//TODO
		
		return abilities.get(chosenAbilityID);
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
		boolean hasClass = playerHooks.playerClass.getPlayerClass() != EnumBattleClassesPlayerClass.PlayerClass_NONE;
		if(!hasClass) {
			return false;
		}
		boolean cooldownFreeClass = !playerHooks.playerClass.isOnCooldown();
		if(!cooldownFreeClass) {
			//HUD WARNING MESSAGE
			return false;
		}
		
		boolean battleMode = ((IBattlePlayer) entityPlayer).isBattlemode();
		
		return hasClass && cooldownFreeClass && battleMode;
	}
	
	public void setAbilitiesByClass(EnumBattleClassesPlayerClass playerClass) {
		abilities.clear();
		switch (playerClass) {
		case PlayerClass_NONE: {
		}
			break;
		case PlayerClass_MAGE: {
			abilities.put(100, new BattleClassesAbilityTest(100));
			abilities.put(101, new BattleClassesAbilityTest(101));
			abilities.put(102, new BattleClassesAbilityTest(102));
			abilities.put(110, new BattleClassesAbilityTest(110));
			abilities.put(111, new BattleClassesAbilityTest(111));
			abilities.put(112, new BattleClassesAbilityTest(112));
			abilities.put(120, new BattleClassesAbilityTest(120));
		}
			break;
		case PlayerClass_HUNTER:
			break;
		

		case PlayerClass_PALADIN:
			break;
		case PlayerClass_PRIEST:
			break;
		case PlayerClass_ROGUE:
			break;
		case PlayerClass_WARLOCK:
			break;
		case PlayerClass_WARRIOR:
			break;
		default:
			break;
		}
	}
	
	public int getChosenAbilityIndex() {
		return this.chosenAbilityIndex;
	}
	
	public void setChosenAbilityIndex(int i) {
		if(i >= 0 && i < SPELLBOOK_CAPACITY) {
			this.chosenAbilityIndex = i;
		}
	}
	
	public void incrementChosenAbilityIndex() {
		this.chosenAbilityIndex++;
		if(this.chosenAbilityIndex >= SPELLBOOK_CAPACITY) {
			this.chosenAbilityIndex = 0;
		}
	}
	
	public void decrementChosenAbilityIndex() {
		this.chosenAbilityIndex--;
		if(this.chosenAbilityIndex < 0) {
			this.chosenAbilityIndex = SPELLBOOK_CAPACITY - 1;
		}
	}
	
	public void setChosenAbilityID(){
		
	}
	
	public void setGlobalCooldown() {
		
	}
}
