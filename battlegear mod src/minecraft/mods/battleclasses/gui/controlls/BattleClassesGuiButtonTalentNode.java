package mods.battleclasses.gui.controlls;

import mods.battleclasses.ability.BattleClassesAbstractTalent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;

public class BattleClassesGuiButtonTalentNode extends BattleClassesGuiButton {
	
	public BattleClassesAbstractTalent talent;

	public BattleClassesGuiButtonTalentNode(int id, BattleClassesAbstractTalent talentAbility) {
		super(id, 0, 0, 20, 20, "talentNode");
		this.talent = talentAbility;
	}

	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible && talent != null)
        {	
            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            //mc.getTextureManager().bindTexture(this.resource);
            
            //InWindow
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            
            //Draw talent icon (alpha by availability)
            if(talent.isLitOnUI()) {
            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            else {
            	GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            }
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            mc.getTextureManager().bindTexture(talent.getIconResourceLocation());
            this.myDrawTexturedModalRect(this.xPosition, this.yPosition, 16, 16);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            //Draw hover-over square
            if (this.field_146123_n)
            {
            	GL11.glPushMatrix();
                //GL11.glDisable(GL11.GL_LIGHTING);
                //GL11.glDisable(GL11.GL_DEPTH_TEST);
                int j1 = this.xPosition;
                int k1 = this.yPosition;
                GL11.glColorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                //GL11.glEnable(GL11.GL_LIGHTING);
                //GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glPopMatrix();
            }
            
            //Draw hover-over Tooltip
            /*
            if( this.field_146123_n && this.displayTooltip) {
            	this.drawHoveringText(this.getDescriptionList(), p_146112_2_, p_146112_3_, fontrenderer);
            }
            */
        }
    }
}
