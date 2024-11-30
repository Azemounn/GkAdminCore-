package com.gkadmincore.commands

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import com.gkadmincore.utils.ConfigUtils // <-- Make sure to import ConfigUtils
import kotlin.random.Random

class RTPCommand : CommandExecutor {
    // Reference to the plugin's config
    private val plugin = Bukkit.getPluginManager().getPlugin("YourPluginName")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // Ensure only players can use this command
        if (sender !is Player) {
            sender.sendMessage(ConfigUtils.getMessage("rtp.only_players"))
            return true
        }

        // Fetch configurable values from the config
        val xRange = plugin?.config?.getInt("rtp.x-range", 5000) ?: 5000
        val zRange = plugin?.config?.getInt("rtp.z-range", 5000) ?: 5000
        val yMin = plugin?.config?.getInt("rtp.y-min", 60) ?: 60
        val yMax = plugin?.config?.getInt("rtp.y-max", 100) ?: 100

        // Generate random coordinates
        val world: World = sender.world
        val x = Random.nextInt(-xRange, xRange)
        val z = Random.nextInt(-zRange, zRange)

        // Find the highest block Y at the generated X and Z
        var y = world.getHighestBlockYAt(x, z)

        // Ensure the teleportation Y level is within the defined range
        if (y < yMin) {
            y = yMin
        } else if (y > yMax) {
            y = yMax
        }

        // Create the teleportation location
        val randomLocation = world.getBlockAt(x, y, z).location

        // Attempt teleportation
        try {
            sender.teleport(randomLocation)
            val successMessage = ConfigUtils.getMessage("rtp.teleport_success")
                .replace("{x}", randomLocation.blockX.toString())
                .replace("{y}", randomLocation.blockY.toString())
                .replace("{z}", randomLocation.blockZ.toString())
            sender.sendMessage(successMessage)
        } catch (e: Exception) {
            sender.sendMessage(ConfigUtils.getMessage("rtp.teleport_error"))
        }

        return true
    }
}
