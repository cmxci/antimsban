package cmxci.antimsban.mixin;

import com.mojang.authlib.minecraft.BanDetails;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
@Environment(EnvType.CLIENT)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "isMultiplayerEnabled", cancellable = true)
    public void multiplayerIsEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "getMultiplayerBanDetails", cancellable = true)
    public void noBansHere(CallbackInfoReturnable<BanDetails> cir) {
        cir.setReturnValue(null);
    }
}
