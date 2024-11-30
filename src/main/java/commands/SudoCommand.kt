package com.gkadmincore.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import com.gkadmincore.utils.ConfigUtils // <-- Make sure to import ConfigUtils
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit
import org.bukkit.plugin.java.JavaPlugin

class SudoCommand(private val plugin: JavaPlugin) : CommandExecutor {
    private val cooldown: Long = plugin.config.getLong("sudo.cooldown", 0L) // Cooldown in seconds

    private val lastUsed: MutableMap<String, Long> = mutableMapOf()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage(ConfigUtils.getMessage("sudo.usage"))
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage(ConfigUtils.getMessage("sudo.player_not_found"))
            return true
        }

        // Handle cooldown logic
        if (cooldown > 0) {
            val lastUseTime = lastUsed.getOrDefault(sender.name, 0L)
            val timePassed = System.currentTimeMillis() - lastUseTime
            if (timePassed < TimeUnit.SECONDS.toMillis(cooldown)) {
                val remainingTime = TimeUnit.MILLISECONDS.toSeconds(TimeUnit.SECONDS.toMillis(cooldown) - timePassed)
                sender.sendMessage(ConfigUtils.getMessage("sudo.cooldown").replace("{time}", remainingTime.toString()))
                return true
            }
        }

        // Execute the sudo command
        val cmd = args.drop(1).joinToString(" ")
        Bukkit.dispatchCommand(target, cmd)
        sender.sendMessage(ConfigUtils.getMessage("sudo.success").replace("{player}", target.name).replace("{command}", cmd))

        // Update last used time for cooldown
        lastUsed[sender.name] = System.currentTimeMillis()

        return true
    }
}
