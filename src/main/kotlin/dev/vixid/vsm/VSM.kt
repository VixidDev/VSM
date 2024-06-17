package dev.vixid.vsm

import com.github.kwhat.jnativehook.GlobalScreen
import dev.vixid.vsm.commands.Commands
import dev.vixid.vsm.config.ConfigManager
import dev.vixid.vsm.config.VSMConfig
import dev.vixid.vsm.features.SpotifyDisplay
import dev.vixid.vsm.jnativehook.VSMLibraryLocator
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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
        ConfigManager.firstLoad()

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(SpotifyDisplay)

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
        val config: VSMConfig get() = ConfigManager.config

        private val globalJob: Job = Job(null)
        val coroutineScope = CoroutineScope(CoroutineName("VSM") + SupervisorJob(globalJob))

        var screenToOpen: GuiScreen? = null
        var screenTicks = 0
    }
}
