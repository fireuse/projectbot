package commands.handlers;

import data.RoleManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.CategorizableChannel;
import discord4j.core.object.entity.channel.Channel;

import java.util.function.Consumer;

public class DeleteChannelHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        CategorizableChannel channel = event.getOption("channel").get().getValue().get().asChannel().filter(x->!x.getType().equals(Channel.Type.GUILD_CATEGORY)).cast(CategorizableChannel.class).block();
        if (channel == null || channel.getCategoryId().isEmpty() || channel.getPermissionOverwrites().isEmpty()) {
            event.reply("Brak uprawnień").withEphemeral(true).subscribe();
            return;
        }
        Snowflake role = RoleManager.getRoleByCategory(channel.getCategoryId().get());
        if (role == null || !event.getInteraction().getMember().get().getRoleIds().contains(role)) {
            event.reply("Brak uprawnień").withEphemeral(true).subscribe();
            return;
        }
        channel.delete().block();
        event.reply("Sukces").withEphemeral(true).subscribe();
    }
}
