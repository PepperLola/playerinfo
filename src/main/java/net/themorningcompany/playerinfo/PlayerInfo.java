package net.themorningcompany.playerinfo;

import net.themorningcompany.playerinfo.listeners.FovListener;
import net.themorningcompany.playerinfo.listeners.HitListener;
import net.themorningcompany.playerinfo.listeners.NoteBlockListener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.themorningcompany.playerinfo.commands.CalcCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("playerinfo")
public class PlayerInfo {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "playerinfo";
    public static final Random random = new Random();

    public PlayerInfo() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverStarting);
    }

    @SubscribeEvent
    public void onModStart(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new FovListener());
        MinecraftForge.EVENT_BUS.register(new HitListener());
        MinecraftForge.EVENT_BUS.register(NoteBlockListener.class);

        ClientRegistry.registerKeyBinding(new KeyBinding("key.zoom", 89, "Player Info"));
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
//        InfoCommand.register(event.getCommandDispatcher());
//        FOVCommand.register(event.getCommandDispatcher());
        CalcCommand.register(event.getServer().getCommandManager().getDispatcher());
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }
}
