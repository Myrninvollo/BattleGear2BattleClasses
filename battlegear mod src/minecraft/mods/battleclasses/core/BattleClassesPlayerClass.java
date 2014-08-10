package mods.battleclasses.core;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearAnimationPacket;
import mods.battlegear2.utils.EnumBGAnimations;

public class BattleClassesPlayerClass implements ICooldownHolder {
	
	public static final float CLASS_SWITCH_COOLDOWN = 30.0F;
	
	protected BattleClassesPlayerHooks playerHooks;
	protected EnumBattleClassesPlayerClass playerClass;
	
	public BattleClassesPlayerClass(BattleClassesPlayerHooks parPlayerHooks, EnumBattleClassesPlayerClass parPlayerClass) {
		this.playerHooks = parPlayerHooks;
		this.setPlayerClass(parPlayerClass);
	}
	
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		this.setPlayerClass(parPlayerClass);

		this.setToCooldown();
		
		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(playerHooks.ownerPlayer, playerClass).generatePacket();
		
		if(playerHooks.ownerPlayer instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.ownerPlayer;
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class switch sync to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				Battlegear.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
		
		
		
        
		
		BattleClassesUtils.Log(playerHooks.ownerPlayer.getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
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

	private float setTime;
	
	@Override
	public float getCooldown() {
		return CLASS_SWITCH_COOLDOWN;
	}

	@Override
	public void setToCooldown() {
		if(!isOnCooldown()) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
		}
	}

	@Override
	public void setToCooldownForced() {
		this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
	}

	@Override
	public float getCooldownRemaining() {
		float timeRemaining = BattleClassesUtils.getCurrentTimeInSeconds() - getCooldown();
		if(timeRemaining < 0 ) {
			timeRemaining = 0;
		}
		return timeRemaining;
	}

	@Override
	public boolean isOnCooldown() {
		return getCooldownRemaining() > 0;
	}
	
}
