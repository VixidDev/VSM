package dev.vixid.vsm.features.spotify

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent

object ControlUtils {

    fun postSkipSong() {
        GlobalScreen.postNativeEvent(
            NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_PRESSED,
            0,
            176,
            NativeKeyEvent.VC_MEDIA_NEXT,
            NativeKeyEvent.CHAR_UNDEFINED
        ))
    }

    fun postPreviousSong() {
        GlobalScreen.postNativeEvent(NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_PRESSED,
            0,
            176,
            NativeKeyEvent.VC_MEDIA_PREVIOUS,
            NativeKeyEvent.CHAR_UNDEFINED
        ))
    }

    fun postPlaySong() {
        GlobalScreen.postNativeEvent(NativeKeyEvent(
            NativeKeyEvent.NATIVE_KEY_PRESSED,
            0,
            176,
            NativeKeyEvent.VC_MEDIA_PLAY,
            NativeKeyEvent.CHAR_UNDEFINED
        ))
    }
}