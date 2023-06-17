package family_fun_pack.commands;

import family_fun_pack.FamilyFunPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.inventory.ClickType;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class BtpCommand extends Command {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static int count;
    public static boolean inv;
    public static BlockPos bed;
    public static BlockPos chest1;
    public static BlockPos chest2;

    public static boolean enabled;
    public static int filldelay;
    public static int delay;

    public static int betweenchests = 1;

    public static int mbdelay;

    public BtpCommand() {
        super("bedtp");
    }

    @Override
    public String usage() {

        return "bedtp";
    }

    @Override
    public String execute(String[] args) {

        if (enabled) {
            enabled = false;
            return "Bedtp disabled";
        }
        if (chest1 == null && chest2 == null)
            return "Chests unset! Failed to enable BedTp.";
        if (bed == null)
            return "Bed unset! Failed to enable BedTp";
        enabled = true;
        new Thread /*Dies from cringe*/(() -> {
            sleep(1000);
            while (enabled && betweenchests == 1 || enabled && betweenchests == 2)
                if (betweenchests == 1) {
                    bedtp1();
                }
                else
                    bedtp2();

        }).start();
        return "BetTp enabled";
    }




    private void sleep(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setLookAt(BlockPos pos) {

        setLookAt(pos.getX(), pos.getY(), pos.getZ());
    }

    private void setLookAt(double x, double y, double z) {

        Vec3d eyes = mc.player.getPositionEyes(mc.getRenderPartialTicks());
        double dirx = eyes.x - x;
        double diry = eyes.y - y;
        double dirz = eyes.z - z;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI + 90;
        mc.player.rotationYaw = (float) yaw;
        mc.player.rotationPitch = (float) pitch;
    }

    public static boolean isInvFull() {
        for (int i = 0; i < 36; i++) {
            inv = !mc.player.inventory.getStackInSlot(i).isEmpty();
        }

        return inv;

    }


    private void fillChest() {
        for (int i = 54; i <= 89; i++) {
            if (mc.player.openContainer.inventorySlots.get(i).getHasStack()) {
                sleep(filldelay);
                mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                count++;
            }
            if (!enabled) {
                break;
            }
        }
    }

    private void closeChest() {
        mc.player.closeScreen();
        FamilyFunPack.printMessage("closed chest");
    }

    public static boolean isNight() {

        long timeDay = mc.world.getWorldTime() % 24000;

        return (timeDay > 12700) && (timeDay < 22500);

    }


    private void bedtp1() {
        if (isNight()) {
            setLookAt(bed);
            mc.playerController.processRightClickBlock(mc.player, mc.world, bed, EnumFacing.getDirectionFromEntityLiving(bed, mc.player),
                    new Vec3d(bed.getX(), bed.getY(), bed.getZ()), EnumHand.MAIN_HAND);
            FamilyFunPack.printMessage("Got in Bed!");
            sleep(300);
            boolean playerSleeping = mc.player.isPlayerSleeping();
            if (playerSleeping) {
                mc.player.sendChatMessage("/kill");
            }
            sleep(1000);
            mc.player.respawnPlayer();
            sleep(3000);
            while (!isInvFull()) {
                sleep(500);
            }
            if (isInvFull()) {
                FamilyFunPack.getNetworkHandler().disconnect();
            }
            sleep(delay);
            setLookAt(chest1);
            FamilyFunPack.printMessage("Looked at chest");
            sleep(800);
            mc.playerController.processRightClickBlock(mc.player, mc.world, chest1, EnumFacing.getDirectionFromEntityLiving(chest1, mc.player),
                    new Vec3d(chest1.getX(), chest1.getY(), chest1.getZ()), EnumHand.MAIN_HAND);
            FamilyFunPack.printMessage("Opened chest");
            sleep(600);
            fillChest();
            FamilyFunPack.printMessage("Filled chest");
            sleep(500);
            closeChest();
            FamilyFunPack.printMessage("Closed Chest");
            sleep(200);
            FamilyFunPack.printMessage("Successfully Bed TP'ed");
            sleep(mbdelay);
            betweenchests = 2;
        } else {
            sleep(1000);
            FamilyFunPack.printMessage("Not Night Time!");
        }
    }

    private void bedtp2() {
        if (isNight()) {
            setLookAt(bed);
            mc.playerController.processRightClickBlock(mc.player, mc.world, bed, EnumFacing.getDirectionFromEntityLiving(bed, mc.player),
                    new Vec3d(bed.getX(), bed.getY(), bed.getZ()), EnumHand.MAIN_HAND);
            FamilyFunPack.printMessage("Got in Bed!");
            sleep(300);
            boolean playerSleeping = mc.player.isPlayerSleeping();
            if (playerSleeping) {
                mc.player.sendChatMessage("/kill");
            }
            sleep(1000);
            mc.player.respawnPlayer();
            sleep(3000);
            while (!isInvFull()) {
                sleep(500);
            }
            if (isInvFull()) {
                FamilyFunPack.getNetworkHandler().disconnect();
            }
            sleep(delay);
            setLookAt(chest2);
            FamilyFunPack.printMessage("Looked at chest");
            sleep(700);
            mc.playerController.processRightClickBlock(mc.player, mc.world, chest2, EnumFacing.getDirectionFromEntityLiving(chest2, mc.player),
                    new Vec3d(chest2.getX(), chest2.getY(), chest2.getZ()), EnumHand.MAIN_HAND);
            FamilyFunPack.printMessage("Opened chest");
            sleep(600);
            fillChest();
            FamilyFunPack.printMessage("Filled chest");
            sleep(500);
            closeChest();
            FamilyFunPack.printMessage("Closed Chest");
            sleep(200);
            FamilyFunPack.printMessage("Successfully Bed TP'ed");
            sleep(mbdelay);
            betweenchests = 1;

        }
        else {
            sleep(1000);
            FamilyFunPack.printMessage("Not Night Time!");
        }
    }
}







