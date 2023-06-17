package family_fun_pack.event.events;

import family_fun_pack.FamilyFunPack;
import family_fun_pack.modules.CommandsModule;
import family_fun_pack.modules.PacketInterceptionModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import family_fun_pack.commands.AdCommand;

@Mod.EventBusSubscriber(Side.CLIENT)
public class onDisconnect {

    public static final Minecraft mc = Minecraft.getMinecraft();


    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        // Get the server address and port
        String serverAddress = event.getManager().getRemoteAddress().toString();
        // Check if the server address is "9b9t.org" and the "enabled" variable is true
        if (serverAddress.equals("9b9t.com") && AdCommand.enabled) {
            PacketInterceptionModule intercept = (PacketInterceptionModule) FamilyFunPack.getModules().getByClass(PacketInterceptionModule.class);
            CommandsModule cmd = (CommandsModule) FamilyFunPack.getModules().getByClass(CommandsModule.class);
            intercept.toggle(false);
            cmd.handleCommand("ad");
            sleep(10000);
            if (mc.player.isRiding()){
                dismount();
            }
            sleep(200);
            cmd.handleCommand("ad");
        }
    }
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void dismount(){
        while(mc.player.isRiding()){
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            sleep(50);
        }
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
    }
}
