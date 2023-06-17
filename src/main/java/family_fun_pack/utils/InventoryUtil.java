package family_fun_pack.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author noil
 */

public final class InventoryUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isInventoryFull() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasItem(Item input) {
        for (int i = 0; i < 36; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                return true;
            }
        }

        return false;
    }

    public static int getItemCount(Item input) {
        int items = 0;
        for (int i = 0; i < 45; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                items += 1;
            }
        }

        return items;
    }

    public static int getBlockCount(Block input) {
        int blocks = 0;
        for (int i = 0; i < 45; i++) {
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemBlock) {
                final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                if (itemBlock.getBlock() == input) {
                    blocks += itemStack.getCount();
                }
            }
        }

        return blocks;
    }

    public static int getSlotForItem(Item input) {
        for (int i = 0; i < 36; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isItemStackEnderChest(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock)
            return ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockEnderChest;

        return false;
    }

    public static int findEnderChestInHotbar(final EntityPlayerSP player) {
        for (int index = 0; InventoryPlayer.isHotbar(index); index++)
            if (isItemStackEnderChest(player.inventory.getStackInSlot(index)))
                return index;

        return -1;
    }

    public static boolean isItemStackObsidian(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock)
            return ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockObsidian;

        return false;
    }

    public static int findObsidianInHotbar(final EntityPlayerSP player) {
        for (int index = 0; InventoryPlayer.isHotbar(index); index++)
            if (isItemStackObsidian(player.inventory.getStackInSlot(index)))
                return index;

        return -1;
    }
    public void fillChest() {
        for (int i = 54; i <= 89; i++) {
            if (mc.player.openContainer.inventorySlots.get(i).getHasStack()) {
                mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
            }
        }
    }
    public void lootDonkey() {
        for (int i = 1; i <= 16; i++) {
            mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
        }
    }
}