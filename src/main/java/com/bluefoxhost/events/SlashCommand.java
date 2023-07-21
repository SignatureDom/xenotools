package com.bluefoxhost.events;

import com.bluefoxhost.handlers.CommandHandler;
import com.bluefoxhost.util.LogHelper;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Logger;

public class SlashCommand extends ListenerAdapter {
    private static Logger logger;

    static {
        try {
            logger = LogHelper.setupLogger("CommandLog", "command.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        CommandHandler.handle(event);

        if (logger == null) {
            System.out.println("Logger is null.");
            return;
        }

        String user = event.getUser().getName();
        String command = event.getCommandString();
        String guild = (event.getGuild() != null) ? event.getGuild().getName() : "Direct Message";

        ZoneId cst = ZoneId.of("America/Chicago");
        ZonedDateTime now = ZonedDateTime.now(cst);
        String timestamp = now.format(DateTimeFormatter.ofPattern("M/d/yyyy h:mm a 'CST'"));

        logger.info(String.format("[%s] [%s] (%s): %s", timestamp, guild, user, command));
    }


}
