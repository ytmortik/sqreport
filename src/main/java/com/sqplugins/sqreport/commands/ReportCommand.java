package com.sqplugins.sqreport.commands;

import com.sqplugins.sqreport.SqReport;
import com.sqplugins.sqreport.storage.ReportStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReportCommand implements CommandExecutor, TabCompleter {

    private final SqReport plugin;

    public ReportCommand(SqReport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.report-usage"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.player-not-found"));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.report-self"));
            return true;
        }

        // Join the reason
        String reason = String.join(" ", args).replace(args[0], "").trim();

        // Save report
        ReportStorage storage = plugin.getReportStorage();
        storage.addReport(player.getName(), target.getName(), reason);

        // Notify reporter
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("messages.report-sent")
                        .replace("%target%", target.getName())
                        .replace("%reason%", reason)));

        // Notify admins
        String notify = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("messages.report-notify")
                        .replace("%reporter%", player.getName())
                        .replace("%target%", target.getName())
                        .replace("%reason%", reason));

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.hasPermission("sqreport.view")) {
                p.sendMessage(notify);
            }
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                suggestions.add(online.getName());
            }
        }
        return suggestions;
    }
}