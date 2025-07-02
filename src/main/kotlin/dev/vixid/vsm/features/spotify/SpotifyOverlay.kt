package dev.vixid.vsm.features.spotify

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.SpotifyConfig
import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.events.KeyPress
import dev.vixid.vsm.events.MousePress
import dev.vixid.vsm.overlays.Overlay
import dev.vixid.vsm.overlays.OverlayPositions
import dev.vixid.vsm.utils.ChatUtils
import dev.vixid.vsm.utils.JNAHelper
import dev.vixid.vsm.utils.Rectangle
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen

object SpotifyOverlay : Overlay() {

    private val config: SpotifyConfig get() = VSM.config.instance.spotifyConfig

    override val position: Position
        get() = config.position

    private var songName: String = "§cCannot detect song name!"
    private var totalTicks = 0

    fun initialise() {
        OverlayPositions.addOverlay(this)
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
        KeyPress.PRESSED.register(this::onKeyPress)
        MousePress.PRESSED.register(this::onMousePress)
    }

    private fun onTick(client: MinecraftClient) {
        if (!isEnabled()) return

        totalTicks++
        if (totalTicks < 20) return

        var windowTitle = JNAHelper.getProcessWindowTitle("Spotify.exe")

        if (windowTitle == "Spotify") {
            windowTitle = songName
        } else if (windowTitle.isNotEmpty()) {
            windowTitle = "§a$windowTitle".replace(" - ", " §f-§b ")

            if (songName != windowTitle) ChatUtils.chat("§bVSM §f> $windowTitle")
        }

        songName = windowTitle
        totalTicks = 0
    }

    override fun renderOverlay(drawContext: DrawContext) {
        if (!isEnabled()) return

        drawContext.drawTextWithShadow(textRenderer, songName, position.x, position.y, -1)
    }

    private fun onKeyPress(key: Int, scancode: Int, action: Int, mods: Int) {
        onPress(key)
    }

    private fun onMousePress(button: Int, action: Int, mods: Int) {
        onPress(button)
    }

    private fun onPress(key: Int) {
        if (!isEnabled() || MinecraftClient.getInstance().currentScreen != null) return

        when (key) {
            config.skipForwardKey -> ControlUtils.postSkipSong()
            config.skipBackwardKey -> ControlUtils.postPreviousSong()
            config.playPauseKey -> ControlUtils.postPlaySong()
        }
    }

    override fun getBounds(): Rectangle {
        val textRenderer = MinecraftClient.getInstance().textRenderer
        val width = textRenderer.getWidth(songName)
        val height = textRenderer.fontHeight

        return Rectangle(
            this.position.x - 3f,
            this.position.y - 3f,
            width + 2f,
            height + 1f)
    }

    override fun isEnabled() = config.enabled
}