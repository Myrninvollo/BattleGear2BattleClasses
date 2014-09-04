package mods.battleclasses.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battleclasses.packet.BattleClassesPacketChosenAbilityIDSync;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.core.IBattlePlayer;

public class BattleClassesSpellBook {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public static final int SPELLBOOK_CAPACITY = 7;
	
	public LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> activeAbilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityActive>();
	protected int chosenAbilityIndex = 0;
	public int chosenAbilityID = 0;
	public LinkedHashMap<Integer, BattleClassesAbstractAbilityPassive> passiveAbilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityPassive>();
	
	public BattleClassesSpellBook(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		protoSpell = new BattleClassesAbilityTest(100);
	}
	
	public BattleClassesAbstractAbilityActive protoSpell;
	
	public BattleClassesAbstractAbilityActive getChosenAbility() {
		//TODO
		return activeAbilities.get(chosenAbilityID);
	}
	
	public void CastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!this.isAvailable(itemStack, entityPlayer) || this.getChosenAbility() == null) {
			return;
		}
		this.getChosenAbility().onCastStart(itemStack, world, entityPlayer);
	}
	
	public void CastTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.isAvailable(itemStack, entityPlayer) || this.getChosenAbility() == null) {
			return;
		}
		this.getChosenAbility().onCastTick(itemStack, entityPlayer, tickCount);
	}
	
	public void CastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.isAvailable(itemStack, entityPlayer) || this.getChosenAbility() == null) {
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
		activeAbilities.clear();
		switch (playerClass) {
		case PlayerClass_NONE: {
		}
			break;
		case PlayerClass_MAGE: {
			activeAbilities.put(100, new BattleClassesAbilityTest(100));
			activeAbilities.put(101, new BattleClassesAbilityTest(101));
			activeAbilities.put(102, new BattleClassesAbilityTest(102));
			activeAbilities.put(110, new BattleClassesAbilityTest(110));
			activeAbilities.put(111, new BattleClassesAbilityTest(111));
			activeAbilities.put(112, new BattleClassesAbilityTest(112));
			activeAbilities.put(120, new BattleClassesAbilityTest(120));
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
		updateChosenAbilityID();
		for(BattleClassesAbstractAbilityActive ability : getAbilitiesInArray()){
		    ability.setPlayerHooks(this.playerHooks);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int getChosenAbilityIndex() {
		return this.chosenAbilityIndex;
	}
	
	@SideOnly(Side.CLIENT)
	public void setChosenAbilityIndex(int i) {
		if(i >= 0 && i < SPELLBOOK_CAPACITY) {
			this.chosenAbilityIndex = i;
		}
		updateChosenAbilityID();
	}
	
	@SideOnly(Side.CLIENT)
	public void incrementChosenAbilityIndex() {
		this.chosenAbilityIndex++;
		if(this.chosenAbilityIndex >= SPELLBOOK_CAPACITY) {
			this.chosenAbilityIndex = 0;
		}
		updateChosenAbilityID();
	}
	
	@SideOnly(Side.CLIENT)
	public void decrementChosenAbilityIndex() {
		this.chosenAbilityIndex--;
		if(this.chosenAbilityIndex < 0) {
			this.chosenAbilityIndex = SPELLBOOK_CAPACITY - 1;
		}
		updateChosenAbilityID();
	}
	
	@SideOnly(Side.CLIENT)
	public void updateChosenAbilityID() {
		if(chosenAbilityIndex < getAbilitiesInArray().size()) {
			setChosenAbilityID(getAbilitiesInArray().get(chosenAbilityIndex).getAbilityID());
			cancelCasting();
			FMLProxyPacket p = new BattleClassesPacketChosenAbilityIDSync(playerHooks.getOwnerPlayer(), this.chosenAbilityID).generatePacket();
			//Should be sidesafe
			Battlegear.packetHandler.sendPacketToServerWithSideCheck(p);
		}
	}
	
	public void setChosenAbilityID(int parID) {
		this.chosenAbilityID = parID;
		cancelCasting();
	}
	
	public void setGlobalCooldown() {
		
	}
	
	//Helper
	public ArrayList<BattleClassesAbstractAbilityActive> getAbilitiesInArray() {
		return new ArrayList<BattleClassesAbstractAbilityActive>(this.activeAbilities.values());
	}
	
	public void cancelCasting() {
		this.playerHooks.ownerPlayer.clearItemInUse();
	}
}
