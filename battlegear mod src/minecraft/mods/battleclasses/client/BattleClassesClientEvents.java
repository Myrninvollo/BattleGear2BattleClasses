package mods.battleclasses.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
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
		tabsList.add(new GuiTabBarButtonConfig(5, 60, 60, false));
	}

	@SubscribeEvent
	public void postRenderOverlay(RenderGameOverlayEvent.Post event) {
		if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
			inGameGUI.renderGameOverlay(event.partialTicks, event.mouseX, event.mouseY);
		}
	}
	
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		BattleClassesUtils.Log("Gui Opened", LogType.GUI);
		if(event.gui instanceof GuiContainer ) {
			
			GuiContainer guiContiner = (GuiContainer)event.gui;
			
			BattleClassesUtils.Log("Gui Opened1", LogType.GUI);
			
			Minecraft mc = Minecraft.getMinecraft();
			GuiContainer guiContainer = ((GuiContainer) mc.currentScreen);
        	
        	if(guiContainer != null) {
        		/** The X size of the inventory window in pixels. */
            	int xSize = 176;
                /** The Y size of the inventory window in pixels. */
                int ySize = 166;
            	int guiLeft = (guiContainer.width - xSize) / 2 - 32;
            	int guiTop = (guiContainer.height - ySize) / 2;
                
            	System.out.println("Current gui class:" + event.gui);
            	
            	Field f;
				try {
					
					f = GuiScreen.class.getDeclaredField("buttonList");
					f.setAccessible(true);
                	List buttonListRefl = (List) f.get( ((GuiScreen)guiContainer) );
                	
                    
                    BattleClassesClientEvents.onOpenGui(buttonListRefl, guiLeft, guiTop);
                    
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
        	}
			
		}
	}
	
	public static final int TABS_ON_LEFT = 5; 
	
    /**
     * Helper method to add buttons to a gui when opened
     * @param buttons the List<GuiButton> of the opened gui
     * @param guiLeft horizontal placement parameter
     * @param guiTop vertical placement parameter
     */
	public static void onOpenGui(List buttons, int guiLeft, int guiTop) {
		BattleClassesUtils.Log("Adding Extra inventory buttons", LogType.GUI);
        if(BattlegearConfig.enableGuiButtons){
			int i = 0;
			for (BattleClassesGuiTabBarButton button : tabsList) {
				if(i < TABS_ON_LEFT) {
					//Placing to horizontal bar button positions
					button.place(i, guiLeft + 0, guiTop + 11 + i * (BattleClassesGuiTabBarButton.BAR_BUTTON_GAP + BattleClassesGuiTabBarButton.BAR_BUTTON_SIZE_H_H) );
				}
				else {
					//Placing to vertical bar button positions
					button.place(i, guiLeft + 28 + 11 + (i - 5) *(BattleClassesGuiTabBarButton.BAR_BUTTON_GAP + BattleClassesGuiTabBarButton.BAR_BUTTON_SIZE_W_V),
							guiTop + 163);
				}
				button.id = buttons.size()+2;//Due to GuiInventory and GuiContainerCreative button performed actions, without them having buttons...
				i++;
				buttons.add(button);
			}
        }
	}
	
	/**
     * Helper method to return to Inventory from displayed tabs
     */
	public static void returnToInventory(EntityPlayer entityPlayer) {
		BattleClassesUtils.Log("Trying to close opened gui!", LogType.GUI);
		if(entityPlayer instanceof EntityPlayerMP) {
    		
    		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
    		entityPlayerMP.closeScreen();
    		
    		/*
			Minecraft mc =  Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiInventory(entityPlayer));
			*/
    	}
	}
	
}
