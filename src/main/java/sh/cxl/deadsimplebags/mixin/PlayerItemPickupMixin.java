package sh.cxl.deadsimplebags.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sh.cxl.deadsimplebags.component.DeadSimpleBagsComponents;
import sh.cxl.deadsimplebags.component.PickupMode;
import sh.cxl.deadsimplebags.item.BagItem;

import java.util.ArrayList;

@Mixin(ItemEntity.class)
public abstract class PlayerItemPickupMixin {
    @Redirect(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean insertStack(PlayerInventory inventory, ItemStack stack) {
        ArrayList<ItemStack> bags = new ArrayList<>();

        for (ItemStack invStack : /*? <1.21.5 {*/ /*inventory.main *//*?} else {*/ inventory.getMainStacks() /*?}*/) {
            if (invStack.getItem() instanceof BagItem) {
                bags.add(invStack);
            }
        }

        for (ItemStack bag : bags) {
            PickupMode pickupMode = bag.get(DeadSimpleBagsComponents.PICKUP_MODE);
            if (pickupMode == null) continue;

            if (pickupMode == PickupMode.ALL || pickupMode == PickupMode.EXISTING) {
                if (((BagItem) bag.getItem()).getInventory(inventory.player, bag).insertStack(pickupMode, stack)) return true;
            }
        }

        if (inventory.insertStack(stack)) return true;

        for (ItemStack bag : bags) {
            PickupMode pickupMode = bag.get(DeadSimpleBagsComponents.PICKUP_MODE);
            if (pickupMode == null) continue;

            if (pickupMode == PickupMode.OVERFLOW) {
                if (((BagItem) bag.getItem()).getInventory(inventory.player, bag).insertStack(pickupMode, stack)) return true;
            }
        }

        return stack.getCount() == 0;
    }
}
