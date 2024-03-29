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
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbilityTest;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.ability.BattleClassesAbstractAbilityPassive;
import mods.battleclasses.client.BattleClassesInGameGUI;
import mods.battleclasses.enumhelper.EnumBattleClassesCooldownType;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battleclasses.packet.BattleClassesPacketChosenAbilityIDSync;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.core.IBattlePlayer;

public class BattleClassesSpellBook {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public static final int SPELLBOOK_CAPACITY = 7;
	public static final float GLOBAL_COOLDOWN_DURATION = 1.0F;
	
	public LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> activeAbilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityActive>();
	protected int chosenAbilityIndex = 0;
	public int chosenAbilityID = 0;
	//public LinkedHashMap<Integer, BattleClassesAbstractAbilityPassive> passiveAbilities = new LinkedHashMap<Integer, BattleClassesAbstractAbilityPassive>();
	
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
		boolean battleMode = ((IBattlePlayer) entityPlayer).isBattlemode();
		if(!battleMode) {
			BattleClassesInGameGUI.displayWarning(BattleClassesInGameGUI.HUD_W_BATTLEMODE_REQUIRED);
			return false;
		}
		boolean hasClass = playerHooks.playerClass.getPlayerClass() != EnumBattleClassesPlayerClass.PlayerClass_NONE;
		if(!hasClass) {
			BattleClassesInGameGUI.displayWarning(BattleClassesInGameGUI.HUD_W_CLASS_REQUIRED);
			return false;
		}
		boolean cooldownFreeClass = !playerHooks.playerClass.isOnCooldown();
		if(!cooldownFreeClass) {
			BattleClassesInGameGUI.displayWarning(BattleClassesInGameGUI.HUD_W_ON_CLASS_COOLDOWN);
			return false;
		}
		
		return hasClass && cooldownFreeClass && battleMode;
	}
	
	public void learnAbility(BattleClassesAbstractAbilityActive ability) {
		if(!this.activeAbilities.containsValue(ability)) {
			ability.setPlayerHooks(this.playerHooks);
			this.playerHooks.mainCooldownMap.put(ability.getCooldownHashCode(), ability);
			this.activeAbilities.put(ability.getAbilityID(), ability);
		}
	}
	
	public void unLearnAbility(BattleClassesAbstractAbilityActive ability) {
		if(hasAbility(ability)) {
			BattleClassesUtils.Log("Unlearning ability (ID: " + ability.getAbilityID() + ")", LogType.ABILITY);
			this.playerHooks.mainCooldownMap.remove(ability.getAbilityID());
			this.activeAbilities.remove(ability.getAbilityID());
		}
	}
	
	public boolean hasAbility(BattleClassesAbstractAbilityActive ability) {
		return this.activeAbilities.containsKey(ability.getAbilityID());
	}
	
	public void clearSpellBook() {
		for(BattleClassesAbstractAbilityActive ability : getAbilitiesInArray()) {
			this.unLearnAbility(ability);
		}
		this.activeAbilities.clear();
	}
	
	public void initWithAbilities(LinkedHashMap<Integer, BattleClassesAbstractAbilityActive> parAbilities) {
		clearSpellBook();
		for(BattleClassesAbstractAbilityActive ability : parAbilities.values()){
		    this.learnAbility(ability);
		}
		updateChosenAbilityID();
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
			
			BattleClassesInGameGUI.displayChosenAbilityName(this.getChosenAbility().getName());
		}
	}
	
	public void setChosenAbilityID(int parID) {
		this.chosenAbilityID = parID;
		cancelCasting();
	}
		
	public void setGlobalCooldown() {
		for(BattleClassesAbstractAbilityActive ability : getAbilitiesInArray()) {
			if(!ability.ignoresGlobalCooldown && !ability.isOnCooldown()) {
				ability.setCooldown(GLOBAL_COOLDOWN_DURATION, false, EnumBattleClassesCooldownType.CooldownType_GLOBAL);
			}
		}
	}
	
	public void cancelGlobalCooldown() {
		for(BattleClassesAbstractAbilityActive ability : getAbilitiesInArray()) {
			if(!ability.ignoresGlobalCooldown && ability.isOnCooldown() && ability.getLastUsedCooldownType() == EnumBattleClassesCooldownType.CooldownType_GLOBAL) {
				ability.cancelCooldown();
			}
		}
	}
	
	//Helper
	public ArrayList<BattleClassesAbstractAbilityActive> getAbilitiesInArray() {
		return new ArrayList<BattleClassesAbstractAbilityActive>(this.activeAbilities.values());
	}
	
	public void cancelCasting() {
		this.playerHooks.ownerPlayer.clearItemInUse();
	}
	
	public boolean isCastingInProgress() {
		return ((IBattlePlayer)this.playerHooks.ownerPlayer).isBattlemode() && this.playerHooks.ownerPlayer.isUsingItem() && this.getChosenAbility() != null;
	}
}
