package dev.vixid.vsm

import com.github.kwhat.jnativehook.GlobalScreen
import dev.vixid.vsm.commands.Commands
import dev.vixid.vsm.config.VSMConfig
import dev.vixid.vsm.config.core.VSMGsonMapper
import dev.vixid.vsm.features.spotify.SpotifyDisplay
import dev.vixid.vsm.jnativehook.VSMLibraryLocator
import dev.vixid.vsm.overlays.OverlayPositions
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import java.io.File
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@Mod(modid = "vsm", useMetadata = true)
class VSM {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        Commands.initCommands()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)

        SpotifyDisplay.initialise()
        OverlayPositions.initialise()

        System.setProperty("jnativehook.lib.locator", VSMLibraryLocator::class.java.canonicalName)
        GlobalScreen.registerNativeHook()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (screenToOpen != null) {
            screenTicks++
            if (screenTicks == 5) {
                Minecraft.getMinecraft().thePlayer.closeScreen()
                Minecraft.getMinecraft().displayGuiScreen(screenToOpen)
                screenTicks = 0
                screenToOpen = null
            }
        }
    }

    companion object {
        @JvmStatic
        val config = ManagedConfig.create(File("config/vsm/config.json"), VSMConfig::class.java) {
            mapper = VSMGsonMapper(this.clazz)
        }

        var screenToOpen: GuiScreen? = null
        var screenTicks = 0
    }
}
