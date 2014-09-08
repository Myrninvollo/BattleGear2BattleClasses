package mods.battleclasses.gui.controlls;

import org.lwjgl.opengl.GL11;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.packet.BattleClassesPacketPlayerClassSnyc;
import mods.battlegear2.Battlegear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class BattleClassesGuiButtonTalentReset extends BattleClassesGuiButton {

	public BattleClassesGuiButtonTalentReset(int id, int x, int y) {
		super(id, x, y, 54, 20, "Reset");
		this.displayString = "Reset";
		this.resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceTalent.png");
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		if (inWindow) {
			BattleClassesUtils.Log("Reset button clicked", LogType.GUI);
			//TODO
		}
		return inWindow;
	}
}
