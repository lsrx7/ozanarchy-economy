package net.ozanarchy.ozanarchyEconomy.events;

import net.ozanarchy.ozanarchyEconomy.OzanarchyEconomy;
import net.ozanarchy.ozanarchyEconomy.handlers.JoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    static OzanarchyEconomy plugin = OzanarchyEconomy.getPlugin(OzanarchyEconomy.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onConnection(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            JoinHandler.generateUser(player.getUniqueId(), player.getName());
        });
    }
}
