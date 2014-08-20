package mods.battleclasses.core;

import java.util.EnumSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.battleclasses.EnumBattleClassesWieldAccess;
import mods.battleclasses.ability.BattleClassesWeaponSkill;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.weapons.IBackStabbable;
import mods.battlegear2.api.weapons.IHitTimeModifier;
import mods.battlegear2.api.weapons.IPenetrateWeapon;
import mods.battlegear2.api.weapons.IPotionEffect;
import mods.battlegear2.api.weapons.ISpecialEffect;

public class BattleClassesWeaponHitHandler {
	
	protected BattleClassesPlayerHooks playerHooks;
	
	EnumSet<EnumBattleClassesWieldAccess> accessSet;
	
	public BattleClassesWeaponSkill mainHand;
	public BattleClassesWeaponSkill offHand;
	
	public BattleClassesWeaponHitHandler(BattleClassesPlayerHooks parPlayerHooks) {
		this.playerHooks = parPlayerHooks;
	}
	
	public void initAccessSet() {
		accessSet.clear();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttack(LivingAttackEvent event){
		 
    }
}
