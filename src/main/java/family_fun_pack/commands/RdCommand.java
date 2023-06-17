package family_fun_pack.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.AbstractChestHorse;

public class RdCommand extends Command {

	public RdCommand() {
		
		super("rd");
	}

	public String usage() {
		
		return this.getName();
	}

	public String execute(String[] args) {

		Minecraft mc = Minecraft.getMinecraft();
		if (mc.objectMouseOver.entityHit == null || !(mc.objectMouseOver.entityHit instanceof AbstractChestHorse))
			return "No entity mouse over (or entity mouseover is not horse/donkey/llama).";
		AdCommand.rideEntity = mc.objectMouseOver.entityHit;
		return "Set ride entity to " + mc.objectMouseOver.entityHit.getEntityId();
	}
}
