package nl.tricraft.tricraftcore;

import nl.tricraft.tricraftcore.database.DatabaseManager;
import nl.tricraft.tricraftcore.inventory.WorldInventoryManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class TricraftCore extends JavaPlugin {

    private static TricraftCore instance;

    private DatabaseManager databaseManager;
    private WorldInventoryManager worldInventoryManager;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        // Database
        databaseManager = new DatabaseManager(this);
        databaseManager.connect();

        // World Inventories
        worldInventoryManager = new WorldInventoryManager(this);

        getServer().getPluginManager().registerEvents(
                worldInventoryManager,
                this
        );

        getLogger().info("=================================");
        getLogger().info("       TricraftCore gestart");
        getLogger().info("       Versie: " + getDescription().getVersion());
        getLogger().info("       Database: AAN");
        getLogger().info("       World Inventory: AAN");
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {

        // Online spelers opslaan
        if (worldInventoryManager != null) {
            for (Player player : getServer().getOnlinePlayers()) {
                worldInventoryManager.savePlayer(player);
            }
        }

        // Database sluiten
        if (databaseManager != null) {
            databaseManager.close();
        }

        getLogger().info("TricraftCore uitgeschakeld.");
    }

    public static TricraftCore getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public WorldInventoryManager getWorldInventoryManager() {
        return worldInventoryManager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!command.getName().equalsIgnoreCase("tricraft")) {
            return false;
        }

        if (args.length == 0) {

            sender.sendMessage(
                    ChatColor.AQUA + "§lTricraftCore"
            );

            sender.sendMessage(
                    ChatColor.GRAY + "Versie: "
                            + ChatColor.WHITE
                            + getDescription().getVersion()
            );

            sender.sendMessage("");
            sender.sendMessage(
                    ChatColor.GRAY + "/tricraft reload"
            );

            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("tricraft.admin")) {
                sender.sendMessage(
                        ChatColor.RED + "Je hebt geen toestemming."
                );
                return true;
            }

            reloadConfig();

            sender.sendMessage(
                    ChatColor.GREEN
                            + "TricraftCore configuratie opnieuw geladen."
            );

            return true;
        }

        sender.sendMessage(
                ChatColor.RED + "Onbekend commando."
        );

        return true;
    }
}
