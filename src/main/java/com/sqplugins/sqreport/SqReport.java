package com.sqplugins.sqreport;

import com.sqplugins.sqreport.commands.ReportCommand;
import com.sqplugins.sqreport.commands.ReportsCommand;
import com.sqplugins.sqreport.commands.SqReportCommand;
import com.sqplugins.sqreport.storage.ReportStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SqReport extends JavaPlugin {

    private static SqReport instance;
    private ReportStorage reportStorage;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getLogger().info("§aSqReport has been enabled!");

        reportStorage = new ReportStorage(this);
        reportStorage.loadReports();

        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("reports").setExecutor(new ReportsCommand(this));
        getCommand("sqreport").setExecutor(new SqReportCommand(this));

        getCommand("report").setTabCompleter(new ReportCommand(this));
        getCommand("reports").setTabCompleter(new ReportsCommand(this));
        getCommand("sqreport").setTabCompleter(new SqReportCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("§eSaving all reports...");
        reportStorage.saveReports();
        getLogger().info("§cSqReport has been disabled!");
    }

    public static SqReport getInstance() {
        return instance;
    }

    public ReportStorage getReportStorage() {
        return reportStorage;
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§7[§cSqReport§7] §f" + message);
    }

}
