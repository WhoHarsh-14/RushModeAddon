package me.harsh.rushmodeaddon.listeners;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.ShopGUIPostProcessEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.game.shop.ShopItem;
import de.marcely.bedwars.api.game.shop.price.ShopPrice;
import de.marcely.bedwars.api.game.upgrade.TeamEnchantment;
import de.marcely.bedwars.libraries.com.cryptomorin.xseries.XMaterial;
import me.harsh.rushmodeaddon.RushModeAddon;
import me.harsh.rushmodeaddon.config.Config;
import me.harsh.rushmodeaddon.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Bed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
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
            Bed toGive = null;
            if (arena.getBedParts(team).length == 0) continue;
            for (Block block : arena.getBedParts(team)){
                if (!(block.getState().getData() instanceof Bed)){
                    System.out.println("[!] block state data is not bed for some weird reason");
                    return;
                }
                final Bed b = (Bed) block.getState().getData();
                if (b.isHeadOfBed())
                    toGive = b;
            };
            if (toGive == null) continue;
            if (bed == null) continue;
            arena.addTeamEnchantment(team, new TeamEnchantment(TeamEnchantment.Target.SWORD, Enchantment.DAMAGE_ALL, 1));
            arena.addTeamEnchantment(team, new TeamEnchantment(TeamEnchantment.Target.AXE, Enchantment.DIG_SPEED, 1));
            arena.addTeamEnchantment(team, new TeamEnchantment(TeamEnchantment.Target.PICKAXE, Enchantment.DIG_SPEED, 1));
            arena.addTeamEnchantment(team, new TeamEnchantment(TeamEnchantment.Target.ARMOR, Enchantment.PROTECTION_ENVIRONMENTAL, 1));
            Util.setProtectBlocks(arena, team, toGive, bed.getLocation());
            arena.broadcast(Util.colorize("&a&lRush Mode has started!"));
        }
    }

    @EventHandler
    public void onShopOpen(ShopGUIPostProcessEvent event){
        if (event.getPage() == null) return;
        final Player player =event.getPlayer();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);
        if (arena == null || !(Util.getRushArenas().contains(arena)) || arena.getStatus() != ArenaStatus.RUNNING) return;
        for (ShopItem shopItem : event.getPage().getItems()) {
            if (shopItem == null) continue;
            for (ShopPrice price : shopItem.getPrices()) {
                if (price == null) continue;
                if (shopItem.getIcon().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                    final ItemStack itemStack = price.getDisplayItem(player);
                    final ItemMeta meta = itemStack.getItemMeta();
                    meta.setLore(Arrays.asList("", "&bPrice: &a2 Emerald"));
                    itemStack.setItemMeta(meta);
                }
                if (shopItem.getIcon().getType() == XMaterial.EGG.parseMaterial())
                    price.setGeneralAmount(1);
            }
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
                    bridgingMap.remove(player.getUniqueId());
                    bridgingMap.put(player.getUniqueId(), false);
                    Util.sendActionBar(player, Config.BRIDGE_MODE_DEACTIVATED);
                }
                bridgingMap.remove(player.getUniqueId());
                bridgingMap.put(player.getUniqueId(), true);
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
        || event.getBlockPlaced().getType() != arena.formatItemStack(XMaterial.WHITE_WOOL.parseItem(), player, arena.getPlayerTeam(player)).getType() || !(bridgingMap.containsKey(player.getUniqueId()))
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
