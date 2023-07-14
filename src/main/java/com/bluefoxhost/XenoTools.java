package com.bluefoxhost;

import com.bluefoxhost.commands.IPLookup;
import com.bluefoxhost.commands.Stats;
import com.bluefoxhost.events.Ready;
import com.bluefoxhost.handlers.CommandHandler;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class XenoTools {
    public static void main(String[] args) throws LoginException, InterruptedException {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        if (token == null) {
            throw new RuntimeException("Missing DISCORD_TOKEN environment variable");
        }

        JDABuilder builder = JDABuilder.createDefault(token);

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);

        // Create CommandHandler and register commands
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.registerCommand(new Stats());
        commandHandler.registerCommand(new IPLookup());

        builder.addEventListeners(new Ready(), commandHandler);

        JDA jda = builder.build().awaitReady();

        // Update list of slash commands
        jda.upsertCommand(new IPLookup().getCommandData()).queue();
        jda.upsertCommand(new Stats().getCommandData()).queue();

    }
}
