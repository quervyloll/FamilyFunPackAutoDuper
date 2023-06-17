package family_fun_pack.event.events;

import net.minecraft.entity.Entity;

public class EventAddEntity {

    private Entity entity;

    public EventAddEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}