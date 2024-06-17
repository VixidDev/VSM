package dev.vixid.vsm.events

import dev.vixid.vsm.events.KeyPress.Pressed
import dev.vixid.vsm.events.KeyPress.Released
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

class KeyPress {
    companion object {
        @JvmStatic
        var PRESSED: Event<Pressed> =
            EventFactory.createArrayBacked(Pressed::class.java) { listeners: Array<Pressed> ->
                Pressed { key: Int, scancode: Int, action: Int, modifiers: Int ->
                    for (listener in listeners) {
                        listener.onPressed(key, scancode, action, modifiers)
                    }
                }
            }

        @JvmStatic
        var RELEASED: Event<Released> =
            EventFactory.createArrayBacked(Released::class.java) { listeners: Array<Released> ->
                Released { key: Int, scancode: Int, action: Int, modifiers: Int ->
                    for (listener in listeners) {
                        listener.onReleased(key, scancode, action, modifiers)
                    }
                }
            }
    }

    fun interface Pressed {
        fun onPressed(key: Int, scancode: Int, action: Int, modifiers: Int)
    }

    fun interface Released {
        fun onReleased(key: Int, scancode: Int, action: Int, modifiers: Int)
    }
}