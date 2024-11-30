package com.gkadmincore.commands

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class RTPCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command!")
            return true
        }

        val world: World = sender.world
        val x = Random.nextInt(-5000, 5000)
        val z = Random.nextInt(-5000, 5000)
        val y = world.getHighestBlockYAt(x, z)

        val randomLocation = world.getBlockAt(x, y, z).location
        sender.teleport(randomLocation)
        sender.sendMessage("§aTeleported to random location: ${randomLocation.blockX}, ${randomLocation.blockY}, ${randomLocation.blockZ}")
        return true
    }
}
