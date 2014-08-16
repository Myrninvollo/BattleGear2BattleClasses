package mods.battleclasses.client;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.RenderItemBarEvent;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.quiver.QuiverArrowRegistry;
import mods.battlegear2.api.shield.IShield;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.ClientProxy;
import mods.battlegear2.client.gui.BattlegearInGameGUI;

public class BattleClassesInGameGUI extends BattlegearInGameGUI {
	
	public static void popGUI(Minecraft mc) {
		/*
		Minecraft mcc = FMLClientHandler.instance().getClient();
		GuiScreen currentGui = mcc.currentScreen;
		if(currentGui != null) {
			EntityPlayerMP entityPlayerMP = FMLClientHandler.instance().getClient();
			if(entityPlayerMP != null) {
				entityPlayerMP.closeScreen();
				entityPlayerMP.closeContainer();
			}
			//currentGui.onGuiClosed();
			//mcc.currentScreen = null;
		}
		*/
	}
	
    public void renderGameOverlay(float frame, int mouseX, int mouseY) {
    	
        if(Battlegear.battlegearEnabled){
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            RenderGameOverlayEvent renderEvent = new RenderGameOverlayEvent(frame, scaledresolution, mouseX, mouseY);

            if (!this.mc.playerController.enableEverythingIsScrewedUpMode()) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                zLevel = -90.0F;
                if(!ClientProxy.tconstructEnabled || mc.thePlayer.capabilities.isCreativeMode){
	                if(mc.currentScreen==null) {
                        if(previousGui!=null)
	                	    previousGui=null;
	                }else{
                        Class<?> currentGui = mc.currentScreen.getClass();
                        if(currentGui!=previousGui && (currentGui.equals(GuiContainerCreative.class) || currentGui.equals(GuiInventory.class))){
                        	
                        	GuiContainer guiContainer = ((GuiContainer) mc.currentScreen);
                        	
                        	if(guiContainer != null) {
                        		/** The X size of the inventory window in pixels. */
                            	int xSize = 176;
                                /** The Y size of the inventory window in pixels. */
                                int ySize = 166;
                            	int guiLeft = (guiContainer.width - xSize) / 2 ;
                            	int guiTop = (guiContainer.height - ySize) / 2;
                                
                            	System.out.println("Current gui class:" + currentGui);
                            	
                            	Field f;
								try {
									
									f = GuiScreen.class.getDeclaredField("buttonList");
									f.setAccessible(true);
	                            	List buttonListRefl = (List) f.get( ((GuiScreen)guiContainer) );
	                            	
	                                
	                                BattleClassesClientEvents.onOpenGui(buttonListRefl, guiLeft - 28, guiTop);
	                                previousGui = currentGui;
	                                
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                            	
                        	}
                        }
                    }
                }

                RenderItemBarEvent event = new RenderItemBarEvent.BattleSlots(renderEvent, true);
                if(!MinecraftForge.EVENT_BUS.post(event)){
                    renderBattleSlots(width / 2 + 121 + event.xOffset, height - 22 + event.yOffset, frame, true);
                }
                event = new RenderItemBarEvent.BattleSlots(renderEvent, false);
                if(!MinecraftForge.EVENT_BUS.post(event)){
                    renderBattleSlots(width / 2 - 184 + event.xOffset, height - 22 + event.yOffset, frame, false);
                }

                ItemStack offhand = ((InventoryPlayerBattle) mc.thePlayer.inventory).getCurrentOffhandWeapon();
                if(offhand!= null && offhand.getItem() instanceof IShield){
                    event = new RenderItemBarEvent.ShieldBar(renderEvent, offhand);
                    if(!MinecraftForge.EVENT_BUS.post(event))
                        renderBlockBar(width / 2 - 91 + event.xOffset, height - 35 + event.yOffset);
                }

                ItemStack mainhand = mc.thePlayer.getCurrentEquippedItem();
                if(mainhand != null){
                    ItemStack quiver = QuiverArrowRegistry.getArrowContainer(mainhand, mc.thePlayer);
                    if(quiver != null){
                        event = new RenderItemBarEvent.QuiverSlots(renderEvent, mainhand, quiver);
                        if(!MinecraftForge.EVENT_BUS.post(event))
                            renderQuiverBar(quiver, frame, event.xOffset+width/2, event.yOffset);
                    }
                }

            }
        }
    }
	
}
