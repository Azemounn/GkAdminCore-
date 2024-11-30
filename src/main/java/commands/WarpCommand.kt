package com.gkadmincore.commands

import com.gkadmincore.utils.ConfigUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

private val warpCooldowns = mutableMapOf<UUID, Long>() // Cooldown tracking for warp command

class WarpCommand : CommandExecutor {
    private val warpCooldown: Long = ConfigUtils.getCooldown("warp.cooldown") // Cooldown fetched from config

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(ConfigUtils.getMessage("warp.usage"))
            return true
        }

        val warpName = args[0]
        val location = ConfigUtils.getWarp(warpName)

        if (location == null) {
            sender.sendMessage(ConfigUtils.getMessage("warp.not_found").replace("{warp}", warpName))
            return true
        }

        if (sender !is Player) {
            sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
            return true
        }

        // Check cooldown
        val cooldownTime = warpCooldowns[sender.uniqueId] ?: 0L
        if (System.currentTimeMillis() < cooldownTime) {
            val remaining = (cooldownTime - System.currentTimeMillis()) / 1000
            sender.sendMessage(ConfigUtils.getMessage("warp.cooldown").replace("{time}", remaining.toString()))
            return true
        }

        // Perform teleportation
        sender.teleport(location)  // Teleport the player to the location

        sender.sendMessage(ConfigUtils.getMessage("warp.success").replace("{warp}", warpName))

        // Set cooldown
        warpCooldowns[sender.uniqueId] = System.currentTimeMillis() + (warpCooldown * 1000) // Apply cooldown from config

        return true
    }
}
