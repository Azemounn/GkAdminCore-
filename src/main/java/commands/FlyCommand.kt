package com.gkadmincore.commands

import com.gkadmincore.utils.MessagesUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // Check if sender has the required permission
        if (!sender.hasPermission("gkadmincore.fly")) {
            MessagesUtils.sendNoPermissionMessage(sender)
            return true
        }

        // Ensure sender is a player
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command!")
            return true
        }

        // Handle optional arguments (on/off)
        if (args.isNotEmpty()) {
            when (args[0].lowercase()) {
                "on" -> {
                    sender.allowFlight = true
                    sender.sendMessage("§aFlight mode enabled.")
                    return true
                }
                "off" -> {
                    sender.allowFlight = false
                    sender.sendMessage("§cFlight mode disabled.")
                    return true
                }
                else -> {
                    sender.sendMessage("§cInvalid argument. Usage: /fly [on|off]")
                    return true
                }
            }
        }

        // Toggle flight mode if no arguments are provided
        sender.allowFlight = !sender.allowFlight
        sender.sendMessage("§aFlight mode ${if (sender.allowFlight) "enabled" else "disabled"}.")
        return true
    }
}
