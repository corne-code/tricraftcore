package nl.tricraft.tricraftcore.commands;

import nl.tricraft.tricraftcore.TricraftCore;
import nl.tricraft.tricraftcore.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MoneyCommand implements CommandExecutor {

    private final TricraftCore plugin;

    public MoneyCommand(TricraftCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("tricraft.money.admin")) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming voor dit commando."
            );

            return true;
        }

        if (args.length == 0) {

            sendHelp(sender);

            return true;
        }

        String action = args[0].toLowerCase();

        if (action.equals("give")) {

            return give(sender, args);
        }

        if (action.equals("take")) {

            return take(sender, args);
        }

        if (action.equals("set")) {

            return set(sender, args);
        }

        if (action.equals("balance")
                || action.equals("bal")) {

            return balance(sender, args);
        }

        sendHelp(sender);

        return true;
    }

    private boolean give(
            CommandSender sender,
            String[] args
    ) {

        if (args.length != 3) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /money give <speler> <bedrag>"
            );

            return true;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[1]);

        Double amount =
                parseAmount(sender, args[2]);

        if (amount == null) {
            return true;
        }

        EconomyManager economy =
                plugin.getEconomyManager();

        economy.deposit(
                target,
                amount
        );

        sender.sendMessage(
                ChatColor.GREEN
                        + "Je hebt "
                        + ChatColor.GOLD
                        + economy.format(amount)
                        + ChatColor.GREEN
                        + " gegeven aan "
                        + ChatColor.YELLOW
                        + target.getName()
                        + ChatColor.GREEN
                        + "."
        );

        return true;
    }

    private boolean take(
            CommandSender sender,
            String[] args
    ) {

        if (args.length != 3) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /money take <speler> <bedrag>"
            );

            return true;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[1]);

        Double amount =
                parseAmount(sender, args[2]);

        if (amount == null) {
            return true;
        }

        EconomyManager economy =
                plugin.getEconomyManager();

        double current =
                economy.getBalance(target);

        double newBalance =
                Math.max(
                        0,
                        current - amount
                );

        economy.setBalance(
                target,
                newBalance
        );

        sender.sendMessage(
                ChatColor.GREEN
                        + "Je hebt "
                        + ChatColor.GOLD
                        + economy.format(amount)
                        + ChatColor.GREEN
                        + " afgehaald van "
                        + ChatColor.YELLOW
                        + target.getName()
                        + ChatColor.GREEN
                        + "."
        );

        return true;
    }

    private boolean set(
            CommandSender sender,
            String[] args
    ) {

        if (args.length != 3) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /money set <speler> <bedrag>"
            );

            return true;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[1]);

        Double amount =
                parseAmount(sender, args[2]);

        if (amount == null) {
            return true;
        }

        EconomyManager economy =
                plugin.getEconomyManager();

        economy.setBalance(
                target,
                amount
        );

        sender.sendMessage(
                ChatColor.GREEN
                        + "Het saldo van "
                        + ChatColor.YELLOW
                        + target.getName()
                        + ChatColor.GREEN
                        + " is ingesteld op "
                        + ChatColor.GOLD
                        + economy.format(amount)
                        + ChatColor.GREEN
                        + "."
        );

        return true;
    }

    private boolean balance(
            CommandSender sender,
            String[] args
    ) {

        if (args.length != 2) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /money balance <speler>"
            );

            return true;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[1]);

        EconomyManager economy =
                plugin.getEconomyManager();

        double balance =
                economy.getBalance(target);

        sender.sendMessage(
                ChatColor.YELLOW
                        + target.getName()
                        + ChatColor.GREEN
                        + " heeft "
                        + ChatColor.GOLD
                        + economy.format(balance)
                        + ChatColor.GREEN
                        + "."
        );

        return true;
    }

    private Double parseAmount(
            CommandSender sender,
            String input
    ) {

        double amount;

        try {

            amount =
                    Double.parseDouble(input);

        } catch (NumberFormatException e) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Vul een geldig bedrag in."
            );

            return null;
        }

        if (amount < 0) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Het bedrag mag niet negatief zijn."
            );

            return null;
        }

        if (amount > 1_000_000_000) {

            sender.sendMessage(
                    ChatColor.RED
                            + "Het bedrag is te hoog."
            );

            return null;
        }

        return amount;
    }

    private void sendHelp(
            CommandSender sender
    ) {

        sender.sendMessage(
                ChatColor.AQUA
                        + "§lTricraft Economy"
        );

        sender.sendMessage(
                ChatColor.GRAY
                        + "/money give <speler> <bedrag>"
        );

        sender.sendMessage(
                ChatColor.GRAY
                        + "/money take <speler> <bedrag>"
        );

        sender.sendMessage(
                ChatColor.GRAY
                        + "/money set <speler> <bedrag>"
        );

        sender.sendMessage(
                ChatColor.GRAY
                        + "/money balance <speler>"
        );
    }
}
