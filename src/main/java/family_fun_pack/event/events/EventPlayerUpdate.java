package family_fun_pack.event.events;

import family_fun_pack.event.EventCancellable;

public class EventPlayerUpdate extends EventCancellable {
    public EventPlayerUpdate(EventStage stage) {
        super(stage);
    }
}
