package com.adharsh.mymcLite.Utils;

import com.adharsh.mymcLite.Events.JoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PluginMessages implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {

        if (!channel.equals("BungeeCord")) return;

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String subchannel = in.readUTF();

            if (subchannel.equals("GetServers")) {
                String serverList = in.readUTF();
                String[] servers = serverList.split(", ");
                JoinEvent.setServers(new ArrayList<>(Arrays.asList(servers)));
                JoinEvent.setRequestedServerList(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
