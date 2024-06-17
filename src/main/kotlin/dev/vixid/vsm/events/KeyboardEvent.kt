package dev.vixid.vsm.events

import net.minecraftforge.fml.common.eventhandler.Event
import org.lwjgl.input.Keyboard

class KeyboardEvent(
    val key: Int = if (Keyboard.getEventKey() == 0) -1 else Keyboard.getEventKey(),
    val keystate: Boolean = Keyboard.getEventKeyState(),
    val isRepeat: Boolean = Keyboard.isRepeatEvent()
) : Event() {}