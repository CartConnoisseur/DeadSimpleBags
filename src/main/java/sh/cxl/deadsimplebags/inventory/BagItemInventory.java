package sh.cxl.deadsimplebags.inventory;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import sh.cxl.deadsimplebags.component.DeadSimpleBagsComponents;
import sh.cxl.deadsimplebags.component.PickupMode;
import sh.cxl.deadsimplebags.screen.ItemInventoryScreenHandler;

public class BagItemInventory implements NamedScreenHandlerFactory, Inventory {
    private final ItemStack root;
    private final int rows;
    private final DefaultedList<ItemStack> items;

    public BagItemInventory(ItemStack stack, int rows) {
        this.root = stack;
        this.rows = rows;

        this.items = DefaultedList.ofSize(rows * 9, ItemStack.EMPTY);
        ContainerComponent component = stack.get(DataComponentTypes.CONTAINER);
        if (component == null) component = ContainerComponent.fromStacks(items);
        component.copyTo(items);
    }

    public ItemStack getRootStack() {
        return this.root;
    }

    @Override
    public Text getDisplayName() {
        return root.getName();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ScreenHandlerType<?> type = switch (this.rows) {
            case 1:
                yield ScreenHandlerType.GENERIC_9X1;
            case 2:
                yield ScreenHandlerType.GENERIC_9X2;
            case 3:
                yield ScreenHandlerType.GENERIC_9X3;
            case 4:
                yield ScreenHandlerType.GENERIC_9X4;
            case 5:
                yield ScreenHandlerType.GENERIC_9X5;
            case 6:
                yield ScreenHandlerType.GENERIC_9X6;
            default:
                throw new IndexOutOfBoundsException();
        };

        return new ItemInventoryScreenHandler(type, syncId, playerInventory, this, this.rows);
    }

    @Override
    public int size() {
        return this.rows * 9;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(items, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        stack.capCount(stack.getMaxCount());
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void onClose(PlayerEntity player) {
        root.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(items));
        root.set(DeadSimpleBagsComponents.OPEN, false);
    }

    private boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        return !existingStack.isEmpty() && ItemStack.areItemsAndComponentsEqual(existingStack, stack) && stack.isStackable() && existingStack.isStackable() && existingStack.getCount() < this.getMaxCount(existingStack);
    }

    public int getSlotWithRoomForStack(ItemStack stack) {
        for (int slot = 0; slot < this.items.size(); slot++) {
            if (this.canStackAddMore(this.items.get(slot), stack) || this.items.get(slot).isEmpty()) {
                return slot;
            }
        }

        return -1;
    }

    private int addStack(ItemStack stack) {
        int slot = this.getSlotWithRoomForStack(stack);
        return slot == -1 ? stack.getCount() : this.addStack(slot, stack);
    }

    private int addStack(int slot, ItemStack stack) {
        int count = stack.getCount();
        ItemStack itemStack = this.getStack(slot);
        if (itemStack.isEmpty()) {
            itemStack = stack.copyWithCount(0);
            this.setStack(slot, itemStack);
        }

        int free = this.getMaxCount(itemStack) - itemStack.getCount();
        int amount = Math.min(count, free);
        if (amount != 0) {
            itemStack.increment(amount);
            itemStack.setBobbingAnimationTime(5);
        }

        return count - amount;
    }

    public boolean insertStack(PickupMode mode, ItemStack stack) {
        if (stack.isEmpty()) return false;

        boolean validItem = false;
        if (mode == PickupMode.EXISTING) {
            for (ItemStack item : this.items) {
                if (item.isOf(stack.getItem())) {
                    validItem = true;
                    break;
                }
            }
        } else {
            validItem = true;
        }

        if (!validItem) return false;

        int count = Integer.MAX_VALUE;
        while (!stack.isEmpty() && stack.getCount() < count) {
            count = stack.getCount();
            stack.setCount(this.addStack(stack));
        }

        root.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(items));
        return count == 0;
    }
}
