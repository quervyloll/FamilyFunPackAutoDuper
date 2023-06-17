package family_fun_pack.utils;

import family_fun_pack.FamilyFunPack;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author bon55, linustouchtips
 * @since 05/13/2021
 */
public interface Wrapper {

    // minecraft instance
    Minecraft mc = Minecraft.getMinecraft();

    /**
     * Checks if the world and the player are not null (usually unsafe to run operations if these are null)
     * @return Whether the world and the player are not null
     */
    default boolean nullCheck() {
        return mc.player != null && mc.world != null;
    }

    /**
     * Gets the client instance
     * @return The client instance
     */
    default FamilyFunPack getClient() {
        return FamilyFunPack.INSTANCE;
    }

    /**
     * Gets the logger instance
     * @return The logger instance
     */
    default Logger getLogger() {
        return LogManager.getLogger(FamilyFunPack.NAME + " | " + FamilyFunPack.VERSION);
    }
}
