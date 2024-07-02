@file:Suppress("FunctionName", "LocalVariableName")

package dev.vixid.vsm.utils

import com.sun.jna.Native
import com.sun.jna.Platform
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary
import com.sun.jna.win32.W32APIOptions

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
                val INSTANCE = Native.load("user32", User32::class.java, W32APIOptions.DEFAULT_OPTIONS) as User32
            }

            fun EnumWindows(lpEnumFunc: WinUser.WNDENUMPROC?, arg: Pointer?): Boolean
            fun GetWindowThreadProcessId(hWnd: WinDef.HWND?, lpdwProcessId: IntByReference?): Int
            fun GetWindowTextW(hwnd: WinDef.HWND?, lpString: CharArray?, nMaxCount: Int): Int
        }

        interface Kernel32 : StdCallLibrary {
            companion object {
                val INSTANCE = Native.load("Kernel32", Kernel32::class.java, W32APIOptions.DEFAULT_OPTIONS) as Kernel32
            }

            fun OpenProcess(fdwAccess: Int, fInherit: Boolean, IDProcess: Int): WinNT.HANDLE?
        }

        interface Psapi : StdCallLibrary {
            companion object {
                val INSTANCE = Native.load("Psapi", Psapi::class.java, W32APIOptions.DEFAULT_OPTIONS) as Psapi
            }

            fun GetModuleBaseNameW(hProcess: Pointer, hModule: Pointer?, lpBaseName: CharArray, nSize: Int): WinDef.DWORD
        }

        private val ignoredStrings = arrayOf("Default IME", "MSCTFIME UI", "GDI+ Window")

        override fun getProcessWindowTitle(process: String): String {
            val PROCESS_VM_READ = 0x0010
            val PROCESS_QUERY_INFORMATION = 0x0400

            val user32 = User32.INSTANCE
            val kernel32 = Kernel32.INSTANCE
            val psapi = Psapi.INSTANCE

            var title = ""

            user32.EnumWindows({ hwnd: WinDef.HWND, _: Pointer? ->
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
        override fun getProcessWindowTitle(process: String): String {
            throw UnsupportedOperationException("This function has not been implemented for this platform yet!")
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