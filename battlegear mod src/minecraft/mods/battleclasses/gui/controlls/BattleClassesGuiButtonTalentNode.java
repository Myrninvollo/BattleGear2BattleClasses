package mods.battleclasses.gui.controlls;

import mods.battleclasses.ability.BattleClassesAbstractTalent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;

public class BattleClassesGuiButtonTalentNode extends BattleClassesGuiButton {
	
	public BattleClassesAbstractTalent talent;

	public BattleClassesGuiButtonTalentNode(int id, int x, int y) {
		super(id, x, y, 20, 20, "talentNode");
		// TODO Auto-generated constructor stub
	}

	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(this.resource);
            
            //InWindow
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
            /*
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.origin_x, this.origin_y + k*this.height, this.width, this.height);
            */
            
            //Draw colored frame by state
            float state = ((float) talent.getCurrentState()) / ((float) talent.getMaximalState());
            //Draw talent icon (alpha by availability)
            
            //Draw hover-over square
            
            //Draw hover-over Tooltip
            if( this.field_146123_n && this.displayTooltip) {
            	this.drawHoveringText(this.getDescriptionList(), p_146112_2_, p_146112_3_, fontrenderer);
            }
        }
    }
}
