package mods.battleclasses.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.boss.BossStatus;
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
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.ability.BattleClassesAbstractAbilityActive;
import mods.battleclasses.core.BattleClassesSpellBook;
import mods.battleclasses.enumhelper.EnumBattleClassesPlayerClass;
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
    
    public BattleClassesInGameGUI() {
    	super();
    	this.initHighLightLabels();
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
    
    public static final int ABILITY_ACTIONBAR_WIDTH = 142;
    public static final int ABILITY_ACTIONBAR_HEIGHT = 22;

    public void renderAbilityActionBar(float frame, int xOffset, int yOffset) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resourceLocationHUD);

        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int actionbarWidth = ABILITY_ACTIONBAR_WIDTH;
        int actionbarHeight = ABILITY_ACTIONBAR_HEIGHT;
        int actionbarPosX = width/2 - actionbarWidth/2;
        int actionbarPosY = 0;
        this.drawTexturedModalRect(actionbarPosX, actionbarPosY, 0, 0, actionbarWidth, actionbarHeight);
        
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        ArrayList<BattleClassesAbstractAbilityActive> spellbookAbilities = new ArrayList<BattleClassesAbstractAbilityActive>(BattleClassesUtils.getPlayerSpellBook(mc.thePlayer).activeAbilities.values()); 
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
    
    protected boolean shouldDrawBossHealthBar() {
    	return (BossStatus.bossName != null && BossStatus.statusBarTime > 0);
    }
    
    public void drawBossHealth()
    {
        if (shouldDrawBossHealthBar())
        {
            --BossStatus.statusBarTime;
            FontRenderer fontrenderer = this.mc.fontRenderer;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            short short1 = 182;
            int j = i / 2 - short1 / 2;
            int k = (int)(BossStatus.healthScale * (float)(short1 + 1));
            
            byte b0 = 12 + BattleClassesInGameGUI.ABILITY_ACTIONBAR_HEIGHT;	//MAIN Y coord
            
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);

            if (k > 0)
            {
                this.drawTexturedModalRect(j, b0, 0, 79, k, 5);
            }

            String s = BossStatus.bossName;
            fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, b0 - 10, 16777215);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }
    
    public void drawCastbar() {
    	
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
    	if(ability != null && ability.getIconResourceLocation() != null) {
    		mc.getTextureManager().bindTexture(ability.getIconResourceLocation());
    		myDrawTexturedModalRect(x, y, 16, 16);
        	//drawTexturedModelRectFromIcon(x, y, ability.getAbilityIcon(), 16, 16);
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
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
    		this.drawTexturedModelRectFromIcon(posX, posY, cooldownIcon, cooldownIcon.getIconWidth(), cooldownIcon.getIconHeight());
    		
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	}
	}
    
	 // 3.  You'll need to write your own version of the Gui.drawTexturedModalRect() method
	//  This method can go into your own Gui class:
	public void myDrawTexturedModalRect(int x, int y, int width, int height)
	{
		 Tessellator tessellator = Tessellator.instance;
		 tessellator.startDrawingQuads();    
		 tessellator.addVertexWithUV(x        , y + height, (double)this.zLevel, 0.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y + height, (double)this.zLevel, 1.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y         , (double)this.zLevel, 1.0, 0.0);
		 tessellator.addVertexWithUV(x        , y         , (double)this.zLevel, 0.0, 0.0);
		 tessellator.draw();
	}
	
	public static GuiHighLightLabel chosenAbilit_HLL = new GuiHighLightLabel();
	public static GuiHighLightLabel targetDisplay_HLL = new GuiHighLightLabel();
	public static GuiHighLightLabel warningDisplay_HLL = new GuiHighLightLabel();
	
	public void initHighLightLabels() {

        targetDisplay_HLL.horizontalAlignmentMode = 0;
		warningDisplay_HLL.horizontalAlignmentMode = 2;
		warningDisplay_HLL.setColorHEX(0xFF0000);
		chosenAbilit_HLL.horizontalAlignmentMode = 1;
		
	}
	
	public void drawHighLightedLabels() {
		ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int centerGap = 5;
		targetDisplay_HLL.posX = scaledresolution.getScaledWidth() / 2 + centerGap;
		targetDisplay_HLL.posY = scaledresolution.getScaledHeight() / 2 - centerGap - mc.fontRenderer.FONT_HEIGHT/2;
		warningDisplay_HLL.posX = scaledresolution.getScaledWidth() / 2 - centerGap;
		warningDisplay_HLL.posY = scaledresolution.getScaledHeight() / 2 + centerGap;
		
		targetDisplay_HLL.draw(mc.fontRenderer);
		warningDisplay_HLL.draw(mc.fontRenderer);
	}
	
	public static void displayTargetingInfo(String message) {
		targetDisplay_HLL.setText(message);
		targetDisplay_HLL.show();
	}
	
	public static void displayWarning(String message) {
		warningDisplay_HLL.setText(message);
		warningDisplay_HLL.show();
	}
	
	public static void displayChosenAbilityName(String message) {
		chosenAbilit_HLL.setText(message);
		chosenAbilit_HLL.show();
	}
}
