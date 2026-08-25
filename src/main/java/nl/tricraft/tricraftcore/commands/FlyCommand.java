package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.fly")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om fly te gebruiken."
            );
            return true;
        }

        boolean flying = !player.getAllowFlight();

        player.setAllowFlight(flying);

        if (!flying) {
            player.setFlying(false);
        }

        if (flying) {

            player.sendMessage(
                    ChatColor.GREEN
                            + "Fly is "
                            + ChatColor.YELLOW
                            + "aan"
                            + ChatColor.GREEN
                            + "."
            );

        } else {

            player.sendMessage(
                    ChatColor.GREEN
                            + "Fly is "
                            + ChatColor.RED
                            + "uit"
                            + ChatColor.GREEN
                            + "."
            );
        }

        return true;
    }
}
