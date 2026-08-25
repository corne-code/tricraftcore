package nl.tricraft.tricraftcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

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

        if (!player.hasPermission("tricraft.gamemode")) {
            player.sendMessage(
                    ChatColor.RED
                            + "Je hebt geen toestemming om je gamemode te veranderen."
            );
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(
                    ChatColor.RED
                            + "Gebruik: /gamemode <survival|creative|adventure|spectator>"
            );
            return true;
        }

        GameMode gameMode;

        switch (args[0].toLowerCase()) {

            case "survival":
            case "s":
            case "0":
                gameMode = GameMode.SURVIVAL;
                break;

            case "creative":
            case "c":
            case "1":
                gameMode = GameMode.CREATIVE;
                break;

            case "adventure":
            case "a":
            case "2":
                gameMode = GameMode.ADVENTURE;
                break;

            case "spectator":
            case "spec":
            case "sp":
            case "3":
                gameMode = GameMode.SPECTATOR;
                break;

            default:
                player.sendMessage(
                        ChatColor.RED
                                + "Ongeldige gamemode."
                );
                return true;
        }

        player.setGameMode(gameMode);

        player.sendMessage(
                ChatColor.GREEN
                        + "Gamemode veranderd naar "
                        + ChatColor.YELLOW
                        + gameMode.name().toLowerCase()
                        + ChatColor.GREEN
                        + "."
        );

        return true;
    }
}
