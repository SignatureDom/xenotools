package com.bluefoxhost.commands;

import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ServerInfo extends Command {

    public ServerInfo() {
        super(new CommandData("serverinfo", "Get the server's information"));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Guild guild = event.getGuild();

        if (guild == null) {
            CustomEmbedBuilder errorEmbed = new CustomEmbedBuilder()
                    .setTitle("Error")
                    .setDescription("This command can only be used in a server.")
                    .setColor(Color.RED);
            event.replyEmbeds(errorEmbed.build()).queue();
            return;
        }

        Member owner = guild.retrieveOwner().complete();
        String guildOwner = (owner != null) ? owner.getUser().getAsTag() : "Unknown";
        Role boostRole = guild.getBoostRole();

        CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                .setTitle("Server Information")
                .setDescription(
                        "**ID**: " + guild.getId() + "\n" +
                                "**Name**: " + guild.getName() + "\n" +
                                "**Description**: " + ((guild.getDescription() != null) ? guild.getDescription() : "None") + "\n" +
                                "**Owner**: " + guildOwner + "\n" +
                                "**Members**: " + guild.getMemberCount() + "\n" +
                                "**Boosts**: " + guild.getBoostCount() + "\n" +
                                "**Boost Role**: " + ((boostRole != null) ? boostRole.getAsMention() : "None") + "\n" +
                                "**Creation**: " + guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n"
                )
                .setThumbnail(guild.getIconUrl());

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
