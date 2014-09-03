package mods.battleclasses.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.gui.BattleClassesGuiHelper;
import mods.battlegear2.Battlegear;
import mods.battlegear2.api.RenderItemBarEvent;
import mods.battlegear2.api.core.IBattlePlayer;
import mods.battlegear2.api.core.InventoryPlayerBattle;
import mods.battlegear2.api.quiver.IArrowContainer2;
import mods.battlegear2.api.quiver.QuiverArrowRegistry;
import mods.battlegear2.api.shield.IShield;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.BattlegearClientTickHandeler;
import mods.battlegear2.client.ClientProxy;
import mods.battlegear2.client.gui.BattlegearInGameGUI;

public class BattleClassesInGameGUI extends BattlegearInGameGUI {
	
    public static Class<?> previousGui;
    //public static Minecraft mc;
    public static final ResourceLocation resourceLocationHUD = new ResourceLocation("battleclasses", "textures/gui/InGameGUI.png");
	
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
                        if(currentGui!=previousGui /*&& !(currentGui.equals(GuiContainerCreative.class)*/ && currentGui.equals(GuiInventory.class) ){
                        	
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
                renderAbilityActionBar(frame, event.xOffset+width/2, event.yOffset);

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
                
                /*
                if(mainhand != null){
                    ItemStack quiver = QuiverArrowRegistry.getArrowContainer(mainhand, mc.thePlayer);
                    if(quiver != null){
                        event = new RenderItemBarEvent.QuiverSlots(renderEvent, mainhand, quiver);
                        if(!MinecraftForge.EVENT_BUS.post(event))
                            renderQuiverBar(quiver, frame, event.xOffset+width/2, event.yOffset);
                    }
                }
				*/
            }
        }
    }
    
    public void renderBattleSlots(int x, int y, float frame, boolean isMainHand) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resourceLocationHUD);
        int offsetX = SLOT_H;
        drawTexturedModalRect(x + offsetX, y, 180, 0, SLOT_H, SLOT_H);
        //drawTexturedModalRect(x + 31, y, 151, 0, 31, SLOT_H);
        
        /*
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int battleSlotPosY = height - SLOT_H;
        if(isMainHand) {
        	drawTexturedModalRect((int) (width*0.25), battleSlotPosY, 180, 0, SLOT_H, SLOT_H);
        }
        else {
        	drawTexturedModalRect((int) (width*0.75), battleSlotPosY, 180, 0, SLOT_H, SLOT_H);
        }
        */
        this.mc.renderEngine.bindTexture(resourceLocation);
        if (mc.thePlayer!=null && ((IBattlePlayer) mc.thePlayer).isBattlemode()) {
            this.drawTexturedModalRect(x + offsetX-1 + (mc.thePlayer.inventory.currentItem - InventoryPlayerBattle.OFFSET) * 20,
                    y - 1, 0, 22, 24, SLOT_H);
        }
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        if(mc.thePlayer!=null){
            for (int i = 0; i < 1 /*InventoryPlayerBattle.WEAPON_SETS*/; ++i) {
                int varx = x + i * 20 + 3 + offsetX;
                this.renderInventorySlot(i + InventoryPlayerBattle.OFFSET+(isMainHand?0:InventoryPlayerBattle.WEAPON_SETS), varx, y+3, frame);
            }
        }
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderAbilityActionBar(float frame, int xOffset, int yOffset) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resourceLocationHUD);

        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int actionbarWidth = 142;
        int actionbarHeight = 22;
        int actionbarPosX = width/2 - actionbarWidth/2;
        int actionbarPosY = 0;
        this.drawTexturedModalRect(actionbarPosX, actionbarPosY, 0, 0, actionbarWidth, actionbarHeight);
        
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        ArrayList<BattleClassesAbstractAbilityActive> spellbookAbilities = new ArrayList<BattleClassesAbstractAbilityActive>(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).abilities.values()); 
        for(int i = 0; i < spellbookAbilities.size(); ++i) {
        	this.drawAbility(actionbarPosX+3 + i*20, actionbarPosY+3, spellbookAbilities.get(i));
        }
        
        this.mc.renderEngine.bindTexture(resourceLocationHUD);
        boolean hasClass = BattleClassesUtils.getPlayerClass(mc.thePlayer) != EnumBattleClassesPlayerClass.PlayerClass_NONE;
        if (hasClass && ((IBattlePlayer) mc.thePlayer).isBattlemode()) {
        	int chosenIndex = BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).getChosenAbilityIndex();
            this.drawTexturedModalRect(actionbarPosX-1 + chosenIndex*20, actionbarPosY-1, 232, 0, 24, 24);
        }

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderBlockBar(int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resourceLocationShield);

        if(mc.thePlayer!=null){
            if(mc.thePlayer.capabilities.isCreativeMode){
                if(mc.thePlayer.isRidingHorse()){
                    y-=5;
                }
            }else{
                y-= 16;
                if(ForgeHooks.getTotalArmorValue(mc.thePlayer) > 0 || mc.thePlayer.isRidingHorse() || mc.thePlayer.getAir() < 300){
                    y-=10;
                }
            }
        }

        this.drawTexturedModalRect(x, y, 0, 0, 182, 9);

        float[] colour = BattlegearClientTickHandeler.COLOUR_DEFAULT;
        if(BattlegearClientTickHandeler.blockBar < 0.33F){
            colour = BattlegearClientTickHandeler.COLOUR_RED;
        }
        if(BattlegearClientTickHandeler.getFlashTimer() > 0 && (System.currentTimeMillis() / 250) % 2 == 0){
            colour = BattlegearClientTickHandeler.COLOUR_YELLOW;
        }
        GL11.glColor3f(colour[0], colour[1], colour[2]);
        this.drawTexturedModalRect(x, y, 0, 9, (int) (182 * BattlegearClientTickHandeler.blockBar), 9);

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderInventorySlot(int par1, int par2, int par3, float par4) {
        ItemStack itemstack = this.mc.thePlayer.inventory.getStackInSlot(par1);
        renderStackAt(par2, par3, itemstack, par4);
    }

    private void renderStackAt(int x, int y, ItemStack itemstack, float frame){
        if (itemstack != null) {
            float f1 = (float) itemstack.animationsToGo - frame;

            if (f1 > 0.0F) {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float) (x + 8), (float) (y + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, x, y);
            if (f1 > 0.0F) {
                GL11.glPopMatrix();
            }
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemstack, x, y);
        }
    }
    
    public void drawAbility(int x, int y, BattleClassesAbstractAbilityActive ability ) {
    	if(ability != null && ability.getAbilityIcon() != null) {
    		//BattleClassesUtils.Log("Drawing Ability with ID: " + ability.getAbilityID() + ", X: " + x + ", Y: " + y, LogType.GUI);
    		mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        	drawTexturedModelRectFromIcon(x, y, ability.getAbilityIcon(), 16, 16);
        	drawCooldown(x, y, BattleClassesUtils.getCooldownPercentage(ability));
        	
    	}
    }
	
    public void drawCooldown(int posX, int posY, float f) {
    	if(f > 0) {
    		int frameIndex = BattleClassesGuiHelper.cooldownIcons.length-1;
    		if(f < 1) {
    			frameIndex = (int) (((float) BattleClassesGuiHelper.cooldownIcons.length)*f);
    		}
    		IIcon cooldownIcon = BattleClassesGuiHelper.cooldownIcons[frameIndex];
    	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);

    		this.drawTexturedModelRectFromIcon(posX, posY, cooldownIcon, cooldownIcon.getIconWidth(), cooldownIcon.getIconHeight());
    		
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	}
	}
}
