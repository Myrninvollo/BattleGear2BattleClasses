package mods.battleclasses.ability;

import java.util.HashSet;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.EnumBattleClassesTargetType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbilityActive extends BattleClassesAbstractAbility implements ICooldownHolder {

	public BattleClassesAbstractAbilityActive(int parAbilityID) {
		super(parAbilityID);
	}
	
	//public static HashSet<BattleClassesAbstractAbilityActive> activeAbilityFactoryHashSet;
		
	public void setPlayerHooks(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
		parPlayerHooks.mainCooldownMap.put(this.getCooldownHashCode(), this);
	}
	
	protected EnumBattleClassesTargetType targetType;
	protected float castTime = 0;
	protected int channelTickCount = 1;
	protected boolean channeled = false;
	protected boolean ignoresGlobalCooldown = false;
	protected boolean ignoresSilence = false;
	protected boolean requiresMeleeSwing = false;
	
	
	/**
	 * Called when player presses Mouse-Right button
	 */
	public void onUseStart() {
		switch (this.targetType) {
		case TargetType_AREAEFFECT_OFFENSIVE:
			break;
		case TargetType_AREAEFFECT_SUPPORTIVE:
			break;
		case TargetType_AREAEFFECT_UNIVERSAL:
			break;
		case TargetType_OPTIONAL_OFFENSIVE:
			break;
		case TargetType_OPTIONAL_SUPPORTIVE:
			break;
		case TargetType_OPTIONAL_UNIVERSAL:
			break;
		case TargetType_REQUIRED_OFFENSIVE:
			break;
		case TargetType_REQUIRED_SUPPORTIVE:
			break;
		case TargetType_UNNECESSARY_UNIVERSAL:
			break;
		default:
			break;
		}
	}
	
	protected void startCasting(EntityPlayer entityPlayer, ItemStack itemStack) {
		BattleClassesUtils.setEntityPlayerItemInUseInSeconds(entityPlayer, itemStack, this.castTime);
	}
	
	/**
	 * Called when player holds down Mouse-Right button
	 */
	public void onUseTick() {
		switch (this.targetType) {
		case TargetType_AREAEFFECT_OFFENSIVE:
			break;
		case TargetType_AREAEFFECT_SUPPORTIVE:
			break;
		case TargetType_AREAEFFECT_UNIVERSAL:
			break;
		case TargetType_OPTIONAL_OFFENSIVE:
			break;
		case TargetType_OPTIONAL_SUPPORTIVE:
			break;
		case TargetType_OPTIONAL_UNIVERSAL:
			break;
		case TargetType_REQUIRED_OFFENSIVE:
			break;
		case TargetType_REQUIRED_SUPPORTIVE:
			break;
		case TargetType_UNNECESSARY_UNIVERSAL:
			break;
		default:
			break;
		}
	}
	
	public void cancelCasting(EntityPlayer entityPlayer) {
		entityPlayer.clearItemInUse();
	}
	
	/**
	 * Called when player releases Mouse-Right button
	 */
	public void onUseEnd() {
		switch (this.targetType) {
		case TargetType_AREAEFFECT_OFFENSIVE:
			break;
		case TargetType_AREAEFFECT_SUPPORTIVE:
			break;
		case TargetType_AREAEFFECT_UNIVERSAL:
			break;
		case TargetType_OPTIONAL_OFFENSIVE:
			break;
		case TargetType_OPTIONAL_SUPPORTIVE:
			break;
		case TargetType_OPTIONAL_UNIVERSAL:
			break;
		case TargetType_REQUIRED_OFFENSIVE:
			break;
		case TargetType_REQUIRED_SUPPORTIVE:
			break;
		case TargetType_UNNECESSARY_UNIVERSAL:
			break;
		default:
			break;
		}
	}
	
	public abstract boolean performEffect(EntityLiving targetEntity);
	
	public void onFinishedCasting() {
		this.setToCooldown();
		//ADD Global coolDown here!
	}
	
	
	// -------------------- ICooldownHolder implementation --------------------

	protected float cooldownDuration = 0.0F;
	
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
