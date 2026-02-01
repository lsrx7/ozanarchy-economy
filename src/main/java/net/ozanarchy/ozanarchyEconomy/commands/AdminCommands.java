package net.ozanarchy.ozanarchyEconomy.commands;

import net.ozanarchy.ozanarchyEconomy.api.EconomyAPI;
import net.ozanarchy.ozanarchyEconomy.handlers.CoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import static net.ozanarchy.ozanarchyEconomy.OzanarchyEconomy.config;

public class AdminCommands implements CommandExecutor {
    private final CoinHandler economy;
    String usage = getColor(config.getString("messages.usage"));
    String playerNotFound = getColor(config.getString("messages.playernotfound"));

    public AdminCommands(CoinHandler economy) {
        this.economy = economy;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (!p.hasPermission(config.getString("admin.permission"))) {
            p.sendMessage(getColor(config.getString("messages.nopermission")));
            return true;
        }

        if (economy == null) {
            p.sendMessage(getColor("&cEconomy service not available."));
            return true;
        }

        if (args.length < 1) {
            p.sendMessage(usage);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "add" -> {
                if (args.length != 3) {
                    p.sendMessage(usage);
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    p.sendMessage(playerNotFound);
                    return true;
                }

                double amount;
                try {
                    amount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    p.sendMessage(getColor("&cAmount must be a number."));
                    return true;
                }

                if (amount <= 0) {
                    p.sendMessage(getColor("&cAmount must be greater than 0."));
                    return true;
                }

                economy.add(target.getUniqueId(), amount);
                p.sendMessage(getColor("&aAdded &7" + amount + "&a to player balance."));
            }

            case "remove" -> {
                if (args.length != 3 ){
                    p.sendMessage(usage);
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null){
                    p.sendMessage(playerNotFound);
                    return true;
                }

                double amount;
                try {
                    amount = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    p.sendMessage(getColor("&cAmount must be a number."));
                    return true;
                }

                if (amount <= 0) {
                    p.sendMessage(getColor("&cAmount must be greater than 0."));
                    return true;
                }

                economy.remove(target.getUniqueId(), amount, success -> {
                    if (success) {
                        p.sendMessage(getColor("&aRemoved &7" + amount + "&a from player balance."));
                    } else {
                        p.sendMessage(getColor("&cPlayer doesn't have that much."));
                    }
                });
            }

            case "balance" -> {
                Player target;

                if (args.length == 1) {
                    target = p;
                } else if (args.length == 2) {
                    target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        p.sendMessage(playerNotFound);
                        return true;
                    }
                } else {
                    p.sendMessage(usage);
                    return true;
                }

                economy.getBalance(target, bal ->
                        p.sendMessage(getColor("&a" + target.getName() + " &ahas a balance of &7" + bal))
                );
            }

            default -> p.sendMessage(usage);
        }

        return true;
    }

    public static String getColor(String message) {
        if (message == null)
            return "";

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
