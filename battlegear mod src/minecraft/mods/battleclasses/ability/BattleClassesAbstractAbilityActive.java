package mods.battleclasses.ability;

import java.util.HashSet;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.EnumBattleClassesTargetType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbilityActive extends BattleClassesAbstractAbilityCooldownHolder {

	public BattleClassesAbstractAbilityActive(int parAbilityID) {
		super(parAbilityID);
	}
	
	//public static HashSet<BattleClassesAbstractAbilityActive> activeAbilityFactoryHashSet;
	
	protected EnumBattleClassesTargetType targetType = EnumBattleClassesTargetType.TargetType_UNNECESSARY_UNIVERSAL;
	protected float castTime = 0;
	protected int channelTickCount = 1;
	protected boolean channeled = false;
	protected boolean ignoresGlobalCooldown = false;
	protected boolean ignoresSilence = false;
	protected boolean requiresMeleeSwing = false;
	protected int requiredItemLevel = 0;
	
	/**
	 * Called when player presses Mouse-Right button
	 */
	public void onCastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!this.isAvailable(entityPlayer, itemStack)) {
			return;
		}
		
		if(this.isInstant()) {
			this.requestProcession(entityPlayer, itemStack, 0);
		}
		else {
			this.startCasting(entityPlayer, itemStack);
		}
	}
	
	public void onCastTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(!this.channeled) {
			return;
		}
		int currentCastTick = tickCount - 72000;
		if(currentCastTick > 0) {
			int ticksPerProceed = this.getCastTimeInTicks() / this.channelTickCount;
			if((currentCastTick % ticksPerProceed) == 0) {
				this.requestProcession(entityPlayer, itemStack, tickCount);
			}
		}
	}
	
	public void onCastRelease(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
		if(this.channeled) {
			return;
		}
		int currentCastTick = tickCount - 72000;
		if(currentCastTick <= 0) {
			this.requestProcession(entityPlayer, itemStack, tickCount);
		}
	}
	
	protected void startCasting(EntityPlayer entityPlayer, ItemStack itemStack) {
		BattleClassesUtils.setEntityPlayerItemInUseInSeconds(entityPlayer, itemStack, this.castTime);
	}
	
	/**
	 * Called on CastStart to check if the ability is available and ready to use
	 * @param entityPlayer - the caster player
	 * @param itemStack - the itemStack containing the held item
	 * @return - availability of the ability
	 */
	public boolean isAvailable(EntityPlayer entityPlayer, ItemStack itemStack) {
		boolean hasRequiredItem = false;
		if(itemStack.getItem() instanceof BattleClassesItemWeapon) {
			BattleClassesItemWeapon weapon = (BattleClassesItemWeapon) itemStack.getItem();
			if(weapon.getClassAccessSet().contains(BattleClassesUtils.getPlayerClass(entityPlayer)) ) {
				if(weapon.getItemAbilityLevel() >= this.requiredItemLevel) {
					hasRequiredItem = true;
				}
				else {
					//HUD WARNING MESSAGE
				}
			}
			else {
				//HUD WARNING MESSAGE
			}
		}
		
		boolean cooldownFree = !this.isOnCooldown();
		if(!cooldownFree) {
			//HUD WARNING MESSAGE
		}
		
		boolean hasRequiredAmmo = true;
		if(!hasRequiredAmmo) {
			//HUD WARNING MESSAGE
		}
		
		return hasRequiredItem && cooldownFree && hasRequiredAmmo;
	}
	
	/**
	 * Called before the proceedAbility method to check if should happen. Working differently on client and server due to targeting.
	 * @param entityPlayer - the caster player
	 * @param itemStack - the itemStack containing the held item
	 * @return - proceedAbility method should happen or not
	 */
	/*
	public boolean isAbleToProceed(EntityPlayer entityPlayer, ItemStack itemStack) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		//Checking CLIENT SIDE
		if(side == Side.CLIENT) {
			if(this.requiresRayTracingForTarget()) {
				//CHECK TARGET REQUIREMENTS
				
				//CHECK RANGE REQUIREMENTS
				
			}
			return true;
		}
		//Checking SERVER SIDE
		else {
			if(this.requiresRayTracingForTarget()) {
				return false;
			}
			
			return true;
		}
	}
	*/
	
	public boolean requiresRayTracingForTarget() {
		switch (this.targetType) {
		case TargetType_AREAEFFECT_OFFENSIVE:
			return false;
		case TargetType_AREAEFFECT_SUPPORTIVE:
			return false;
		case TargetType_AREAEFFECT_UNIVERSAL:
			return false;
		case TargetType_OPTIONAL_OFFENSIVE:
			return true;
		case TargetType_OPTIONAL_SUPPORTIVE:
			return true;
		case TargetType_OPTIONAL_UNIVERSAL:
			return true;
		case TargetType_REQUIRED_OFFENSIVE:
			return true;
		case TargetType_REQUIRED_SUPPORTIVE:
			return true;
		case TargetType_UNNECESSARY_UNIVERSAL:
			return false;
		default:
			return false;
		}
		//return false;
	}
	
	public boolean isInstant() {
		return this.castTime == 0;
	}
	
	public int getCastTimeInTicks() {
		return (int) (castTime * 20);
	}
	
	public void cancelCasting(EntityPlayer entityPlayer) {
		entityPlayer.clearItemInUse();
	}
	
	public void requestProcession(EntityPlayer entityPlayer, ItemStack itemStack, int tickCount) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		//Checking CLIENT SIDE
		if(side == Side.CLIENT) {
			if(!this.requiresRayTracingForTarget()) {
				return;
			}
			//GET TARGET
			
			//CHECK TARGET REQUIREMENTS
			
			//CHECK RANGE REQUIREMENTS
			
			//IF REQUIREMENTS ARE SATISFIED, SEND PERFORMREQUEST PACKET
		}
		//Checking SERVER SIDE
		else {
			if(this.requiresRayTracingForTarget()) {
				return;
			}
			EntityLiving targetEntity = null;
			this.proceedAbility(targetEntity, tickCount);
		}
	}
	
	public boolean proceedAbility(EntityLiving targetEntity, int tickCount) {
		boolean performSucceeded = this.performEffect(targetEntity, tickCount);
		if(performSucceeded) {
			this.onCastFinished(targetEntity, tickCount);
		}
		
		return performSucceeded;
	}
	
	public abstract boolean performEffect(EntityLiving targetEntity, int tickCount);
		
	public void onCastFinished(EntityLiving targetEntity, int tickCount) {
		this.setToCooldown();
		//ADD Global coolDown here!
	}
	
}
