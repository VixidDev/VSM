package dev.vixid.vsm.overlays

import dev.vixid.vsm.VSM
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
        val label = getHoveredOverlayData(mouseX, mouseY)
        val width = textRenderer.getWidth(label.first) / 2
        val position = label.second

        if (position.x != -999 && position.y != -999 && button == 0) {
            position.set(mouseX.toInt() - width, mouseY.toInt() - textRenderer.fontHeight / 2)
        }
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