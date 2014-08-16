package mods.battleclasses.gui.tab;

import mods.battlegear2.client.gui.BattleEquipGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;

public class GuiTabBarButtonVanillaInventory extends BattleClassesGuiTabBarButton {
	
	public GuiTabBarButtonVanillaInventory(int par1, int par2, int par3) {
		super(par1, par2, par3, "Inventory");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void openGui(Minecraft mc) {
		
		
		mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		//GuiInventory.open(mc.thePlayer);
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return GuiInventory.class;
	}
}
