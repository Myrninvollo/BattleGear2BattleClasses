package mods.battleclasses.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.battleclasses.gui.tab.BattleClassesGuiTabBarButton;
import mods.battleclasses.gui.tab.GuiTabBarButtonBattleInventory;
import mods.battleclasses.gui.tab.GuiTabBarButtonClassSelector;
import mods.battleclasses.gui.tab.GuiTabBarButtonConfig;
import mods.battleclasses.gui.tab.GuiTabBarButtonHelp;
import mods.battleclasses.gui.tab.GuiTabBarButtonTalentSelector;
import mods.battleclasses.gui.tab.GuiTabBarButtonVanillaInventory;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.gui.BattlegearInGameGUI;
import mods.battlegear2.client.gui.controls.GuiBGInventoryButton;
import mods.battlegear2.client.gui.controls.GuiPlaceableButton;
import mods.battlegear2.client.gui.controls.GuiSigilButton;
import mods.battlegear2.utils.BattlegearConfig;

public class BattleClassesClientEvents extends BattlegearClientEvents {
	
	private final BattleClassesInGameGUI inGameGUI = new BattleClassesInGameGUI();
	
	public static List<BattleClassesGuiTabBarButton> tabsList = new ArrayList<BattleClassesGuiTabBarButton>();
	static {
		tabsList.add(new GuiTabBarButtonVanillaInventory(0, 10, 10));
		tabsList.add(new GuiTabBarButtonBattleInventory(1, 20, 20));
		tabsList.add(new GuiTabBarButtonTalentSelector(2, 30, 30));
		tabsList.add(new GuiTabBarButtonClassSelector(3, 40, 40));
		tabsList.add(new GuiTabBarButtonHelp(4, 50, 50));
		tabsList.add(new GuiTabBarButtonConfig(5, 60, 60));
	}

	@SubscribeEvent
	public void postRenderOverlay(RenderGameOverlayEvent.Post event) {
		if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
			inGameGUI.renderGameOverlay(event.partialTicks, event.mouseX, event.mouseY);
		}
	}
	
    /**
     * Helper method to add buttons to a gui when opened
     * @param buttons the List<GuiButton> of the opened gui
     * @param guiLeft horizontal placement parameter
     * @param guiTop vertical placement parameter
     */
	public static void onOpenGui(List buttons, int guiLeft, int guiTop) {
        if(BattlegearConfig.enableGuiButtons){
			int count = 0;
			for (GuiPlaceableButton button : tabsList) {
				button.place(count, guiLeft, guiTop);
				button.id = buttons.size()+2;//Due to GuiInventory and GuiContainerCreative button performed actions, without them having buttons...
				count++;
				buttons.add(button);
			}
        }
	}
	
}
