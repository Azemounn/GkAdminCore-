package com.gkadmincore.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SudoCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage("§cUsage: /sudo <player> <command>")
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage("§cPlayer not found!")
            return true
        }

        val cmd = args.drop(1).joinToString(" ")
        Bukkit.dispatchCommand(target, cmd)
        sender.sendMessage("§aExecuted '$cmd' as ${target.name}.")
        return true
    }
}
