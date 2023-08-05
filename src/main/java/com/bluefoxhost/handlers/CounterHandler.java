package com.bluefoxhost.handlers;

import com.bluefoxhost.models.Counter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CounterHandler {
    private static final DatabaseHandler instance = DatabaseHandler.getInstance();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static JDA jdaInstance = null;
    private static List<Counter> counters;

    public static void initialize(JDA jda) {
        jdaInstance = jda;
        scheduler.scheduleAtFixedRate(() -> updateCounters(), 0, 5, TimeUnit.MINUTES);
    }

    private static void updateCounters() {
        System.out.println("Updating counters");
        try {
            counters = instance.getAllCounters();
            for (Counter counter : counters) {
                updateCounter(counter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateCounter(Counter counter) throws SQLException {
        VoiceChannel voiceChannel = jdaInstance.getVoiceChannelById(counter.getChannelId());
        if (voiceChannel == null) {
            System.out.println("Voice channel is invalid, removing it from the database");
            instance.removeCounter(counter.getGuildId(), counter.getChannelId());
            return;
        }

        switch (counter.getType()) {
            case "members": {
                voiceChannel.getManager().setName("Members: " + voiceChannel.getGuild().getMemberCount()).queue();
                break;
            }
            case "roles": {
                voiceChannel.getManager().setName("Roles: " + voiceChannel.getGuild().getRoles().size()).queue();
                break;
            }
            case "channels": {
                voiceChannel.getManager().setName("Channels: " + voiceChannel.getGuild().getChannels().size()).queue();
                break;
            }
        }
    }
}
