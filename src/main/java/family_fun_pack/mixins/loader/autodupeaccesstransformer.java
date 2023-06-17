package family_fun_pack.mixins.loader;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;
import java.io.IOException;

public class autodupeaccesstransformer extends AccessTransformer {
    public autodupeaccesstransformer() throws IOException {
        super("autodupe_at.cfg");
    }
}