package com.gkadmincore.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object ConfigUtils {

    private lateinit var plugin: JavaPlugin
    private lateinit var config: YamlConfiguration
    private lateinit var warps: YamlConfiguration
    private lateinit var portals: YamlConfiguration
    private lateinit var messages: YamlConfiguration

    // --- Load all configurations ---
    fun loadConfig(plugin: JavaPlugin) {
        this.plugin = plugin // Store the plugin instance for later use
        config = loadYaml(plugin, "config.yml")
        warps = loadYaml(plugin, "warps.yml")
        portals = loadYaml(plugin, "portals.yml")
        messages = loadYaml(plugin, "messages.yml")
    }

    // --- Load a YAML file ---
    private fun loadYaml(plugin: JavaPlugin, filename: String): YamlConfiguration {
        val file = File(plugin.dataFolder, filename)
        if (!file.exists()) {
            plugin.saveResource(filename, false)  // Save the file if it doesn't exist
        }
        return YamlConfiguration.loadConfiguration(file)
    }

    // --- Get the main plugin config ---
    fun getConfig(): YamlConfiguration {
        return config
    }

    // --- Get messages config ---
    fun getMessages(): YamlConfiguration {
        return messages
    }

    // --- Get Warp Information ---
    fun getWarp(warpName: String): Location? {
        val warpSection = warps.getConfigurationSection("warps.$warpName") ?: return null
        return parseLocation(warpSection.getString("location") ?: return null)
    }

    // --- Add a Warp ---
    fun addWarp(name: String, location: Location): Boolean {
        val warpSection = warps.createSection("warps.$name")
        warpSection.set("location", locationToString(location))
        return saveConfig(warps, "warps.yml")
    }

    // --- Delete a Warp ---
    fun deleteWarp(name: String): Boolean {
        if (warps.contains("warps.$name")) {
            warps.set("warps.$name", null)  // Remove the warp section
            return saveConfig(warps, "warps.yml")
        }
        return false
    }

    // --- Get the list of all warp names ---
    fun getWarpList(): List<String> {
        return warps.getConfigurationSection("warps")?.getKeys(false)?.toList() ?: emptyList()
    }

    // --- Get Portal Information ---
    fun getPortal(portalName: String): Triple<Location, Location, String>? {
        val portalSection = portals.getConfigurationSection("portals.$portalName") ?: return null

        val pos1 = portalSection.getString("pos1")?.let { parseLocation(it) } ?: return null
        val pos2 = portalSection.getString("pos2")?.let { parseLocation(it) } ?: return null
        val destination = portalSection.getString("destination") ?: return null

        return Triple(pos1, pos2, destination)
    }

    // --- Add Portal to Configuration ---
    fun addPortal(name: String, pos1: Location, pos2: Location, destinationName: String): Boolean {
        val portalSection = portals.createSection("portals.$name")
        portalSection.set("pos1", locationToString(pos1))
        portalSection.set("pos2", locationToString(pos2))
        portalSection.set("destination", destinationName)

        return saveConfig(portals, "portals.yml")
    }

    // --- Delete Portal from Configuration ---
    fun deletePortal(name: String): Boolean {
        if (portals.contains("portals.$name")) {
            portals.set("portals.$name", null)  // Remove the portal section
            return saveConfig(portals, "portals.yml")
        }
        return false
    }

    // --- Get the list of all portal names ---
    fun getPortalList(): List<String> {
        return portals.getConfigurationSection("portals")?.getKeys(false)?.toList() ?: emptyList()
    }

    // --- Get the portal sound from configuration ---
    fun getPortalSound(portalName: String): String {
        return portals.getString("portals.$portalName.sound") ?: "ENTITY_ENDERMAN_TELEPORT" // Default sound if null
    }

    // --- Get the portal particle from configuration ---
    fun getPortalParticle(portalName: String): String {
        return portals.getString("portals.$portalName.particle") ?: "EXPLOSION_NORMAL" // Default particle if null
    }

    // --- Get the portal cooldown from configuration ---
    fun getPortalCooldown(portalName: String): Int {
        return portals.getInt("portals.$portalName.cooldown", 5) // Default to 5 seconds if not found
    }

    // --- Get the cooldown from the main config ---
    fun getCooldown(path: String): Long {
        return config.getLong(path, 10000) // Default to 10 seconds (10000 ms)
    }

    // --- Save a configuration to its respective file ---
    private fun saveConfig(config: YamlConfiguration, filename: String): Boolean {
        return try {
            config.save(File(plugin.dataFolder, filename))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // --- Helper to Convert Location to String ---
    private fun locationToString(location: Location): String {
        return "${location.world?.name},${location.x},${location.y},${location.z},${location.pitch},${location.yaw}"
    }

    // --- Helper to Parse Location from String ---
    private fun parseLocation(locationString: String): Location {
        val parts = locationString.split(",")
        val world = Bukkit.getWorld(parts[0]) ?: return Location(Bukkit.getWorlds()[0], 0.0, 0.0, 0.0) // Default world if null
        val x = parts[1].toDouble()
        val y = parts[2].toDouble()
        val z = parts[3].toDouble()
        val pitch = parts[4].toFloat()
        val yaw = parts[5].toFloat()

        return Location(world, x, y, z, yaw, pitch)
    }

    // --- Get the value of a message from the messages config ---
    fun getMessage(key: String): String {
        return messages.getString(key) ?: "§c[GKAdmin] Message not found for key: $key" // Default if key not found
    }

    // --- Other configuration helpers ---
    fun getBoolean(path: String): Boolean {
        return config.getBoolean(path)
    }

    fun getInt(path: String): Int {
        return config.getInt(path)
    }

    fun getString(path: String): String {
        return config.getString(path) ?: "" // Default to empty string if path is not found
    }

    // --- Save the main config ---
    fun saveMainConfig(): Boolean {
        return saveConfig(config, "config.yml")
    }

    // --- Helper to check if a config path exists ---
    fun configPathExists(path: String): Boolean {
        return config.contains(path)
    }
}
