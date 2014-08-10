package mods.battleclasses;

import net.minecraft.client.Minecraft;

public class BattleClassesUtils {
	
	public static float getCurrentTimeInSeconds() {
		return ((float) Minecraft.getSystemTime()) / 1000;
	}
	
}
