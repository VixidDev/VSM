package dev.vixid.vsm.jnativehook;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeLibraryLocator;
import com.github.kwhat.jnativehook.NativeSystem;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class VSMLibraryLocator implements NativeLibraryLocator {
    private static final Logger log = Logger.getLogger(GlobalScreen.class.getPackage().getName());

    @Override
    public Iterator<File> getLibraries() {
        List<File> libraries = new ArrayList<File>(1);

        String libName = System.getProperty("jnativehook.lib.name", "JNativeHook");

        // Get the package name for the GlobalScreen.
        String basePackage = GlobalScreen.class.getPackage().getName().replace('.', '/');

        String libNativeArch = NativeSystem.getArchitecture().toString().toLowerCase();
        String libNativeName = System
                .mapLibraryName(libName) // Get what the system "thinks" the library name should be.
                .replaceAll("\\.jnilib$", "\\.dylib"); // Hack for OS X JRE 1.6 and earlier.

        // Resource path for the native library.
        String libResourcePath = "/" + basePackage + "/lib/" +
                NativeSystem.getFamily().toString().toLowerCase() +
                '/' + libNativeArch + '/' + libNativeName;

        File libFile;
        // https://stackoverflow.com/questions/1611357/how-to-make-a-jar-file-that-includes-dll-files
        String tempDirName = "VSM_" + new Date().getTime();
        try {
            InputStream is = GlobalScreen.class.getResourceAsStream(libResourcePath);
            libFile = new File(System.getProperty("java.io.tmpdir") + "/" + tempDirName + libResourcePath);
            System.out.println("Extracting JNativeHook dll to: " + libFile.getAbsolutePath());
            OutputStream os = FileUtils.openOutputStream(libFile);
            IOUtils.copy(is, os);
            is.close();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load required DLL", e);
        }

        if (!libFile.exists()) {
            throw new RuntimeException("Failed to create library file in temp directory: " + libFile.getPath() + "!\n");
        }

        log.fine("Loading library: " + libFile.getPath() + ".\n");
        libraries.add(libFile);

        return libraries.iterator();
    }
}
