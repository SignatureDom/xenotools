package com.bluefoxhost.commands.general;

import com.bluefoxhost.commands.Command;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Ping extends Command {

    public Ping() {
        super(new CommandData("ping", "Checks the bot's latency"));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        long latency = event.getJDA().getGatewayPing();

        CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                .setTitle("Latency")
                .setDescription(latency + "ms");

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
