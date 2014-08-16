package mods.battleclasses.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import mods.battleclasses.gui.tab.BattleClassesTabEquipment;
import mods.battlegear2.Battlegear;
import mods.battlegear2.client.gui.BattleEquipGUI;
import mods.battlegear2.client.gui.BattlegearSigilGUI;
import mods.battlegear2.client.gui.GuiFlagDesigner;
import mods.battlegear2.gui.BattlegearGUIHandeler;
import mods.battlegear2.gui.ContainerBattle;
import mods.battlegear2.gui.ContainerHeraldry;

public class BattleClassesGUIHandler extends BattlegearGUIHandeler {

	public static final int vanillaInventoryID = 0;
    public static final int equipID = 1;
    public static final int talentsID = 2;
    public static final int classSelectorID = 3;
    public static final int helpID = 4;
    public static final int configID = 5;
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        switch (ID) {
            case equipID:
                return new ContainerBattle(player.inventory, !world.isRemote, player);
            default:
                return null;
        }

    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        switch (ID) {
            case equipID:
                return new BattleClassesTabEquipment(player, world.isRemote);
            default:
                return null;
        }
    }

}
