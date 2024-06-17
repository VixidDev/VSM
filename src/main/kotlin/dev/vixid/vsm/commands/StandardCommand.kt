package dev.vixid.vsm.commands

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

/**
 * Command structure based on [Skyhanni](https://github.com/hannibal002/Skyhanni/blob/beta/src/main/java/at/hannibal2/skyhanni/config/commands/SimpleCommand.kt)'s
 * commands
 */
class StandardCommand(private val commandName: String, private val runnable: CommandRunnable) : CommandBase() {

    abstract class CommandRunnable {
        abstract fun processCommand(sender: ICommandSender?, args: Array<String>?)
    }

    override fun getCommandName() = commandName

    override fun getCommandUsage(sender: ICommandSender?) = "/$commandName"

    override fun processCommand(sender: ICommandSender?, args: Array<String>?) {
        try {
            runnable.processCommand(sender, args)
        } catch (error: Throwable) {
            error.printStackTrace()
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?) = true
}