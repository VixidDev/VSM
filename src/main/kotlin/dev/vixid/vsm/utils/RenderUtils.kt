package dev.vixid.vsm.utils

import net.minecraft.client.gui.DrawContext

object RenderUtils {

    fun drawBoundingRectangle(drawContext: DrawContext,  rectangle: Rectangle) {
        drawContext.drawHorizontalLine(rectangle.x.toInt(), (rectangle.x + rectangle.width + 2).toInt(), rectangle.y.toInt(), -1)
        drawContext.drawHorizontalLine(rectangle.x.toInt(), (rectangle.x + rectangle.width + 2).toInt(), (rectangle.y + rectangle.height + 2).toInt(), -1)
        drawContext.drawVerticalLine(rectangle.x.toInt(), rectangle.y.toInt(), (rectangle.y + rectangle.height + 2).toInt(), -1)
        drawContext.drawVerticalLine((rectangle.x + rectangle.width + 2).toInt(), rectangle.y.toInt(), (rectangle.y + rectangle.height + 2).toInt(), -1)
    }

}