package com.bluefoxhost.events;

import com.bluefoxhost.handlers.DatabaseHandler;
import com.bluefoxhost.handlers.StatusHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class Ready extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.playing(" just started"));

        DatabaseHandler database = DatabaseHandler.getInstance();

        for (Guild guild : event.getJDA().getGuilds()) {
            // Leave guild if banned
            if (database.isGuildBanned(guild.getId())) {
                guild.leave().queue();
                continue;
            }
            // Ensure guild exists in database
            try {
                if (!database.getInstance().guildExists(guild.getId())) {
                    database.getInstance().addGuild(guild.getId());
                }
            } catch (SQLException e) {
                System.err.println("Failed to ensure guild with ID " + guild.getId() + " is in database");
                e.printStackTrace();
            }
        }
    }

}
