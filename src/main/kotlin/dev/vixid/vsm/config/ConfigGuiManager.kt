package dev.vixid.vsm.config

import dev.vixid.vsm.VSM
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor

object ConfigGuiManager {
    private var editor: MoulConfigEditor<VSMConfig>? = null

    private fun getEditorInstance() = editor ?: MoulConfigEditor(ConfigManager.configProcessor).also { editor = it }

    fun openConfig() {
        val editor = getEditorInstance()
        VSM.screenToOpen = GuiScreenElementWrapper(editor)
    }
}