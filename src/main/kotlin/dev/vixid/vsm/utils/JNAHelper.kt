@file:Suppress("FunctionName", "LocalVariableName")

package dev.vixid.vsm.utils

import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.Pointer
import com.sun.jna.platform.unix.X11.Display
import com.sun.jna.platform.unix.X11.Window
import com.sun.jna.platform.unix.X11.WindowByReference
import com.sun.jna.platform.unix.X11.XTextProperty
import com.sun.jna.platform.win32.WinDef.DWORD
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinNT.HANDLE
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions
import java.util.Collections

object JNAHelper {

    private val INSTANCE: NativeJNAHelper by lazy {
        if (Platform.isWindows()) {
            WindowsJNAHelper
        } else if (Platform.isX11()) {
            X11JNAHelper
        } else if (Platform.isMac()) {
            MacJNAHelper
        } else {
            val os = System.getProperty("os.name")
            throw UnsupportedOperationException("No support for $os")
        }
    }

    private object WindowsJNAHelper : NativeJNAHelper() {
        interface User32 : StdCallLibrary {
            companion object {
                val INSTANCE = Native.loadLibrary("user32", User32::class.java, W32APIOptions.DEFAULT_OPTIONS) as User32
            }

            fun EnumWindows(lpEnumFunc: WNDENUMPROC?, arg: Pointer?): Boolean
            fun GetWindowThreadProcessId(hWnd: HWND?, lpdwProcessId: IntByReference?): Int
            fun GetWindowTextW(hwnd: HWND?, lpString: CharArray?, nMaxCount: Int): Int
        }

        interface Kernel32 : StdCallLibrary {
            companion object {
                val INSTANCE = Native.loadLibrary("Kernel32", Kernel32::class.java, W32APIOptions.DEFAULT_OPTIONS) as Kernel32
            }

            fun OpenProcess(fdwAccess: Int, fInherit: Boolean, IDProcess: Int): HANDLE?
        }

        interface Psapi : StdCallLibrary {
            companion object {
                val INSTANCE = Native.loadLibrary("Psapi", Psapi::class.java, W32APIOptions.DEFAULT_OPTIONS) as Psapi
            }

            fun GetModuleBaseNameW(hProcess: Pointer, hModule: Pointer?, lpBaseName: CharArray, nSize: Int): DWORD
        }

        private val ignoredStrings = arrayOf("Default IME", "MSCTFIME UI", "GDI+ Window")

        override fun getProcessWindowTitle(process: String): String {
            val PROCESS_VM_READ = 0x0010
            val PROCESS_QUERY_INFORMATION = 0x0400

            val user32 = User32.INSTANCE
            val kernel32 = Kernel32.INSTANCE
            val psapi = Psapi.INSTANCE

            var title = ""

            user32.EnumWindows({ hwnd: HWND, _: Pointer? ->
                val pid = IntByReference()
                user32.GetWindowThreadProcessId(hwnd, pid)
                val processHandle = kernel32.OpenProcess(PROCESS_VM_READ or PROCESS_QUERY_INFORMATION, false, pid.value)

                if (processHandle != null) {
                    val moduleNameBuf = CharArray(512)
                    psapi.GetModuleBaseNameW(processHandle.pointer, Pointer.NULL, moduleNameBuf, 512)
                    val moduleName = Native.toString(moduleNameBuf)

                    if (moduleName.equals(process)) {
                        val titleBuf = CharArray(512)
                        user32.GetWindowTextW(hwnd, titleBuf, 512)
                        val windowTitle = Native.toString(titleBuf)

                        if (windowTitle.isNotEmpty() && !ignore(windowTitle)) {
                            title = windowTitle
                        }
                    }
                }
                true
            }, null)

            return title
        }

        private fun ignore(title: String): Boolean {
            for (string in ignoredStrings) {
                if (title.contains(string)) return true
            }
            return false
        }
    }

    private object X11JNAHelper : NativeJNAHelper() {
        interface X11 : StdCallLibrary {
            companion object {
                val INSTANCE = Native.loadLibrary("X11", X11::class.java) as X11
            }

            fun XOpenDisplay(name: String?): Display
            fun XDefaultRootWindow(display: Display): Window
            fun XQueryTree(display: Display, window: Window, root: WindowByReference, parent: WindowByReference, children: PointerByReference, childCount: IntByReference): Int
            fun XGetWMName(display: Display, window: Window, textPropertyReturn: XTextProperty): Int
            fun XFree(data: Pointer): Int
        }

        override fun getProcessWindowTitle(process: String): String {
            throw UnsupportedOperationException("This function has not been implemented for this platform yet!")

            val x11 = X11.INSTANCE

            val display = x11.XOpenDisplay(null)
            val root = x11.XDefaultRootWindow(display)

            recurse(x11, display, root, 0, process)
        }

        /**
         * https://stackoverflow.com/a/39020214
         */
        private fun recurse(x11: X11, display: Display, root: Window, depth: Int, process: String) {
            val windowRef = WindowByReference()
            val parentRef = WindowByReference()
            val childrenRef = PointerByReference()
            val childCountRef = IntByReference()

            x11.XQueryTree(display, root, windowRef, parentRef, childrenRef, childCountRef)
            if (childrenRef.value == null) {
                return
            }

            val ids: LongArray

            when (Native.LONG_SIZE) {
                Long.SIZE_BYTES -> {
                    ids = childrenRef.value.getLongArray(0, childCountRef.value)
                }
                Int.SIZE_BYTES -> {
                    val intIds = childrenRef.value.getIntArray(0, childCountRef.value)
                    ids = LongArray(intIds.size)
                    for (i in intIds.indices) {
                        ids[i] = intIds[i].toLong()
                    }
                }
                else -> throw IllegalStateException("Unexpected size for Native.LONG_SIZE " + Native.LONG_SIZE)
            }

            for (id in ids) {
                if (id == 0L) continue

                val window = Window(id)
                val name = XTextProperty()

                x11.XGetWMName(display, window, name)
                println(Collections.nCopies(depth, "  ").joinToString("") + name.value)
                x11.XFree(name.pointer)

                recurse(x11, display, window, depth + 1, process)
            }
        }
    }

    private object MacJNAHelper : NativeJNAHelper() {
        override fun getProcessWindowTitle(process: String): String {
            throw UnsupportedOperationException("This function has not been implemented for this platform yet!")
        }
    }

    private abstract class NativeJNAHelper {
        abstract fun getProcessWindowTitle(process: String): String
    }

    fun getProcessWindowTitle(process: String): String = INSTANCE.getProcessWindowTitle(process)
}