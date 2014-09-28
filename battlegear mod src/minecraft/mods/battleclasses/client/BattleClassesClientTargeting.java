package mods.battleclasses.client;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battlegear2.Battlegear;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class BattleClassesClientTargeting {
	public static EntityLivingBase lastTarget;
	
	public static EntityLivingBase getClientTarget(float distance) {
		MovingObjectPosition mop =  Battlegear.proxy.getMouseOver(1, distance);
		if(mop != null && mop.entityHit != null) {
			if(mop.entityHit instanceof EntityLivingBase) {
				return (EntityLivingBase) mop.entityHit;
			}
		}

		return null;
	}
	
	public static void generateTargetingInfo() {
		Minecraft mc = Minecraft.getMinecraft();
		
		BattleClassesAbstractAbilityActive chosenAbility = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbility();
		if(!BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).isCastingInProgress()) {
			BattleClassesClientTargeting.lastTarget = null;
			return;
		}
		
		
		if(chosenAbility != null) {
			//TODO : Range
			float range = 40.0F;
			//chosenAbility.getRange() ...
			String targetInfo = null;
			
			if(chosenAbility.requiresRayTracingForTarget()) {
				EntityLivingBase target = BattleClassesClientTargeting.getClientTarget(range);
				EntityLivingBase finalTarget = chosenAbility.getFinalTargetFromRaytracedEntity(target);
				if(finalTarget != null) {
					if(BattleClassesUtils.isTargetFriendly(mc.thePlayer, finalTarget)) {
						targetInfo = "Healing " + getEntityName( finalTarget );
					}
					else {
						targetInfo = "Targeting " + getEntityName( finalTarget );
					}
				}
				else if (BattleClassesClientTargeting.lastTarget != null) {
					targetInfo = "Target lost!";
				}
				
				//Saving latest target
				BattleClassesClientTargeting.lastTarget = finalTarget;
			}
			/*
			else {
				switch(chosenAbility.getTargetingType()) {
					case TargetType_AREAEFFECT_OFFENSIVE: {
						targetInfo = "Targeting enemies around";
					}
						break;
					case TargetType_AREAEFFECT_SUPPORTIVE: {
						targetInfo = "Targeting allies around";
					}
						break;
					default:
						break;
				}
				
				BattleClassesClientTargeting.lastTarget = null;
			}
			*/
			
			
			if(targetInfo != null) {
				BattleClassesInGameGUI.displayTargetInfo(targetInfo);			
			}
		}
	}
	
	public static String getEntityName(EntityLivingBase entity) {
		Minecraft mc = Minecraft.getMinecraft();
		if(entity == mc.thePlayer) {
			return "Self";
		}
		return entity.getCommandSenderName();
	}
}
