package mods.battleclasses.ability;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbilityCooldownHolder extends BattleClassesAbstractAbility implements ICooldownHolder {
	
	public BattleClassesAbstractAbilityCooldownHolder(int parAbilityID) {
		super(parAbilityID);
		this.initCooldownHolder();
	}
	
	@Override
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		parPlayerHooks.mainCooldownMap.put(this.getCooldownHashCode(), this);
	}

	// -------------------- ICooldownHolder implementation --------------------
	

	protected float cooldownDuration = 0.0F;
	
	public void setCooldownDuration(float d) {
		this.cooldownDuration = d;
	}

	private float setTime;
	private float setDuration;
	
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
		this.setCooldown(this.getCooldownDuration(), false);
	}

	@Override
	public void setToCooldownForced() {
		this.setCooldown(this.getCooldownDuration(), true);
	}
	
	public void setCooldown(float duration, boolean forced) {
		if( duration > this.getCooldownRemaining() || forced) {
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
		return this.getAbilityID();
	}
	
	@Override
	public float getSetDuration() {
		return this.setDuration;
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
