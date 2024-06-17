package dev.vixid.vsm.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object ChatUtils {
    private val mc = MinecraftClient.getInstance()

    fun chat(string: String) {
        mc.inGameHud.chatHud.addMessage(Text.literal(string))
    }
}