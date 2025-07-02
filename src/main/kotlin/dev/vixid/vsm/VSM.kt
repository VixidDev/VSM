package dev.vixid.vsm

import com.github.kwhat.jnativehook.GlobalScreen
import dev.vixid.vsm.config.VSMConfig
import dev.vixid.vsm.features.spotify.SpotifyOverlay
import dev.vixid.vsm.overlays.OverlayPositions
import io.github.notenoughupdates.moulconfig.managed.GsonMapper
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import java.io.File
import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

object VSM : ClientModInitializer {
    private val logger = LoggerFactory.getLogger("vsm")

	val config = ManagedConfig.create(File("config/vsm/config.json"), VSMConfig::class.java) {
		(mapper as GsonMapper<*>).gsonBuilder.setPrettyPrinting()
	}

	override fun onInitializeClient() {
		GlobalScreen.registerNativeHook()

		config.instance.initialise()
		SpotifyOverlay.initialise()

		OverlayPositions.initialise()
	}
}