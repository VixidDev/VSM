package dev.vixid.vsm.events

import dev.vixid.vsm.events.MousePress.Pressed
import dev.vixid.vsm.events.MousePress.Released
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

class MousePress {
    companion object {
        @JvmStatic
        var PRESSED: Event<Pressed> =
            EventFactory.createArrayBacked(Pressed::class.java) { listeners: Array<Pressed> ->
                Pressed { button: Int, actions: Int, mods: Int ->
                    for (listener in listeners) {
                        listener.onPressed(button, actions, mods)
                    }
                }
            }

        @JvmStatic
        var RELEASED: Event<Released> =
            EventFactory.createArrayBacked(Released::class.java) { listeners: Array<Released> ->
                Released { button: Int, actions: Int, mods: Int ->
                    for (listener in listeners) {
                        listener.onReleased(button, actions, mods)
                    }
                }
            }
    }

    fun interface Pressed {
        fun onPressed(button: Int, actions: Int, mods: Int)
    }

    fun interface Released {
        fun onReleased(button: Int, actions: Int, mods: Int)
    }
}