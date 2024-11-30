package com.gkadmincore.commands

import com.gkadmincore.utils.ConfigUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /warp <name>")
            return true
        }

        val warpName = args[0]
        val location = ConfigUtils.getWarp(warpName)

        if (location == null) {
            sender.sendMessage("§cWarp $warpName not found!")
            return true
        }

        if (sender is Player) {
            sender.teleport(location)
            sender.sendMessage("§aTeleported to warp $warpName.")
        } else {
            sender.sendMessage("§cOnly players can use this command!")
        }
        return true
    }
}
