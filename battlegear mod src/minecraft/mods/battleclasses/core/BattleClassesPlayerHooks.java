package mods.battleclasses.core;

import java.util.HashMap;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.core.classes.BattleClassesPlayerClassMage;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;


public class BattleClassesPlayerHooks {
	
	protected EntityPlayer ownerPlayer;
	
	public BattleClassesPlayerClass playerClass;
	public BattleClassesWeaponHitHandler weaponHitHandler;
	public HashMap<Integer, ICooldownHolder> mainCooldownMap;
	
	public BattleClassesPlayerHooks(EntityPlayer parOwnerPlayer) {
		this.ownerPlayer = parOwnerPlayer;
		mainCooldownMap = new HashMap<Integer, ICooldownHolder>();
		
		playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.PlayerClass_NONE);
		weaponHitHandler = new BattleClassesWeaponHitHandler(this);
	}
	
	public void switchToPlayerClass(EnumBattleClassesPlayerClass parPlayerClass) {
		
		switch(parPlayerClass) {
			case MAGE: {
				this.playerClass = new BattleClassesPlayerClassMage(this);
			}
				break;
				/*
			case PlayerClass_PRIEST:
				break;
			case PlayerClass_WARLOCK:
				break;
			case PlayerClass_ROGUE:
				break;
			case PlayerClass_HUNTER:
				break;
			case PlayerClass_PALADIN:
				break;
			case PlayerClass_WARRIOR:
				break;
				*/
			default: {
				this.playerClass = new BattleClassesPlayerClass(this, EnumBattleClassesPlayerClass.PlayerClass_NONE);
			}
				break;
		
		}
		
		FMLProxyPacket p = new BattleClassesPacketPlayerClassSnyc(this.ownerPlayer, parPlayerClass).generatePacket();
		
		if(this.getOwnerPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP) this.ownerPlayer;
			if(entityPlayerMP != null) {
				BattleClassesUtils.Log("Sending class switch sync to client: " + entityPlayerMP.getDisplayName(), LogType.PACKET);
				Battlegear.packetHandler.sendPacketToPlayer(p, entityPlayerMP);
			}
		}
		
		BattleClassesUtils.Log(this.ownerPlayer.getDisplayName() + " switched to class: " + parPlayerClass.toString(), LogType.CORE);
		
		playerClass.setToCooldownForced();
	}
	
	public EntityPlayer getOwnerPlayer() {
		return this.ownerPlayer;
	}
	
}
