package sh.cxl.deadsimplebags.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import sh.cxl.deadsimplebags.inventory.ItemInventory;

public class ItemInventoryScreenHandler extends ScreenHandler {
    private final ItemInventory inventory;
    private final int rows;

    public ItemInventoryScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ItemInventory inventory, int rows) {
        super(type, syncId);
        checkSize(inventory, rows * 9);
        this.inventory = inventory;
        this.rows = rows;
        inventory.onOpen(playerInventory.player);
        int i = (this.rows - 4) * 18;

        int j;
        int k;
        for(j = 0; j < this.rows; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlot(new ItemInventorySlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(j = 0; j < 3; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlot(new ItemInventoryPlayerSlot(inventory.getRootStack(), playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for(j = 0; j < 9; ++j) {
            this.addSlot(new ItemInventoryPlayerSlot(inventory.getRootStack(), playerInventory, j, 8 + j * 18, 161 + i));
        }
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.rows * 9) {
                if (!this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
        }

        return itemStack;
    }

    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getRows() {
        return this.rows;
    }

    private static class ItemInventorySlot extends Slot {
        public ItemInventorySlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.getItem().canBeNested();
        }
    }

    private static class ItemInventoryPlayerSlot extends Slot {
        private final ItemStack root;

        public ItemInventoryPlayerSlot(ItemStack root, Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.root = root;
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return !this.getStack().equals(this.root);
        }
    }
}
