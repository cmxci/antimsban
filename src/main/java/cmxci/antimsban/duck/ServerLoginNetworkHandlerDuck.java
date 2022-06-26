package cmxci.antimsban.duck;

import cmxci.antimsban.AMSBLoginHelloC2SPacket;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerLoginNetworkHandler;

public interface ServerLoginNetworkHandlerDuck {
    void amsb$onAMSBHello(AMSBLoginHelloC2SPacket packet);
    boolean amsb$canJoin();
    void amsb$setProfile(GameProfile profile);
    void amsb$setState(ServerLoginNetworkHandler.State state);
    GameProfile amsb$getClientAuthProfile();
}
