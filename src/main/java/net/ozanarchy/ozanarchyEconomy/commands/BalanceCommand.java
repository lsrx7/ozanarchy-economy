package net.ozanarchy.ozanarchyEconomy.commands;

import net.ozanarchy.ozanarchyEconomy.handlers.CoinHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.ozanarchy.ozanarchyEconomy.commands.AdminCommands.getColor;

public class BalanceCommand implements CommandExecutor {
    private final CoinHandler economy;

    public BalanceCommand(CoinHandler economy) {
        this.economy = economy;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player p)) return true;

        economy.getBalance(p, bal ->
                p.sendMessage(getColor("&f&l[&c&lO&4&lZ&aECONOMY&f&l] &aBalance &e$" + bal + "&a."))
        );

        return true;
    }
}
