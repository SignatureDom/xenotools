package com.bluefoxhost;

import com.bluefoxhost.commands.admin.Counter;
import com.bluefoxhost.commands.admin.Premium;
import com.bluefoxhost.commands.admin.Purge;
import com.bluefoxhost.commands.general.*;
import com.bluefoxhost.events.GuildJoin;
import com.bluefoxhost.events.GuildLeave;
import com.bluefoxhost.events.Ready;
import com.bluefoxhost.events.SlashCommand;
import com.bluefoxhost.handlers.CommandHandler;
import com.bluefoxhost.handlers.CounterHandler;
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
        DatabaseHandler.getInstance().createCountersTable();

        // Cache banned guilds
        DatabaseHandler.getInstance().cacheBannedGuilds();

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);

        // Register commands
        CommandHandler.register(new Stats(), new IPLookup(), new Ping(), new ServerInfo(), new Purge(), new UserInfo(), new Help(), new MCServer(), new Premium(), new Counter());

        // Register events
        builder.addEventListeners(new Ready(), new SlashCommand(), new GuildJoin(), new GuildLeave());

        // Build JDA instance
        JDA jda = builder.build().awaitReady();

        // Initialize status handler
        StatusHandler.initialize(jda);

        // Initialize counter handler
        CounterHandler.initialize(jda);

        // Update list of slash commands
        jda.upsertCommand(new IPLookup().getCommandData()).queue();
        jda.upsertCommand(new Stats().getCommandData()).queue();
        jda.upsertCommand(new Ping().getCommandData()).queue();
        jda.upsertCommand(new ServerInfo().getCommandData()).queue();
        jda.upsertCommand(new Purge().getCommandData()).queue();
        jda.upsertCommand(new UserInfo().getCommandData()).queue();
        jda.upsertCommand(new Help().getCommandData()).queue();
        jda.upsertCommand(new MCServer().getCommandData()).queue();
        jda.upsertCommand(new Premium().getCommandData()).queue();
        jda.upsertCommand(new Counter().getCommandData()).queue();

    }
}
