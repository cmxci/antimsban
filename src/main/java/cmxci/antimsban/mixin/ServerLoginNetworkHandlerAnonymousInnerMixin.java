package cmxci.antimsban.mixin;

import cmxci.antimsban.AMSB;
import cmxci.antimsban.duck.ServerLoginNetworkHandlerDuck;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraft.server.network.ServerLoginNetworkHandler$1"})
public class ServerLoginNetworkHandlerAnonymousInnerMixin {
    @Shadow
    @Final
    ServerLoginNetworkHandler field_14176;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerLoginNetworkHandler;disconnect(Lnet/minecraft/text/Text;)V"), method = "run", cancellable = true)
    public void allowBannedPlayers(CallbackInfo ci) {
        if (field_14176 instanceof ServerLoginNetworkHandlerDuck) {
            GameProfile clientAuthPofile = ((ServerLoginNetworkHandlerDuck) field_14176).amsb$getClientAuthProfile();
            AMSB.LOGGER.info("Player {} ({}) appears to be globally banned, attempting to authenticate as client...", clientAuthPofile.getName(), clientAuthPofile.getId().toString());
            if (((ServerLoginNetworkHandlerDuck) field_14176).amsb$canJoin()) {
                ((ServerLoginNetworkHandlerDuck) field_14176).amsb$setProfile(clientAuthPofile);
                ((ServerLoginNetworkHandlerDuck) field_14176).amsb$setState(ServerLoginNetworkHandler.State.READY_TO_ACCEPT);
                ci.cancel();
            }
        }
    }
}
