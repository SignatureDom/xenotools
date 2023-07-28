package com.bluefoxhost;

import com.bluefoxhost.commands.*;
import com.bluefoxhost.events.Ready;
import com.bluefoxhost.events.SlashCommand;
import com.bluefoxhost.handlers.CommandHandler;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class XenoTools {
    public static void main(String[] args) throws LoginException, InterruptedException {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        if (token == null) throw new RuntimeException("Missing DISCORD_TOKEN environment variable");

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);

        // Register commands
        CommandHandler.register(new Stats(), new IPLookup(), new Ping(), new ServerInfo(), new Purge(), new UserInfo());

        // Register events
        builder.addEventListeners(new Ready(), new SlashCommand());

        // Build JDA instance
        JDA jda = builder.build().awaitReady();

        // Update list of slash commands
        jda.upsertCommand(new IPLookup().getCommandData()).queue();
        jda.upsertCommand(new Stats().getCommandData()).queue();
        jda.upsertCommand(new Ping().getCommandData()).queue();
        jda.upsertCommand(new ServerInfo().getCommandData()).queue();
        jda.upsertCommand(new Purge().getCommandData()).queue();
        jda.upsertCommand(new UserInfo().getCommandData()).queue();

    }
}
