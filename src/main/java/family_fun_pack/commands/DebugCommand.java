package family_fun_pack.commands;


public class DebugCommand extends Command{
    public DebugCommand() {
        super("debug");
    }
    public String usage() {
        return this.getName();
    }

    public String execute(String[] args) {
        if (args.length != 1) return usage();
        AdCommand.debug = !AdCommand.debug;
        return "debug set to "+ AdCommand.debug;
    }
}
