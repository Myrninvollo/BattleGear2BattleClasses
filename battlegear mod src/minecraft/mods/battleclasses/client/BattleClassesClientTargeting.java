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
		
		//TODO : Range
		float range = 40.0F;
		//chosenAbility.getRange() ...
		String targetInfo = null;
		EntityLivingBase target = BattleClassesClientTargeting.getClientTarget(range);
		if(target != null) {
			targetInfo = "Targeting: " + getEntityName(target);
		}
		else if (BattleClassesClientTargeting.lastTarget != null) {
			targetInfo = "Target lost!";
		}
		
		if(targetInfo != null) {
			BattleClassesInGameGUI.displayTargetInfo(targetInfo);			
		}
		
		//Saving latest target
		BattleClassesClientTargeting.lastTarget = target;
	}
	
	public static String getEntityName(EntityLivingBase entity) {
		//String targetFullClassPath = entity.toString();
		//String[] pathParts = targetFullClassPath.split("\\.");
		//return pathParts[pathParts.length-1];
		return entity.getCommandSenderName();
	}
}
