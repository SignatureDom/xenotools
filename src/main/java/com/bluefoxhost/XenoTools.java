package com.bluefoxhost;

import com.bluefoxhost.commands.admin.Purge;
import com.bluefoxhost.commands.general.*;
import com.bluefoxhost.events.GuildJoin;
import com.bluefoxhost.events.GuildLeave;
import com.bluefoxhost.events.Ready;
import com.bluefoxhost.events.SlashCommand;
import com.bluefoxhost.handlers.CommandHandler;
import com.bluefoxhost.handlers.StatusHandler;
import com.bluefoxhost.handlers.DatabaseHandler;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class XenoTools {
    public static void main(String[] args) throws LoginException, InterruptedException, SQLException, ClassNotFoundException {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        if (token == null) throw new RuntimeException("Missing DISCORD_TOKEN environment variable");

        // Initialize MySQL connection
        DatabaseHandler.initialize(
                dotenv.get("MYSQL_URL"),
                dotenv.get("MYSQL_USERNAME"),
                dotenv.get("MYSQL_PASSWORD")
        );

        // Create tables
        DatabaseHandler.getInstance().createGuildsTable();

        // Cache banned guilds
        DatabaseHandler.getInstance().cacheBannedGuilds();

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);

        // Register commands
        CommandHandler.register(new Stats(), new IPLookup(), new Ping(), new ServerInfo(), new Purge(), new UserInfo(), new Help());

        // Register events
        builder.addEventListeners(new Ready(), new SlashCommand(), new GuildJoin(), new GuildLeave());

        // Build JDA instance
        JDA jda = builder.build().awaitReady();

        // Initialize status handler
        StatusHandler.initialize(jda);

        // Update list of slash commands
        jda.upsertCommand(new IPLookup().getCommandData()).queue();
        jda.upsertCommand(new Stats().getCommandData()).queue();
        jda.upsertCommand(new Ping().getCommandData()).queue();
        jda.upsertCommand(new ServerInfo().getCommandData()).queue();
        jda.upsertCommand(new Purge().getCommandData()).queue();
        jda.upsertCommand(new UserInfo().getCommandData()).queue();
        jda.upsertCommand(new Help().getCommandData()).queue();

    }
}
