package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WeatherCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.weather")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om het weer te veranderen."
            );
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /weather <clear|rain|storm>"
            );
            return true;
        }

        World world = player.getWorld();

        switch (args[0].toLowerCase()) {

            case "clear":

                world.setStorm(false);
                world.setThundering(false);

                player.sendMessage(
                        ChatColor.GREEN
                                + "Het weer is veranderd naar helder."
                );

                break;

            case "rain":

                world.setStorm(true);
                world.setThundering(false);

                player.sendMessage(
                        ChatColor.GREEN
                                + "Het regent nu."
                );

                break;

            case "storm":

                world.setStorm(true);
                world.setThundering(true);

                player.sendMessage(
                        ChatColor.GREEN
                                + "Er is nu een onweersstorm."
                );

                break;

            default:

                player.sendMessage(
                        ChatColor.RED
                                + "Gebruik: /weather <clear|rain|storm>"
                );

                break;
        }

        return true;
    }
}
