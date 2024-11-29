package com.gameknight.admincore.commands

import com.gameknight.admincore.utils.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class TeleportCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(MessageUtils.getMessage("errors.player_only"))
            return true
        }

        when (label.lowercase()) {
            "tp" -> {
                if (!sender.hasPermission("gkadmincore.tp")) {
                    sender.sendMessage(MessageUtils.getMessage("errors.no_permission"))
                    return true
                }

                if (args.size < 1) {
                    sender.sendMessage(
                        MessageUtils.getMessage(
                            "errors.invalid_args",
                            mapOf("usage" to "/tp <player>")
                        )
                    )
                    return true
                }

                val target = Bukkit.getPlayerExact(args[0])
                if (target == null || !target.isOnline) {
                    sender.sendMessage(MessageUtils.getMessage("errors.player_not_found", mapOf("player" to args[0])))
                    return true
                }

                sender.teleport(target.location)
                sender.sendMessage(
                    MessageUtils.getMessage(
                        "success.teleport_success",
                        mapOf("location" to target.name)
                    )
                )
            }

            "rtp" -> {
                if (!sender.hasPermission("gkadmincore.rtp")) {
                    sender.sendMessage(MessageUtils.getMessage("errors.no_permission"))
                    return true
                }

                val randomLocation = Location(sender.world, Random.nextDouble(-1000.0, 1000.0), 70.0, Random.nextDouble(-1000.0, 1000.0))
                sender.teleport(randomLocation)
                sender.sendMessage(MessageUtils.getMessage("success.teleport_random"))
            }
        }

        return true
    }
}
