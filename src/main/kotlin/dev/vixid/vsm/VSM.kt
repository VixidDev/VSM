package dev.vixid.vsm

import com.github.kwhat.jnativehook.GlobalScreen
import dev.vixid.vsm.config.VSMConfig
import dev.vixid.vsm.config.core.VSMGsonMapper
import dev.vixid.vsm.config.core.annotations.ConfigEditorKeybind
import dev.vixid.vsm.config.core.gui.GuiOptionEditorKeybind
import dev.vixid.vsm.overlays.OverlayPositions
import dev.vixid.vsm.features.spotify.SpotifyDisplay
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import java.io.File
import net.fabricmc.api.ClientModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object VSM : ClientModInitializer {
    val logger: Logger = LoggerFactory.getLogger("vsm")

	val config = ManagedConfig.create(File("config/vsm/config.json"), VSMConfig::class.java) {
		// Overwrite the GsonMapper to our own, so we can exclude fields without Expose annotation
		mapper = VSMGsonMapper(this.clazz)

		// Add in Keybind gui editor
		this.customProcessor(ConfigEditorKeybind::class.java) {
			processedOption, keybind ->
			GuiOptionEditorKeybind(processedOption, keybind.defaultKey)
		}
	}

	override fun onInitializeClient() {
		GlobalScreen.registerNativeHook()

		config.instance.initialise()
		SpotifyDisplay.initialise()

		// Initialise after all overlay objects have been initialised
		OverlayPositions.initialise()
	}
}