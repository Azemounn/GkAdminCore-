package com.gkadmincore.commands

import com.gameknight.admincore.utils.MessagesUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin


class HelpCommand(private val plugin: JavaPlugin) : CommandExecutor {

    private val messagesUtils = MessagesUtils(plugin) // Initialize MessagesUtils with the plugin instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val page = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 1 else 1

        when (page) {
            1 -> {
                sendHelpPage(sender, page, "1", "3", "help-page-1", "help-page-1-commands")
            }
            2 -> {
                sendHelpPage(sender, page, "2", "3", "help-page-2", "help-page-2-commands")
            }
            3 -> {
                sendHelpPage(sender, page, "3", "3", "help-page-3", "help-page-3-commands")
            }
            else -> {
                sender.sendMessage(messagesUtils.getMessage("help-invalid-page"))
            }
        }

        return true
    }

    private fun sendHelpPage(sender: CommandSender, currentPage: Int, pageNum: String, totalPages: String, titleKey: String, commandsKey: String) {
        sender.sendMessage(messagesUtils.getMessage(titleKey, pageNum, totalPages))
        val commands = messagesUtils.getList(commandsKey)
        commands.forEach { command ->
            sender.sendMessage(command)  // This explicitly sends each command
        }
        sender.sendMessage(messagesUtils.getMessage("help-next-page", pageNum.toInt() + 1, totalPages))
    }
}
