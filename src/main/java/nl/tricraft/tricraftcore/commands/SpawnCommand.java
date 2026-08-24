package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

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

        Location spawn =
                player.getWorld().getSpawnLocation();

        player.teleport(spawn);

        player.sendMessage(
                ChatColor.GREEN
                        + "Je bent naar de spawn geteleporteerd."
        );

        return true;
    }
}
