package com.gameknight.admincore.utils

import org.bukkit.Location
import org.bukkit.entity.Player

object PortalUtils {
    fun teleportToPortal(player: Player, destination: Location) {
        player.teleport(destination)
        player.sendMessage("You have been teleported!")
    }
}
