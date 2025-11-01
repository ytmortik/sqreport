package com.sqplugins.sqreport.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    /**
     * Převádí & barvy na Minecraft barvy
     * Např. &aHello -> Hello zeleně
     */
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Přidá prefix [SqReport] před zprávu
     */
    public static String prefix(String message) {
        return color("&7[&cSqReport&7] &f" + message);
    }
}