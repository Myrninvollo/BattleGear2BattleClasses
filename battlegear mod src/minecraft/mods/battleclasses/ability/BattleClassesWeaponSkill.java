package mods.battleclasses.ability;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;


public class BattleClassesWeaponSkill extends BattleClassesAbstractAbilityCooldownHolder {

	public BattleClassesWeaponSkill(int parAbilityID) {
		super(parAbilityID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		parPlayerHooks.mainCooldownMap.put(this.getCooldownHashCode(), this);
	}

	@Override
	public void performEffect() {
		// TODO Auto-generated method stub
		
	}

	public static final int MELEE_SWING_COOLDOWN = 1;
	
	@Override
	public float getCooldownDuration() {
		// CHECK WEAPON SPEED !! TO DO
		return MELEE_SWING_COOLDOWN;
	}
	
	
	
	
	/*
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
	*/

}
