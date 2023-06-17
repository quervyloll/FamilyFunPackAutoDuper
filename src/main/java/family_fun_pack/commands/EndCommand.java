package family_fun_pack.commands;

import net.minecraft.client.Minecraft;

public class EndCommand extends Command {

	public EndCommand() {
		
		super("end");
	}

	@Override
	public String usage() {
		
		return "end <num>";
	}

	@Override
	public String execute(String[] args) {

		if (args.length != 2)
			return "Too few/many args. Usage: " + usage();
		if (!args[1].equals("1") && !args[1].equals("2"))
			return "This arg should be a number (only 1 or 2).";
		Minecraft mc = Minecraft.getMinecraft();
		if (args[1].equals("1"))
			AdCommand.end1 = mc.player.getPosition();
		else
			AdCommand.end2 = mc.player.getPosition();
		return "Set end " + args[1] + " to current position.";
	}
}
