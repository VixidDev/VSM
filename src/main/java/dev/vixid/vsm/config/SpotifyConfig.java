package dev.vixid.vsm.config;

import com.google.gson.annotations.Expose;
import dev.vixid.vsm.config.core.Position;
import dev.vixid.vsm.config.core.annotations.ConfigEditorKeybind;
import dev.vixid.vsm.overlays.OverlayPositions;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class SpotifyConfig {

    @Expose
    @ConfigOption(name = "Enable", desc = "Enable Spotify Overlay")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    public Position overlayPosition = new Position(10, 10);

    @Expose
    @ConfigOption(name = "Skip Forward", desc = "Key bind to skip forward a song")
    @ConfigEditorKeybind(defaultKey = "key.keyboard.unknown")
    public String skipForwardKey = "key.keyboard.unknown";

    @Expose
    @ConfigOption(name = "Skip Backward", desc = "Key bind to skip backward a song")
    @ConfigEditorKeybind(defaultKey = "key.keyboard.unknown")
    public String skipBackwardKey = "key.keyboard.unknown";

    @Expose
    @ConfigOption(name = "Play / Pause", desc = "Key bind to play / pause a song")
    @ConfigEditorKeybind(defaultKey = "key.keyboard.unknown")
    public String playPauseKey = "key.keyboard.unknown";

    @ConfigOption(name = "Edit Song Location", desc = "Change the position of the song overlay")
    @ConfigEditorButton(buttonText = "Edit")
    public Runnable editPosition = OverlayPositions.INSTANCE::openPositionEditor;
}
