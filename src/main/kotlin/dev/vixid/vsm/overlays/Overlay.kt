package dev.vixid.vsm.overlays

import net.minecraft.client.gui.DrawContext

abstract class Overlay {

    abstract fun renderOverlay(context: DrawContext)
}