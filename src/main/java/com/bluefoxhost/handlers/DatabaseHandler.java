package com.bluefoxhost.handlers;

import com.bluefoxhost.models.Counter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler {
    private static DatabaseHandler INSTANCE;
    private Connection connection;
    private final HashMap<String, Boolean> bannedGuilds = new HashMap<>();

    public DatabaseHandler(String url, String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to MySQL database");
    }

    public static DatabaseHandler getInstance() {
        return INSTANCE;
    }

    public static void initialize(String url, String username, String password) throws SQLException, ClassNotFoundException {
        INSTANCE = new DatabaseHandler(url, username, password);
    }


    public void createGuildsTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS guilds (" +
                "guild_id VARCHAR(255) NOT NULL PRIMARY KEY," +
                "premium BOOLEAN DEFAULT FALSE," +
                "disabled_commands TEXT," +
                "banned BOOLEAN DEFAULT FALSE" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void createCountersTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS counters (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "guild_id VARCHAR(255) NOT NULL," +
                "type VARCHAR(255) NOT NULL," +
                "channel_id VARCHAR(255) NOT NULL," +
                "FOREIGN KEY (guild_id) REFERENCES guilds(guild_id)" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void createCounter(String guildId, String type, String channelId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO counters (guild_id, type, channel_id) VALUES (?, ?, ?)");
        stmt.setString(1, guildId);
        stmt.setString(2, type);
        stmt.setString(3, channelId);
        stmt.executeUpdate();
    }

    public void removeCounter(String guildId, String channelId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM counters WHERE guild_id = ? AND channel_id = ?");
        stmt.setString(1, guildId);
        stmt.setString(2, channelId);
        stmt.executeUpdate();
    }

    public List<Counter> getCounters(String guildId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT type, channel_id FROM counters WHERE guild_id = ?");
        stmt.setString(1, guildId);
        ResultSet rs = stmt.executeQuery();
        List<Counter> counters = new ArrayList<>();
        while(rs.next()) {
            counters.add(new Counter(rs.getString("type"), rs.getString("channel_id"), rs.getString("guild_id")));
        }
        return counters;
    }

    public List<Counter> getAllCounters() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT guild_id, type, channel_id FROM counters");
        ResultSet rs = stmt.executeQuery();
        List<Counter> counters = new ArrayList<>();
        while(rs.next()) {
            counters.add(new Counter(rs.getString("type"), rs.getString("channel_id"), rs.getString("guild_id")));
        }
        return counters;
    }

    public HashMap<String, Boolean> getBannedGuilds() {
        return bannedGuilds;
    }

    public void cacheBannedGuilds() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT guild_id FROM guilds WHERE banned = TRUE");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            bannedGuilds.put(rs.getString("guild_id"), true);
        }
    }

    public boolean isGuildBanned(String guildId) {
        return bannedGuilds.containsKey(guildId);
    }

    public boolean isGuildPremium(String guildId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT premium FROM guilds WHERE guild_id = ?");
        stmt.setString(1, guildId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("premium");
        }
        return false;
    }

    public void banGuild(String guildId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE guilds SET banned = TRUE WHERE guild_id = ?");
        stmt.setString(1, guildId);
        stmt.executeUpdate();
        bannedGuilds.put(guildId, true);
    }

    public boolean guildExists(String guildId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM guilds WHERE guild_id = ?");
        stmt.setString(1, guildId);
        ResultSet rs = stmt.executeQuery();

        return rs.next();
    }

    public void addGuild(String guildId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO guilds (guild_id) VALUES (?)");
        stmt.setString(1, guildId);
        stmt.executeUpdate();
    }

    public void removeGuild(String guildId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM guilds WHERE guild_id = ?");
        stmt.setString(1, guildId);
        stmt.executeUpdate();
    }

    public void updateGuild(String guildId, GuildColumns column, String value) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE guilds SET " + column.name().toLowerCase() + " = ? WHERE guild_id = ?");
        stmt.setString(1, value);
        stmt.setString(2, guildId);
        stmt.executeUpdate();
    }

    public enum GuildColumns {
        GUILD_ID,
        PREMIUM,
        DISABLED_COMMANDS,
        BANNED
    }
}