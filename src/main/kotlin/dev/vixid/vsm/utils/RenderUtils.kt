package dev.vixid.vsm.utils

import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.overlays.OverlayPositions
import java.awt.Color
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui

object RenderUtils {

    private val fontRenderer = Minecraft.getMinecraft().fontRendererObj

    fun drawCenteredTextWithShadow(string: String, x: Int, y: Int) {
        drawCenteredTextWithShadow(string, x, y, Color.WHITE.rgb)
    }

    fun drawCenteredTextWithShadow(string: String, x: Int, y: Int, color: Int) {
        val halfStringWidth = fontRenderer.getStringWidth(string) / 2
        fontRenderer.drawStringWithShadow(string, x.toFloat() - halfStringWidth, y.toFloat(), color)
    }

    fun drawTextWithShadow(string: String, position: Position) {
        drawTextWithShadow(string, position.x, position.y, Color.WHITE.rgb)
    }

    fun drawTextWithShadow(string: String, x: Int, y: Int, color: Int) {
        fontRenderer.drawStringWithShadow(string, x.toFloat(), y.toFloat(), color)
    }

    fun drawTextWithShadowBoxed(string: String, position: Position) {
        drawTextWithShadow(string, position)
        val stringWidth = fontRenderer.getStringWidth(string)
        val stringHeight = fontRenderer.FONT_HEIGHT
        drawRectangleStringOutline(position, stringWidth, stringHeight)
    }

    fun drawRectangleStringOutline(position: Position, width: Int, height: Int) {
        drawHorizontalLine(position.x - 3, position.x + width + 2, position.y - 3, Color.WHITE.rgb)
        drawHorizontalLine(position.x - 3, position.x + width + 2, position.y + height + 1, Color.WHITE.rgb)
        drawVerticalLine(position.y - 3, position.y + height + 1, position.x - 3, Color.WHITE.rgb)
        drawVerticalLine(position.y - 3, position.y + height + 1, position.x + width + 2, Color.WHITE.rgb)
    }

    fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Int) {
        var inStartX = startX
        var inEndX = endX

        if (endX < startX) {
            val temp = inStartX
            inStartX = inEndX
            inEndX = temp
        }
        Gui.drawRect(inStartX, y, inEndX + 1, y + 1, color)
    }

    fun drawVerticalLine(startY: Int, endY: Int, x: Int, color: Int) {
        var inStartY = startY
        var inEndY = endY

        if (endY < startY) {
            val temp = inStartY
            inStartY = inEndY
            inEndY = temp
        }
        Gui.drawRect(x, inStartY + 1, x + 1, inEndY, color)
    }

    fun getRectangleBoundsForString(string: String, position: Position) : Vec4f {
        val width = fontRenderer.getStringWidth(string)
        val height = fontRenderer.FONT_HEIGHT

        val bounds = Vec4f()
        bounds.x = position.x - 3f
        bounds.y = position.y - 3f
        bounds.z = position.x + width + 2f
        bounds.w = position.y + height + 1f
        return bounds
    }

    fun Position.drawTextWithShadow(string: String) {
        drawTextWithShadow(string, this)
        OverlayPositions.add(this.uuid, Pair(string, this))
    }
}