package com.bluefoxhost.commands.general;

import com.bluefoxhost.commands.Command;
import com.bluefoxhost.util.CustomEmbedBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class IPLookup extends Command {
    Dotenv dotenv = Dotenv.load();
    private String IPINFO_TOKEN = dotenv.get("IPINFO_TOKEN");;
    private OkHttpClient httpClient = new OkHttpClient();

    public IPLookup() {
        super(new CommandData("iplookup", "Lookup an IP address")
                .addOption(OptionType.STRING, "ip", "The IP address to lookup", true));
    }

    @Override
    public void execute(SlashCommandEvent event) {
        String ip = event.getOption("ip").getAsString();

        event.deferReply().queue();

        String json = fetchIPInfo(ip);
        if (json != null) {
            JSONObject jsonObject = new JSONObject(json);
            String city = jsonObject.optString("city");
            String region = jsonObject.optString("region");
            String country = jsonObject.optString("country");
            String loc = jsonObject.optString("loc");
            String org = jsonObject.optString("org");
            String postal = jsonObject.optString("postal");
            String timezone = jsonObject.optString("timezone");
            String hostname = jsonObject.optString("hostname");
            boolean anycast = jsonObject.optBoolean("anycast");

            CustomEmbedBuilder embedBuilder = new CustomEmbedBuilder()
                    .setTitle("IP Lookup")
                    .setDescription(
                            "**IP**: " + ip + "\n" +
                            "**Hostname**: " + hostname + "\n" +
                            "**Anycast**: " + anycast + "\n" +
                            "**City**: " + city + "\n" +
                            "**Region**: " + region + "\n" +
                            "**Country**: " + country + "\n" +
                            "**Location**: " + loc + "\n" +
                            "**Organization**: " + org + "\n" +
                            "**Postal Code**: " + postal + "\n" +
                            "**Timezone**: " + timezone + "\n"
                    );
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        } else {
            event.getHook().editOriginal("Failed to fetch information for IP: " + ip).queue();
        }
    }


    private String fetchIPInfo(String ip) {
        Request request = new Request.Builder()
                .url("https://ipinfo.io/" + ip + "?token=" + IPINFO_TOKEN)
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
