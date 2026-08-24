package nl.tricraft.tricraftcore.database;

import nl.tricraft.tricraftcore.TricraftCore;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class DatabaseManager {

    private final TricraftCore plugin;
    private Connection connection;

    public DatabaseManager(TricraftCore plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        try {
            File databaseFile = new File(plugin.getDataFolder(), "tricraft.db");

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + databaseFile.getAbsolutePath()
            );

            createTables();

            plugin.getLogger().info("Database verbonden.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Database kon niet worden geopend.");
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS world_inventories (
                    uuid TEXT NOT NULL,
                    world TEXT NOT NULL,
                    inventory TEXT,
                    armor TEXT,
                    PRIMARY KEY (uuid, world)
                );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public void saveInventory(
            UUID uuid,
            String world,
            String inventory,
            String armor
    ) {

        String sql = """
                INSERT INTO world_inventories
                (uuid, world, inventory, armor)
                VALUES (?, ?, ?, ?)
                ON CONFLICT(uuid, world)
                DO UPDATE SET
                    inventory = excluded.inventory,
                    armor = excluded.armor;
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, world);
            statement.setString(3, inventory);
            statement.setString(4, armor);

            statement.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe(
                    "Inventory kon niet worden opgeslagen."
            );
            e.printStackTrace();
        }
    }

    public InventoryData loadInventory(
            UUID uuid,
            String world
    ) {

        String sql = """
                SELECT inventory, armor
                FROM world_inventories
                WHERE uuid = ? AND world = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());
            statement.setString(2, world);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new InventoryData(
                            result.getString("inventory"),
                            result.getString("armor")
                    );
                }
            }

        } catch (SQLException e) {
            plugin.getLogger().severe(
                    "Inventory kon niet worden geladen."
            );
            e.printStackTrace();
        }

        return null;
    }

    public void close() {

        if (connection != null) {

            try {
                connection.close();

                plugin.getLogger().info(
                        "Database verbinding gesloten."
                );

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public record InventoryData(
            String inventory,
            String armor
    ) {
    }
}
