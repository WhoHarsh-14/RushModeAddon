package me.harsh.rushmodeaddon;

import de.marcely.bedwars.api.BedwarsAddon;
import org.bukkit.plugin.Plugin;

public class Rush extends BedwarsAddon {
    public Rush(Plugin plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "RushModeAddon";
    }
}
