package com.bluefoxhost.events;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ready extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        int guildCount = event.getJDA().getGuilds().size();
        event.getJDA().getPresence().setActivity(Activity.watching(guildCount + " guilds"));
    }
}
