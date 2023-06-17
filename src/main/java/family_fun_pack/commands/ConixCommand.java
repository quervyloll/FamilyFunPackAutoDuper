package family_fun_pack.commands;

import family_fun_pack.FamilyFunPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.inventory.ClickType;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ConixCommand extends Command {


    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean enabled;
    public static BlockPos bed;






    public ConixCommand() {
        super("Conix");
    }
    @Override
    public String usage() {

        return "Conix";
    }

    @Override
    public String execute(String[] args) {
        getonbed();
        return "conix dupe enabled";
    }

    private void sleep(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getonbed() {
        sleep(3000);
        mc.playerController.processRightClickBlock(mc.player, mc.world, bed, EnumFacing.getDirectionFromEntityLiving(bed, mc.player),
                new Vec3d(bed.getX(), bed.getY(), bed.getZ()), EnumHand.MAIN_HAND);
        sleep(2000);
        mc.player.sendChatMessage("/kill");

    }
}
