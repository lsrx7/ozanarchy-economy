package net.ozanarchy.ozanarchyEconomy;

import net.ozanarchy.ozanarchyEconomy.api.EconomyAPI;
import net.ozanarchy.ozanarchyEconomy.commands.AdminCommands;
import net.ozanarchy.ozanarchyEconomy.commands.BalanceCommand;
import net.ozanarchy.ozanarchyEconomy.events.JoinEvent;
import net.ozanarchy.ozanarchyEconomy.handlers.CoinHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class OzanarchyEconomy extends JavaPlugin {
    private Connection connection;
    public String host, database, username, password, table;
    public  int port;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        //Config
        config = getConfig();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        //Register events
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        //MySQL
        setupMySql();
        createTables();

        //EconomyAPI
        CoinHandler coinHandler = new CoinHandler(this);
        getServer().getServicesManager().register(
                EconomyAPI.class,
                coinHandler,
                this,
                ServicePriority.Normal
        );

        //Commands
        if(config.getBoolean("balanceenabled")){
            getCommand("balance").setExecutor(new BalanceCommand(coinHandler));
        }
        if(config.getBoolean("admin.commandsenabled")){
            getCommand("ozeco").setExecutor(new AdminCommands(coinHandler));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupMySql(){
        host = config.getString("mysql.host");
        port = config.getInt("mysql.port");
        username = config.getString("mysql.username");
        password = config.getString("mysql.password");
        database = config.getString("mysql.database");

        try {
            synchronized (this) {
                if(getConnection() != null && !getConnection().isClosed()) return;
                Class.forName("com.mysql.cj.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false&allowPublicKeyRetrieval=true", this.username, this.password));
                getLogger().info("MYSQL Connected Successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                         "UUID VARCHAR(36) PRIMARY KEY," +
                         "username VARCHAR(16) NOT NULL," +
                         "coins DOUBLE DEFAULT 0.0" +
                         ")";
            stmt.executeUpdate(sql);
            getLogger().info("Tables checked/created successfully.");
        } catch (SQLException e) {
            getLogger().severe("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
