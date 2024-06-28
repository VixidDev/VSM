package dev.vixid.vsm.overlays

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.utils.RenderUtils
import java.util.UUID
import net.minecraft.client.gui.GuiScreen

class PositionEditor(private val editorOverlays: MutableMap<UUID, Pair<String, Position>>) : GuiScreen() {

    private var isDraggingOverlay = false
    private var draggedOverlayData: Pair<String, Position> = Pair("", Position(-999, -999))

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()

        renderOverlays()
    }

    private fun renderOverlays() {
        RenderUtils.drawCenteredTextWithShadow("VSM Overlay Position Editor", this.width / 2, 10)

        for (entry in editorOverlays) {
            RenderUtils.drawTextWithShadowBoxed(entry.value.first, entry.value.second)
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val label = getHoveredOverlayData(mouseX, mouseY)
        val position = label.second

        if (position.x != -999 && position.y != -999 && mouseButton == 0) {
            isDraggingOverlay = true
            draggedOverlayData = label
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        val halfWidth = fontRendererObj.getStringWidth(draggedOverlayData.first) / 2
        val halfHeight = fontRendererObj.FONT_HEIGHT / 2
        val position = draggedOverlayData.second

        if (isDraggingOverlay && position.x != -999 && position.y != -999 && clickedMouseButton == 0) {
            position.set(mouseX - halfWidth, mouseY - halfHeight)
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        isDraggingOverlay = false
        draggedOverlayData = Pair("", Position(-999, -999))
    }

    private fun getHoveredOverlayData(mouseX: Int, mouseY: Int) : Pair<String, Position> {
        for (overlay in editorOverlays) {
            val string = overlay.value.first
            val position = overlay.value.second
            val bounds = RenderUtils.getRectangleBoundsForString(string, position)
            if (bounds.containsPosition(Position(mouseX, mouseY))) return overlay.value
        }
        return Pair("", Position(-999, -999))
    }

    override fun onGuiClosed() {
        VSM.config.instance.saveNow()
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}