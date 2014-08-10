package mods.battleclasses.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import mods.battlegear2.Battlegear;
import mods.battlegear2.packet.BattlegearPacketHandeler;

public class BattleClassesPacketHandeler extends BattlegearPacketHandeler {
	
	public Side side;

	public BattleClassesPacketHandeler() {
		super();
		side = FMLCommonHandler.instance().getEffectiveSide();
		map.put(BattleClassesPacketPlayerClassSnyc.packetName, new BattleClassesPacketPlayerClassSnyc());
	}
	
	/*
    public void sendPacketToPlayer(FMLProxyPacket packet, EntityPlayerMP player){
    	if(side == Side.SERVER) {
    		System.out.println("asd");
			super.sendPacketToPlayer(packet,player);
		}
    }

    public void sendPacketToServer(FMLProxyPacket packet){
    	if(side == Side.CLIENT) {
			super.sendPacketToServer(packet);
		}
    }

    public void sendPacketAround(Entity entity, double range, FMLProxyPacket packet){
    	if(side == Side.SERVER) {
			super.sendPacketAround(entity, range, packet);
		}
    }

    public void sendPacketToAll(FMLProxyPacket packet){
        channels.get(packet.channel()).sendToAll(packet);
    }
    */
}
