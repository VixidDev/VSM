package dev.vixid.vsm.events

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

class MousePress {
    companion object {
        @JvmStatic
        var PRESSED: Event<Pressed> =
            EventFactory.createArrayBacked(Pressed::class.java) { listeners: Array<Pressed> ->
                Pressed { button: Int, action: Int, modifiers: Int ->
                    for (listener in listeners) {
                        listener.onPressed(button, action, modifiers)
                    }
                }
            }

        @JvmStatic
        var RELEASED: Event<Released> =
            EventFactory.createArrayBacked(Released::class.java) { listeners: Array<Released> ->
                Released { button: Int, action: Int, modifiers: Int ->
                    for (listener in listeners) {
                        listener.onReleased(button, action, modifiers)
                    }
                }
            }
    }

    fun interface Pressed {
        fun onPressed(button: Int, action: Int, modifiers: Int)
    }

    fun interface Released {
        fun onReleased(button: Int, action: Int, modifiers: Int)
    }
}