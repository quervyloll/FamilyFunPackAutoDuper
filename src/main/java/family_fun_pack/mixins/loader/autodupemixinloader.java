package family_fun_pack.mixins.loader;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions(value = "family_fun_pack.mixins.loader")
@IFMLLoadingPlugin.Name(value = "autodupe")
@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
public final class autodupemixinloader implements IFMLLoadingPlugin {

    public autodupemixinloader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.autodupe.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return autodupeaccesstransformer.class.getName();
    }
}
