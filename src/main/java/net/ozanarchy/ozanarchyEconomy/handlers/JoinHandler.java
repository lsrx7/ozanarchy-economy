package net.ozanarchy.ozanarchyEconomy.handlers;

import net.ozanarchy.ozanarchyEconomy.OzanarchyEconomy;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class JoinHandler {
    static OzanarchyEconomy plugin = OzanarchyEconomy.getPlugin(OzanarchyEconomy.class);

    public static void generateUser(UUID uuid, String name){
        String sql = """
        INSERT INTO users (UUID, username)
        VALUES (?, ?)
        ON DUPLICATE KEY UPDATE UUID = UUID
        """;

        try (PreparedStatement stmt = plugin.getConnection().prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
