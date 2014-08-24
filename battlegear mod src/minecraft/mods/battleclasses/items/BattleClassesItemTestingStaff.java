package mods.battleclasses.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import mods.battleclasses.BattleClassesUtils;
import mods.battlegear2.utils.BattlegearConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class BattleClassesItemTestingStaff extends ItemSword {

    protected final ToolMaterial material;
	
	public BattleClassesItemTestingStaff(ToolMaterial toolMaterial) {
		super(toolMaterial);
		// TODO Auto-generated constructor stub
		this.material = toolMaterial;
        this.setCreativeTab(BattlegearConfig.customTab);
        
        this.setUnlocalizedName("battleclasses:"+"weapons/WoodenStaff");
		this.setTextureName("battleclasses:"+"weapons/WoodenStaff");
		
	}
	
	/**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
    	BattleClassesUtils.getPlayerSpellBook(entityPlayer).CastStart(itemStack, world, entityPlayer);
    	return itemStack;
    }
	
    public void onUsingTick(ItemStack itemStack, EntityPlayer entityPlayer, int tickCount) {
    	BattleClassesUtils.getPlayerSpellBook(entityPlayer).CastTick(itemStack, entityPlayer, tickCount);
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int tickCount) {
    	BattleClassesUtils.getPlayerSpellBook(entityPlayer).CastRelease(itemStack, entityPlayer, tickCount);
    }
}
