package dev.vixid.vsm.overlays

import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.Identifier

object OverlayPositions {

    private val overlays: MutableList<Overlay> = mutableListOf()

    fun initialise() {
        HudLayerRegistrationCallback.EVENT.register { layeredDrawer -> layeredDrawer.attachLayerBefore(IdentifiedLayer.DEBUG, Identifier.of("vsm", "vsm_overlays_layer"), this::render) }
    }

    fun addOverlay(overlay: Overlay) {
        overlays.add(overlay)
    }

    fun getOverlays() = overlays

    private fun render(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        if (MinecraftClient.getInstance().currentScreen is PositionEditor) return

        for (overlay in overlays) {
            overlay.renderOverlay(drawContext)
        }
    }

    fun openPositionEditor() {
        MinecraftClient.getInstance().send { MinecraftClient.getInstance().setScreen(PositionEditor()) }
    }
}