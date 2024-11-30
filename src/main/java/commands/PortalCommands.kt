package com.gkadmincore.commands

import com.gkadmincore.utils.ConfigUtils
import com.gkadmincore.portals.PortalManager
import org.bukkit.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object PortalCommands : Listener {

    private val playerPositions = mutableMapOf<UUID, Pair<Location?, Location?>>()
    private val portalCooldowns = mutableMapOf<UUID, Long>() // Cooldown management
    private const val WAND_NAME = "§6Portal Wand"
    private val PREVIEW_PARTICLE = Particle.WHITE_SMOKE
    private val TELEPORT_SOUND = Sound.ENTITY_ENDERMAN_TELEPORT
    private const val COOLDOWN_SECONDS = 5 // Cooldown duration in seconds

    init {
        PortalManager.loadPortals() // Load portals when the plugin starts
    }

    // --- EVENT LISTENERS ---
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val location = player.location

        // Cooldown check
        if (portalCooldowns[player.uniqueId]?.let { System.currentTimeMillis() < it } == true) return

        // Find and teleport to portal if nearby
        val portal = PortalManager.getActivePortal(location) ?: return
        player.teleport(portal.destination)
        player.playSound(player.location, TELEPORT_SOUND, 1.0f, 1.0f)
        player.sendMessage(ConfigUtils.getMessage("portal.teleport").replace("{portal}", portal.name))

        // Cooldown set
        portalCooldowns[player.uniqueId] = System.currentTimeMillis() + COOLDOWN_SECONDS * 1000
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if (item.type != Material.STICK || item.itemMeta?.displayName != WAND_NAME) return

        when (event.action) {
            Action.LEFT_CLICK_BLOCK -> {
                val clickedBlock = event.clickedBlock?.location ?: return
                setPlayerPosition1(player.uniqueId, clickedBlock)
                player.sendMessage(ConfigUtils.getMessage("portal.set_position1")
                    .replace("{x}", clickedBlock.blockX.toString())
                    .replace("{y}", clickedBlock.blockY.toString())
                    .replace("{z}", clickedBlock.blockZ.toString()))
                clickedBlock.world?.spawnParticle(PREVIEW_PARTICLE, clickedBlock, 10)
                event.isCancelled = true
            }
            Action.RIGHT_CLICK_BLOCK -> {
                val clickedBlock = event.clickedBlock?.location ?: return
                setPlayerPosition2(player.uniqueId, clickedBlock)
                player.sendMessage(ConfigUtils.getMessage("portal.set_position2")
                    .replace("{x}", clickedBlock.blockX.toString())
                    .replace("{y}", clickedBlock.blockY.toString())
                    .replace("{z}", clickedBlock.blockZ.toString()))
                clickedBlock.world?.spawnParticle(PREVIEW_PARTICLE, clickedBlock, 10)
                event.isCancelled = true
            }
            Action.RIGHT_CLICK_AIR -> {
                val positions = getPlayerPositions(player.uniqueId)
                val pos1 = positions?.first
                val pos2 = positions?.second

                if (pos1 != null && pos2 != null) {
                    previewPortalRegion(player, pos1, pos2)
                } else {
                    player.sendMessage(ConfigUtils.getMessage("portal.preview_error"))
                }
                event.isCancelled = true
            }
            else -> return
        }
    }

    private fun previewPortalRegion(player: Player, pos1: Location, pos2: Location) {
        val world = pos1.world ?: return
        if (world != pos2.world) {
            player.sendMessage(ConfigUtils.getMessage("portal.different_worlds"))
            return
        }

        // Get the min/max coordinates for the X, Y, and Z axes
        val minX = minOf(pos1.blockX, pos2.blockX)
        val maxX = maxOf(pos1.blockX, pos2.blockX)
        val minY = minOf(pos1.blockY, pos2.blockY)
        val maxY = maxOf(pos1.blockY, pos2.blockY)
        val minZ = minOf(pos1.blockZ, pos2.blockZ)
        val maxZ = maxOf(pos1.blockZ, pos2.blockZ)

        // Use BukkitRunnable to periodically spawn particles for better performance
        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                if (count++ > 5) cancel()

                val particleType = Particle.WHITE_SMOKE  // Particle type you want to use (e.g., Particle.END_ROD, Particle.FLAME)

                // Outline the rectangular region using particles on all 4 edges
                for (x in minX..maxX) {
                    for (z in listOf(minZ, maxZ)) {
                        world.spawnParticle(particleType, x.toDouble(), minY.toDouble(), z.toDouble(), 1)
                        world.spawnParticle(particleType, x.toDouble(), maxY.toDouble(), z.toDouble(), 1)
                    }
                }

                for (z in minZ..maxZ) {
                    for (y in listOf(minY, maxY)) {
                        world.spawnParticle(particleType, minX.toDouble(), y.toDouble(), z.toDouble(), 1)
                        world.spawnParticle(particleType, maxX.toDouble(), y.toDouble(), z.toDouble(), 1)
                    }
                }

                // Optionally, outline the vertical edges (not necessary if it's just the base)
                for (x in minX..maxX) {
                    for (y in minY..maxY) {
                        world.spawnParticle(particleType, x.toDouble(), y.toDouble(), minZ.toDouble(), 1)
                        world.spawnParticle(particleType, x.toDouble(), y.toDouble(), maxZ.toDouble(), 1)
                    }
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("GKAdminCore")!!, 0L, 5L)

        // Notify the player about the preview
        player.sendMessage(ConfigUtils.getMessage("portal.preview_start"))
    }

    // --- POSITION MANAGEMENT ---
    private fun setPlayerPosition1(playerId: UUID, position: Location) {
        val currentPositions = playerPositions[playerId] ?: Pair(null, null)
        playerPositions[playerId] = Pair(position, currentPositions.second)
    }

    private fun setPlayerPosition2(playerId: UUID, position: Location) {
        val currentPositions = playerPositions[playerId] ?: Pair(null, null)
        playerPositions[playerId] = Pair(currentPositions.first, position)
    }

    private fun getPlayerPositions(playerId: UUID): Pair<Location?, Location?>? {
        return playerPositions[playerId]
    }

    // --- COMMAND CLASSES ---
    class PortalWandCommand : CommandExecutor {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
            if (sender !is Player) {
                sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
                return true
            }

            val wand = ItemStack(Material.STICK)
            val meta: ItemMeta = wand.itemMeta!!
            meta.setDisplayName(WAND_NAME)
            meta.lore = listOf(
                "§7Left-click: Set Position 1",
                "§7Right-click: Set Position 2",
                "§7Right-click air: Preview region"
            )
            wand.itemMeta = meta

            sender.inventory.addItem(wand)
            sender.sendMessage(ConfigUtils.getMessage("portal.wand_granted"))
            return true
        }
    }

    class SetPortalPositionsCommand(private val positionType: String) : CommandExecutor {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
            if (sender !is Player) {
                sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
                return true
            }

            val player = sender
            val currentLocation = player.location

            if (positionType == "pos1") {
                setPlayerPosition1(player.uniqueId, currentLocation)
                player.sendMessage(ConfigUtils.getMessage("portal.set_position1")
                    .replace("{x}", currentLocation.blockX.toString())
                    .replace("{y}", currentLocation.blockY.toString())
                    .replace("{z}", currentLocation.blockZ.toString()))
            } else if (positionType == "pos2") {
                setPlayerPosition2(player.uniqueId, currentLocation)
                player.sendMessage(ConfigUtils.getMessage("portal.set_position2")
                    .replace("{x}", currentLocation.blockX.toString())
                    .replace("{y}", currentLocation.blockY.toString())
                    .replace("{z}", currentLocation.blockZ.toString()))
            }
            return true
        }
    }

    class SetPortalDestinationCommand : CommandExecutor {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
            if (sender !is Player) {
                sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
                return true
            }

            if (args.isEmpty()) {
                sender.sendMessage(ConfigUtils.getMessage("portal.destination_usage"))
                return true
            }

            val destinationName = args[0]
            val currentLocation = sender.location

            if (ConfigUtils.addWarp(destinationName, currentLocation)) {
                sender.sendMessage(ConfigUtils.getMessage("portal.destination_set").replace("{destination}", destinationName))
            } else {
                sender.sendMessage(ConfigUtils.getMessage("portal.destination_error"))
            }
            return true
        }
    }

    class CreatePortalCommand : CommandExecutor {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
            if (sender !is Player) {
                sender.sendMessage(ConfigUtils.getMessage("general.players_only"))
                return true
            }

            if (args.size < 2) {
                sender.sendMessage(ConfigUtils.getMessage("portal.create_usage"))
                return true
            }

            val portalName = args[0]
            val destinationName = args[1]
            val destination = ConfigUtils.getWarp(destinationName)

            if (destination == null) {
                sender.sendMessage(ConfigUtils.getMessage("portal.destination_not_found").replace("{destination}", destinationName))
                return true
            }

            val positions = getPlayerPositions(sender.uniqueId)
            if (positions?.first == null || positions.second == null) {
                sender.sendMessage(ConfigUtils.getMessage("portal.positions_not_set"))
                return true
            }

            if (PortalManager.createPortal(portalName, positions.first!!, positions.second!!, destinationName)) {
                sender.sendMessage(ConfigUtils.getMessage("portal.created").replace("{portal}", portalName))
            } else {
                sender.sendMessage(ConfigUtils.getMessage("portal.create_failed"))
            }
            return true
        }
    }

    class DeletePortalCommand : CommandExecutor {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
            if (args.isEmpty()) {
                sender.sendMessage(ConfigUtils.getMessage("portal.delete_usage"))
                return true
            }

            val portalName = args[0]
            if (PortalManager.deletePortal(portalName)) {
                sender.sendMessage(ConfigUtils.getMessage("portal.deleted").replace("{portal}", portalName))
            } else {
                sender.sendMessage(ConfigUtils.getMessage("portal.delete_failed").replace("{portal}", portalName))
            }
            return true
        }
    }

    class ListPortalsCommand : CommandExecutor {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
            val portals = PortalManager.getPortalList()
            if (portals.isEmpty()) {
                sender.sendMessage(ConfigUtils.getMessage("portal.list_empty"))
            } else {
                sender.sendMessage(ConfigUtils.getMessage("portal.list").replace("{portals}", portals.joinToString(", ")))
            }
            return true
        }
    }
}
