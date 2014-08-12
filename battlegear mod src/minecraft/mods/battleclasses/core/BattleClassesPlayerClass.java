package mods.battleclasses.core;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearAnimationPacket;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesPlayerClass implements ICooldownHolder {
		
	protected BattleClassesPlayerHooks playerHooks;
	protected EnumBattleClassesPlayerClass playerClass;
	
	public BattleClassesPlayerClass(BattleClassesPlayerHooks parPlayerHooks, EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks = parPlayerHooks;
		parPlayerHooks.mainCooldownMap.put(this.getCooldownHashCode(), this);
		this.setPlayerClass(parPlayerClass);
		this.initCooldownHolder();
	}
	
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.setPlayerClass(parPlayerClass);

		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(playerHooks.ownerPlayer, playerClass).generatePacket();
		
		if(playerHooks.ownerPlayer instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.ownerPlayer;
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class switch sync to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				Battlegear.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
		
		BattleClassesUtils.Log(playerHooks.ownerPlayer.getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
		
		this.setToCooldownForced();
	}
	
	protected void setPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerClass = parPlayerClass;
		
		//TO DO
		//Set SpellBook content
		//Set Talents content
		//Set Weapon skill abilities
	}
	
	public EnumBattleClassesPlayerClass getPlayerClass() {
		return this.playerClass;
	}
	
	// -------------------- ICooldownHolder implementation --------------------
	
	public static final float CLASS_SWITCH_COOLDOWN_VALUE = 30.0F;
	public static final int CLASS_SWITCH_COOLDOWN_HASHCODE = 1399;

	private float setTime;
	
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
		if(!isOnCooldown()) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
			if(playerHooks.ownerPlayer instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.ownerPlayer;
				if(entityPlayerMP != null) {
					BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
					FMLProxyPacket p = new BattleClassesPacketCooldownSet(playerHooks.ownerPlayer, this.getCooldownHashCode(), false).generatePacket();
					Battlegear.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
				}
			}
			
		}
	}

	@Override
	public void setToCooldownForced() {
		this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
		
		if(playerHooks.ownerPlayer instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.ownerPlayer;
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				FMLProxyPacket p = new BattleClassesPacketCooldownSet(playerHooks.ownerPlayer, this.getCooldownHashCode(), true).generatePacket();
				Battlegear.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
			}
		}
	}

	@Override
	public float getCooldownRemaining() {
		float timeRemaining = getSetTime() + getCooldownDuration() - BattleClassesUtils.getCurrentTimeInSeconds();
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
	
}
