package mods.battleclasses.gui.tab;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.client.BattleClassesClientEvents;
import mods.battleclasses.gui.BattleClassesGUIHandler;
import mods.battlegear2.Battlegear;
import mods.battlegear2.client.gui.BattleEquipGUI;
import mods.battlegear2.gui.BattlegearGUIHandeler;
import mods.battlegear2.packet.BattlegearGUIPacket;
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
		BattleClassesUtils.Log("GuiTabBarButtonVanillaInventory openGui", LogType.GUI);
		//BattleClassesClientEvents.returnToInventory(mc.thePlayer);
		//mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		//GuiInventory.open(mc.thePlayer);
		
		Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.vanillaInventoryID).generatePacket());
	}

	@Override
	protected Class<? extends GuiScreen> getGUIClass() {
		return GuiInventory.class;
	}
}
