package com.bluefoxhost.handlers;

import com.bluefoxhost.commands.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {
    public static final Map<String, Command> commands = new HashMap<>();

    public static void registerCommand(Command command) {
        commands.put(command.getCommandData().getName(), command);
    }

    public static void registerCommands(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    public static void handle(SlashCommandEvent event) {
        String commandName = event.getName();
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(event);
        }
    }
}
