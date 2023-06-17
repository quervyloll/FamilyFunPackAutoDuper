package family_fun_pack.event.events;

import net.minecraft.network.Packet;
import family_fun_pack.event.EventCancellable;

public class EventReceivePacket extends EventCancellable {

    private Packet packet;

    public EventReceivePacket(EventStage stage, Packet packet) {
        super(stage);
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
