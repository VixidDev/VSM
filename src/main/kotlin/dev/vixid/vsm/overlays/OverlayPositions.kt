package dev.vixid.vsm.overlays

import dev.vixid.vsm.config.core.Position
import java.util.UUID
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

object OverlayPositions {

    private val overlays: MutableList<Overlay> = mutableListOf()
    private val editorOverlays: MutableMap<UUID, Pair<String, Position>> = mutableMapOf()

    fun initialise() {
        HudRenderCallback.EVENT.register(this::renderOverlays)
    }

    fun addOverlay(overlay: Overlay) {
        this.overlays.add(overlay)
    }

    fun getOverlays() : List<Overlay> = overlays

    fun add(uuid: UUID, pair: Pair<String, Position>) {
        this.editorOverlays.putIfAbsent(uuid, pair)
    }

    fun getEditorOverlays() : Map<UUID, Pair<String, Position>> = editorOverlays

    private fun renderOverlays(context: DrawContext, tickDelta: Float) {
        if (MinecraftClient.getInstance().currentScreen is PositionEditor) return

        for (overlay: Overlay in overlays) {
            overlay.renderOverlay(context)
        }
    }

    fun openPositionEditor() {
        MinecraftClient.getInstance().send { MinecraftClient.getInstance().setScreen(PositionEditor(editorOverlays)) }
    }
}