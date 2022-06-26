package cmxci.antimsban;

import cmxci.antimsban.duck.ServerLoginNetworkHandlerDuck;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.network.listener.ServerLoginPacketListener;

import java.util.Optional;

public record AMSBLoginHelloC2SPacket(String name, Optional<PlayerPublicKey.PublicKeyData> publicKey, GameProfile clientAuthProfile, String clientAccessToken, String clientServerId) implements Packet<ServerLoginPacketListener> {
    public AMSBLoginHelloC2SPacket(PacketByteBuf buf) {
        this(buf.readString(16), buf.readOptional(PlayerPublicKey.PublicKeyData::new), buf.readGameProfile(), buf.readString(), buf.readString());
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.name, 16);
        buf.writeOptional(this.publicKey, (buf2, publicKey) -> publicKey.write(buf));
        buf.writeGameProfile(this.clientAuthProfile);
        buf.writeString(this.clientAccessToken);
        buf.writeString(this.clientServerId);
    }

    public void apply(ServerLoginPacketListener arg) {
        if (arg instanceof ServerLoginNetworkHandlerDuck) ((ServerLoginNetworkHandlerDuck)arg).amsb$onAMSBHello(this);
    }
}
