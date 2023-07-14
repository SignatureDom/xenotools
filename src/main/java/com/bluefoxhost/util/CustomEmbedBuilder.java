package com.bluefoxhost.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.List;

public class CustomEmbedBuilder extends EmbedBuilder {
    public CustomEmbedBuilder() {
        super();
        setFooter("XenoTools");
        setColor(Color.BLUE);
    }

    public CustomEmbedBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    public CustomEmbedBuilder setDescription(String content) {
        super.setDescription(content);
        return this;
    }

    public CustomEmbedBuilder setField(String name, String value, boolean inline) {
        super.addField(name, value, inline);
        return this;
    }

    public CustomEmbedBuilder setFields(List<MessageEmbed.Field> fields) {
        for (MessageEmbed.Field field : fields) {
            addField(field);
        }
        return this;
    }
}
