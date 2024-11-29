package com.gameknight.admincore.portal

import org.bukkit.Location

class PortalRegion(val name: String, val pos1: Location, val pos2: Location, var destination: Location?) {

    // Check if a location is inside the portal region
    fun isInsideRegion(location: Location): Boolean {
        val minX = pos1.x.coerceAtMost(pos2.x)
        val maxX = pos1.x.coerceAtLeast(pos2.x)
        val minY = pos1.y.coerceAtMost(pos2.y)
        val maxY = pos1.y.coerceAtLeast(pos2.y)
        val minZ = pos1.z.coerceAtMost(pos2.z)
        val maxZ = pos1.z.coerceAtLeast(pos2.z)

        return location.x in minX..maxX && location.y in minY..maxY && location.z in minZ..maxZ
    }

    // Get a string representation of the region
    override fun toString(): String {
        return "PortalRegion(name='$name', pos1=$pos1, pos2=$pos2, destination=$destination)"
    }
}
