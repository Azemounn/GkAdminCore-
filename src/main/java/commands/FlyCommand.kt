package com.gkadmincore.commands

import com.gameknight.admincore.utils.MessagesUtils // Ensure this import is correct
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class FlyCommand(private val plugin: JavaPlugin) : CommandExecutor {

    private val messagesUtils = MessagesUtils(plugin) // Initialize MessagesUtils with the plugin instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // Check if sender has the required permission
        if (!sender.hasPermission("gkadmincore.fly")) {
            sender.sendMessage(messagesUtils.getMessage("error-no-permission"))
            return true
        }

        // Ensure sender is a player
        if (sender !is Player) {
            sender.sendMessage(messagesUtils.getMessage("error-player-only"))
            return true
        }

        // Handle optional arguments (on/off)
        if (args.isNotEmpty()) {
            when (args[0].lowercase()) {
                "on" -> {
                    sender.allowFlight = true
                    sender.sendMessage(messagesUtils.getMessage("fly-enabled"))
                    return true
                }
                "off" -> {
                    sender.allowFlight = false
                    sender.sendMessage(messagesUtils.getMessage("fly-disabled"))
                    return true
                }
                else -> {
                    sender.sendMessage(messagesUtils.getMessage("fly-usage"))
                    return true
                }
            }
        }

        // Toggle flight mode if no arguments are provided
        sender.allowFlight = !sender.allowFlight
        sender.sendMessage(messagesUtils.getMessage("fly-toggle", sender.allowFlight))
        return true
    }
}
