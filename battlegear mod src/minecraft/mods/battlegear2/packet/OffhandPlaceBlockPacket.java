package mods.battlegear2.packet;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class OffhandPlaceBlockPacket extends AbstractMBPacket{
    public static final String packetName = "MB2|Place";
    private int xPosition;
    private int yPosition;
    private int zPosition;

    /** The offset to use for block/item placement. */
    private int direction;
    private ItemStack itemStack;

    /** The offset from xPosition where the actual click took place */
    private float xOffset;

    /** The offset from yPosition where the actual click took place */
    private float yOffset;

    /** The offset from zPosition where the actual click took place */
    private float zOffset;

    public OffhandPlaceBlockPacket() {}

    public OffhandPlaceBlockPacket(int par1, int par2, int par3, int par4, ItemStack par5ItemStack, float par6, float par7, float par8)
    {
        this.xPosition = par1;
        this.yPosition = par2;
        this.zPosition = par3;
        this.direction = par4;
        this.itemStack = par5ItemStack != null ? par5ItemStack.copy() : null;
        this.xOffset = par6;
        this.yOffset = par7;
        this.zOffset = par8;
    }

    @Override
    public String getChannel() {
        return packetName;
    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(this.xPosition);
        out.writeByte(this.yPosition);
        out.writeInt(this.zPosition);
        out.writeByte(this.direction);
        ByteBufUtils.writeItemStack(out, this.itemStack);
        out.writeByte((int)(this.xOffset * 16.0F));
        out.writeByte((int)(this.yOffset * 16.0F));
        out.writeByte((int)(this.zOffset * 16.0F));
    }

    @Override
    public void process(ByteBuf in, EntityPlayer player) {
        try{
            this.xPosition = in.readInt();
            this.yPosition = in.readByte();
            this.zPosition = in.readInt();
            this.direction = in.readByte();
            this.itemStack = ByteBufUtils.readItemStack(in);
            this.xOffset = (float)in.readByte() / 16.0F;
            this.yOffset = (float)in.readByte() / 16.0F;
            this.zOffset = (float)in.readByte() / 16.0F;
        }catch(Exception io){
            return;
        }
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer worldserver = mcServer.worldServerForDimension(player.dimension);
        boolean flag = true;
        int i = xPosition;
        int j = yPosition;
        int k = zPosition;
        int l = direction;
        ((EntityPlayerMP)player).func_143004_u();

        if (direction == 255){
            if (itemStack == null){
                return;
            }

            PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1);
            if (event.useItem != Event.Result.DENY){
                ((EntityPlayerMP)player).theItemInWorldManager.tryUseItem(player, worldserver, itemStack);
            }
            flag = false;
        }
        else if (yPosition >= mcServer.getBuildLimit() - 1 && (direction == 1 || yPosition >= mcServer.getBuildLimit())){
            ChatComponentTranslation chat = new ChatComponentTranslation("build.tooHigh", mcServer.getBuildLimit());
            chat.getChatStyle().setColor(EnumChatFormatting.RED);
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S02PacketChat(chat));
        }
        else{
            double dist = ((EntityPlayerMP)player).theItemInWorldManager.getBlockReachDistance() + 1;
            dist *= dist;
            if (player.getDistanceSq((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D) < dist && !mcServer.isBlockProtected(worldserver, i, j, k, player))
            {
                ((EntityPlayerMP)player).theItemInWorldManager.activateBlockOrUseItem(player, worldserver, itemStack, i, j, k, l, xOffset, yOffset, zOffset);
            }
        }
        if (flag){
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(i, j, k, worldserver));
            if (l == 0){
                --j;
            }
            if (l == 1){
                ++j;
            }
            if (l == 2){
                --k;
            }
            if (l == 3){
                ++k;
            }
            if (l == 4){
                --i;
            }
            if (l == 5){
                ++i;
            }
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(i, j, k, worldserver));
        }

        if (itemStack != null && itemStack.stackSize <= 0){
            ForgeEventFactory.onPlayerDestroyItem(player, itemStack);
            player.inventory.setInventorySlotContents(player.inventory.currentItem+ 3, null);
            itemStack = null;
        }
    }
}
