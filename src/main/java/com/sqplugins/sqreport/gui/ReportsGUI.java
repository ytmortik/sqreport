package com.sqplugins.sqreport.gui;

import com.sqplugins.sqreport.SqReport;
import com.sqplugins.sqreport.storage.ReportStorage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

import static org.bukkit.ChatColor.*;

public class ReportsGUI implements Listener {

    private final SqReport plugin;
    private final Player viewer;
    private Inventory gui;

    public ReportsGUI(SqReport plugin, Player viewer) {
        this.plugin = plugin;
        this.viewer = viewer;
    }

    public void open() {
        ReportStorage storage = plugin.getReportStorage();
        List<ReportStorage.Report> reports = storage.getReports();

        gui = Bukkit.createInventory(null, 54, plugin.getConfig().getString("gui.title"));

        for (int i = 0; i < reports.size() && i < 54; i++) {
            ReportStorage.Report r = reports.get(i);
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(Bukkit.getPlayerExact(r.getTarget()));
            meta.setDisplayName(GOLD + r.getTarget());
            meta.setLore(List.of(
                    GREEN + "Reported by: " + r.getReporter(),
                    RED + "Reason: " + r.getReason()
            ));
            skull.setItemMeta(meta);
            gui.setItem(i, skull);
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                if (e.getInventory().equals(gui)) {
                    e.setCancelled(true);

                    if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                        SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
                        if (meta != null && meta.getOwningPlayer() != null) {
                            Player target = Bukkit.getPlayer(meta.getOwningPlayer().getUniqueId());
                            if (target != null && viewer.isOnline()) {
                                viewer.closeInventory();
                                viewer.setGameMode(GameMode.SPECTATOR);
                                viewer.teleport(target);
                                viewer.sendMessage(GREEN + "You are now spectating " + target.getName());
                            }
                        }
                    }
                }
            }
        }, plugin);

        viewer.openInventory(gui);
    }

}
