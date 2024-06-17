package dev.vixid.vsm.overlays

import dev.vixid.vsm.config.core.Position
import dev.vixid.vsm.utils.RenderUtils
import java.util.UUID
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class PositionEditor(private val editorOverlays: Map<UUID, Pair<String, Position>>) : Screen(Text.literal("Position Editor")) {

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

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        val position = mouseIsOverAnOverlay(mouseX, mouseY)
        if (position.x != -1 && position.y != -1 && button == 0) {
            position.add(deltaX.toInt(), deltaY.toInt())
        }
        return true
    }

    private fun mouseIsOverAnOverlay(mouseX: Double, mouseY: Double) : Position {
        for (overlay in editorOverlays) {
            val string = overlay.value.first
            val position = overlay.value.second
            val bounds = RenderUtils.getRectangleBoundsForString(string, position)
            if (bounds.containsPosition(Position(mouseX, mouseY))) return overlay.value.second
        }
        return Position(-1, -1)
    }
}