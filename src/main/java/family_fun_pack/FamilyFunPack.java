package family_fun_pack;

//-Dfml.coreMods.load=family_fun_pack.mixins.loader.autodupemixinloader

import family_fun_pack.commands.AdCommand;
import family_fun_pack.event.Listener;
import family_fun_pack.gui.MainGui;
import family_fun_pack.gui.overlay.OverlayGui;
import family_fun_pack.key.KeyListener;
import family_fun_pack.modules.Module;
import family_fun_pack.modules.Modules;
import family_fun_pack.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@Mod(modid = FamilyFunPack.MODID, name = FamilyFunPack.NAME, version = FamilyFunPack.VERSION)
@SideOnly(Side.CLIENT)
public class FamilyFunPack
{
    public static final String MODID = "banana";
    public static final String NAME = "BananaWare";
    public static final String VERSION = "0.1";

    public static FamilyFunPack INSTANCE;

    private static NetworkHandler networkHandler;
    private static Modules modules;
    private static OverlayGui overlay;
    private static KeyListener keyListener;
    private static MainGui mainGui;

    AdCommand autodupe = new AdCommand();




    private File confFile;

    /* Get NetworkHandler, for registering packets listeners */
    public static NetworkHandler getNetworkHandler() {
        return FamilyFunPack.networkHandler;
    }

    /* Get all Modules */
    public static Modules getModules() {
        return FamilyFunPack.modules;
    }

    /* Overlay GUI */
    public static OverlayGui getOverlay() {
        return FamilyFunPack.overlay;
    }

    /* Main GUI */
    public static MainGui getMainGui() {
        return FamilyFunPack.mainGui;
    }


    // TODO: DELETE
    public static void addModuleKey(int key, Module module) {
        FamilyFunPack.keyListener.addModuleKey(key, module);
    }

    /* Print message in chat */
    public static void printMessage(String msg) {
        Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString(TextFormatting.BLUE + "[monkeyyyyyy] " + TextFormatting.RESET + msg));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        this.confFile = event.getSuggestedConfigurationFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if(event.getSide() == Side.CLIENT) {

            // Init overlay
            FamilyFunPack.overlay = new OverlayGui();

            // Init network handler
            FamilyFunPack.networkHandler = new NetworkHandler();

            // Init key listener
            FamilyFunPack.keyListener = new KeyListener();

            // load modules configuration
            FamilyFunPack.modules = new Modules(this.confFile);

            // register overlay
            MinecraftForge.EVENT_BUS.register(FamilyFunPack.overlay);

            // register network
            MinecraftForge.EVENT_BUS.register(FamilyFunPack.networkHandler);

            // Init Main GUI
            FamilyFunPack.mainGui = new MainGui(FamilyFunPack.modules);
            FamilyFunPack.keyListener.setGui(FamilyFunPack.mainGui);

            // Register key listener
            MinecraftForge.EVENT_BUS.register(FamilyFunPack.keyListener);

            MinecraftForge.EVENT_BUS.register(autodupe);

        }
    }
}
