package me.harsh.rushmodeaddon.utils;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.libraries.com.cryptomorin.xseries.XMaterial;
import de.marcely.bedwars.libraries.com.cryptomorin.xseries.messages.ActionBar;
import de.marcely.bedwars.tools.PersistentBlockData;
import me.harsh.rushmodeaddon.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Bed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static void tell(Player player, String message){
        player.sendMessage(colorize(Config.PREFIX + " " + message));
    }
    public static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<Arena> getRushArenas(){
        final List<Arena> arenas = new ArrayList<>();
        for (String s : Config.ARENA_LIST) {
            final Arena arena = GameAPI.get().getArenaByExactName(s);
            if (arena != null)
                arenas.add(arena);
        }
        return arenas;
    }

    public static List<String> getArenaList(){
        final List<String> arenas = new ArrayList<>();
        GameAPI.get().getArenas().forEach(arena -> {
            arenas.add(arena.getName());
        });
        return arenas;
    }
    public static void sendActionBar(Player player, String message){
        ActionBar.sendActionBar(player, colorize(message));
    }

    public static void tell(CommandSender sender, String message){
        if (sender instanceof  Player){
            final Player player = (Player) sender;
            tell(player, message);
        }
    }

    public static void setProtectBlocks(Arena arena, Team team, Bed bed, Location bedLoc) {
        // For marcel :- make sure the "bed" is head for exact measurements
        int deltaX = bed.getFacing().getModX();
        int deltaZ = bed.getFacing().getModZ();
        // Because bed is head
        bedLoc.subtract(deltaX, 0.0D, deltaZ);
        ArrayList<Location> woods = new ArrayList<>();
        ArrayList<Location> wools = new ArrayList<>();
        ArrayList<Location> glasses = new ArrayList<>();
        if (deltaX != 0) {
            woods = new ArrayList<>(Arrays.asList(bedLoc
                    .clone().add(-deltaX, 0.0D, 0.0D), bedLoc
                    .clone().add(0.0D, 0.0D, 1.0D), bedLoc
                    .clone().add(0.0D, 0.0D, -1.0D), bedLoc
                    .clone().add(0.0D, 1.0D, 0.0D), bedLoc
                    .clone().add((deltaX + deltaX), 0.0D, 0.0D), bedLoc
                    .clone().add(deltaX, 0.0D, 1.0D), bedLoc
                    .clone().add(deltaX, 0.0D, -1.0D), bedLoc
                    .clone().add(deltaX, 1.0D, 0.0D)));
            wools = new ArrayList<>(Arrays.asList(bedLoc
                    .clone().add((-deltaX * 2), 0.0D, 0.0D), bedLoc
                    .clone().add(-deltaX, 1.0D, 0.0D), bedLoc
                    .clone().add(-deltaX, 0.0D, 1.0D), bedLoc
                    .clone().add(-deltaX, 0.0D, -1.0D), bedLoc
                    .clone().add(0.0D, 2.0D, 0.0D), bedLoc
                    .clone().add(0.0D, 1.0D, 1.0D), bedLoc
                    .clone().add(0.0D, 1.0D, -1.0D), bedLoc
                    .clone().add(0.0D, 0.0D, 2.0D), bedLoc
                    .clone().add(0.0D, 0.0D, -2.0D), bedLoc
                    .clone().add((deltaX * 2 + deltaX), 0.0D, 0.0D),
                    bedLoc
                            .clone().add((deltaX + deltaX), 1.0D, 0.0D), bedLoc
                    .clone().add((deltaX + deltaX), 0.0D, 1.0D), bedLoc
                    .clone().add((deltaX + deltaX), 0.0D, -1.0D), bedLoc
                    .clone().add(deltaX, 2.0D, 0.0D), bedLoc
                    .clone().add(deltaX, 1.0D, 1.0D), bedLoc
                    .clone().add(deltaX, 1.0D, -1.0D), bedLoc
                    .clone().add(deltaX, 0.0D, 2.0D), bedLoc
                    .clone().add(deltaX, 0.0D, -2.0D)));
            glasses = new ArrayList<>(Arrays.asList(bedLoc
                    .clone().add((-deltaX * 3), 0.0D, 0.0D), bedLoc
            .clone().add((-deltaX * 2), 1.0D, 0.0D), bedLoc
            .clone().add((-deltaX * 2), 0.0D, -1.0D), bedLoc
            .clone().add((-deltaX * 2), 0.0D, 1.0D), bedLoc
            .clone().add(-deltaX, 1.0D, 1.0D), bedLoc
            .clone().add(-deltaX, 1.0D, -1.0D), bedLoc
            .clone().add(-deltaX, 0.0D, 2.0D), bedLoc
            .clone().add(-deltaX, 0.0D, -2.0D), bedLoc
            .clone().add(-deltaX, 2.0D, 0.0D), bedLoc
            .clone().add(0.0D, 0.0D, -3.0D),
                    bedLoc
                            .clone().add(0.0D, 0.0D, 3.0D), bedLoc
                    .clone().add(0.0D, 1.0D, 2.0D), bedLoc
                    .clone().add(0.0D, 1.0D, -2.0D), bedLoc
                    .clone().add(0.0D, 2.0D, 1.0D), bedLoc
                    .clone().add(0.0D, 2.0D, -1.0D), bedLoc
                    .clone().add(0.0D, 3.0D, 0.0D), bedLoc
                    .clone().add((deltaX * 3 + deltaX), 0.0D, 0.0D), bedLoc
                    .clone().add((deltaX * 2 + deltaX), 1.0D, 0.0D), bedLoc
                    .clone().add((deltaX * 2 + deltaX), 0.0D, -1.0D), bedLoc
                    .clone().add((deltaX * 2 + deltaX), 0.0D, 1.0D),
                    bedLoc
                            .clone().add((deltaX + deltaX), 1.0D, 1.0D), bedLoc
                    .clone().add((deltaX + deltaX), 1.0D, -1.0D), bedLoc
                    .clone().add((deltaX + deltaX), 0.0D, 2.0D), bedLoc
                    .clone().add((deltaX + deltaX), 0.0D, -2.0D), bedLoc
                    .clone().add((deltaX + deltaX), 2.0D, 0.0D), bedLoc
                    .clone().add(deltaX, 0.0D, -3.0D), bedLoc
                    .clone().add(deltaX, 0.0D, 3.0D), bedLoc
                    .clone().add(deltaX, 1.0D, 2.0D), bedLoc
                    .clone().add(deltaX, 1.0D, -2.0D), bedLoc
                    .clone().add(deltaX, 2.0D, 1.0D),
                    bedLoc
                            .clone().add(deltaX, 2.0D, -1.0D), bedLoc
                    .clone().add(deltaX, 3.0D, 0.0D)));
        } else if (deltaZ != 0) {
            woods = new ArrayList<>(Arrays.asList(bedLoc
                    .clone().add(0.0D, 0.0D, -deltaZ), bedLoc
                    .clone().add(1.0D, 0.0D, 0.0D), bedLoc
                    .clone().add(-1.0D, 0.0D, 0.0D), bedLoc
                    .clone().add(0.0D, 1.0D, 0.0D), bedLoc
                    .clone().add(0.0D, 0.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(1.0D, 0.0D, deltaZ), bedLoc
                    .clone().add(-1.0D, 0.0D, deltaZ), bedLoc
                    .clone().add(0.0D, 1.0D, deltaZ)));
            wools = new ArrayList<>(Arrays.asList(bedLoc
                    .clone().add(0.0D, 0.0D, (-deltaZ * 2)), bedLoc
            .clone().add(0.0D, 1.0D, -deltaZ), bedLoc
            .clone().add(1.0D, 0.0D, -deltaZ), bedLoc
            .clone().add(-1.0D, 0.0D, -deltaZ), bedLoc
            .clone().add(0.0D, 2.0D, 0.0D), bedLoc
            .clone().add(1.0D, 1.0D, 0.0D), bedLoc
            .clone().add(-1.0D, 1.0D, 0.0D), bedLoc
            .clone().add(2.0D, 0.0D, 0.0D), bedLoc
            .clone().add(-2.0D, 0.0D, 0.0D), bedLoc
            .clone().add(0.0D, 0.0D, (deltaZ * 2 + deltaZ)),
                    bedLoc
                            .clone().add(0.0D, 1.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(1.0D, 0.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(-1.0D, 0.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(0.0D, 2.0D, deltaZ), bedLoc
                    .clone().add(1.0D, 1.0D, deltaZ), bedLoc
                    .clone().add(-1.0D, 1.0D, deltaZ), bedLoc
                    .clone().add(2.0D, 0.0D, deltaZ), bedLoc
                    .clone().add(-2.0D, 0.0D, deltaZ)));
            glasses = new ArrayList<>(Arrays.asList(bedLoc
                    .clone().add(0.0D, 0.0D, (-deltaZ * 3)), bedLoc
            .clone().add(0.0D, 1.0D, (-deltaZ * 2)), bedLoc
            .clone().add(-1.0D, 0.0D, (-deltaZ * 2)), bedLoc
            .clone().add(1.0D, 0.0D, (-deltaZ * 2)), bedLoc
            .clone().add(1.0D, 1.0D, -deltaZ), bedLoc
            .clone().add(-1.0D, 1.0D, -deltaZ), bedLoc
            .clone().add(2.0D, 0.0D, -deltaZ), bedLoc
            .clone().add(-2.0D, 0.0D, -deltaZ), bedLoc
            .clone().add(0.0D, 2.0D, -deltaZ), bedLoc
            .clone().add(-3.0D, 0.0D, 0.0D),
                    bedLoc
                            .clone().add(3.0D, 0.0D, 0.0D), bedLoc
                    .clone().add(2.0D, 1.0D, 0.0D), bedLoc
                    .clone().add(-2.0D, 1.0D, 0.0D), bedLoc
                    .clone().add(1.0D, 2.0D, 0.0D), bedLoc
                    .clone().add(-1.0D, 2.0D, 0.0D), bedLoc
                    .clone().add(0.0D, 3.0D, 0.0D), bedLoc
                    .clone().add(0.0D, 0.0D, (deltaZ * 3 + deltaZ)), bedLoc
                    .clone().add(0.0D, 1.0D, (deltaZ * 2 + deltaZ)), bedLoc
                    .clone().add(-1.0D, 0.0D, (deltaZ * 2 + deltaZ)), bedLoc
                    .clone().add(1.0D, 0.0D, (deltaZ * 2 + deltaZ)),
                    bedLoc
                            .clone().add(1.0D, 1.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(-1.0D, 1.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(2.0D, 0.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(-2.0D, 0.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(0.0D, 2.0D, (deltaZ + deltaZ)), bedLoc
                    .clone().add(-3.0D, 0.0D, deltaZ), bedLoc
                    .clone().add(3.0D, 0.0D, deltaZ), bedLoc
                    .clone().add(2.0D, 1.0D, deltaZ), bedLoc
                    .clone().add(-2.0D, 1.0D, deltaZ), bedLoc
                    .clone().add(1.0D, 2.0D, deltaZ),
                    bedLoc
                            .clone().add(-1.0D, 2.0D, deltaZ), bedLoc
                    .clone().add(0.0D, 3.0D, deltaZ)));
        }
        woods.forEach(location -> {
            if (location.getBlock().getType() == Material.AIR) {
                location.getBlock().setType(XMaterial.OAK_PLANKS.parseMaterial());
                arena.setBlockPlayerPlaced(location.getBlock(), true);
            }
        });
        wools.forEach(location -> {
            if (location.getBlock().getType() == Material.AIR) {
                location.getBlock().setType(XMaterial.WHITE_WOOL.parseMaterial());
                final PersistentBlockData data = PersistentBlockData.fromMaterial(Material.WOOL).getDyedData(team.getDyeColor());
                data.place(location.getBlock(), true);
            }
        });
        glasses.forEach(location -> {
            if (location.getBlock().getType() == Material.AIR) {
                location.getBlock().setType(XMaterial.WHITE_STAINED_GLASS.parseMaterial());
                final PersistentBlockData data = PersistentBlockData.fromMaterial(Material.STAINED_GLASS).getDyedData(team.getDyeColor());
                data.place(location.getBlock(), true);
            }
        });
    }
}
