package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

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

        String commandName = command.getName().toLowerCase();

        switch (commandName) {

            case "home":

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Home-systeem wordt binnenkort gekoppeld."
                );

                break;

            case "sethome":

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Home wordt opgeslagen zodra de database-koppeling klaar is."
                );

                break;

            case "delhome":

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Home verwijderen wordt binnenkort toegevoegd."
                );

                break;

            case "homes":

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Je homes worden binnenkort weergegeven."
                );

                break;

            default:

                player.sendMessage(
                        ChatColor.RED
                                + "Onbekend home-commando."
                );
        }

        return true;
    }
}
