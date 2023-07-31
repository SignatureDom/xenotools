package com.bluefoxhost.commands.general;

import com.bluefoxhost.commands.Command;
import com.bluefoxhost.util.CustomEmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class MCServer extends Command {

    private static final OkHttpClient httpClient = new OkHttpClient();

    public MCServer() {
        super(new CommandData("mcserver", "Check the status of a Minecraft server.")
                .addOptions(new OptionData(OptionType.STRING, "host", "The host address of the Minecraft server.").setRequired(true))
                .addOptions(new OptionData(OptionType.INTEGER, "port", "The port of the Minecraft server (default is 25565).")));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        String host = event.getOption("host").getAsString();
        int port = event.getOption("port") == null ? 25565 : (int) event.getOption("port").getAsLong();

        // Acknowledge the command immediately
        event.deferReply().queue();

        String json = fetchMinecraftServerInfo(host, port);
        if (json != null) {
            JSONObject jsonObject = new JSONObject(json);

            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("Minecraft Server Status")
                    .setDescription(
                            "**Online**: " + jsonObject.optBoolean("online") + "\n" +
                                    "**Host**: " + jsonObject.optString("host") + "\n" +
                                    "**Port**: " + jsonObject.optInt("port") + "\n" +
                                    "**Version**: " + jsonObject.optJSONObject("version").optString("name_clean") + "\n" +
                                    "**Players**: " + jsonObject.optJSONObject("players").optInt("online") + "/" + jsonObject.optJSONObject("players").optInt("max") + "\n" +
                                    "**MOTD**: ```" + jsonObject.optJSONObject("motd").optString("clean") + "```"
                    )
                    .setThumbnail("https://api.mcstatus.io/v2/icon/" + host + ":" + port);
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        } else {
            event.getHook().editOriginal("Failed to fetch minecraft server information for: " + host + ":" + port).queue();
        }
    }

    private String fetchMinecraftServerInfo(String host, int port) {
        Request request = new Request.Builder()
                .url("https://api.mcstatus.io/v2/status/java/" + host + ":" + port)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
