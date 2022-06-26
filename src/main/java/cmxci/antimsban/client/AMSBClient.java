package cmxci.antimsban.client;

import cmxci.antimsban.AMSB;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AMSBClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AMSB.LOGGER.info("AntiMSBan has been initialized client-side. Any server that you join will require this mod.");
    }
}
