package family_fun_pack.commands;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class filldelaycommand extends Command{

    public filldelaycommand() {
        super("filldelay");
    }

    public String usage() {
        return this.getName() + " in milliseconds";
    }

    public String execute(String[] args) {
        try{
            if(args.length == 2) {
                int millisecDelay = Integer.parseInt(args[1]);
                AdCommand.filldelay=millisecDelay;
                BtpCommand.filldelay=millisecDelay;
                return ("Set filldelay to " + millisecDelay + " milliseconds");
            }else{
                return "inproper usage";
            }
        }catch (NumberFormatException e){
            return  "inproper usage";
        }
    }
}
