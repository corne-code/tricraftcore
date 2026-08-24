package nl.tricraft.tricraftcore.economy;

import nl.tricraft.tricraftcore.TricraftCore;
import nl.tricraft.tricraftcore.database.DatabaseManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EconomyManager {

    private final TricraftCore plugin;
    private final DatabaseManager database;

    public EconomyManager(TricraftCore plugin) {
        this.plugin = plugin;
        this.database = plugin.getDatabaseManager();
    }

    public double getBalance(UUID uuid) {
        return database.getBalance(uuid);
    }

    public double getBalance(Player player) {
        return getBalance(player.getUniqueId());
    }

    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getUniqueId());
    }

    public void setBalance(UUID uuid, double amount) {

        if (amount < 0) {
            amount = 0;
        }

        database.setBalance(uuid, amount);
    }

    public void setBalance(Player player, double amount) {
        setBalance(player.getUniqueId(), amount);
    }

    public void setBalance(
            OfflinePlayer player,
            double amount
    ) {
        setBalance(player.getUniqueId(), amount);
    }

    public void deposit(UUID uuid, double amount) {

        if (amount <= 0) {
            return;
        }

        double current = getBalance(uuid);

        setBalance(
                uuid,
                current + amount
        );
    }

    public void deposit(
            OfflinePlayer player,
            double amount
    ) {
        deposit(
                player.getUniqueId(),
                amount
        );
    }

    public boolean withdraw(
            UUID uuid,
            double amount
    ) {

        if (amount <= 0) {
            return false;
        }

        double current = getBalance(uuid);

        if (current < amount) {
            return false;
        }

        setBalance(
                uuid,
                current - amount
        );

        return true;
    }

    public boolean withdraw(
            OfflinePlayer player,
            double amount
    ) {
        return withdraw(
                player.getUniqueId(),
                amount
        );
    }

    public boolean has(
            UUID uuid,
            double amount
    ) {
        return getBalance(uuid) >= amount;
    }

    public boolean has(
            OfflinePlayer player,
            double amount
    ) {
        return has(
                player.getUniqueId(),
                amount
        );
    }

    public boolean createAccount(
            OfflinePlayer player
    ) {
        database.getBalance(
                player.getUniqueId()
        );

        return true;
    }

    public String format(double amount) {

        String currency =
                plugin.getConfig().getString(
                        "economy.currency",
                        "$"
                );

        return currency +
                String.format(
                        "%.2f",
                        amount
                );
    }

    public String getCurrency() {

        return plugin.getConfig().getString(
                "economy.currency",
                "$"
        );
    }
}
