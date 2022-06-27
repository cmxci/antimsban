package cmxci.antimsban.mixin;

import cmxci.antimsban.AMSBLoginHelloC2SPacket;
import cmxci.antimsban.duck.ClientLoginNetworkHandlerDuck;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets={"net.minecraft.client.gui.screen.ConnectScreen$1"})
@Environment(EnvType.CLIENT)
public class ConnectScreenAnonymousInnerMixin {
    @Shadow
    @Final
    MinecraftClient field_33738;
    @Shadow
    @Final
    ConnectScreen field_2416;

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "net/minecraft/network/ClientConnection.send(Lnet/minecraft/network/Packet;)V"))
    public void sendModifiedHelloPacket(ClientConnection instance, Packet<?> packet) {
        ClientConnection connection = field_2416.connection;
        assert connection != null;
        if (packet instanceof LoginHelloC2SPacket) {
            connection.send(new AMSBLoginHelloC2SPacket(this.field_33738.getSession().getUsername(), this.field_33738.getProfileKeys().getPublicKeyData(), this.field_33738.getSession().getProfile(), this.field_33738.getSession().getAccessToken(), ((ClientLoginNetworkHandlerDuck) field_2416).amsb$getServerId()));
        } else {
            connection.send(packet);
        }
    }
}
