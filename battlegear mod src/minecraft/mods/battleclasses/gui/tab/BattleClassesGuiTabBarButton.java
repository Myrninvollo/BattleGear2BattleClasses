package mods.battleclasses.gui.tab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IIcon;
import mods.battlegear2.client.gui.BattleEquipGUI;
import mods.battlegear2.client.gui.controls.GuiPlaceableButton;

public abstract class BattleClassesGuiTabBarButton extends GuiPlaceableButton {
	
	public IIcon tabButtonIcon;
	
	public boolean horizontal = true; 

	public BattleClassesGuiTabBarButton(int par1, int par2, int par3,
			String name) {
		super(par1, par2, par3, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected abstract Class<? extends GuiScreen> getGUIClass();

	@Override
	protected abstract void openGui(Minecraft mc);

}
