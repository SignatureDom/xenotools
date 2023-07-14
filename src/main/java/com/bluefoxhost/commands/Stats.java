package com.bluefoxhost.commands;

import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class Stats extends Command {

    public Stats() {
        super(new CommandData("stats", "Get the bot's stats"));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long days = TimeUnit.MILLISECONDS.toDays(uptime);
        long hours = TimeUnit.MILLISECONDS.toHours(uptime) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime) % 60;

        int guildCount = event.getJDA().getGuilds().size();

        double cpuUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        String uptimeString = String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);

        CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                .setTitle("Bot Statistics")
                .setField("Uptime", uptimeString, false)
                .setField("Guild Count", String.valueOf(guildCount), true)
                .setField("CPU Usage", String.format("%.2f%%", cpuUsage), true)
                .setField("Memory Usage", String.format("%d MB", usedMemory / 1024 / 1024), true);

        event.replyEmbeds(embedBuilder.build()).queue();
    }


}
