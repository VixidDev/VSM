package dev.vixid.vsm.config

import com.google.gson.annotations.Expose
import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.overlays.OverlayPositions
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import org.lwjgl.glfw.GLFW

class SpotifyConfig {

    @Expose
    @ConfigOption(name = "Enable", desc = "Enable Spotify Overlay")
    @ConfigEditorBoolean
    var enabled = true

    @Expose
    val position: Position = Position(10, 10)

    @Expose
    @ConfigOption(name = "Skip Forward", desc = "Keybind to skip forward a song")
    @ConfigEditorKeybind(defaultKey = GLFW.GLFW_KEY_UNKNOWN)
    var skipForwardKey = GLFW.GLFW_KEY_UNKNOWN

    @Expose
    @ConfigOption(name = "Skip Backward", desc = "Keybind to skip backward a song")
    @ConfigEditorKeybind(defaultKey = GLFW.GLFW_KEY_UNKNOWN)
    var skipBackwardKey = GLFW.GLFW_KEY_UNKNOWN

    @Expose
    @ConfigOption(name = "Play / Pause", desc = "Keybind to play / pause a song")
    @ConfigEditorKeybind(defaultKey = GLFW.GLFW_KEY_UNKNOWN)
    var playPauseKey = GLFW.GLFW_KEY_UNKNOWN

    @ConfigOption(name = "Edit Song Location", desc = "Change the location of the song name on screen")
    @ConfigEditorButton(buttonText = "Edit")
    val editPosition: Runnable = Runnable(OverlayPositions::openPositionEditor)
}