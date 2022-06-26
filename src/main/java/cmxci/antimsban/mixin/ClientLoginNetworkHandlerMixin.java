package cmxci.antimsban.mixin;

import cmxci.antimsban.AMSB;
import cmxci.antimsban.duck.ClientLoginNetworkHandlerDuck;
import com.mojang.authlib.exceptions.*;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLoginNetworkHandler.class)
@Environment(EnvType.CLIENT)
public class ClientLoginNetworkHandlerMixin implements ClientLoginNetworkHandlerDuck {
    @Final
    @Shadow
    private MinecraftClient client;

    private String serverId;

    @Inject(at = @At("HEAD"), method = "joinServerSession", cancellable = true)
    public void joinServerSession(String serverId, CallbackInfoReturnable<Text> cir) {
        this.serverId = serverId;
        try {
            assert this.getSessionService() != null;
            this.getSessionService().joinServer(this.client.getSession().getProfile(), this.client.getSession().getAccessToken(), serverId);
        }
        catch (AuthenticationUnavailableException authenticationUnavailableException) {
            cir.setReturnValue(Text.translatable("disconnect.loginFailedInfo", Text.translatable("disconnect.loginFailedInfo.serversUnavailable")));
        }
        catch (InvalidCredentialsException invalidCredentialsException) {
            cir.setReturnValue(Text.translatable("disconnect.loginFailedInfo", Text.translatable("disconnect.loginFailedInfo.invalidSession")));
        }
        catch (InsufficientPrivilegesException | UserBannedException insufficientPrivilegesException) {
            AMSB.LOGGER.warn("This player cannot join multiplayer games. This may be due to Microsoft account settings or a global ban.");
            cir.setReturnValue(null);
        } catch (AuthenticationException authenticationException) {
            cir.setReturnValue(Text.translatable("disconnect.loginFailedInfo", authenticationException.getMessage()));
        }
        cir.setReturnValue(null);
    }

    @Shadow
    private MinecraftSessionService getSessionService() {
        return null;
    }

    public String amsb$getServerId() {
        return this.serverId;
    }
}
