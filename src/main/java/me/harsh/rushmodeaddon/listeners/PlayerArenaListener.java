package me.harsh.rushmodeaddon.listeners;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import me.harsh.rushmodeaddon.RushModeAddon;
import me.harsh.rushmodeaddon.config.Config;
import me.harsh.rushmodeaddon.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Bed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;


public class PlayerArenaListener implements Listener {

    private final HashMap<UUID, Boolean> bridgingMap = new HashMap<>();

    @EventHandler
    public void onArenaEnable(RoundStartEvent event){
        final Arena arena = event.getArena();
        if (!Util.getRushArenas().contains(arena)) return;
        for (Team team : event.getArena().getRemainingTeams()) {
            final Block bed = arena.getBedLocation(team).getBlock(arena.getGameWorld());
            if (bed == null || bed.getType() != Material.BED) return;
            Util.setProtectBlocks(arena, team, (Bed) bed, bed.getLocation());
        }
    }

    @EventHandler
    public void onPlayerToggle(PlayerInteractEvent event){
        final Player player = event.getPlayer();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena == null || !(Util.getRushArenas().contains(arena)) || arena.getStatus() != ArenaStatus.RUNNING) return;
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            if (bridgingMap.containsKey(player.getUniqueId())){
                if (bridgingMap.get(player.getUniqueId())) {
                    bridgingMap.replace(player.getUniqueId(), false);
                    Util.sendActionBar(player, Config.BRIDGE_MODE_DEACTIVATED);
                }
                bridgingMap.replace(player.getUniqueId(), true);
                Util.sendActionBar(player, Config.BRIDGE_MODE_ACTIVATED);
            }else {
                bridgingMap.put(player.getUniqueId(), true);
                Util.sendActionBar(player, Config.BRIDGE_MODE_ACTIVATED);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        final Player player = event.getPlayer();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena == null || !(Util.getRushArenas().contains(arena)) || arena.getStatus() != ArenaStatus.RUNNING
        || event.getBlockPlaced().getType() != Material.WOOL || !(bridgingMap.containsKey(player.getUniqueId()))
            || !(bridgingMap.get(player.getUniqueId()))) return;
        placeBlocks(event.getBlockPlaced(), event.getBlockAgainst(), arena);
    }


    private void placeBlocks(Block placed, Block against, Arena arena){
        BlockFace face = placed.getFace(against);
        final Vector vector = new Vector(-face.getModX(), -face.getModY(), -face.getModZ());
        final Location nextBlock = placed.getLocation().clone().add(vector);
        (new BukkitRunnable() {
            int place = 0;

            public void run() {
                if (this.place <= 4 && nextBlock
                        .getWorld().getNearbyEntities(nextBlock, 0.45D, 0.5D, 0.45D).size() <= 0 && nextBlock
                        .getBlock().getType() == Material.AIR &&
                        arena.isBlockPlayerPlaced(nextBlock.getBlock())) {
                    nextBlock.getBlock().setTypeIdAndData(placed.getType().getId(), placed.getData(), true);
                    nextBlock.getWorld().playSound(nextBlock, Sound.valueOf(String.format("BLOCK_%s_BREAK", nextBlock.getBlock().getType().name())), 1.0F, 1.0F);
                    arena.setBlockPlayerPlaced(nextBlock.getBlock(), true);
                    nextBlock.add(vector);
                    this.place++;
                } else {
                    cancel();
                }
            }
        }).runTaskTimer(RushModeAddon.getPlugin(), 0L, 2L);
    }
}
