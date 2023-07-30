package com.bluefoxhost.events;

import com.bluefoxhost.handlers.StatusHandler;
import com.bluefoxhost.handlers.DatabaseHandler;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;

public class GuildJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        StatusHandler.increase();

        // Send a message to the owner of the server
        Member owner = event.getGuild().getOwner();
        if (owner != null) {
            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("XenoTools")
                    .setDescription("Thanks for adding me to your server!\n" +
                            "To get started, type `/help` in any channel.")
                    .setColor(Color.RED);

            owner.getUser().openPrivateChannel().queue((channel) ->
                    channel.sendMessage(embedBuilder.build()).queue());
        } else {
            System.err.println("Could not retrieve owner for guild " + event.getGuild().getId());
        }

        // Ensure guild exists in database
        DatabaseHandler instance = DatabaseHandler.getInstance();
        try {
            if (!instance.guildExists(event.getGuild().getId())) {
                instance.addGuild(event.getGuild().getId());
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring guild " + event.getGuild().getId() + " is in database");
            e.printStackTrace();
        }


    }
}
