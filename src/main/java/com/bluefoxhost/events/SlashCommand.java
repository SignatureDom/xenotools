package com.bluefoxhost.events;

import com.bluefoxhost.handlers.CommandHandler;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        CommandHandler.handle(event);
    }
}
