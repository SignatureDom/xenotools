package com.bluefoxhost.handlers;

import java.sql.*;

public class DatabaseHandler {
    private static DatabaseHandler INSTANCE;
    private Connection connection;

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