package family_fun_pack.commands;

import java.util.List;
import java.util.Objects;
import java.util.Timer;

import family_fun_pack.event.Listener;
import family_fun_pack.event.events.EventReceivePacket;
import family_fun_pack.modules.CommandsModule;
import family_fun_pack.modules.PacketInterceptionModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Syntax;

import static family_fun_pack.FamilyFunPack.*;

public class AdCommand extends Command {


	public static final Minecraft mc = Minecraft.getMinecraft();

	public static int count;

	public static Entity rideEntity;
	public static BlockPos end1;
	public static BlockPos end2;
	public static BlockPos chest1;
	public static BlockPos chest2;
	public static int delay = 3000;


	public static boolean enabled;
	public static int filldelay = 75;

	public static boolean debug = false;

	public static boolean kick = false;
	public static int kickdelay = 0;
	public Entity ddonker = null;
	public static boolean shouldRestart;
	public static boolean checkitems = true;
	Timer monkey = new Timer();

	public AdCommand() {
		super("ad");
	}

	@Override
	public String usage() {
		return "ad";
	}
	@Override
	public String execute(String[] args) {
		if (enabled) {
			enabled = false;
			CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
			PacketInterceptionModule intercept = (PacketInterceptionModule) getModules().getByClass(PacketInterceptionModule.class);
			intercept.toggle(false);
			if (RollbackCommand.teleport_id != -1){
				cmd.handleCommand("rollback");
			}


			return "AutoDupe disabled";
		}
		if (end1 == null || end2 == null || chest1 == null || chest2 == null)
			return "Chests or Donkeys unset! Failed to enable AutoDupe.";
		enabled = true;
		new Thread (() -> {
			while (enabled) {
				try {
					dupe();
				}catch (RuntimeException e){
					System.out.println("** RuntimeException from main");
				}
			}
		}).start();
		return "AutoDupe enabled";
	}

