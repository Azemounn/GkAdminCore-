package com.gameknight.admincore.utils

import org.bukkit.Location
import org.bukkit.entity.Player

object TeleportUtils {

    // Teleport a player to a specific location
    fun teleportPlayer(player: Player, location: Location) {
        player.teleport(location)
        player.sendMessage("You have been teleported to ${CommandUtils.formatLocation(location.blockX, location.blockY, location.blockZ, location.world?.name ?: "Unknown World")}.")
    }

    // Validate if a location is safe (example: no lava or void)
    fun isSafeLocation(location: Location): Boolean {
        val block = location.block
        return !(block.isLiquid || block.isEmpty)
    }
}
