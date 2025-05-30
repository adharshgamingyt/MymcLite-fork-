package com.adharsh.mymcLite.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class DisabledCommands implements Listener {

    private final Plugin plugin;
    public DisabledCommands(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        List<String> disabledCommands = plugin.getConfig().getStringList("disabled-commands.commands");
        if (disabledCommands.isEmpty()) {
            return;
        }

        String command = e.getMessage().substring(1);
        String[] commandParts = command.split(" ");
        String mainCommand = commandParts[0].toLowerCase();
        Player p = e.getPlayer();

        if (disabledCommands.contains(mainCommand) && !p.hasPermission("mymclite.disabled-commands.bypass")) {
            e.setCancelled(true);

            String disableMessage = plugin.getConfig().getString("disabled-commands.disable-message");
            if (disableMessage == null) {
                disableMessage = "&cThis command has been disabled.";
            }

            p.sendMessage(ChatColor.translateAlternateColorCodes('&', disableMessage));
        }
    }
}
