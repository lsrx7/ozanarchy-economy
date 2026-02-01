package net.ozanarchy.ozanarchyEconomy.api;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public interface EconomyAPI {
    void add(UUID uuid, double amount);

    void remove(UUID uuid, double amount, Consumer<Boolean> callback);

    void getBalance(UUID uuid, Consumer<Double> callback);

    default void getBalance(Player player, Consumer<Double> callback) {
        getBalance(player.getUniqueId(), callback);
    }
}
