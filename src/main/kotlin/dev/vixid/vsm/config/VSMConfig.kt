package dev.vixid.vsm.config

import com.google.gson.annotations.Expose
import com.mojang.brigadier.CommandDispatcher
import dev.vixid.vsm.VSM
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess

class VSMConfig : Config() {

    override fun getTitle(): String {
        return "§bVixid's Skyblock Mod Config"
    }

    fun initialise() {
        ClientCommandRegistrationCallback.EVENT.register(this::configCommand)
    }

    private fun configCommand(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(literal("vsm")
            .executes {
                MinecraftClient.getInstance().send { VSM.config.openConfigGui() }
                0
            }
            .then(literal("saveconfig").executes {
                VSM.config.instance.saveNow()
                0
            }))
    }

    @Expose
    @Category(name = "Spotify", desc = "Settings for local Spotify control")
    var spotifyConfig: SpotifyConfig = SpotifyConfig()
}