package nl.tricraft.tricraftcore.commands;

import nl.tricraft.tricraftcore.TricraftCore;
import nl.tricraft.tricraftcore.economy.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    private final TricraftCore plugin;

    public PayCommand(TricraftCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(
                    "Dit commando kan alleen door een speler worden gebruikt."
            );
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /pay <speler> <bedrag>"
            );
            return true;
        }

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(
                    ChatColor.RED
                            + "Deze speler is niet gevonden."
            );
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je kunt jezelf geen geld sturen."
            );
            return true;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(
                    ChatColor.RED
                            + "Vul een geldig bedrag in."
            );
            return true;
        }

        if (amount <= 0) {
            player.sendMessage(
                    ChatColor.RED
                            + "Het bedrag moet groter zijn dan 0."
            );
            return true;
        }

        if (amount > 1_000_000_000) {
            player.sendMessage(
                    ChatColor.RED
                            + "Dat bedrag is te hoog."
            );
            return true;
        }

        EconomyManager economy =
                plugin.getEconomyManager();

        if (!economy.withdraw(player, amount)) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt niet genoeg geld."
            );
            return true;
        }

        economy.deposit(target, amount);

        player.sendMessage(
                ChatColor.GREEN
                        + "Je hebt "
                        + ChatColor.GOLD
                        + economy.format(amount)
                        + ChatColor.GREEN
                        + " gestuurd naar "
                        + ChatColor.YELLOW
                        + target.getName()
                        + ChatColor.GREEN
                        + "."
        );

        if (target.isOnline()) {

            Player onlineTarget =
                    target.getPlayer();

            if (onlineTarget != null) {
                onlineTarget.sendMessage(
                        ChatColor.GREEN
                                + "Je hebt "
                                + ChatColor.GOLD
                                + economy.format(amount)
                                + ChatColor.GREEN
                                + " ontvangen van "
                                + ChatColor.YELLOW
                                + player.getName()
                                + ChatColor.GREEN
                                + "."
                );
            }
        }

        return true;
    }
}
