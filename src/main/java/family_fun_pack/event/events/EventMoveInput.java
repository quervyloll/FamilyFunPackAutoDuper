package family_fun_pack.event.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;

public class EventMoveInput {

    private final MovementInput input;
    private final EntityPlayer player;

    public EventMoveInput(EntityPlayer player, MovementInput input) {
        this.input = input;
        this.player = player;
    }

    public MovementInput getInput() {
        return input;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}