package com.gkadmincore.commands

import com.gkadmincore.utils.MessagesUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlySpeedCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("gkadmincore.fly.speed")) {
            MessagesUtils.sendNoPermissionMessage(sender)
            return true
        }

        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command!")
            return true
        }

        if (args.isEmpty() || args[0].toFloatOrNull() == null) {
            sender.sendMessage("§cUsage: /flyspeed <speed> (1 - 10)")
            return true
        }

        val inputSpeed = args[0].toFloat()
        if (inputSpeed !in 1f..10f) {
            sender.sendMessage("§cSpeed must be between 1 and 10.")
            return true
        }

        val normalizedSpeed = inputSpeed / 10 // Convert to Minecraft's range (0.1 - 1.0)
        sender.flySpeed = normalizedSpeed
        sender.sendMessage("§aFlight speed set to $inputSpeed.")
        return true
    }
}
