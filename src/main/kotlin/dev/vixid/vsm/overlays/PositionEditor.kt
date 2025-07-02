package dev.vixid.vsm.overlays

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.utils.Rectangle
import dev.vixid.vsm.utils.RenderUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class PositionEditor : Screen(Text.literal("Overlay Position Editor")) {

    private var isDraggingOverlay = false
    private var draggedOverlayData: Pair<Rectangle, Position> = Pair(Rectangle(), Position(-999, -999))

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderDarkening(drawContext)

        this.renderOverlays(drawContext)
    }

    private fun renderOverlays(drawContext: DrawContext) {
        val overlays = OverlayPositions.getOverlays()

        drawContext.drawCenteredTextWithShadow(textRenderer, "VSM Overlay Position Editor", this.width / 2, 20, -1)

        for (overlay in overlays) {
            RenderUtils.drawBoundingRectangle(drawContext, overlay.getBounds())
            overlay.renderOverlay(drawContext)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        val overlayPos = getHoveredOverlayPos(mouseX, mouseY)

        if (overlayPos != null && mouseButton == 0) {
            isDraggingOverlay = true
            draggedOverlayData = overlayPos
        }

        return true
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (isDraggingOverlay && draggedOverlayData.second != Position(-999, -999)) {
            val halfWidth = draggedOverlayData.first.width / 2f
            val halfHeight = textRenderer.fontHeight / 2f

            draggedOverlayData.second.set(mouseX - halfWidth, mouseY - halfHeight)
        }

        return true
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        isDraggingOverlay = false
        draggedOverlayData = Pair(Rectangle(), Position(-999, -999))
        return true
    }

    private fun getHoveredOverlayPos(mouseX: Double, mouseY: Double): Pair<Rectangle, Position>? {
        val overlays = OverlayPositions.getOverlays()
        for (overlay in overlays) {
            val bounds = overlay.getBounds()
            if (bounds.containsPosition(Position(mouseX, mouseY))) return Pair(bounds, overlay.position)
        }
        return null
    }

    override fun close() {
        super.close()
        VSM.config.instance.saveNow()
    }

}