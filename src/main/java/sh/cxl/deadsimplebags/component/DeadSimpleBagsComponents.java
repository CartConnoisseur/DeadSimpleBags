package sh.cxl.deadsimplebags.component;

import com.mojang.serialization.Codec;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import sh.cxl.deadsimplebags.DeadSimpleBags;

public abstract class DeadSimpleBagsComponents {
    public static final ComponentType<Boolean> OPEN = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(DeadSimpleBags.MOD_ID, "open"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static final ComponentType<PickupMode> PICKUP_MODE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(DeadSimpleBags.MOD_ID, "pickup_mode"),
            ComponentType.<PickupMode>builder().codec(PickupMode.CODEC).build()
    );

    public static void register() {
        DeadSimpleBags.LOGGER.info("Registering {} components", DeadSimpleBags.MOD_ID);

        PolymerComponent.registerDataComponent(OPEN);
        PolymerComponent.registerDataComponent(PICKUP_MODE);
    }
}
