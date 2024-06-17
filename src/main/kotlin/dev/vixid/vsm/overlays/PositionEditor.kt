package dev.vixid.vsm.overlays

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.utils.RenderUtils
import java.util.UUID
import net.minecraft.client.gui.GuiScreen

class PositionEditor(private val editorOverlays: MutableMap<UUID, Pair<String, Position>>) : GuiScreen() {

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

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        val label = getHoveredOverlayData(mouseX, mouseY)
        val halfWidth = fontRendererObj.getStringWidth(label.first) / 2
        val halfHeight = fontRendererObj.FONT_HEIGHT / 2
        val position = label.second

        if (position.x != -999 && position.y != -999 && clickedMouseButton == 0) {
            position.set(mouseX - halfWidth, mouseY - halfHeight)
        }
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
        VSM.config.saveNow()
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }
}