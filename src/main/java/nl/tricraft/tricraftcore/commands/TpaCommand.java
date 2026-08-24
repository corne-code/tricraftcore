package nl.tricraft.tricraftcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {

    private static final Map<UUID, UUID> requests = new HashMap<>();

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

        String commandName =
                command.getName().toLowerCase();

        // =========================
        // /tpa <speler>
        // =========================

        if (commandName.equals("tpa")) {

            if (args.length != 1) {

                player.sendMessage(
                        ChatColor.RED
                                + "Gebruik: /tpa <speler>"
                );

                return true;
            }

            Player target =
                    Bukkit.getPlayerExact(args[0]);

            if (target == null) {

                player.sendMessage(
                        ChatColor.RED
                                + "Deze speler is niet online."
                );

                return true;
            }

            if (target.equals(player)) {

                player.sendMessage(
                        ChatColor.RED
                                + "Je kunt jezelf geen teleportverzoek sturen."
                );

                return true;
            }

            requests.put(
                    target.getUniqueId(),
                    player.getUniqueId()
            );

            player.sendMessage(
                    ChatColor.GREEN
                            + "Teleportverzoek gestuurd naar "
                            + ChatColor.YELLOW
                            + target.getName()
            );

            target.sendMessage("");
            target.sendMessage(
                    ChatColor.AQUA
                            + "§lTeleportverzoek"
            );

            target.sendMessage(
                    ChatColor.YELLOW
                            + player.getName()
                            + ChatColor.GRAY
                            + " wil naar jou teleporteren."
            );

            target.sendMessage(
                    ChatColor.GREEN
                            + "/tpaccept"
                            + ChatColor.GRAY
                            + " om te accepteren."
            );

            target.sendMessage(
                    ChatColor.RED
                            + "/tpdeny"
                            + ChatColor.GRAY
                            + " om te weigeren."
            );

            target.sendMessage("");

            return true;
        }

        // =========================
        // /tpaccept
        // =========================

        if (commandName.equals("tpaccept")) {

            UUID requesterUUID =
                    requests.remove(player.getUniqueId());

            if (requesterUUID == null) {

                player.sendMessage(
                        ChatColor.RED
                                + "Je hebt geen openstaand teleportverzoek."
                );

                return true;
            }

            Player requester =
                    Bukkit.getPlayer(requesterUUID);

            if (requester == null) {

                player.sendMessage(
                        ChatColor.RED
                                + "De speler is niet meer online."
                );

                return true;
            }

            requester.teleport(player);

            player.sendMessage(
                    ChatColor.GREEN
                            + requester.getName()
                            + " is naar jou geteleporteerd."
            );

            requester.sendMessage(
                    ChatColor.GREEN
                            + "Je bent naar "
                            + ChatColor.YELLOW
                            + player.getName()
                            + ChatColor.GREEN
                            + " geteleporteerd."
            );

            return true;
        }

        // =========================
        // /tpdeny
        // =========================

        if (commandName.equals("tpdeny")) {

            UUID requesterUUID =
                    requests.remove(player.getUniqueId());

            if (requesterUUID == null) {

                player.sendMessage(
                        ChatColor.RED
                                + "Je hebt geen openstaand teleportverzoek."
                );

                return true;
            }

            Player requester =
                    Bukkit.getPlayer(requesterUUID);

            player.sendMessage(
                    ChatColor.RED
                            + "Teleportverzoek geweigerd."
            );

            if (requester != null) {

                requester.sendMessage(
                        ChatColor.RED
                                + player.getName()
                                + " heeft je teleportverzoek geweigerd."
                );
            }

            return true;
        }

        return false;
    }
}
