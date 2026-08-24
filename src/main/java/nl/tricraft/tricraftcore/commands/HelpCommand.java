package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        sender.sendMessage("");
        sender.sendMessage(
                ChatColor.AQUA
                        + "§lTricraftCore"
        );
        sender.sendMessage("");

        sender.sendMessage(
                ChatColor.YELLOW
                        + "/balance"
                + ChatColor.GRAY
                        + " - Bekijk je geld"
        );

        sender.sendMessage(
                ChatColor.YELLOW
                        + "/pay <speler> <bedrag>"
                + ChatColor.GRAY
                        + " - Stuur geld"
        );

        sender.sendMessage(
                ChatColor.YELLOW
                        + "/spawn"
                + ChatColor.GRAY
                        + " - Ga naar spawn"
        );

        sender.sendMessage(
                ChatColor.YELLOW
                        + "/help"
                + ChatColor.GRAY
                        + " - Bekijk deze help"
        );

        if (sender.hasPermission("tricraft.money.admin")) {

            sender.sendMessage("");

            sender.sendMessage(
                    ChatColor.RED
                            + "Admin Economy:"
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

        sender.sendMessage("");

        return true;
    }
}
