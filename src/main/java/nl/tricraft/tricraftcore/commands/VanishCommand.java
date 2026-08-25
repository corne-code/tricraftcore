package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishCommand implements CommandExecutor {

    private final Set<UUID> vanishedPlayers = new HashSet<>();

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

        if (!player.hasPermission("tricraft.vanish")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om vanish te gebruiken."
            );
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (vanishedPlayers.contains(uuid)) {

            vanishedPlayers.remove(uuid);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(
                        Bukkit.getPluginManager()
                                .getPlugin("TricraftCore"),
                        player
                );
            }

            player.sendMessage(
                    ChatColor.GREEN
                            + "Vanish is "
                            + ChatColor.RED
                            + "uit"
                            + ChatColor.GREEN
                            + "."
            );

        } else {

            vanishedPlayers.add(uuid);

            for (Player online : Bukkit.getOnlinePlayers()) {

                if (!online.hasPermission("tricraft.vanish.see")) {
                    online.hidePlayer(
                            Bukkit.getPluginManager()
                                    .getPlugin("TricraftCore"),
                            player
                    );
                }
            }

            player.sendMessage(
                    ChatColor.GREEN
                            + "Vanish is "
                            + ChatColor.YELLOW
                            + "aan"
                            + ChatColor.GREEN
                            + "."
            );
        }

        return true;
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(
                player.getUniqueId()
        );
    }
}
