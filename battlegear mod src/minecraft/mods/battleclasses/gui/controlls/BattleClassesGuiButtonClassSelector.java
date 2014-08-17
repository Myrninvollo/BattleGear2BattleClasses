package mods.battleclasses.gui.controlls;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.EnumBattleClassesPlayerClass;
import mods.battleclasses.gui.tab.BattleClassesTabClassSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButtonClassSelector extends BattleClassesGuiButton {
	
	public EnumBattleClassesPlayerClass playerClass;
	public IIcon classIcon;

	public BattleClassesGuiButtonClassSelector(int id, ResourceLocation resource, EnumBattleClassesPlayerClass parPlayerClass) {
		super(id, resource);
		this.playerClass = parPlayerClass;
	}
	
	public BattleClassesGuiButtonClassSelector(int id, ResourceLocation resource, EnumBattleClassesPlayerClass parPlayerClass, String classIconName) {
		super(id, resource);
		this.playerClass = parPlayerClass;
		this.iconName = classIconName;
	}
	
	public boolean shouldBeDisabled() {
		return BattleClassesUtils.getPlayerClass(Minecraft.getMinecraft().thePlayer) == this.playerClass;
	}
	
	public String getIconRegisterPath() {
		return ( "battleclasses:sharedicons/classes/"+this.iconName);
	}
	
	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {	
            super.drawButton(mc, p_146112_2_, p_146112_3_);
            FontRenderer fontrenderer = mc.fontRenderer;
            //Rendering Tab Icon
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            this.drawTexturedModelRectFromIcon(this.xPosition + this.width/2 - classIcon.getIconWidth()/2, 
            								this.yPosition + this.height/2 -  classIcon.getIconHeight()/2,
            								classIcon, classIcon.getIconWidth(), classIcon.getIconHeight());
            
            //Rendering Class Description
            if( field_146123_n ) {
            	ArrayList stringList = new ArrayList();
            	stringList.add(BattleClassesTabClassSelector.getClassDescription(playerClass));
            	this.drawHoveringText(stringList, p_146112_2_, p_146112_3_, fontrenderer);
            }
        }
    }
    
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		if (inWindow) {
			BattleClassesUtils.getPlayerHooks(Minecraft.getMinecraft().thePlayer).playerClass.switchToPlayerClass(this.playerClass);
		}
		return inWindow;
	}

}
