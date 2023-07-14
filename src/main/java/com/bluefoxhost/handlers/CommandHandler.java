package com.bluefoxhost.handlers;

import com.bluefoxhost.commands.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {
    private final Map<String, Command> commandMap = new HashMap<>();

    public void registerCommand(Command command) {
        commandMap.put(command.getCommandData().getName(), command);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String commandName = event.getName();
        Command command = commandMap.get(commandName);
        if (command != null) {
            command.execute(event);
        }
    }
}
