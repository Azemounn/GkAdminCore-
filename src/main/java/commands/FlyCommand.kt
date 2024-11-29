package com.gameknight.admincore.commands

import com.gameknight.admincore.utils.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import com.gameknight.admincore.utils.MessageUtils
import com.gameknight.admincore.portals.PortalUtils


class FlyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(MessageUtils.getMessage("errors.player_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.fly")) {
            sender.sendMessage(MessageUtils.getMessage("errors.no_permission"))
            return true
        }

        val player = sender as Player
        val isFlying = !player.isFlying

        player.allowFlight = true
        player.isFlying = isFlying

        val messageKey = if (isFlying) "success.fly_enabled" else "success.fly_disabled"
        player.sendMessage(MessageUtils.getMessage(messageKey))

        return true
    }
}
