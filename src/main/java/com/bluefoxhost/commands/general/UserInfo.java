package com.bluefoxhost.commands.general;

import com.bluefoxhost.commands.Command;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class UserInfo extends Command {

    public UserInfo() {
        super(new CommandData("userinfo", "Get the user's information")
                .addOption(OptionType.USER, "user", "The user to get the information of", false)
        );
    }

    @Override
    public void execute(SlashCommandEvent event) {
        User user;
        OptionMapping userOption = event.getOption("user");

        if (userOption == null) {
            user = event.getUser();
        } else {
            user = userOption.getAsUser();
        }

        CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                .setTitle("User Information")
                .setDescription(
                        "**ID**: " + user.getId() + "\n" +
                                "**Name**: " + user.getAsTag() + "\n" +
                                "**Creation**: " + user.getTimeCreated().format(java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                                "**Bot**: " + (user.isBot() ? "yes" : "no")

                )
                .setThumbnail(user.getEffectiveAvatarUrl());

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}