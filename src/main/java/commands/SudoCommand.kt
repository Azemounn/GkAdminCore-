package com.gameknight.admincore.commands

import com.gameknight.admincore.utils.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SudoCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("gkadmincore.sudo")) {
            sender.sendMessage(MessageUtils.getMessage("errors.no_permission"))
            return true
        }

        if (args.size < 2) {
            sender.sendMessage(
                MessageUtils.getMessage(
                    "errors.invalid_args",
                    mapOf("usage" to "/sudo <player> <command>")
                )
            )
            return true
        }

        val target = Bukkit.getPlayerExact(args[0])
        if (target == null || !target.isOnline) {
            sender.sendMessage(MessageUtils.getMessage("errors.player_not_found", mapOf("player" to args[0])))
            return true
        }

        val commandToRun = args.drop(1).joinToString(" ")
        target.performCommand(commandToRun)
        sender.sendMessage(
            MessageUtils.getMessage(
                "success.sudo_success",
                mapOf("target" to target.name, "command" to commandToRun)
            )
        )

        return true
    }
}
