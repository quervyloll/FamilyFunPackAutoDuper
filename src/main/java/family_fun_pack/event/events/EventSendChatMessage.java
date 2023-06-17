package family_fun_pack.event.events;

import family_fun_pack.event.EventCancellable;

public class EventSendChatMessage extends EventCancellable {

    private String message;

    public EventSendChatMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
