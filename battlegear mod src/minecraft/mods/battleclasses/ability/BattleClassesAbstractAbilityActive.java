package mods.battleclasses.ability;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesPlayerHooks;
import mods.battleclasses.core.ICooldownHolder;
import mods.battleclasses.enumhelper.EnumBattleClassesAbilitySchool;
import mods.battleclasses.enumhelper.EnumBattleClassesCastType;
import mods.battleclasses.enumhelper.EnumBattleClassesTargetType;
import mods.battleclasses.items.BattleClassesItemWeapon;
import mods.battleclasses.packet.BattleClassesPacketCooldownSet;
import mods.battlegear2.Battlegear;

public abstract class BattleClassesAbstractAbilityActive extends BattleClassesAbstractAbilityCooldownHolder {

	public BattleClassesAbstractAbilityActive(int parAbilityID) {
		super(parAbilityID);
		abilityIconResourceLocation = new ResourceLocation("battleclasses", getAbilityIconPath() + getAbilityIconName() + ".png");
	}
	
	protected IIcon abilityIcon;
	public ResourceLocation abilityIconResourceLocation;
	
	protected EnumBattleClassesAbilitySchool school = EnumBattleClassesAbilitySchool.UNKNOWN;
	protected EnumBattleClassesTargetType targetType = EnumBattleClassesTargetType.TargetType_UNNECESSARY_UNIVERSAL;
	protected EnumBattleClassesCastType castingType = EnumBattleClassesCastType.CastType_UNKNOWN;
	protected float castTime = 0;
	protected int channelTickCount = 1;
	protected boolean channeled = false;
	public boolean ignoresGlobalCooldown = false;
	protected boolean ignoresSilence = false;
	protected boolean requiresMeleeSwing = false;
	protected int requiredItemLevel = 0;
	protected String name = "Unknown Ability";
	
	/**
	 * Called when player presses Mouse-Right button
	 */
	public void onCastStart(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if(!this.isAvailable(entityPlayer, itemStack)) {
			return;
		}
		BattleClassesUtils.getPlayerSpellBook(entityPlayer).setGlobalCooldown();
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
		if(currentCastTick >= 0) {
			int ticksPerProceed = this.getCastTimeInTicks() / this.channelTickCount;
			int currentCastTickInverted = this.getCastTimeInTicks() - currentCastTick;
			if(currentCastTickInverted > 0 && (currentCastTickInverted % ticksPerProceed) == 0) {
				BattleClassesUtils.Log("Channeling... Current tick: " + currentCastTickInverted + " Cast time in tick " + this.getCastTimeInTicks(), LogType.ABILITY);
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
		BattleClassesUtils.Log("Casting started!", LogType.ABILITY);
		BattleClassesUtils.setEntityPlayerItemInUseInSeconds(entityPlayer, itemStack, this.castTime);
	}
	
	public EnumBattleClassesAbilitySchool getSchool() {
		return this.school;
	}
	
	public int getChannelTicks() {
		return this.channelTickCount;
	}
	
	public boolean isChanneled() {
		return this.channeled;
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
	}
	
	//Helper method
	public void setCastingType(EnumBattleClassesCastType parCastType) {
		this.castingType = parCastType;
		switch(this.castingType) {
			case CastType_CASTED: {
				this.channeled = false;
			}
				break;
			case CastType_CHANNELED: {
				this.channeled = true;
			}
				break;
			case CastType_INSTANT: {
				this.channeled = false;
				this.castTime = 0;
			}
				break;
			default:
				break;
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected String getAbilityIconName() {
		return "ability_" + this.abilityID;
	}
	
    @SideOnly(Side.CLIENT)
    public String getAbilityIconPath() {
    	return "textures/spells/icons/";
    }
    
    @SideOnly(Side.CLIENT)    
    public ResourceLocation getIconResourceLocation() {
    	return abilityIconResourceLocation;
    }
    
    @SideOnly(Side.CLIENT)
    public String getName() {
    	return name;
    }
    
    @SideOnly(Side.CLIENT)
    public float getCastPercentage(EntityPlayer entityPlayer) {
    	float f = 0;
    	float total = (float) this.getCastTimeInTicks();
    	float current = total - (float) (entityPlayer.getItemInUseCount() - 72000);
    	f = current/total;
    	if(this.channeled) {
    		f = 1.0F - f;
    	}
    	if(f > 1) {
    		f = 1;
    	}
    	if(f < 0) {
    		f = 0;
    	}
    	//System.out.println(current + "/" + total + " | F = " + f);
    	return f;
    }
	
}
