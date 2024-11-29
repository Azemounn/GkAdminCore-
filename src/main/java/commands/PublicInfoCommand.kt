package com.gameknight.admincore.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class PublicInfoCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage(
            """
            ${ChatColor.DARK_AQUA}========== ${ChatColor.GOLD}GKAdminCore ${ChatColor.DARK_AQUA}==========
            ${ChatColor.GREEN}This server is using ${ChatColor.YELLOW}GKAdminCore ${ChatColor.GREEN}v1.0.
            ${ChatColor.GREEN}Plugin created by ${ChatColor.YELLOW}Azemounn.
            ${ChatColor.DARK_AQUA}==================================
            """.trimIndent()
        )
        return true
    }
}
