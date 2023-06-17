package family_fun_pack.event.events;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import family_fun_pack.event.EventCancellable;

import java.util.List;

public class EventRenderTooltip extends EventCancellable {

    private ItemStack itemStack;

    private List<String> lines;

    private int x;

    private int y;

    public EventRenderTooltip(ItemStack itemStack, int x, int y) {
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getLines(){
        return lines;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}