package com.bluefoxhost.commands.admin;

import com.bluefoxhost.commands.Command;
import com.bluefoxhost.handlers.DatabaseHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class Counter extends Command {

    public Counter() {
        super(new CommandData("counter", "Manage the current server's counters")
                .addSubcommands(
                        new SubcommandData("create", "Create a new counter")
                                .addOptions(
                                        new OptionData(OptionType.STRING, "type", "Counter type", true)
                                                .addChoice("members", "members")
                                                .addChoice("roles", "roles")
                                                .addChoice("channels", "channels")
                                )
                )
        );
    }

    @Override
    public void execute(SlashCommandEvent event) {
        DatabaseHandler instance = DatabaseHandler.getInstance();
        Guild guild = event.getGuild();

        event.deferReply().queue();

        switch (event.getSubcommandName()) {
            case "create": {
                String type = event.getOption("type").getAsString();

                switch (type) {
                    case "members": {
                        guild.createVoiceChannel("Members: " + guild.getMemberCount()).queue(newVoiceChannel -> {
                            try {
                                instance.createCounter(guild.getId(), type, newVoiceChannel.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                    case "roles": {
                        guild.createVoiceChannel("Roles: " + guild.getRoles().size()).queue(newVoiceChannel -> {
                            try {
                                instance.createCounter(guild.getId(), type, newVoiceChannel.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                    case "channels": {
                        guild.createVoiceChannel("Channels: " + guild.getChannels().size()).queue(newVoiceChannel -> {
                            try {
                                instance.createCounter(guild.getId(), type, newVoiceChannel.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }

                }

                event.getHook().editOriginal("Counter created").queue();
                break;
            }

        }
    }
}
