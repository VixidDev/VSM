package dev.vixid.vsm.config.features;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import org.lwjgl.input.Keyboard;

public class SpotifyConfig {

    @Expose
    @ConfigOption(name = "Enable", desc = "Enable Spotify Overlay")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    @ConfigOption(name = "Skip Forward", desc = "Key bind to skip forward a song")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_RBRACKET)
    public int skipFowardKey = Keyboard.KEY_RBRACKET;

    @Expose
    @ConfigOption(name = "Skip Backward", desc = "Key bind to skip backward a song")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_LBRACKET)
    public int skipBackwardKey = Keyboard.KEY_LBRACKET;

    @Expose
    @ConfigOption(name = "Play / Pause", desc = "Key bind to play / pause a song")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_EQUALS)
    public int playPauseKey = Keyboard.KEY_EQUALS;
}
