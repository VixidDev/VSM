package dev.vixid.vsm.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.vixid.vsm.VSM
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object ConfigManager {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()

    val configDirectory = File("config/vsm")
    val configFile by lazy { File(configDirectory, "config.json") }

    lateinit var configProcessor: MoulConfigProcessor<VSMConfig>
    lateinit var config: VSMConfig

    fun firstLoad() {
        configDirectory.mkdirs()

        if (!configFile.exists()) {
            if (!configFile.createNewFile()) {
                throw Error("Could not create config file")
            } else {
                BufferedWriter(OutputStreamWriter(FileOutputStream(configFile), StandardCharsets.UTF_8)).use { writer ->
                    writer.write(gson.toJson(VSMConfig()))
                }
            }
        }

        try {
            BufferedReader(InputStreamReader(FileInputStream(configFile), StandardCharsets.UTF_8)).use { reader ->
                config = gson.fromJson(reader.readText(), VSMConfig::class.java)
            }
        } catch (error: Exception) {
            error.printStackTrace()
        }

        configProcessor = MoulConfigProcessor(config)
        BuiltinMoulConfigGuis.addProcessors(configProcessor)
        ConfigProcessorDriver(configProcessor).processConfig(config)
    }

    fun saveConfig() {
        try {
            configFile.parentFile.mkdirs()
            val unit = configFile.parentFile.resolve("config.json.write")
            unit.createNewFile()
            BufferedWriter(OutputStreamWriter(FileOutputStream(unit), StandardCharsets.UTF_8)).use { writer ->
                writer.write(gson.toJson(VSM.config))
            }
            try {
                Files.move(
                    unit.toPath(),
                    configFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
                )
            } catch (e: AccessDeniedException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}