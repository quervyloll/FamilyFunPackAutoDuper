package family_fun_pack.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.EntityPig;

@Mixin(EntityPig.class)
public class MixinEntityPig
{
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    private void canBeSteered(CallbackInfoReturnable<Boolean> info) {
    	
    	info.cancel();
        info.setReturnValue(true);
    }

    @Inject(method = "getSaddled", at = @At("HEAD"), cancellable = true)
    private void getSaddled(CallbackInfoReturnable<Boolean> info) {
    	
    	info.cancel();
        info.setReturnValue(true);
    }
}