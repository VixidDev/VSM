package dev.vixid.vsm.utils

import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.overlays.OverlayPositions
import java.awt.Color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext

object RenderUtils {
    private val textRenderer: TextRenderer by lazy { MinecraftClient.getInstance().textRenderer }

    fun drawCenteredTextWithShadow(context: DrawContext, string: String, centerX: Int, y: Int) {
        drawCenteredTextWithShadow(context, string, centerX, y, Color.WHITE.rgb)
    }

    fun drawCenteredTextWithShadow(context: DrawContext, string: String, centerX: Int, y: Int, color: Int) {
        context.drawCenteredTextWithShadow(textRenderer, string, centerX, y, color)
    }

    fun drawTextWithShadow(context: DrawContext, string: String, position: Position) {
        drawTextWithShadow(context, string, position.x, position.y)
    }

    fun drawTextWithShadow(context: DrawContext, string: String, x: Int, y: Int) {
        drawTextWithShadow(context, string, x, y, Color.WHITE.rgb)
    }

    fun drawTextWithShadow(context: DrawContext, string: String, x: Int, y: Int, color: Int) {
        context.drawTextWithShadow(textRenderer, string, x, y, color)
    }

    fun drawTextWithShadowBoxed(context: DrawContext, string: String, position: Position) {
        drawTextWithShadow(context, string, position)
        val stringWidth = textRenderer.getWidth(string)
        val stringHeight = textRenderer.fontHeight
        drawRectangleOutline(context, position, stringWidth, stringHeight)
    }

    fun drawRectangleOutline(context: DrawContext, position: Position, width: Int, height: Int) {
        context.drawHorizontalLine(position.x - 3, position.x + width + 2, position.y - 3, Color.WHITE.rgb)
        context.drawHorizontalLine(position.x - 3, position.x + width + 2, position.y + height + 1, Color.WHITE.rgb)
        context.drawVerticalLine(position.x - 3, position.y - 3, position.y + height + 1, Color.WHITE.rgb)
        context.drawVerticalLine(position.x + width + 2, position.y - 3, position.y + height + 1, Color.WHITE.rgb)
    }

    fun getRectangleBoundsForString(string: String, position: Position) : Vec4f {
        val width = textRenderer.getWidth(string)
        val height = textRenderer.fontHeight

        val bounds = Vec4f()
        bounds.x = position.x - 3f
        bounds.y = position.y - 3f
        bounds.z = position.x + width + 2f
        bounds.w = position.y + height + 1f
        return bounds
    }

    fun Position.drawTextWithShadow(context: DrawContext, string: String) {
        drawTextWithShadow(context, string, this)
        OverlayPositions.add(this.uuid, Pair(string, this))
    }
}