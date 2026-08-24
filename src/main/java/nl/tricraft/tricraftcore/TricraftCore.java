package nl.tricraft.tricraftcore;

import nl.tricraft.tricraftcore.database.DatabaseManager;
import nl.tricraft.tricraftcore.economy.EconomyManager;
import nl.tricraft.tricraftcore.economy.TricraftEconomyProvider;
import nl.tricraft.tricraftcore.inventory.WorldInventoryManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class TricraftCore extends JavaPlugin {

    private static TricraftCore instance;

    private DatabaseManager databaseManager;
    private WorldInventoryManager worldInventoryManager;
    private EconomyManager economyManager;
    private TricraftEconomyProvider economyProvider;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        // ==============================
        // DATABASE
        // ==============================

        databaseManager =
                new DatabaseManager(this);

        databaseManager.connect();

        // ==============================
        // WORLD INVENTORIES
        // ==============================

        worldInventoryManager =
                new WorldInventoryManager(this);

        getServer()
                .getPluginManager()
                .registerEvents(
                        worldInventoryManager,
                        this
                );

        // ==============================
        // ECONOMY
        // ==============================

        if (getConfig().getBoolean(
                "economy.enabled",
                true
        )) {

            economyManager =
                    new EconomyManager(this);

            economyProvider =
                    new TricraftEconomyProvider(
                            economyManager
                    );

            getServer()
                    .getServicesManager()
                    .register(
                            Economy.class,
                            economyProvider,
                            this,
                            ServicePriority.Highest
                    );

            getLogger().info(
                    "TricraftCore Economy geregistreerd via Vault."
            );
        }

        getLogger().info(
                "================================="
        );

        getLogger().info(
                "       TricraftCore gestart"
        );

        getLogger().info(
                "       Versie: "
                        + getDescription().getVersion()
        );

        getLogger().info(
                "       Database: AAN"
        );

        getLogger().info(
                "       World Inventory: AAN"
        );

        getLogger().info(
                "       Economy: AAN"
        );

        getLogger().info(
                "================================="
        );
    }

    @Override
    public void onDisable() {

        if (economyProvider != null) {

            getServer()
                    .getServicesManager()
                    .unregister(
                            Economy.class,
                            economyProvider
                    );
        }

        if (worldInventoryManager != null) {

            for (
                    Player player :
                    getServer().getOnlinePlayers()
            ) {

                worldInventoryManager.savePlayer(
                        player
                );
            }
        }

        if (databaseManager != null) {
            databaseManager.close();
        }

        getLogger().info(
                "TricraftCore uitgeschakeld."
        );
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

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!command.getName()
                .equalsIgnoreCase("tricraft")) {

            return false;
        }

        if (args.length == 0) {

            sender.sendMessage(
                    ChatColor.AQUA
                            + "§lTricraftCore"
            );

            sender.sendMessage(
                    ChatColor.GRAY
                            + "Versie: "
                            + ChatColor.WHITE
                            + getDescription()
                            .getVersion()
            );

            sender.sendMessage("");

            sender.sendMessage(
                    ChatColor.GRAY
                            + "/tricraft reload"
            );

            return true;
        }

        if (args[0]
                .equalsIgnoreCase("reload")) {

            if (!sender.hasPermission(
                    "tricraft.admin"
            )) {

                sender.sendMessage(
                        ChatColor.RED
                                + "Je hebt geen toestemming."
                );

                return true;
            }

            reloadConfig();

            sender.sendMessage(
                    ChatColor.GREEN
                            + "TricraftCore configuratie "
                            + "opnieuw geladen."
            );

            return true;
        }

        sender.sendMessage(
                ChatColor.RED
                        + "Onbekend commando."
        );

        return true;
    }
}
