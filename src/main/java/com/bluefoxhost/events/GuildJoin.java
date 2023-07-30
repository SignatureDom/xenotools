package com.bluefoxhost.events;

import com.bluefoxhost.handlers.StatusHandler;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class GuildJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        StatusHandler.increase();

        Member owner = event.getGuild().getOwner();

        CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                .setTitle("XenoTools")
                .setDescription("Thanks for adding me to your server!\n" +
                        "To get started, type `/help` in any channel.")
                .setColor(Color.RED);

        owner.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage(embedBuilder.build()).queue());
    }
}
