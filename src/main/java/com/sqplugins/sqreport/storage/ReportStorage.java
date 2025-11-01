package com.sqplugins.sqreport.storage;

import com.sqplugins.sqreport.SqReport;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportStorage {

    private final SqReport plugin;
    private final File reportFile;
    private final FileConfiguration config;

    public ReportStorage(SqReport plugin) {
        this.plugin = plugin;
        this.reportFile = new File(plugin.getDataFolder(), "reports.yml");

        if (!reportFile.exists()) {
            plugin.saveResource("reports.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(reportFile);
    }

    // -------------------------
    //   Report Objekt
    // -------------------------
    public static class Report {
        private final String reporter;
        private final String target;
        private final String reason;

        public Report(String reporter, String target, String reason) {
            this.reporter = reporter;
            this.target = target;
            this.reason = reason;
        }

        public String getReporter() { return reporter; }
        public String getTarget() { return target; }
        public String getReason() { return reason; }
    }

    // -------------------------
    //   Přidání reportu
    // -------------------------
    public void addReport(String reporter, String target, String reason) {
        String key = "reports." + System.currentTimeMillis();
        config.set(key + ".reporter", reporter);
        config.set(key + ".target", target);
        config.set(key + ".reason", reason);
        saveReports();
    }

    // -------------------------
    //   Načtení všech reportů
    // -------------------------
    public List<Report> getReports() {
        List<Report> list = new ArrayList<>();
        if (config.contains("reports")) {
            for (String key : config.getConfigurationSection("reports").getKeys(false)) {
                String reporter = config.getString("reports." + key + ".reporter");
                String target = config.getString("reports." + key + ".target");
                String reason = config.getString("reports." + key + ".reason");
                if (reporter != null && target != null && reason != null) {
                    list.add(new Report(reporter, target, reason));
                }
            }
        }
        return list;
    }

    // -------------------------
    //   Smazání všech reportů
    // -------------------------
    public void clearReports() {
        config.set("reports", null);
        saveReports();
    }

    // -------------------------
    //   Uložení do souboru
    // -------------------------
    public void saveReports() {
        try {
            config.save(reportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------
    //   Načtení reportů při startu
    // -------------------------
    public void loadReports() {
        // config je načten při inicializaci
        // pokud chceš, můžeš přidat log
        plugin.getLogger().info("Loaded " + getReports().size() + " reports.");
    }
}