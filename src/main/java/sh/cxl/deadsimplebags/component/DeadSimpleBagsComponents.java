package sh.cxl.deadsimplebags.component;

import com.mojang.serialization.Codec;
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
}
