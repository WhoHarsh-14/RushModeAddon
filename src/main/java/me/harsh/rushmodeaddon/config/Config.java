package me.harsh.rushmodeaddon.config;

import me.harsh.rushmodeaddon.RushModeAddon;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {

    public static String PREFIX;
    public static String ARENA_ADDED;
    public static String ARENA_REMOVED;
    public static String BRIDGE_MODE_ACTIVATED;
    public static String BRIDGE_MODE_DEACTIVATED;
    public static List<String> ARENA_LIST;

    public static void load(){
        RushModeAddon.getPlugin().saveDefaultConfig();
        loadVars();
    }

    public static void reload(){
        RushModeAddon.getPlugin().reloadConfig();
        loadVars();
    }

    private static void loadVars(){
        PREFIX = get().getString("Prefix");
        ARENA_LIST = get().getStringList("Arenas");
        ARENA_ADDED = get().getString("Messages.Arena_added_to_rush_mode");
        ARENA_REMOVED = get().getString("Messages.Arena_removed_from_rush_mode");
        BRIDGE_MODE_ACTIVATED = get().getString("Messages.Bridge_mode_activated");
        BRIDGE_MODE_DEACTIVATED = get().getString("Messages.Bridge_mode_deactivated");
    }

    public static FileConfiguration get(){
        return RushModeAddon.getPlugin().getConfig();
    }

    public static void addArenaRushMode(String arena){
        ARENA_LIST.add(arena);
        get().set("Arenas", ARENA_LIST);
    }
    public static void removeArenaRushMode(String arena){
        ARENA_LIST.remove(arena);
        get().set("Arenas", ARENA_LIST);
    }
}
