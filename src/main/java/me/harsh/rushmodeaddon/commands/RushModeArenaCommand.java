package me.harsh.rushmodeaddon.commands;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.command.CommandHandler;
import de.marcely.bedwars.api.command.SubCommand;
import me.harsh.rushmodeaddon.RushModeAddon;
import me.harsh.rushmodeaddon.config.Config;
import me.harsh.rushmodeaddon.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class RushModeArenaCommand implements CommandHandler {
    @Override
    public Plugin getPlugin() {
        return RushModeAddon.getPlugin();
    }


    @Override
    public void onFire(CommandSender commandSender, String s, String[] strings) {
        if (strings.length == 2){
            switch (strings[0]){
                case "add":
                    final Arena arena = GameAPI.get().getArenaByName(strings[1]);
                    if (arena == null) return;
                    Config.addArenaRushMode(arena.getName());
                    Util.tell(commandSender, Config.ARENA_ADDED.replace("{arena}", arena.getName()));
                    break;
                case "remove":
                    final String arr = strings[1];
                    if (Config.ARENA_LIST.contains(arr)) {
                        Config.removeArenaRushMode(arr);
                    }
                    Util.tell(commandSender, Config.ARENA_REMOVED.replace("{arena}", arr));
                    break;
                case "reload":
                    Config.reload();
                    Util.tell(commandSender, "&aReloaded!");
                    break;
            }
        }
    }

    @Override
    public List<String> onAutocomplete(CommandSender commandSender, String[] strings) {
        final List<String> tab = new ArrayList<>();
        switch (strings.length){
            case 1:
                tab.add("add");
                tab.add("remove");
                tab.add("reload");
                break;
            case 2:
                tab.addAll(Util.getArenaList());
                break;
        }
        return tab;
    }

    @Override
    public void onRegister(SubCommand subCommand) {

    }
}
