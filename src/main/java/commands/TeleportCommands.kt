package com.gkadmincore.commands

import com.gkadmincore.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

private val teleportRequests = mutableMapOf<Player, Player>()
private val teleportCooldowns = mutableMapOf<UUID, Long>() // Cooldown tracking

// --- /tp Command ---
class TpCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.tp")) {
            sender.sendMessage(ConfigUtils.getMessage("general.no_permission"))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(ConfigUtils.getMessage("tp.usage"))
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target != null) {
            sender.teleport(target)
            sender.sendMessage(ConfigUtils.getMessage("tp.success").replace("{target}", target.name))
        } else {
            sender.sendMessage(ConfigUtils.getMessage("tp.not_found"))
        }

        return true
    }
}

// --- /tphere Command ---
class TpHereCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.tp")) {
            sender.sendMessage(ConfigUtils.getMessage("general.no_permission"))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(ConfigUtils.getMessage("tphere.usage"))
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target != null) {
            target.teleport(sender)
            sender.sendMessage(ConfigUtils.getMessage("tphere.success").replace("{target}", target.name))
        } else {
            sender.sendMessage(ConfigUtils.getMessage("tphere.not_found"))
        }

        return true
    }
}

// --- /tpa Command ---
class TpaCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.tpa")) {
            sender.sendMessage(ConfigUtils.getMessage("general.no_permission"))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(ConfigUtils.getMessage("tpa.usage"))
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == sender) {
            sender.sendMessage(ConfigUtils.getMessage("tpa.self_request"))
            return true
        }

        if (target != null) {
            val cooldownTime = teleportCooldowns[sender.uniqueId] ?: 0L
            if (System.currentTimeMillis() < cooldownTime) {
                val remaining = (cooldownTime - System.currentTimeMillis()) / 1000
                sender.sendMessage(ConfigUtils.getMessage("tpa.cooldown").replace("{time}", remaining.toString()))
                return true
            }

            teleportRequests[target] = sender
            target.sendMessage(ConfigUtils.getMessage("tpa.request_received").replace("{player}", sender.name))
            sender.sendMessage(ConfigUtils.getMessage("tpa.request_sent").replace("{target}", target.name))
            teleportCooldowns[sender.uniqueId] = System.currentTimeMillis() + 10 * 1000 // 10-second cooldown
        } else {
            sender.sendMessage(ConfigUtils.getMessage("tpa.not_found"))
        }

        return true
    }
}

// --- /tpaccept Command ---
class TpAcceptCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.tpa")) {
            sender.sendMessage(ConfigUtils.getMessage("general.no_permission"))
            return true
        }

        val requester = teleportRequests[sender]
        if (requester != null) {
            requester.teleport(sender)
            sender.sendMessage(ConfigUtils.getMessage("tpaccept.success").replace("{player}", requester.name))
            requester.sendMessage(ConfigUtils.getMessage("tpaccept.accepted").replace("{target}", sender.name))
            teleportRequests.remove(sender)
        } else {
            sender.sendMessage(ConfigUtils.getMessage("tpaccept.no_request"))
        }

        return true
    }
}

// --- /tpdeny Command ---
class TpDenyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
            return true
        }

        if (!sender.hasPermission("gkadmincore.tpa")) {
            sender.sendMessage(ConfigUtils.getMessage("general.no_permission"))
            return true
        }

        val requester = teleportRequests[sender]
        if (requester != null) {
            sender.sendMessage(ConfigUtils.getMessage("tpdeny.success").replace("{player}", requester.name))
            requester.sendMessage(ConfigUtils.getMessage("tpdeny.denied").replace("{target}", sender.name))
            teleportRequests.remove(sender)
        } else {
            sender.sendMessage(ConfigUtils.getMessage("tpdeny.no_request"))
        }

        return true
    }
}
