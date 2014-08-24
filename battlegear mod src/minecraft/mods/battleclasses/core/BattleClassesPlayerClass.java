package mods.battleclasses.core;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.EnumBattleClassesWieldAccess;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearAnimationPacket;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesPlayerClass implements ICooldownHolder {
	
	public static final int TOTAL_CLASSES = EnumBattleClassesPlayerClass.values().length;
	public static IIcon classIcons[];
	
	public BattleClassesSpellBook spellBook;
		
	protected BattleClassesPlayerHooks playerHooks;
	protected EnumBattleClassesPlayerClass playerClass;
	
	public BattleClassesPlayerClass(BattleClassesPlayerHooks parPlayerHooks, EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks = parPlayerHooks;
		parPlayerHooks.mainCooldownMap.put(this.getCooldownHashCode(), this);
		this.setPlayerClass(parPlayerClass);
		this.initCooldownHolder();
		
		this.spellBook = new BattleClassesSpellBook(parPlayerHooks);
	}
	
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.setPlayerClass(parPlayerClass);

		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(playerHooks.getOwnerPlayer(), playerClass).generatePacket();
		
		if(playerHooks.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.getOwnerPlayer();
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class switch sync to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				Battlegear.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
		
		BattleClassesUtils.Log(playerHooks.getOwnerPlayer().getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
		
		this.setToCooldownForced();
	}
	
	protected void setPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerClass = parPlayerClass;
		
		//TO DO
		//Set SpellBook content
		//Set Talents content
		//Set Weapon skill abilities
	}
	
	public void setWeaponAccessByClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks.weaponHitHandler.initAccessSet();
		
		switch (parPlayerClass) {
			case PlayerClass_NONE : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_DUALWIELD);
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_SHIELD);
			}
				break;
			case PlayerClass_MAGE : {
				//
			}
				break;
			case PlayerClass_PRIEST : {
				//
			}
				break;
			case PlayerClass_WARLOCK : {
				//
			}
				break;
			case PlayerClass_ROGUE : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_DUALWIELD);
			}
				break;
			case PlayerClass_HUNTER : {
				//
			}
				break;
			case PlayerClass_PALADIN : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_SHIELD);
			}
				break;
			case PlayerClass_WARRIOR : {
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_DUALWIELD);
				this.playerHooks.weaponHitHandler.accessSet.add(EnumBattleClassesWieldAccess.WeaponAccess_SHIELD);
			}
				break;
		default:
			break;
		}
	}
	
	public EnumBattleClassesPlayerClass getPlayerClass() {
		return this.playerClass;
	}
	
	public static IIcon getClassIcon(EnumBattleClassesPlayerClass parPlayerClass) {
		return classIcons[parPlayerClass.ordinal()];
	}
	
	// -------------------- ICooldownHolder implementation --------------------
	
	public static final float CLASS_SWITCH_COOLDOWN_VALUE = 30.0F;
	public static final int CLASS_SWITCH_COOLDOWN_HASHCODE = 1399;

	private float setTime;
	private float setDuration;
	
	@Override
	public void initCooldownHolder() {
		setTime = BattleClassesUtils.getCurrentTimeInSeconds() - COOLDOWN_INITIALIZER;
	}
	
	@Override
	public float getCooldownDuration() {
		return CLASS_SWITCH_COOLDOWN_VALUE;
	}

	@Override
	public void setToCooldown() {
		this.setCooldown(this.getCooldownDuration(), false);
	}

	@Override
	public void setToCooldownForced() {
		this.setCooldown(this.getCooldownDuration(), true);
	}
	
	public void setCooldown(float duration, boolean forced) {
		if(!isOnCooldown() || forced) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
			this.setDuration = duration;
			if(playerHooks.getOwnerPlayer() instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.getOwnerPlayer();
				if(entityPlayerMP != null) {
					BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
					FMLProxyPacket p = new BattleClassesPacketCooldownSet(playerHooks.getOwnerPlayer(), this.getCooldownHashCode(), this.getSetTime(), forced).generatePacket();
					Battlegear.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
				}
			}
		}
	}

	@Override
	public float getCooldownRemaining() {
		float timeRemaining = getSetTime() + getSetDuration() - BattleClassesUtils.getCurrentTimeInSeconds();
		if(timeRemaining < 0 ) {
			timeRemaining = 0;
		}
		return timeRemaining;
	}

	@Override
	public boolean isOnCooldown() {
		return getCooldownRemaining() > 0;
	}

	@Override
	public int getCooldownHashCode() {
		return CLASS_SWITCH_COOLDOWN_HASHCODE;
	}

	@Override
	public float getSetTime() {
		return setTime;
	}

	@Override
	public void setSetTime(float t) {
		setTime = t;
	}
	
	@Override
	public float getSetDuration() {
		return this.setTime;
	}
	
}
