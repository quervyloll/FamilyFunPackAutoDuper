package family_fun_pack.commands;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DelayCommand extends Command{

    public DelayCommand() {
        super("delay");
    }

    public String usage() {
        return this.getName() + " in seconds";
    }

    public String execute(String[] args) {
        try{
            if(args.length == 2) {
                int secDelay = Integer.parseInt(args[1]);
                AdCommand.delay=secDelay*1000;
                BtpCommand.delay=secDelay*1000;
                return ("Set delay to " + secDelay + " seconds");
            }else{
                return "inproper usage";
            }
        }catch (NumberFormatException e){
            return  "inproper usage";
        }
    }
}


