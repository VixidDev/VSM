package dev.vixid.vsm.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText

object ChatUtils {
    private val mc: Minecraft = Minecraft.getMinecraft()

    fun chat(message: String) {
        if (mc.thePlayer == null) return
        mc.thePlayer.addChatComponentMessage(ChatComponentText(message))
    }
}