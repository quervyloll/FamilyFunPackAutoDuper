package family_fun_pack.commands;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class kickdelayCommand extends Command{

    public kickdelayCommand() {
        super("kickdelay");
    }

    public String usage() {
        return this.getName() + " in seconds";
    }

    public String execute(String[] args) {
        try{
            if(args.length == 2) {
                int secDelay = Integer.parseInt(args[1]);
                AdCommand.kickdelay=secDelay*1000;
                return ("Set kickdelay to " + secDelay + " seconds");
            }else{
                return "inproper usage";
            }
        }catch (NumberFormatException e){
            return  "inproper usage";
        }
    }
}


