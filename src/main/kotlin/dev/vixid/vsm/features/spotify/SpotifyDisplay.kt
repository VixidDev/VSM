package dev.vixid.vsm.features.spotify

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.SpotifyConfig
import dev.vixid.vsm.events.KeyPress
import dev.vixid.vsm.events.MousePress
import dev.vixid.vsm.overlays.Overlay
import dev.vixid.vsm.overlays.OverlayPositions
import dev.vixid.vsm.utils.ChatUtils
import dev.vixid.vsm.utils.JNAHelper
import dev.vixid.vsm.utils.RenderUtils.drawTextWithShadow
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

    private fun isEnabled(client: MinecraftClient) = client.world != null && config.enabled
}