package dev.vixid.vsm.overlays

import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.utils.Rectangle
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext

abstract class Overlay {

    val textRenderer: TextRenderer by lazy { MinecraftClient.getInstance().textRenderer }

    abstract val position: Position

    abstract fun renderOverlay(drawContext: DrawContext)

    abstract fun getBounds(): Rectangle

    abstract fun isEnabled(): Boolean
}