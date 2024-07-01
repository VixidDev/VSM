package dev.vixid.vsm.features.spotify

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.features.SpotifyConfig
import dev.vixid.vsm.events.KeyboardEvent
import dev.vixid.vsm.overlays.Overlay
import dev.vixid.vsm.overlays.OverlayPositions
import dev.vixid.vsm.utils.ChatUtils
import dev.vixid.vsm.utils.JNAHelper
import dev.vixid.vsm.utils.RenderUtils.drawTextWithShadow
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object SpotifyDisplay : Overlay() {

    private val config: SpotifyConfig get() = VSM.config.instance.spotifyConfig

    private var songName: String = "§cCannot detect song name!"
    private var totalTicks = 0

    fun initialise() {
        OverlayPositions.addOverlay(this)

        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun renderOverlay() {
        if (!isEnabled()) return
        config.overlayPosition.drawTextWithShadow(songName)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) return
        if (!isEnabled()) return

        totalTicks++

        if (totalTicks % 20 == 0) {
            var windowTitle = JNAHelper.getProcessWindowTitle("Spotify.exe")

            if (windowTitle == "Spotify") {
                windowTitle = songName
            } else if (windowTitle.isNotEmpty()) {
                windowTitle = "§a${windowTitle}".replace(" - ", " §f-§b ")

                if (songName != windowTitle) {
                    ChatUtils.chat("§bVSM §f> $windowTitle")
                }
            }
            songName = windowTitle
        }
    }

    @SubscribeEvent
    fun onKeyboardPress(event: KeyboardEvent) {
        if (event.keystate && event.key != -1) {
            checkKeybinds(event.key)
        }
    }

    @SubscribeEvent
    fun onMousePress(event: MouseEvent) {
        if (event.buttonstate) {
            checkKeybinds(event.button - 100)
        }
    }

    private fun checkKeybinds(key: Int) {
        when (key) {
            config.skipFowardKey -> ControlUtils.postSkipSong()
            config.skipBackwardKey -> ControlUtils.postPreviousSong()
            config.playPauseKey -> ControlUtils.postPlaySong()
        }
    }

    private fun isEnabled() = Minecraft.getMinecraft().theWorld != null && config.enabled
}