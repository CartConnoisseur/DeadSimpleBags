package sh.cxl.deadsimplebags.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sh.cxl.deadsimplebags.inventory.ItemInventory;
import sh.cxl.deadsimplebags.component.DeadSimpleBagsComponents;

public class BagItem extends Item implements PolymerItem {
    private final int rows;

    public BagItem(int rows, Settings settings) {
        super(settings
                .maxCount(1)
                .component(DataComponentTypes.CONTAINER, createDefaultContainerComponent(rows))
                .component(DeadSimpleBagsComponents.OPEN, false)
        );
        this.rows = rows;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.BUNDLE;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack stack, TooltipType tooltipType, RegistryWrapper.WrapperLookup lookup, @Nullable ServerPlayerEntity player) {
        ItemStack polymerStack = PolymerItem.super.getPolymerItemStack(stack, tooltipType, lookup, player);
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(this.rows * 9, ItemStack.EMPTY);
        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        boolean open = Boolean.TRUE.equals(stack.get(DeadSimpleBagsComponents.OPEN));

        if (container == null) container = ContainerComponent.fromStacks(stacks);
        container.copyTo(stacks);

        double filled = 0;
        for (ItemStack s : container.iterateNonEmpty()) {
            if (!s.isEmpty()) filled += (double) s.getCount() / s.getMaxCount();
        }

        if (open) {
            polymerStack.set(DataComponentTypes.BUNDLE_CONTENTS, new BundleContentsComponent(DefaultedList.ofSize(0)));
        } else {
            ItemStack bundleStack = Items.DRAGON_EGG.getDefaultStack();
            bundleStack.setCount(Math.clamp((long) filled * bundleStack.getMaxCount() / (this.rows * 9L), 1, bundleStack.getMaxCount()));
            polymerStack.set(DataComponentTypes.BUNDLE_CONTENTS, new BundleContentsComponent(DefaultedList.ofSize(1, bundleStack)));
        }

        return polymerStack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ContainerComponent component = stack.get(DataComponentTypes.CONTAINER);
        if (component == null) component = createDefaultContainerComponent(this.rows);

        stack.set(DeadSimpleBagsComponents.OPEN, true);
        user.openHandledScreen(new ItemInventory(stack, rows));

        return TypedActionResult.success(stack, false);
    }

    @Override
    public boolean canBeNested() {
        return false;
    }

    private static ContainerComponent createDefaultContainerComponent(int rows) {
        return ContainerComponent.fromStacks(DefaultedList.ofSize(rows * 9, ItemStack.EMPTY));
    }
}
