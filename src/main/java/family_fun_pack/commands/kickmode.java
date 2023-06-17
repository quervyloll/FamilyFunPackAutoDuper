package family_fun_pack.commands;


public class kickmode extends Command{
    public kickmode() {
        super("adkick");
    }
    public String usage() {
        return this.getName();
    }

    public String execute(String[] args) {
        if (args.length != 1) return usage();
        AdCommand.kick = !AdCommand.kick;
        return "Kick mode set to "+ AdCommand.kick;
    }
}

