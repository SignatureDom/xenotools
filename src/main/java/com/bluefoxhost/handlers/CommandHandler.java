package com.bluefoxhost.handlers;

import com.bluefoxhost.commands.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {
    public static final Map<String, Command> commands = new HashMap<>();

    public static void register(Command command) {
        commands.put(command.getCommandData().getName(), command);
    }

    public static void register(Command... commands) {
        for (Command command : commands) {
            register(command);
        }
    }

    public static void remove(String commandName) {
        commands.remove(commandName);
    }

    public static void handle(SlashCommandEvent event) {
        String commandName = event.getName();
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(event);
        }
    }
}
