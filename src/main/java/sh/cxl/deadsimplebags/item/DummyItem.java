package sh.cxl.deadsimplebags.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
//? if >=1.21.2
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
//? if <1.21.2
/*import net.minecraft.server.network.ServerPlayerEntity;*/
//? if >=1.21.2
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
//? if >=1.21.2
import xyz.nucleoid.packettweaker.PacketContext;

public class DummyItem extends Item implements PolymerItem {
    public DummyItem(Settings settings) {
        super(settings);
    }


    @Override
    public Item getPolymerItem(ItemStack stack, /*? <1.21.2 {*/ /*@Nullable ServerPlayerEntity serverPlayerEntity *//*?} else {*/ PacketContext context /*?}*/) {
        return Items.BARRIER;
    }

    //? if >=1.21.2 {
    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return Items.AIR.getDefaultStack().get(DataComponentTypes.ITEM_MODEL);
    }
    //?}
}
