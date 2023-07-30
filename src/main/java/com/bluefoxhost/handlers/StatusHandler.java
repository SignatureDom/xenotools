package com.bluefoxhost.handlers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StatusHandler {
    private static AtomicInteger guildCountChange = new AtomicInteger(0);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static JDA jdaInstance = null;
    private static final Object lock = new Object();

    public static void initialize(JDA jda) {
        jdaInstance = jda;
        scheduler.scheduleAtFixedRate(() -> updateStatus(false), 0, 5, TimeUnit.MINUTES);
    }

    public static void increase() {
        synchronized (lock) {
            guildCountChange.incrementAndGet();
        }
    }

    public static void decrease() {
        synchronized (lock) {
            guildCountChange.decrementAndGet();
        }
    }

    public static void updateStatus(boolean force) {
        System.out.println("Updating status function called");
        synchronized (lock) {
            if (force || guildCountChange.get() != 0) {
                System.out.println("Updating status");
                int currentGuildCount = jdaInstance.getGuilds().size();
                jdaInstance.getPresence().setActivity(Activity.watching(currentGuildCount + " guilds"));
                guildCountChange.set(0);
            }
        }
    }
}