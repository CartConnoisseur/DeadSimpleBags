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
    public static final Item TINY_BAG = register("tiny_bag", (settings -> new BagItem(1, settings)), new Item.Settings());
    public static final Item SMALL_BAG = register("small_bag", (settings -> new BagItem(2, settings)), new Item.Settings());
    public static final Item MEDIUM_BAG = register("medium_bag", (settings -> new BagItem(3, settings)), new Item.Settings());
    public static final Item LARGE_BAG = register("large_bag", (settings -> new BagItem(4, settings)), new Item.Settings());
    public static final Item HUGE_BAG = register("huge_bag", (settings -> new BagItem(5, settings)), new Item.Settings());
    public static final Item MASSIVE_BAG = register("massive_bag", (settings -> new BagItem(6, settings)), new Item.Settings());

    public static void register() {
        DeadSimpleBags.LOGGER.info("Registering {} items", DeadSimpleBags.MOD_ID);
    }

    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(DeadSimpleBags.MOD_ID, name));
        Item item = factory.apply(settings.registryKey(key));
        Registry.register(Registries.ITEM, key, item);

        return item;
    }
}
