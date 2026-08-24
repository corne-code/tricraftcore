import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;
package nl.tricraft.tricraftcore.economy;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public class TricraftEconomyProvider extends AbstractEconomy {

    private final EconomyManager economy;

    public TricraftEconomyProvider(
            EconomyManager economy
    ) {
        this.economy = economy;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "TricraftCore";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return economy.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return economy.getCurrency();
    }

    @Override
    public String currencyNameSingular() {
        return economy.getCurrency();
    }

    @Override
    public boolean hasAccount(String playerName) {
        OfflinePlayer player =
                getPlayer(playerName);

        return player != null;
    }

    @Override
    public boolean hasAccount(
            String playerName,
            String worldName
    ) {
        return hasAccount(playerName);
    }

    @Override
    public double getBalance(
            String playerName
    ) {
        OfflinePlayer player =
                getPlayer(playerName);

        if (player == null) {
            return 0.0;
        }

        return economy.getBalance(player);
    }

    @Override
    public double getBalance(
            String playerName,
            String world
    ) {
        return getBalance(playerName);
    }

    @Override
    public boolean has(
            String playerName,
            double amount
    ) {
        OfflinePlayer player =
                getPlayer(playerName);

        if (player == null) {
            return false;
        }

        return economy.has(
                player,
                amount
        );
    }

    @Override
    public boolean has(
            String playerName,
            String worldName,
            double amount
    ) {
        return has(
                playerName,
                amount
        );
    }

    @Override
    public EconomyResponse withdrawPlayer(
            String playerName,
            double amount
    ) {

        if (amount < 0) {
            return failure(
                    amount,
                    "Bedrag mag niet negatief zijn."
            );
        }

        OfflinePlayer player =
                getPlayer(playerName);

        if (player == null) {
            return failure(
                    amount,
                    "Speler bestaat niet."
            );
        }

        if (!economy.withdraw(
                player,
                amount
        )) {

            return failure(
                    amount,
                    "Niet genoeg geld."
            );
        }

        return success(
                amount,
                economy.getBalance(player)
        );
    }

    @Override
    public EconomyResponse withdrawPlayer(
            String playerName,
            String worldName,
            double amount
    ) {
        return withdrawPlayer(
                playerName,
                amount
        );
    }

    @Override
    public EconomyResponse depositPlayer(
            String playerName,
            double amount
    ) {

        if (amount < 0) {
            return failure(
                    amount,
                    "Bedrag mag niet negatief zijn."
            );
        }

        OfflinePlayer player =
                getPlayer(playerName);

        if (player == null) {
            return failure(
                    amount,
                    "Speler bestaat niet."
            );
        }

        economy.deposit(
                player,
                amount
        );

        return success(
                amount,
                economy.getBalance(player)
        );
    }

    @Override
    public EconomyResponse depositPlayer(
            String playerName,
            String worldName,
            double amount
    ) {
        return depositPlayer(
                playerName,
                amount
        );
    }

    @Override
    public EconomyResponse createBank(
            String name,
            String player
    ) {
        return notImplemented();
    }

    @Override
    public EconomyResponse deleteBank(
            String name
    ) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankBalance(
            String name
    ) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankHas(
            String name,
            double amount
    ) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankWithdraw(
            String name,
            double amount
    ) {
        return notImplemented();
    }

    @Override
    public EconomyResponse bankDeposit(
            String name,
            double amount
    ) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankOwner(
            String name,
            String playerName
    ) {
        return notImplemented();
    }

    @Override
    public EconomyResponse isBankMember(
            String name,
            String playerName
    ) {
        return notImplemented();
    }

    @Override
    public List<String> getBanks() {
        return new java.util.ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(
            String playerName
    ) {

        OfflinePlayer player =
                getPlayer(playerName);

        if (player == null) {
            return false;
        }

        return economy.createAccount(player);
    }

    @Override
    public boolean createPlayerAccount(
            String playerName,
            String worldName
    ) {
        return createPlayerAccount(
                playerName
        );
    }

    private OfflinePlayer getPlayer(
            String name
    ) {

        if (name == null || name.isBlank()) {
            return null;
        }

        return org.bukkit.Bukkit
                .getOfflinePlayer(name);
    }

    private EconomyResponse success(
            double amount,
            double balance
    ) {

        return new EconomyResponse(
                amount,
                balance,
                EconomyResponse.ResponseType.SUCCESS,
                null
        );
    }

    private EconomyResponse failure(
            double amount,
            String message
    ) {

        return new EconomyResponse(
                amount,
                0.0,
                EconomyResponse.ResponseType.FAILURE,
                message
        );
    }

    private EconomyResponse notImplemented() {

        return new EconomyResponse(
                0.0,
                0.0,
                EconomyResponse.ResponseType.NOT_IMPLEMENTED,
                "Bank accounts worden niet ondersteund."
        );
    }
}
