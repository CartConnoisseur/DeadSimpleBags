package sh.cxl.deadsimplebags;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sh.cxl.deadsimplebags.item.DeadSimpleBagsItems;

public class DeadSimpleBags implements ModInitializer {
    public static final String MOD_ID = "deadsimplebags";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        DeadSimpleBagsItems.register();
    }
}
