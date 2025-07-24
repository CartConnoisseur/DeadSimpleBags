package sh.cxl.deadsimplebags.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.ContainerComponent;
//? if >=1.21.5
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
//? if <1.21.2
/*import net.minecraft.registry.RegistryWrapper;*/
//? if <1.21.2
/*import net.minecraft.server.network.ServerPlayerEntity;*/
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import sh.cxl.deadsimplebags.component.PickupMode;
import sh.cxl.deadsimplebags.inventory.BagItemInventory;
import sh.cxl.deadsimplebags.component.DeadSimpleBagsComponents;
import sh.cxl.deadsimplebags.screen.ItemInventoryScreenHandler;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
//? if >=1.21.5
import java.util.function.Consumer;

public class BagItem extends Item implements PolymerItem {
    private final int rows;

    public BagItem(int rows, net.minecraft.item.Item.Settings settings) {
        super(settings
                .maxCount(1)
                .component(DataComponentTypes.CONTAINER, createDefaultContainerComponent(rows))
                .component(DeadSimpleBagsComponents.OPEN, false)
                .component(DeadSimpleBagsComponents.PICKUP_MODE, PickupMode.NONE)
        );
        this.rows = rows;
    }

    @Override
    public Item getPolymerItem(ItemStack stack, /*? <1.21.2 {*/ /*@Nullable ServerPlayerEntity serverPlayerEntity *//*?} else {*/ PacketContext context /*?}*/) {
        return Items.BUNDLE;
    }

    //? if >=1.21.2 {
    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return Items.BUNDLE.getDefaultStack().get(DataComponentTypes.ITEM_MODEL);
    }
    //?}

    @Override
    //? if <1.21.2 {
    /*public ItemStack getPolymerItemStack(ItemStack stack, TooltipType tooltipType, RegistryWrapper.WrapperLookup lookup, @Nullable ServerPlayerEntity player) {
        ItemStack polymerStack = PolymerItem.super.getPolymerItemStack(stack, tooltipType, lookup, player);
    *///?} else {
    public ItemStack getPolymerItemStack(ItemStack stack, TooltipType tooltipType, PacketContext context) {
        ItemStack polymerStack = PolymerItem.super.getPolymerItemStack(stack, tooltipType, context);
    //?}
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(this.rows * 9, ItemStack.EMPTY);
        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        boolean open = Boolean.TRUE.equals(stack.get(DeadSimpleBagsComponents.OPEN));

        if (container == null) container = ContainerComponent.fromStacks(stacks);
        container.copyTo(stacks);

        double filled = 0;
        for (ItemStack s : container.iterateNonEmpty()) {
            if (!s.isEmpty()) filled += (double) s.getCount() / s.getMaxCount();
        }


        //? if <1.21.2 {
        /*if (open) {
            polymerStack.set(DataComponentTypes.BUNDLE_CONTENTS, new BundleContentsComponent(DefaultedList.ofSize(0)));
            return polymerStack;
        }
        *///?}

        ItemStack bundleStack = DeadSimpleBagsItems.DUMMY.getDefaultStack();
        bundleStack.setCount(Math.clamp((long) filled * bundleStack.getMaxCount() / (this.rows * 9L), 1, bundleStack.getMaxCount()));
        polymerStack.set(DataComponentTypes.BUNDLE_CONTENTS, new BundleContentsComponent(DefaultedList.ofSize(1, bundleStack)));

        return polymerStack;
    }

    public BagItemInventory getInventory(PlayerEntity player, ItemStack stack) {
        if (player.currentScreenHandler instanceof ItemInventoryScreenHandler itemInventoryScreenHandler) {
            return itemInventoryScreenHandler.getInventory();
        }

        return new BagItemInventory(stack, this.rows);
    }

    @Override
    public /*? <1.21.2 {*/ /*TypedActionResult<ItemStack> *//*?} else {*/ ActionResult /*?}*/ use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        if (container == null) container = createDefaultContainerComponent(this.rows);
        PickupMode pickupMode = stack.get(DeadSimpleBagsComponents.PICKUP_MODE);
        if (pickupMode == null) pickupMode = PickupMode.NONE;

        if (user.isSneaking()) {
            pickupMode = pickupMode.next();
            user.sendMessage(Text.translatable("deadsimplebags.pickup_mode", Text.translatable("deadsimplebags.pickup_mode." + pickupMode.asString().toLowerCase())), false);
            stack.set(DeadSimpleBagsComponents.PICKUP_MODE, pickupMode);
        } else {
            stack.set(DeadSimpleBagsComponents.OPEN, true);
            user.openHandledScreen(new BagItemInventory(stack, rows));

            //? if >=1.21.2
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.PLAYERS, 0.8F, 0.8F + user.getWorld().getRandom().nextFloat() * 0.4F);
        }

        return /*? <1.21.2 {*/ /*TypedActionResult.success(stack) *//*?} else {*/ ActionResult.SUCCESS /*?}*/;
    }

    //? if <1.21.5 {
    /*@Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        PickupMode pickupMode = stack.get(DeadSimpleBagsComponents.PICKUP_MODE);
        if (pickupMode == null) pickupMode = PickupMode.NONE;

        tooltip.add(Text.translatable("deadsimplebags.pickup_mode", Text.translatable("deadsimplebags.pickup_mode." + pickupMode.asString().toLowerCase())).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("deadsimplebags.tooltip.cycle_pickup_mode").formatted(Formatting.DARK_GRAY));
    }
    *///?} else {
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        PickupMode pickupMode = stack.get(DeadSimpleBagsComponents.PICKUP_MODE);
        if (pickupMode == null) pickupMode = PickupMode.NONE;

        textConsumer.accept(Text.translatable("deadsimplebags.pickup_mode", Text.translatable("deadsimplebags.pickup_mode." + pickupMode.asString().toLowerCase())).formatted(Formatting.GRAY));
        textConsumer.accept(Text.translatable("deadsimplebags.tooltip.cycle_pickup_mode").formatted(Formatting.DARK_GRAY));
    }
    //?}

    @Override
    public boolean canBeNested() {
        return false;
    }

    private static ContainerComponent createDefaultContainerComponent(int rows) {
        return ContainerComponent.fromStacks(DefaultedList.ofSize(rows * 9, ItemStack.EMPTY));
    }
}
