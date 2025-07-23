package sh.cxl.deadsimplebags.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import sh.cxl.deadsimplebags.DeadSimpleBags;

import java.util.function.Function;

public abstract class DeadSimpleBagsItems {
    public static final Item TINY_BAG = registerBagItem("tiny_bag", 1);
    public static final Item SMALL_BAG = registerBagItem("small_bag", 2);
    public static final Item MEDIUM_BAG = registerBagItem("medium_bag", 3);
    public static final Item LARGE_BAG = registerBagItem("large_bag", 4);
    public static final Item HUGE_BAG = registerBagItem("huge_bag", 5);
    public static final Item MASSIVE_BAG = registerBagItem("massive_bag", 6);

    public static void register() {
        DeadSimpleBags.LOGGER.info("Registering {} items", DeadSimpleBags.MOD_ID);
    }

    private static Item registerBagItem(String name, int rows) {
        return register(name, (settings -> new BagItem(rows, settings)), new Item.Settings());
    }

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(DeadSimpleBags.MOD_ID, name));
        Item item = factory.apply(/*? <1.21.2 {*/ /*settings *//*?} else {*/ settings.registryKey(key) /*?}*/);
        Registry.register(Registries.ITEM, key, item);

        return item;
    }
}
