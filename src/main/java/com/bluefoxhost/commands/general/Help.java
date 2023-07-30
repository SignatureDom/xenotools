package com.bluefoxhost.commands.general;

import com.bluefoxhost.commands.Command;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Help extends Command {

    public Help() {
        super(new CommandData("help", "Get the bot's help"));
    }
    @Override
    public void execute(SlashCommandEvent event) {
        CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                .setTitle("Help")
                .setField("General", "`help`, `ping`, `serverinfo`, `userinfo`, `stats`, `iplookup`", false)
                .setField("Admin", "`purge`", false);

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
