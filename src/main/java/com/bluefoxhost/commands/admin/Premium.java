package com.bluefoxhost.commands.admin;


import com.bluefoxhost.commands.Command;
import com.bluefoxhost.handlers.DatabaseHandler;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.sql.SQLException;

public class Premium extends Command {

    public Premium() {
        super(new CommandData("premium", "Get the server's premium information"));
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

        DatabaseHandler instance = DatabaseHandler.getInstance();
        boolean premium = false;
        try {
            if (!instance.guildExists(event.getGuild().getId())) {
                premium = instance.isGuildPremium(guild.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error fetching premium status for guild " + event.getGuild().getId() + " in database");
            e.printStackTrace();
        }

        CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                .setTitle("Premium")
                .setDescription(premium ? "This server has premium" : "This server does not have premium");

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
