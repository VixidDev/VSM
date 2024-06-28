package dev.vixid.vsm.spotify

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.SpotifyConfig
import dev.vixid.vsm.events.KeyPress
import dev.vixid.vsm.events.MousePress
import dev.vixid.vsm.overlays.Overlay
import dev.vixid.vsm.overlays.OverlayPositions
import dev.vixid.vsm.utils.ChatUtils
import dev.vixid.vsm.utils.RenderUtils.drawTextWithShadow
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.launch
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.InputUtil

object SpotifyDisplay : Overlay() {

    private val config: SpotifyConfig get() = VSM.config.instance.spotifyConfig

    private var songName: String = "§cCannot detect song name!"
    private var totalTicks = 0

    fun initialise() {
        OverlayPositions.addOverlay(this)

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
        KeyPress.PRESSED.register(this::onKeyPress)
        MousePress.PRESSED.register(this::onMousePress)
    }

    override fun renderOverlay(context: DrawContext) {
        config.overlayPosition.drawTextWithShadow(context, songName)
    }

    private fun onTick(client: MinecraftClient) {
        if (!isEnabled(client)) return

        totalTicks++

        if (totalTicks % 20 == 0) {
            VSM.coroutineScope.launch {
                songName = getSongFromSpotifyProcess()
            }
        }
    }

    private fun onKeyPress(key: Int, scancode: Int, action: Int, modifiers: Int) {
        if (MinecraftClient.getInstance().currentScreen is ChatScreen) return
        val translationKey = InputUtil.fromKeyCode(key, 0).translationKey
        onPress(translationKey)
    }

    private fun onMousePress(button: Int, actions: Int, mods: Int) {
        val translationKey = InputUtil.Type.MOUSE.createFromCode(button).translationKey
        onPress(translationKey)
    }

    private fun onPress(key: String) {
        when (key) {
            config.skipForwardKey -> ControlUtils.postSkipSong()
            config.skipBackwardKey -> ControlUtils.postPreviousSong()
            config.playPauseKey -> ControlUtils.postPlaySong()
        }
    }

    private fun getSongFromSpotifyProcess() : String {
        return try {
            val process = Runtime.getRuntime().exec("powershell.exe (ps Spotify | ? MainWindowTitle | select MainWindowTitle).MainWindowTitle")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line = reader.readLine()
            reader.close()
            if (line == "Spotify") {
                line = songName
            } else if (line != null) {
                line = "§a${line}".replace(" - ", " §f-§b ")
                if (!line.equals(songName)) {
                    ChatUtils.chat("§bVSM §f> $line")
                }
            }
            line ?: "§cCannot detect song name!"
        } catch (error: Exception) {
            error.printStackTrace()
            "§cCannot detect song name!"
        }
    }

    private fun isEnabled(client: MinecraftClient) = client.world != null && config.enabled
}