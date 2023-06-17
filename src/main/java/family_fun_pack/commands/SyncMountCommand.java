package family_fun_pack.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import io.netty.buffer.ByteBuf;

import family_fun_pack.FamilyFunPack;
import family_fun_pack.entities.EntityVoid;
import family_fun_pack.entities.GhostDonkey;
import family_fun_pack.network.PacketListener;

/* Sync our position with server */

@SideOnly(Side.CLIENT)
public class SyncMountCommand extends Command implements PacketListener {

  public SyncMountCommand() {
    super("sync");
  }

  public String usage() {
    return this.getName();
  }

  public String execute(String[] args) {
    Minecraft mc = Minecraft.getMinecraft();

    double x = mc.player.posX + 1000d;
    double y = mc.player.posY;
    double z = mc.player.posZ + 1000d;

    if(args.length > 3) {
      try {
        x = Integer.parseInt(args[1]);
        y = Integer.parseInt(args[2]);
        z = Integer.parseInt(args[3]);
      } catch(NumberFormatException e) {
        return this.getUsage();
      }
    }

    if(mc.player.isRiding()) {
      Entity ride = new EntityVoid(mc.world, 0);
      ride.setPosition(x, y, z); // Don't use this near world border, or get kicked
      FamilyFunPack.getNetworkHandler().registerListener(EnumPacketDirection.CLIENTBOUND, this, 41);
      FamilyFunPack.getNetworkHandler().sendPacket(new CPacketVehicleMove(ride));
    } else {
      FamilyFunPack.getNetworkHandler().registerListener(EnumPacketDirection.CLIENTBOUND, this, 47);
      FamilyFunPack.getNetworkHandler().sendPacket(new CPacketPlayer.Position(x, y, z, true));
    }
    return null;
  }

  public void onDisconnect() {
    FamilyFunPack.getNetworkHandler().unregisterListener(EnumPacketDirection.CLIENTBOUND, this, 41, 47);
  }

  public Packet<?> packetReceived(EnumPacketDirection direction, int id, Packet<?> packet, ByteBuf in) {
    FamilyFunPack.getNetworkHandler().unregisterListener(EnumPacketDirection.CLIENTBOUND, this, 41, 47);

    Minecraft mc = Minecraft.getMinecraft();
    Vec3d loc;

    if(id == 41) {
      SPacketMoveVehicle move = (SPacketMoveVehicle) packet;
      loc = new Vec3d(move.getX(), move.getY(), move.getZ());
    } else {
      SPacketPlayerPosLook move = (SPacketPlayerPosLook) packet;
      loc = new Vec3d(move.getX(), move.getY(), move.getZ());
    }

    if(! this.showDebugInfo()) {
      loc = loc.subtract(mc.player.posX, mc.player.posY, mc.player.posZ);
    }

    FamilyFunPack.printMessage(String.format("%s sync -> (%.2f, %.2f, %.2f)", (id == 41 ? "Vehicle" : "Player"), loc.x, loc.y, loc.z));

    return packet;
  }
}
