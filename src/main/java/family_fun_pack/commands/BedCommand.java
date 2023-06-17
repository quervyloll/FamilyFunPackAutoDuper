package family_fun_pack.commands;


import net.minecraft.client.Minecraft;

public class BedCommand extends Command {

    public BedCommand() {

        super("bed");
    }

    @Override
    public String usage() {

        return "bed <num>";
    }

    @Override
    public String execute(String[] args) {

        Minecraft mc = Minecraft.getMinecraft();
        BtpCommand.bed = mc.objectMouseOver.getBlockPos();
        ConixCommand.bed = mc.objectMouseOver.getBlockPos();
        return "Set bed.";
    }
}
