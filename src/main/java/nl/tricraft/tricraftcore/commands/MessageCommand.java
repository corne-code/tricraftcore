package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageCommand implements CommandExecutor {

    private final Map<UUID, UUID> lastMessage = new HashMap<>();

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

        // =========================
        // /msg <speler> <bericht>
        // =========================

        if (commandName.equals("msg")
                || commandName.equals("tell")
                || commandName.equals("whisper")) {

            if (args.length < 2) {
                player.sendMessage(
                        ChatColor.RED
                                + "Gebruik: /msg <speler> <bericht>"
                );
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                player.sendMessage(
                        ChatColor.RED
                                + "Deze speler is niet online."
                );
                return true;
            }

            String message = String.join(
                    " ",
                    java.util.Arrays.copyOfRange(
                            args,
                            1,
                            args.length
                    )
            );

            sendPrivateMessage(
                    player,
                    target,
                    message
            );

            return true;
        }

        // =========================
        // /r <bericht>
        // =========================

        if (commandName.equals("r")
                || commandName.equals("reply")) {

            if (args.length < 1) {
                player.sendMessage(
                        ChatColor.RED
                                + "Gebruik: /r <bericht>"
                );
                return true;
            }

            UUID targetUUID =
                    lastMessage.get(player.getUniqueId());

            if (targetUUID == null) {
                player.sendMessage(
                        ChatColor.RED
                                + "Je hebt niemand om naar te antwoorden."
                );
                return true;
            }

            Player target =
                    Bukkit.getPlayer(targetUUID);

            if (target == null) {
                player.sendMessage(
                        ChatColor.RED
                                + "Deze speler is niet meer online."
                );
                return true;
            }

            String message = String.join(
                    " ",
                    args
            );

            sendPrivateMessage(
                    player,
                    target,
                    message
            );

            return true;
        }

        return false;
    }

    private void sendPrivateMessage(
            Player sender,
            Player target,
            String message
    ) {

        sender.sendMessage(
                ChatColor.GRAY
                        + "» "
                        + ChatColor.LIGHT_PURPLE
                        + "Jij → "
                        + target.getName()
                        + ChatColor.GRAY
                        + ": "
                        + ChatColor.WHITE
                        + message
        );

        target.sendMessage(
                ChatColor.GRAY
                        + "» "
                        + ChatColor.LIGHT_PURPLE
                        + sender.getName()
                        + " → Jij"
                        + ChatColor.GRAY
                        + ": "
                        + ChatColor.WHITE
                        + message
        );

        lastMessage.put(
                sender.getUniqueId(),
                target.getUniqueId()
        );

        lastMessage.put(
                target.getUniqueId(),
                sender.getUniqueId()
        );
    }
}
