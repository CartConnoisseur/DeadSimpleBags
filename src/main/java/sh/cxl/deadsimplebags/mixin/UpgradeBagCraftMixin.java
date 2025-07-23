package sh.cxl.deadsimplebags.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.CraftingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sh.cxl.deadsimplebags.item.BagItem;

@Mixin(CraftingScreenHandler.class)
public abstract class UpgradeBagCraftMixin {
    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/CraftingRecipe;craft(Lnet/minecraft/recipe/input/RecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack modifyCraftingResult(CraftingRecipe instance, RecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack result = instance.craft((CraftingRecipeInput) input, registries);

        if (result.getItem() instanceof BagItem) {
            ItemStack bag = ItemStack.EMPTY;

            for (ItemStack stack : ((CraftingRecipeInput) input).getStacks()) {
                if (stack.getItem() instanceof BagItem) {
                    bag = stack;
                    break;
                }
            }

            if (bag.isEmpty()) {
                return result;
            }

            result.applyComponentsFrom(bag.getComponents());
        }

        return result;
    }
}
