package family_fun_pack.event.events;

import family_fun_pack.FamilyFunPack;
import family_fun_pack.modules.CommandsModule;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class onJoinWorld {

    @SubscribeEvent
    private void onJoin(PlayerEvent.PlayerLoggedInEvent event){
        sleep(2000);
        CommandsModule cmd = (CommandsModule) FamilyFunPack.getModules().getByClass(CommandsModule.class);
        cmd.handleCommand("ad");

    }
    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
