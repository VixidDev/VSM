package dev.vixid.vsm.overlays

import dev.vixid.vsm.VSM
import dev.vixid.vsm.config.core.Position
import java.util.UUID
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object OverlayPositions {

    private val overlays: MutableList<Overlay> = mutableListOf()
    private val editorOverlays: MutableMap<UUID, Pair<String, Position>> = mutableMapOf()

    fun initialise() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Post) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return
        this.renderOverlays()
    }

    fun addOverlay(overlay: Overlay) {
        this.overlays.add(overlay)
    }

    fun add(uuid: UUID, pair: Pair<String, Position>) {
        this.editorOverlays.putIfAbsent(uuid, pair)
    }

    private fun renderOverlays() {
        if (Minecraft.getMinecraft().currentScreen is PositionEditor) return

        for (overlay: Overlay in overlays) {
            overlay.renderOverlay()
        }
    }

    fun openPositionEditor() {
        VSM.screenToOpen = PositionEditor(editorOverlays)
    }

}