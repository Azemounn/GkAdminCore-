package com.gkadmincore.utils

import org.bukkit.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object ConfigUtils {

    private lateinit var config: YamlConfiguration
    private lateinit var warps: YamlConfiguration
    private lateinit var portals: YamlConfiguration
    private lateinit var messages: YamlConfiguration

    // --- Load configurations ---
    fun loadConfig(plugin: JavaPlugin) {
        config = loadYaml(plugin, "config.yml")
        warps = loadYaml(plugin, "warps.yml")
        portals = loadYaml(plugin, "portals.yml")
        messages = loadYaml(plugin, "messages.yml")
    }

    // --- Messages management ---
    fun getMessage(key: String): String {
        val message = messages.getString(key) ?: "Message not found!"
        return ChatColor.translateAlternateColorCodes('&', message) // Ensure colors are parsed
    }

    // --- Portals management ---
    fun addPortal(name: String, pos1: Location, pos2: Location, destination: String): Boolean {
        val path = "portals.$name"
        portals.set("$path.pos1", serializeLocation(pos1))
        portals.set("$path.pos2", serializeLocation(pos2))
        portals.set("$path.destination", destination)
        return saveYaml("portals.yml", portals)
    }

    fun deletePortal(name: String): Boolean {
        val path = "portals.$name"
        if (!portals.contains(path)) return false
        portals.set(path, null)
        return saveYaml("portals.yml", portals)
    }

    fun getPortal(name: String): Triple<Location, Location, String>? {
        val path = "portals.$name"

        val pos1Serialized = portals.getString("$path.pos1")
        val pos2Serialized = portals.getString("$path.pos2")
        val destination = portals.getString("$path.destination")

        if (pos1Serialized == null || pos2Serialized == null || destination == null) {
            return null // Incomplete portal data
        }

        val pos1 = deserializeLocation(pos1Serialized)
        val pos2 = deserializeLocation(pos2Serialized)

        if (pos1 == null || pos2 == null) {
            return null // Invalid or missing location data
        }

        return Triple(pos1, pos2, destination)
    }

    fun getPortalList(): List<String> {
        val section = portals.getConfigurationSection("portals") ?: return emptyList()
        return section.getKeys(false).toList()
    }

    // --- Warps management ---
    fun addWarp(name: String, location: Location): Boolean {
        val path = "warps.$name"
        warps.set("$path.world", location.world?.name)
        warps.set("$path.x", location.x)
        warps.set("$path.y", location.y)
        warps.set("$path.z", location.z)
        warps.set("$path.yaw", location.yaw)
        warps.set("$path.pitch", location.pitch)
        return saveYaml("warps.yml", warps)
    }

    fun deleteWarp(name: String): Boolean {
        val path = "warps.$name"
        if (!warps.contains(path)) return false
        warps.set(path, null)
        return saveYaml("warps.yml", warps)
    }

    fun getWarp(name: String): Location? {
        val path = "warps.$name"
        val worldName = warps.getString("$path.world") ?: return null
        val world = Bukkit.getWorld(worldName)

        if (world == null) {
            Bukkit.getLogger().warning("Warp '$name' has a world that does not exist: $worldName")
            return null
        }

        val x = warps.getDouble("$path.x")
        val y = warps.getDouble("$path.y")
        val z = warps.getDouble("$path.z")
        val yaw = warps.getDouble("$path.yaw").toFloat()
        val pitch = warps.getDouble("$path.pitch").toFloat()
        return Location(world, x, y, z, yaw, pitch)
    }

    fun getWarpList(): List<String> {
        val section = warps.getConfigurationSection("warps") ?: return emptyList()
        return section.getKeys(false).toList()
    }

    // --- Serialization and deserialization ---
    fun serializeLocation(location: Location): String {
        return "${location.world?.name},${location.x},${location.y},${location.z},${location.yaw},${location.pitch}"
    }

    fun deserializeLocation(serialized: String): Location? {
        val parts = serialized.split(",")
        if (parts.size < 4) {
            Bukkit.getLogger().warning("Invalid serialized location: $serialized")
            return null
        }

        val worldName = parts[0]
        val world = Bukkit.getWorld(worldName)
        if (world == null) {
            Bukkit.getLogger().warning("World not found: $worldName")
            return null
        }

        val x = parts[1].toDoubleOrNull() ?: return null
        val y = parts[2].toDoubleOrNull() ?: return null
        val z = parts[3].toDoubleOrNull() ?: return null
        val yaw = parts.getOrNull(4)?.toFloat() ?: 0f
        val pitch = parts.getOrNull(5)?.toFloat() ?: 0f
        return Location(world, x, y, z, yaw, pitch)
    }

    // --- YAML file handling ---
    private fun loadYaml(plugin: JavaPlugin, fileName: String): YamlConfiguration {
        val file = File(plugin.dataFolder, fileName)
        if (!file.exists()) plugin.saveResource(fileName, false)
        return YamlConfiguration.loadConfiguration(file)
    }

    private fun saveYaml(fileName: String, config: YamlConfiguration): Boolean {
        return try {
            config.save(File(Bukkit.getPluginManager().getPlugin("GKAdminCore")!!.dataFolder, fileName))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
