package com.bluefoxhost.events;

import com.bluefoxhost.handlers.StatusHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildLeave extends ListenerAdapter {
    public void onGuildLeave(net.dv8tion.jda.api.events.guild.GuildLeaveEvent event) {
        StatusHandler.decrease();
    }
}
