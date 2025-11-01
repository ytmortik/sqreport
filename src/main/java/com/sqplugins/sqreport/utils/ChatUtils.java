package com.sqplugins.sqreport.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String prefix(String message) {
        return color("&7[&cSqReport&7] &f" + message);
    }

}
