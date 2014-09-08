package mods.battleclasses.gui.controlls;

import mods.battleclasses.BattleClassesUtils;
import mods.battleclasses.BattleClassesUtils.LogType;
import mods.battleclasses.core.BattleClassesTalentTree;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BattleClassesGuiButtonTalentTree extends BattleClassesGuiButton {
	
	public BattleClassesGuiButtonTalentTree(int id, int x, int y) {
		super(id, x, y, 54, 20, "talentTree");
		this.resource = new ResourceLocation("battleclasses", "textures/gui/InterfaceTalent.png");
	}

	public BattleClassesTalentTree talentTree;

	public void setTalentTree(BattleClassesTalentTree parTalentTree) {
		talentTree = parTalentTree;
		//TODO : Set Title(displayString), description here, etc...
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean inWindow = super.mousePressed(mc, mouseX, mouseY);
		if (inWindow) {
			BattleClassesUtils.Log("Talent tree button clicked", LogType.GUI);
			//TODO
		}
		return inWindow;
	}

}
