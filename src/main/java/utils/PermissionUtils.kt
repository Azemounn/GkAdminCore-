package com.gameknight.admincore.utils

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object PermissionUtils {

    // Check if a sender has a specific permission
    fun hasPermission(sender: CommandSender, permission: String): Boolean {
        return if (sender.hasPermission(permission)) {
            true
        } else {
            sender.sendMessage("${ChatColor.RED}You do not have permission to use this command!")
            false
        }
    }
}
