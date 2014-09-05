package mods.battleclasses.gui.tab;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.core.BattleClassesPlayerClass;
import mods.battleclasses.core.BattleClassesTalentMatrix;
import mods.battleclasses.core.BattleClassesTalentTree;
import mods.battleclasses.gui.BattleClassesGUIHandler;
import mods.battlegear2.Battlegear;
import mods.battlegear2.client.BattlegearClientEvents;
import mods.battlegear2.client.ClientProxy;
import mods.battlegear2.gui.BattlegearGUIHandeler;
import mods.battlegear2.gui.ContainerBattle;
import mods.battlegear2.packet.BattlegearGUIPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class BattleClassesTabTalents extends BattleClassesAbstractTab {
	
	public static final ResourceLocation resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceTalent.png");
	public ResourceLocation[] resourceBackground = new ResourceLocation[3];


    public BattleClassesTabTalents(EntityPlayer entityPlayer, boolean isRemote) {
        super(entityPlayer, isRemote, new BattleClassesContainerEmpty(entityPlayer.inventory, !isRemote, entityPlayer));
        this.allowUserInput = true;
    }
    
    
    @Override
    public void initGui ()
    {
        super.initGui();
        
        int i = 0;
        BattleClassesTalentMatrix talentMatrix = BattleClassesUtils.getPlayerTalentMatrix(mc.thePlayer);
        for(BattleClassesTalentTree talentTree : talentMatrix.talentTrees) {
        	resourceBackground[i] = new ResourceLocation("battleclasses", talentTree.getResoureLocationString());
        	++i;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3){
        super.drawScreen(par1, par2, par3);
        this.xSize_lo = (float) par1;
        this.ySize_lo = (float) par2;
    }
    
    public static final int TALENT_TREE_VIEW_W = 54;
    public static final int TALENT_TREE_VIEW_H = 108;
    
    public void drawTalentTreeView(BattleClassesTalentTree talentTree, int treeIndex, int x, int y) {
    	//DRAW BACKGROUND IMAGE
    	//ResourceLocation resourceBackground = new ResourceLocation("battleclasses", talentTree.getResoureLocationString());
    	//this.mc.renderEngine.bindTexture(resourceBackground);
    	this.mc.renderEngine.bindTexture(resourceBackground[treeIndex]);
    	//Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
    	//this.drawTexturedModalRect(x, y, 0, 0, TALENT_TREE_VIEW_W, TALENT_TREE_VIEW_H);
    	myDrawTexturedModalRect( x,  y,  TALENT_TREE_VIEW_W,  TALENT_TREE_VIEW_H);
    	/*
    	IIcon testicon = BattleClassesPlayerClass.getClassIcon(BattleClassesUtils.getPlayerClass(mc.thePlayer));
        mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
    	this.drawTexturedModelRectFromIcon(x, y, testicon, 16, 16);
    	*/
    	
    	//DRAW FRAME
    	this.mc.renderEngine.bindTexture(resource);
    	this.drawTexturedModalRect(x, y, 176, 0, TALENT_TREE_VIEW_W, TALENT_TREE_VIEW_H);
    	//DRAW ARROWS
    	
    	
    	
    	/* GUICONTAINER.CLASS
        if (this.isMouseOverSlot(slot, par1, par2) && slot.func_111238_b())
        {
            this.theSlot = slot;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int j1 = slot.xDisplayPosition;
            k1 = slot.yDisplayPosition;
            GL11.glColorMask(true, true, true, false);
            this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
            GL11.glColorMask(true, true, true, true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        */
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(resource);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        BattleClassesTalentMatrix talentMatrix = BattleClassesUtils.getPlayerTalentMatrix(mc.thePlayer);
        int n = talentMatrix.getTalentPoints();
        int inset = 8;
        this.drawString(Minecraft.getMinecraft().fontRenderer, "Talent Points: " +n, this.guiLeft + inset,
        		this.guiTop + DEFAULT_GUI_HEIGHT - inset - 5 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, 0xFFFFFF);
        
        
        int posX = this.guiLeft + 7;
        int posY = this.guiTop + 27;
        int i = 0;
        for(BattleClassesTalentTree talentTree : talentMatrix.talentTrees) {
        	this.drawTalentTreeView(talentTree, i, posX + i*TALENT_TREE_VIEW_W, posY);
        	++i;
        }
        
    }
    
    public static void open(EntityPlayer player){
    	//send packet to open container on server
        Battlegear.packetHandler.sendPacketToServer(new BattlegearGUIPacket(BattleClassesGUIHandler.talentsID).generatePacket());
    }
    
	 // 3.  You'll need to write your own version of the Gui.drawTexturedModalRect() method
	//  This method can go into your own Gui class:
	public void myDrawTexturedModalRect(int x, int y, int width, int height)
	{
		 Tessellator tessellator = Tessellator.instance;
		 tessellator.startDrawingQuads();    
		 tessellator.addVertexWithUV(x        , y + height, 0, 0.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y + height, 0, 1.0, 1.0);
		 tessellator.addVertexWithUV(x + width, y         , 0, 1.0, 0.0);
		 tessellator.addVertexWithUV(x        , y         , 0, 0.0, 0.0);
		 tessellator.draw();
	}
    
}
