package nl.tricraft.tricraftcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class TricraftCore extends JavaPlugin {

    private static TricraftCore instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getLogger().info("=================================");
        getLogger().info("       TricraftCore gestart");
        getLogger().info("       Versie: " + getDescription().getVersion());
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("TricraftCore wordt uitgeschakeld.");
    }

    public static TricraftCore getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!command.getName().equalsIgnoreCase("tricraft")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "§lTricraftCore");
            sender.sendMessage(ChatColor.GRAY + "Versie: "
                    + ChatColor.WHITE + getDescription().getVersion());
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "/tricraft reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("tricraft.admin")) {
                sender.sendMessage(ChatColor.RED + "Je hebt geen toestemming.");
                return true;
            }

            reloadConfig();

            sender.sendMessage(
                    ChatColor.GREEN + "TricraftCore configuratie opnieuw geladen."
            );

            return true;
        }

        sender.sendMessage(ChatColor.RED + "Onbekend commando.");
        return true;
    }
}