	private void dupe() {
		CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
		if (RollbackCommand.teleport_id != -1){
			cmd.handleCommand("rollback");
			sleep(600);
		}
		sleep(700);
		PacketInterceptionModule intercept = (PacketInterceptionModule) getModules().getByClass(PacketInterceptionModule.class);
		if (mc.player.getDistanceSq(Walker()) > 4 && checkitems){
			doWalkTo(new BlockPos(Walker().posX, Walker().posY, Walker().posZ));
		}
		if (Walker() != null && mc.player.getRidingEntity() == null && mc.player.getDistanceSq(Walker()) < 36 && enabled) {
			doRdupe1();
			printDebug("doing rdupe1");
			for(int i=0; i<15 && mc.player.getRidingEntity() == null; i++){
				mc.getConnection().sendPacket(new CPacketUseEntity(Walker(), EnumHand.MAIN_HAND));
				sleep(50);
			}
			printDebug("mounting donkey");
		}
		while(mc.player.getRidingEntity() == null && enabled){
			if (Walker()==null){
				printDebug("coudnt mount treewalker, trying to find the motherfucker");
			}
			while(Walker()==null && enabled){
				intercept.toggle(false);
				if(RollbackCommand.teleport_id!=-1){
					cmd.handleCommand("rollback");
				}
				setLookAt(mc.player.getDistanceSq(end1) > mc.player.getDistanceSq(end2) ? end1 : end2);
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
			}
			while(mc.player.getDistanceSq(Walker()) > 36 && enabled) {
				setLookAt(Walker());
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
			}
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
			sleep(30);
		}
		if(mc.player.isRiding() && mc.world != null && enabled) {
			sleep(500);
			BlockPos currTarget = mc.player.getDistanceSq(end1) > mc.player.getDistanceSq(end2) ? end1 : end2;
			doWalkTo(currTarget, true);
			printDebug("Walking to the other end");
			sleep(200);
			Entity donk = findDonkey();
			double donkx = donk.posX, donky = donk.posY, donkz = donk.posZ;
			setLookAt(donk);
			doRdupe2();
			printDebug("Doing Rdupe2");
			sleep(delay);
			if (!mc.world.getChunk(new BlockPos(donkx, donky, donkz)).isEmpty()) {
				mc.player.closeScreen();
				sleep(50);
				intercept.toggle(false);
				sleep(100);
				dismount();
				sleep(700);
				printDebug("Chunk wasn't unloaded, resetting.");
			}
			else if (mc.world != null && mc.getCurrentServerData() != null) {
				if (mc.currentScreen instanceof GuiScreenHorseInventory) {
					lootDonkey();
					printDebug("Looting donkey");
					sleep(800);
				}
				intercept.toggle(false);
				sleep(1000);
				dismount();
				sleep(500);
				if (enabled && checkitems){
					printDebug("Checking if Donkey's items disappeared");
					sleep(300);
					setLookAt(donk);
					sleep(200);
					cmd.handleCommand(String.format("use sneak %d", findDonkey().getEntityId()));
					sleep(800);
					fillDonkeyInventory();
					sleep(100);
					mc.player.closeScreen();
				}
				BlockPos currChest = mc.player.getDistanceSq(chest1) > mc.player.getDistanceSq(chest2) ? chest2 : chest1;
				while (mc.player.openContainer.inventorySlots.size() < 54 && enabled && mc.world != null && mc.getCurrentServerData() != null) {
					sleep(100);
					while (mc.player.getDistanceSq(currChest) > 16) {
						sleep(10);
						setLookAt(currChest);
						KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
					}
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
					printDebug("Opening chest");
					Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketPlayerTryUseItemOnBlock(currChest, EnumFacing.getDirectionFromEntityLiving(currChest, mc.player), EnumHand.MAIN_HAND, currChest.getX(), currChest.getZ(), currChest.getZ()));
				}
				sleep(500);
				printDebug("Filling chest");
				fillChest();
				printDebug("Filled chest");
				sleep(200);
				mc.player.closeScreen();
				sleep(500);
				if (kick){
					getNetworkHandler().disconnect();
					dismount();
					cmd.handleCommand("ad");
				}
			}
		}
		dismount();
		printDebug("Cycle Done");
	}

	public void dismount(){
		while(mc.player.isRiding() && enabled){
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            sleep(100);
			printDebug("Dismounted donkey");
		}
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
	}

	private void resetAfterDismount(BlockPos pos, boolean changeEnd) {
		while (!mc.player.isRiding() && enabled && mc.world != null && mc.getCurrentServerData() != null) {
			if (mc.player.getDistanceSq(Walker()) > 9) {
				doWalkTo(new BlockPos(Walker().posX, Walker().posY, Walker().posZ));
			}
			sleep(100);
			Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketUseEntity(Walker(), EnumHand.MAIN_HAND));
			sleep(300);
		}
		BlockPos newPos = pos;
		if (changeEnd) {
			if (newPos == end1){
				newPos = end2;
			}else{
				newPos = end1;
			}
		}
		doWalkTo(newPos, false);
		if (changeEnd) {
			while(mc.player.isRiding() && mc.world != null && enabled) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
				sleep(50);
			}
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
			while (!mc.player.isRiding() && enabled && mc.world != null) {
				if (mc.player.getDistanceSq(Walker()) > 9) {
					doWalkTo(new BlockPos(Walker().posX, Walker().posY, Walker().posZ));
				}
				sleep(100);
				mc.getConnection().sendPacket(new CPacketUseEntity(Walker(), EnumHand.MAIN_HAND));
				sleep(500);
			}
			doWalkTo(pos, false);
		}
	}

	private void doWalkTo(BlockPos currTarget, boolean changeEnd) {
		while (mc.player.getDistanceSq(currTarget) > 4 && enabled && mc.world != null && mc.getCurrentServerData() != null) {
			setLookAt(currTarget);
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
			sleep(50);
			while (!mc.player.isRiding()) {
				resetAfterDismount(currTarget, changeEnd);
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
			}
		}
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
	}

	private void doWalkTo(BlockPos currTarget) {
		while (mc.player.getDistanceSq(currTarget) > 1.5 && enabled && mc.world != null && mc.getCurrentServerData() != null) {
			setLookAt(currTarget);
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
			sleep(50);
		}
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
	}

	private void doRdupe1() {
		CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
		PacketInterceptionModule intercept = (PacketInterceptionModule) getModules().getByClass(PacketInterceptionModule.class);

		cmd.handleCommand("rollback");
		intercept.addIntercept(EnumPacketDirection.SERVERBOUND, 12);
		intercept.addIntercept(EnumPacketDirection.SERVERBOUND, 13);
		intercept.addIntercept(EnumPacketDirection.SERVERBOUND, 14);
		intercept.addIntercept(EnumPacketDirection.SERVERBOUND, 15);
		intercept.removeIntercept(EnumPacketDirection.SERVERBOUND, 16);
		intercept.toggle(true);
	}

	private void doRdupe2(){
		CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
		PacketInterceptionModule intercept = (PacketInterceptionModule) getModules().getByClass(PacketInterceptionModule.class);

		Entity ddonkey = findDonkey();

		cmd.handleCommand(String.format("use sneak %d", ddonkey.getEntityId()));

		intercept.addIntercept(EnumPacketDirection.SERVERBOUND, 16);
		cmd.handleCommand("rollback double");
	}

	private Entity findDonkey() {
		CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
		Entity ddonkey = null;
		if (mc.getCurrentServerData() != null) {
			List<AbstractChestHorse> donkeys = mc.player.world.getEntitiesWithinAABB(AbstractChestHorse.class, mc.player.getEntityBoundingBox().grow(7.0D, 3.0D, 7.0D));
			for (AbstractChestHorse c : donkeys) {
				if (c != mc.player.getRidingEntity() && mc.player.getDistance(c) < 6 && c != Walker()) {
					ddonkey = c;
					break;
				}
			}
			if (ddonkey == null && mc.getCurrentServerData() != null) {
				printMessage("where's donkey ?");
				dismount();
				sleep(100);
				cmd.handleCommand("ad");
				sleep(200);
				cmd.handleCommand("ad");
			}
		}
		return ddonkey;
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void setLookAt(Entity en) {
		Vec3d center = en.getEntityBoundingBox().getCenter();
		setLookAt(center.x, center.y, center.z);
	}

	private void setLookAt(BlockPos pos) {
		setLookAt(pos.getX(), pos.getY(), pos.getZ());
	}

	private void setLookAt(double x, double y, double z) {
		Vec3d eyes = mc.player.getPositionEyes(mc.getRenderPartialTicks());
	    double dirx = eyes.x - x;
	    double diry = eyes.y - y;
	    double dirz = eyes.z - z;
	    double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

	    dirx /= len;
	    diry /= len;
	    dirz /= len;

	    double pitch = Math.asin(diry);
	    double yaw = Math.atan2(dirz, dirx);
	    pitch = pitch * 180.0d / Math.PI;
	    yaw = yaw * 180.0d / Math.PI + 90;
	    mc.player.rotationYaw = (float) yaw;
	    mc.player.rotationPitch = (float) pitch;
	}

	private void lootDonkey() {
		for (int i = 2; i <= 16; i++) {
			sleep(filldelay);
			mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
			if (!enabled) {
				break;
			}
		}
		sleep(80);
		mc.player.closeScreen();
	}

	private void fillChest() {
		for (int i = 54; i <= 89; i++) {
			if (mc.player.openContainer.inventorySlots.get(i).getHasStack()) {
				sleep(filldelay);
				mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
				count++;
			}
			if (!enabled) {
				break;
			}
		}
	}
	private void fillDonkeyInventory() {
		for (int i = 37; i < 53; i++) {
			if (!mc.player.openContainer.inventorySlots.get(i).getHasStack()) {
				continue;
			}
			sleep(filldelay);
			if(isDonkFull()){
				mc.player.closeScreen();
				break;
			}
			mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
			if (i == 52) {
				mc.player.closeScreen();
				break;
			}
		}
	}

	private Entity Walker() {
		if (mc.getCurrentServerData() != null) {
			List<AbstractHorse> donkeys = mc.player.world.getEntitiesWithinAABB(AbstractHorse.class, mc.player.getEntityBoundingBox().grow(8.0D, 2.0D, 8.0D));
			for (AbstractHorse c : donkeys) {
				if (mc.player.getDistance(c) < 6 && c.hasCustomName()) {
					ddonker = c;
					break;
				}
			}
			if (ddonker == null) {
				lookforwalker();
				printMessage("where's Walker ?");
			}
		}
		return ddonker;
	}

	public void onDisconnect(){
		if (AdCommand.enabled) {
			PacketInterceptionModule intercept = (PacketInterceptionModule) getModules().getByClass(PacketInterceptionModule.class);
			CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
			intercept.toggle(false);
			//cmd.handleCommand("ad");
		}

	}
	private void lookforwalker() {
		CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
		if (RollbackCommand.teleport_id != -1){
			cmd.handleCommand("rollback");
		}
		if (ddonker == null){
			BlockPos currTarget = mc.player.getDistanceSq(end1) > mc.player.getDistanceSq(end2) ? end1 : end2;
			setLookAt(currTarget);
			doWalkTo(currTarget, true);
		}
		if (mc.player.getDistanceSq(Walker()) > 9 && mc.getCurrentServerData() != null) {
			doWalkTo(new BlockPos(Walker().posX, Walker().posY, Walker().posZ));
		}
		Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketUseEntity(Walker(), EnumHand.MAIN_HAND));
		BlockPos currTarget = mc.player.getDistanceSq(end1) > mc.player.getDistanceSq(end2) ? end1 : end2;
		doWalkTo(currTarget, true);
		dismount();
		sleep(200);
		cmd.handleCommand("ad");
		sleep(100);
		cmd.handleCommand("ad");
	}
	public void printDebug(String msg) {
		if(debug && mc.world!=null){
			Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString(TextFormatting.DARK_RED + "[DEBUG] " + TextFormatting.RESET + msg));
		}
	}
	@SubscribeEvent
	public void onUpdate(PlayerEvent event){
		if(shouldRestart){
			CommandsModule cmd = (CommandsModule) getModules().getByClass(CommandsModule.class);
			while(mc.world==null) {
				sleep(500);
			}
			if(mc.currentScreen instanceof GuiConnecting){
				sleep(10000);
			}
			if(mc.world != null && mc.player.isAddedToWorld()) {
				printMessage("resets your dupe bitch");
				cmd.handleCommand("ad");
				sleep(delay);
				cmd.handleCommand("ad");
			}
			shouldRestart=false;
		}
	}
	public static boolean isDonkFull() {
		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiScreenHorseInventory)) {
			return false;
		}
		for (int i = 2; i <= 16; i++) {
			if (!Minecraft.getMinecraft().player.openContainer.getSlot(i).getHasStack()) {
				return false;
			}
		}
		return true;
	}
}
