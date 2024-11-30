package com.gkadmincore.commands

import com.gameknight.admincore.utils.MessagesUtils // Ensure this import is correct
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class FlySpeedCommand(private val plugin: JavaPlugin) : CommandExecutor {

    private val messagesUtils = MessagesUtils(plugin) // Initialize MessagesUtils with the plugin instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // Check if sender has the required permission
        if (!sender.hasPermission("gkadmincore.fly.speed")) {
            sender.sendMessage(messagesUtils.getMessage("error-no-permission"))
            return true
        }

        // Ensure sender is a player
        if (sender !is Player) {
            sender.sendMessage(messagesUtils.getMessage("error-player-only"))
            return true
        }

        // Check if speed argument is provided and valid
        if (args.isEmpty() || args[0].toFloatOrNull() == null) {
            sender.sendMessage(messagesUtils.getMessage("flyspeed-usage"))
            return true
        }

        val inputSpeed = args[0].toFloat()
        if (inputSpeed !in 1f..10f) {
            sender.sendMessage(messagesUtils.getMessage("flyspeed-invalid"))
            return true
        }

        // Set the player's fly speed (Minecraft uses a range from 0.1 to 1.0 for flight speed)
        val normalizedSpeed = inputSpeed / 10 // Normalize input (1 - 10) to Minecraft's (0.1 - 1.0)
        sender.flySpeed = normalizedSpeed

        // Send confirmation message
        sender.sendMessage(messagesUtils.getMessage("flyspeed-set", inputSpeed))
        return true
    }
}
