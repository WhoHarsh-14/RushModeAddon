package me.harsh.rushmodeaddon;

import de.marcely.bedwars.api.BedwarsAddon;
import de.marcely.bedwars.api.command.SubCommand;
import me.harsh.rushmodeaddon.commands.RushModeArenaCommand;
import me.harsh.rushmodeaddon.config.Config;
import me.harsh.rushmodeaddon.listeners.PlayerArenaListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RushModeAddon extends JavaPlugin {

    private static RushModeAddon plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Config.load();
        registerEverything();
    }

    private void registerEverything(){
        final BedwarsAddon addon = new Rush(this);
        addon.register();
        final SubCommand command = addon.getCommandsRoot().addCommand("rush");
        command.setHandler(new RushModeArenaCommand());
        getPlugin().getServer().getPluginManager().registerEvents(new PlayerArenaListener(), this);
    }


    public static RushModeAddon getPlugin() {
        return plugin;
    }
}
