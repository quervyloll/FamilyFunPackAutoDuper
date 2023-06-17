package family_fun_pack.commands;


public class checkitemsCommand extends Command{
    public checkitemsCommand() {
        super("checkitems");
    }
    public String usage() {
        return this.getName();
    }

    public String execute(String[] args) {
        if (args.length != 1) return usage();
        AdCommand.checkitems = !AdCommand.checkitems;
        return "Checker set to "+ AdCommand.checkitems;
    }
}
