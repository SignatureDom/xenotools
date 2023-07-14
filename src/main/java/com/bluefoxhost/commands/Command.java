package com.bluefoxhost.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class Command extends ListenerAdapter {

    private final CommandData commandData;

    protected Command(CommandData commandData) {
        this.commandData = commandData;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals(commandData.getName())) execute(event);
    }

    public abstract void execute(SlashCommandEvent event);
}
