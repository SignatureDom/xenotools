package com.bluefoxhost.commands.admin;

import com.bluefoxhost.commands.Command;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

public class Purge extends Command {

    public Purge() {
        super(new CommandData("purge", "Purge a certain amount of messages")
                .addOptions(new OptionData(OptionType.INTEGER, "amount", "Number of messages to delete").setRequired(true)));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Member member = event.getMember();
        int amount = (int) event.getOption("amount").getAsLong();

        if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("Error")
                    .setDescription("I need the 'Manage Messages' permission in this channel to delete messages.")
                    .setColor(Color.RED);
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            return;
        }

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("Error")
                    .setDescription("You must have Administrator permissions to use this command.")
                    .setColor(Color.RED);
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            return;
        }

        if (member == null) {
            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("Error")
                    .setDescription("This command can only be used in a server.")
                    .setColor(Color.RED);
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            return;
        }

        if (amount < 1 || amount > 100) {
            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("Error")
                    .setDescription("Amount must be between 1 and 100.")
                    .setColor(Color.RED);
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            return;
        }

        event.getChannel().getHistory().retrievePast(amount).queue(messages -> {
            event.getChannel().purgeMessages(messages);
            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("Success")
                    .setDescription("Deleted " + amount + " messages.");
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        });
    }
}
