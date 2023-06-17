package family_fun_pack.event.events;

import net.minecraft.network.Packet;
import family_fun_pack.event.EventCancellable;

public final class EventSendPacket extends EventCancellable {

    private Packet packet;

    public EventSendPacket(EventStage stage, Packet packet) {
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