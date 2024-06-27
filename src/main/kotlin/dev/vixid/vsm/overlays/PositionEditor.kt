package dev.vixid.vsm.overlays

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.utils.RenderUtils
import java.util.UUID
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class PositionEditor(private val editorOverlays: Map<UUID, Pair<String, Position>>) : Screen(Text.literal("Position Editor")) {

    private var isDraggingOverlay = false
    private var draggedOverlayData: Pair<String, Position> = Pair("", Position(-999, -999))

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderDarkening(context)

        renderOverlays(context)
    }

    private fun renderOverlays(context: DrawContext) {
        RenderUtils.drawCenteredTextWithShadow(context, "VSM Overlay Position Editor", this.width / 2, 20)

        for (entry in editorOverlays) {
            RenderUtils.drawTextWithShadowBoxed(context, entry.value.first, entry.value.second)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val label = getHoveredOverlayData(mouseX, mouseY)
        val position = label.second

        if (position.x != -999 && position.y != -999 && button == 0) {
            isDraggingOverlay = true
            draggedOverlayData = label
        }
        return true
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        val halfWidth = textRenderer.getWidth(draggedOverlayData.first) / 2
        val halfHeight = textRenderer.fontHeight / 2
        val position = draggedOverlayData.second

        if (isDraggingOverlay && position.x != -999 && position.y != -999 && button == 0) {
            position.set(mouseX.toInt() - halfWidth, mouseY.toInt() - halfHeight)
        }
        return true
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        isDraggingOverlay = false
        draggedOverlayData = Pair("", Position(-999, -999))
        return true
    }

    private fun getHoveredOverlayData(mouseX: Double, mouseY: Double) : Pair<String, Position> {
        for (overlay in editorOverlays) {
            val string = overlay.value.first
            val position = overlay.value.second
            val bounds = RenderUtils.getRectangleBoundsForString(string, position)
            if (bounds.containsPosition(Position(mouseX, mouseY))) return overlay.value
        }
        return Pair("", Position(-999, -999))
    }

    override fun close() {
        super.close()
        VSM.config.instance.saveNow()
    }
}