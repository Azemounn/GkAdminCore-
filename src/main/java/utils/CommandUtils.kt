package com.gameknight.admincore.utils

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CommandUtils {

    // Send a formatted error message to the command sender
    fun sendError(sender: CommandSender, message: String) {
        sender.sendMessage("${ChatColor.RED}[Error] $message")
    }

    // Send a formatted success message to the command sender
    fun sendSuccess(sender: CommandSender, message: String) {
        sender.sendMessage("${ChatColor.GREEN}[Success] $message")
    }

    // Validate that the sender is a player
    fun ensurePlayer(sender: CommandSender): Player? {
        if (sender is Player) {
            return sender
        }
        sender.sendMessage("${ChatColor.RED}This command can only be used by a player.")
        return null
    }

    // Format a location as a readable string
    fun formatLocation(x: Int, y: Int, z: Int, world: String): String {
        return "${ChatColor.YELLOW}$world ($x, $y, $z)"
    }
}
