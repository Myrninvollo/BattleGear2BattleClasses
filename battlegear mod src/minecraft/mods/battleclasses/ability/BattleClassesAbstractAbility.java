package mods.battleclasses.ability;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbility implements ICooldownHolder {
	
	private int abilityID;
	
	protected BattleClassesPlayerHooks playerHooks;
	
	public int getAbilityID() {
		return abilityID;
	}
	
	public BattleClassesAbstractAbility(BattleClassesPlayerHooks parPlayerHooks, int parAbilityID) {
		this.playerHooks = parPlayerHooks;
		this.abilityID = parAbilityID;
	}
	
	private BattleClassesAbstractAbility() {
		
	}
	
	public abstract void performEffect();
	
	// -------------------- ICooldownHolder implementation --------------------

	private float cooldownDuration = 0.0F;
	
	public void setCooldownDuration(float d) {
		this.cooldownDuration = d;
	}

	private float setTime;
	
	@Override
	public void initCooldownHolder() {
		setTime = BattleClassesUtils.getCurrentTimeInSeconds() - COOLDOWN_INITIALIZER;
	}
	
	@Override
	public float getCooldownDuration() {
		return this.cooldownDuration;
	}

	@Override
	public void setToCooldown() {
		if(!isOnCooldown()) {
			this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
			if(playerHooks.getOwnerPlayer() instanceof EntityPlayerMP) {
				EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.getOwnerPlayer();
				if(entityPlayerMP != null) {
					BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
					FMLProxyPacket p = new BattleClassesPacketCooldownSet(playerHooks.getOwnerPlayer(), this.getCooldownHashCode(), false).generatePacket();
					Battlegear.packetHandler.sendPacketToPlayerWithSideCheck(p, entityPlayerMP);
				}
			}
			
		}
	}

	@Override
	public void setToCooldownForced() {
		this.setTime = BattleClassesUtils.getCurrentTimeInSeconds();
		
		if(playerHooks.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerHooks.getOwnerPlayer();
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class cooldown set to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				FMLProxyPacket p = new BattleClassesPacketCooldownSet(playerHooks.getOwnerPlayer(), this.getCooldownHashCode(), true).generatePacket();
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
		return this.getAbilityID();
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
