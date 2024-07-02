package dev.vixid.vsm.features.spotify

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent

/**
 * Object to handle sending multimedia key events to the OS.
 *
 * Since GLFW detects these key events but does not map them correctly
 * since it doesn't understand the key value, we need to explicitly tell Minecraft
 * to ignore the next onKey callback immediately after one of these key events
 * are sent to the OS.
 */
object ControlUtils {

    var ignoreKeyCallback = false

    fun postSkipSong() {
        GlobalScreen.postNativeEvent(NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_PRESSED,
            0,
            176,
            NativeKeyEvent.VC_MEDIA_NEXT,
            NativeKeyEvent.CHAR_UNDEFINED
        ))
        ignoreKeyCallback = true
    }

    fun postPreviousSong() {
        GlobalScreen.postNativeEvent(NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_PRESSED,
            0,
            176,
            NativeKeyEvent.VC_MEDIA_PREVIOUS,
            NativeKeyEvent.CHAR_UNDEFINED
        ))
        ignoreKeyCallback = true
    }

    fun postPlaySong() {
        GlobalScreen.postNativeEvent(NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_PRESSED,
            0,
            176,
            NativeKeyEvent.VC_MEDIA_PLAY,
            NativeKeyEvent.CHAR_UNDEFINED
        ))
        ignoreKeyCallback = true
    }

    @Deprecated("Use postPlaySong() instead, since that can pause / play. postPauseSong() can only pause")
    fun postPauseSong() {
        GlobalScreen.postNativeEvent(NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_PRESSED,
            0,
            176,
            NativeKeyEvent.VC_MEDIA_STOP,
            NativeKeyEvent.CHAR_UNDEFINED
        ))
        ignoreKeyCallback = true
    }

}