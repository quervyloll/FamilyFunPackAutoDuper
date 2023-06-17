package family_fun_pack.commands;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MountBedDelayCommand extends Command{

        public MountBedDelayCommand() {
                super("mbdelay");
        }

        public String usage() {
                return this.getName() + " in seconds";
        }

        public String execute(String[] args) {
                try{
                        if(args.length == 2) {
                                int secDelay = Integer.parseInt(args[1]);
                                BtpCommand.mbdelay=secDelay*1000;
                                return ("Set MountBedDelay to " + secDelay + " seconds");
                        }else{
                                return "inproper usage";
                        }
                }catch (NumberFormatException e){
                        return  "inproper usage";
                }
        }
}


