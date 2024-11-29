package com.gameknight.admincore.portal

import com.gameknight.admincore.AdminCore
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class PortalManager(private val plugin: AdminCore) {

    private val portalFile: File = File(plugin.dataFolder, "portals.yml")
    private val portalConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(portalFile)

    private val warpFile: File = File(plugin.dataFolder, "warps.yml")
    private val warpConfig: YamlConfiguration = YamlConfiguration.loadConfiguration(warpFile)

    init {
        if (!portalFile.exists()) {
            plugin.saveResource("portals.yml", false)
        }
        if (!warpFile.exists()) {
            plugin.saveResource("warps.yml", false)
        }
    }

    fun setPortalRegion(portalName: String, pos1: Location?, pos2: Location?) {
        if (pos1 != null) portalConfig.set("$portalName.pos1", serializeLocation(pos1))
        if (pos2 != null) portalConfig.set("$portalName.pos2", serializeLocation(pos2))
        savePortalConfig()
    }

    fun setPortalDestination(portalName: String, destination: Location) {
        portalConfig.set("$portalName.destination", serializeLocation(destination))
        warpConfig.set(portalName, serializeLocation(destination))
        savePortalConfig()
        saveWarpConfig()
    }

    fun getPortalDestination(portalName: String): Location? {
        val locationString = portalConfig.getString("$portalName.destination") ?: return null
        return deserializeLocation(locationString)
    }

    fun getAllWarps(): Set<String> {
        return warpConfig.getKeys(false)
    }

    fun getWarpLocation(warpName: String): Location? {
        val locationString = warpConfig.getString(warpName) ?: return null
        return deserializeLocation(locationString)
    }

    private fun savePortalConfig() {
        portalConfig.save(portalFile)
    }

    private fun saveWarpConfig() {
        warpConfig.save(warpFile)
    }

    private fun serializeLocation(location: Location): String {
        return "${location.world?.name},${location.x},${location.y},${location.z},${location.yaw},${location.pitch}"
    }

    private fun deserializeLocation(data: String): Location? {
        val parts = data.split(",")
        if (parts.size != 6) return null
        val world = Bukkit.getWorld(parts[0]) ?: return null
        val x = parts[1].toDouble()
        val y = parts[2].toDouble()
        val z = parts[3].toDouble()
        val yaw = parts[4].toFloat()
        val pitch = parts[5].toFloat()
        return Location(world, x, y, z, yaw, pitch)
    }
}
