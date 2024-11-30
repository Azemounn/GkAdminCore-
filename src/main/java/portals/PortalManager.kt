package com.gkadmincore.portals

import com.gkadmincore.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

object PortalManager {
    private val portals = mutableMapOf<String, Portal>()

    // --- Load portals from configuration ---
    fun loadPortals() {
        portals.clear() // Clear any existing portals before loading
        ConfigUtils.getPortalList().forEach { portalName ->
            val portalData = ConfigUtils.getPortal(portalName) // Triple<Location, Location, String>
            if (portalData != null) {
                val (pos1, pos2, destinationName) = portalData

                // Resolve the destination from the warp list
                val destination = ConfigUtils.getWarp(destinationName)
                if (destination != null) {
                    portals[portalName] = Portal(portalName, pos1, pos2, destination)
                } else {
                    Bukkit.getLogger().warning("Invalid portal destination for portal: $portalName")
                }
            } else {
                Bukkit.getLogger().warning("Portal '$portalName' is not properly configured.")
            }
        }
    }

    // --- Get an active portal for a player's location ---
    fun getActivePortal(location: Location): Portal? {
        return portals.values.find { it.isInside(location) }
    }

    // --- Save a portal to the configuration ---
    fun savePortal(name: String, pos1: Location, pos2: Location, destinationName: String): Boolean {
        val success = ConfigUtils.addPortal(name, pos1, pos2, destinationName)
        if (success) {
            val destination = ConfigUtils.getWarp(destinationName)
            if (destination != null) {
                portals[name] = Portal(name, pos1, pos2, destination)
            }
        }
        return success
    }

    // --- Set the destination for a given portal ---
    fun setPortalDestination(portalName: String, destination: Location): Boolean {
        if (!portals.containsKey(portalName)) {
            Bukkit.getLogger().warning("Portal '$portalName' does not exist.")
            return false
        }

        val portal = portals[portalName] ?: return false
        val updatedPortal = portal.copy(destination = destination) // Create a copy with updated destination
        portals[portalName] = updatedPortal

        // Update the configuration
        val success = ConfigUtils.addPortal(portalName, portal.pos1, portal.pos2, destination.world?.name ?: "")
        if (!success) {
            Bukkit.getLogger().warning("Failed to update destination for portal: $portalName in configuration.")
            return false
        }

        Bukkit.getLogger().info("Portal '$portalName' destination updated successfully.")
        return true
    }

    // --- Get a portal by its name ---
    fun getPortal(portalName: String): Portal? {
        return portals[portalName]
    }

    // --- Delete a portal from the configuration ---
    fun deletePortal(name: String): Boolean {
        val success = ConfigUtils.deletePortal(name)
        if (success) {
            portals.remove(name)
        }
        return success
    }

    // --- Get a list of all portal names ---
    fun getPortalList(): List<String> {
        return portals.keys.toList() // Return a list of portal names
    }

    // --- Create a portal ---
    fun createPortal(name: String, pos1: Location, pos2: Location, destinationName: String): Boolean {
        // Check if portal already exists
        if (portals.containsKey(name)) {
            Bukkit.getLogger().warning("Portal with name '$name' already exists!")
            return false // Portal already exists, can't create another with the same name
        }

        // Ensure both positions are valid
        if (pos1 == null || pos2 == null) {
            Bukkit.getLogger().warning("Invalid portal positions!")
            return false // Invalid positions
        }

        // Check if destination exists
        val destination = ConfigUtils.getWarp(destinationName)
        if (destination == null) {
            Bukkit.getLogger().warning("Destination '$destinationName' not found!")
            return false // Invalid destination
        }

        // Create and save the portal
        portals[name] = Portal(name, pos1, pos2, destination)
        ConfigUtils.addPortal(name, pos1, pos2, destinationName) // Save the portal to config
        Bukkit.getLogger().info("Portal '$name' created successfully!")
        return true
    }

    // --- Data class for portal representation ---
    data class Portal(val name: String, val pos1: Location, val pos2: Location, val destination: Location) {
        fun isInside(location: Location): Boolean {
            val minX = minOf(pos1.x, pos2.x)
            val maxX = maxOf(pos1.x, pos2.x)
            val minY = minOf(pos1.y, pos2.y)
            val maxY = maxOf(pos1.y, pos2.y)
            val minZ = minOf(pos1.z, pos2.z)
            val maxZ = maxOf(pos1.z, pos2.z)

            return location.x in minX..maxX &&
                    location.y in minY..maxY &&
                    location.z in minZ..maxZ &&
                    location.world?.name == pos1.world?.name
        }
    }
}
