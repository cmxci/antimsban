package cmxci.antimsban.mixin;

import cmxci.antimsban.AMSB;
import cmxci.antimsban.AMSBLoginHelloC2SPacket;
import cmxci.antimsban.duck.ServerLoginNetworkHandlerDuck;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.*;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.net.ssl.HandshakeCompletedEvent;
import java.math.BigInteger;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin implements ServerLoginPacketListener, ServerLoginNetworkHandlerDuck {

    public GameProfile clientAuthProfile;
    public String clientAccessToken;
    public String clientServerId;

    @Final
    @Shadow
    MinecraftServer server;
    @Shadow
    GameProfile profile;
    @Shadow
    ServerLoginNetworkHandler.State state;

    @Shadow
    public void onHello(LoginHelloC2SPacket packet) {

    }

    @Shadow
    public void onKey(LoginKeyC2SPacket packet) {

    }

    @Shadow
    public void onQueryResponse(LoginQueryResponseC2SPacket packet) {

    }

    public void amsb$onAMSBHello(AMSBLoginHelloC2SPacket packet) {
        this.clientAuthProfile = packet.clientAuthProfile();
        this.clientAccessToken = packet.clientAccessToken();
        try {
            this.clientServerId = new BigInteger(NetworkEncryptionUtils.computeServerId("", this.server.getKeyPair().getPublic(), NetworkEncryptionUtils.generateSecretKey())).toString(16);
        } catch (NetworkEncryptionException e) {
            AMSB.LOGGER.error("{}", e.toString());
        }
        this.onHello(new LoginHelloC2SPacket(packet.name(), packet.publicKey()));
    }

    public boolean amsb$canJoin() {
        if (this.clientAuthProfile == null || this.clientAccessToken == null || this.clientServerId == null) {
            AMSB.LOGGER.info("Could not authenticate potentially banned player {} because client authentication infomation was not recieved", profile.getName());
            return false;
        }
        try {
            this.server.getSessionService().joinServer(this.clientAuthProfile, this.clientAccessToken, this.clientServerId);
        } catch (InsufficientPrivilegesException | UserBannedException e) {
            AMSB.LOGGER.info("Player {} successfully authenticated as client!", clientAuthProfile.getName());
            return true;
        } catch (AuthenticationException e) {
            AMSB.LOGGER.info("Player {} client authentication was unsuccessful: {}", clientAuthProfile.getName(), e.getMessage());
            return false;
        }
        return true;
    }

    public void amsb$setProfile(GameProfile profile) {
        this.profile = profile;
    }

    public void amsb$setState(ServerLoginNetworkHandler.State state) {
        this.state = state;
    }

    public GameProfile amsb$getClientAuthProfile() {
        return this.clientAuthProfile;
    }

    @Shadow
    public void onDisconnected(Text reason) {

    }

    @Shadow
    public ClientConnection getConnection() {
        return null;
    }
}
