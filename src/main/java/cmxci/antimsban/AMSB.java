package cmxci.antimsban;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AMSB implements ModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("AMSB");

    @Override
    public void onInitialize() {
        LOGGER.info("AntiMSBan has been initialized server-side. Globally banned clients will need this mod to join.");
    }
}
