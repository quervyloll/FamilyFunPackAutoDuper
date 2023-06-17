package family_fun_pack.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.AbstractHorse;

@Mixin(AbstractHorse.class)
public class MixinAbstractHorse
{
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    private void canBeSteered(CallbackInfoReturnable<Boolean> info) {

        info.setReturnValue(true);
        info.cancel();
    }

    @Inject(method = "isHorseSaddled", at = @At("HEAD"), cancellable = true)
    private void isHorseSaddled(CallbackInfoReturnable<Boolean> info) {
    		
    	info.cancel();
    	info.setReturnValue(true);
    }
}