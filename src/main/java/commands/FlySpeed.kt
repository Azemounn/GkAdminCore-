package com.gameknight.admincore.commands

import com.gameknight.admincore.utils.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlySpeedCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(MessageUtils.getMessage("errors.player_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.flyspeed")) {
            sender.sendMessage(MessageUtils.getMessage("errors.no_permission"))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(MessageUtils.getMessage("errors.invalid_args", mapOf("usage" to "/flyspeed <0.1-1.0>")))
            return true
        }

        val speed = args[0].toFloatOrNull()
        if (speed == null || speed !in 0.1f..1.0f) {
            sender.sendMessage(MessageUtils.getMessage("errors.invalid_number"))
            return true
        }

        val player = sender as Player
        player.flySpeed = speed
        sender.sendMessage(
            MessageUtils.getMessage(
                "success.fly_speed_changed",
                mapOf("speed" to speed.toString())
            )
        )

        return true
    }
}
