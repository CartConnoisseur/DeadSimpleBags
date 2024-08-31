package sh.cxl.deadsimplebags.component;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum PickupMode implements StringIdentifiable {
    NONE,
    ALL,
    EXISTING,
    OVERFLOW;

    public static final Codec<PickupMode> CODEC = StringIdentifiable.createCodec(PickupMode::values);

    public PickupMode next() {
        return switch (this) {
            case NONE -> ALL;
            case ALL -> EXISTING;
            case EXISTING -> OVERFLOW;
            case OVERFLOW -> NONE;
        };
    }

    @Override
    public String asString() {
        return this.name();
    }
}
