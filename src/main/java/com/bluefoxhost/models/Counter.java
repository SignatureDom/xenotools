package com.bluefoxhost.models;

public class Counter {
    private String type;
    private String channelId;
    private String guildId;

    public Counter(String type, String channelId, String guildId) {
        this.type = type;
        this.channelId = channelId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getGuildId() {
        return this.guildId;
    }
}