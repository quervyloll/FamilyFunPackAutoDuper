package family_fun_pack.event.events;

import net.minecraft.client.multiplayer.WorldClient;
import family_fun_pack.event.EventCancellable;

public class EventLoadWorld extends EventCancellable {

    private final WorldClient world;

    public EventLoadWorld(WorldClient world) {
        this.world = world;
    }

    public WorldClient getWorld() {
        return world;
    }



}
