package com.sqplugins.sqreport.commands;

import com.sqplugins.sqreport.SqReport;
import com.sqplugins.sqreport.gui.ReportsGUI;
import com.sqplugins.sqreport.storage.ReportStorage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReportsCommand implements CommandExecutor, TabCompleter {

    private final SqReport plugin;

    public ReportsCommand(SqReport plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("sqreport.view")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can open the GUI!");
            return true;
        }

        ReportStorage storage = plugin.getReportStorage();

        if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
            storage.clearReports();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.cleared-all")));
            return true;
        }

        new ReportsGUI(plugin, player).open();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("clear");
        }
        return completions;
    }

}
