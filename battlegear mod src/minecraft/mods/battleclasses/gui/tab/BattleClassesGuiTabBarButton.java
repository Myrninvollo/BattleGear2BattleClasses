package mods.battleclasses.gui.tab;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import mods.battlegear2.client.gui.BattleEquipGUI;
import mods.battlegear2.client.gui.controls.GuiPlaceableButton;

public abstract class BattleClassesGuiTabBarButton extends GuiPlaceableButton {
	
	public IIcon tabButtonIcon;
	
	public boolean horizontal = true;
	
    protected static final ResourceLocation barButtonTexture = new ResourceLocation("battleclasses", "textures/gui/InterfaceOverlay.png");
    
	public BattleClassesGuiTabBarButton(int par1, int par2, int par3,
			String name) {
		super(par1, par2, par3, name);
		this.setContentPositionAndSize();
	}

	public BattleClassesGuiTabBarButton(int par1, int par2, int par3,
			String name, boolean parHorizontal) {
		super(par1, par2, par3, name);
		this.horizontal = parHorizontal;
		this.setContentPositionAndSize();
	}
	
	public static final int BAR_BUTTON_GAP = 1;
	
	public static final int BAR_BUTTON_SIZE_W_H = 32;
	public static final int BAR_BUTTON_SIZE_H_H = 28;
	public static final int BAR_BUTTON_SIZE_W_V = 28;
	public static final int BAR_BUTTON_SIZE_H_V = 31;
	
	protected int origin_X = 0;
	protected int origin_Y = 0;
	
	public void setContentPositionAndSize() {
		if(this.horizontal) {
			origin_X = 0;
			origin_Y = 10;
			
			this.width = BAR_BUTTON_SIZE_W_H;
			this.height = BAR_BUTTON_SIZE_H_H;
		}
		else {
			origin_X = 40;
			origin_Y = 163;
			
			this.width = BAR_BUTTON_SIZE_W_V;
			this.height = BAR_BUTTON_SIZE_H_V;
		}
	}
	
	
    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            p_146112_1_.getTextureManager().bindTexture(barButtonTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            //InWindow
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
            //Rendering Bar Button Texture
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int selectedOffset = (this.isSelected()) ? this.width : 0;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.origin_X + selectedOffset, this.origin_Y, this.width, this.height);
            
            //Rendering Tab Name
            if(k == 2) {
            	
            }
        }
    }
    
    public boolean isSelected() {
    	Minecraft mc = Minecraft.getMinecraft();
    	return (mc.currentScreen.getClass() == this.getGUIClass());
    }

	@Override
	protected abstract Class<? extends GuiScreen> getGUIClass();

	@Override
	protected abstract void openGui(Minecraft mc);

}
