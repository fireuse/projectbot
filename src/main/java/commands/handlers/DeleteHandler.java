package commands.handlers;

import data.ProjectManager;
import data.RoleManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.CategorizableChannel;
import discord4j.core.object.entity.channel.Channel;

import java.util.function.Consumer;

public class DeleteHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        Guild g = event.getInteraction().getGuild().block();
        Snowflake category = event.getInteraction().getChannel().cast(CategorizableChannel.class).flatMap(CategorizableChannel::getCategory).map(x -> x.getId()).block();
        if (category == null || !ProjectManager.checkCategory(category)) {
            event.reply("No project is associated with this channel.").withEphemeral(true).subscribe();
            return;
        }
        g.getChannels().filter(x -> !x.getType().equals(Channel.Type.GUILD_CATEGORY)).cast(CategorizableChannel.class).filter(x -> x.getCategoryId().isPresent())
                .groupBy(x -> x.getCategoryId().get()).filter(x -> x.key().equals(category)).subscribe(x -> x.flatMap(Channel::delete).subscribe(), Throwable::printStackTrace);
        g.getChannelById(category).flatMap(Channel::delete).subscribe();
        g.getRoleById(RoleManager.getRoleByCategory(category)).flatMap(Role::delete).block();
        ProjectManager.deleteProject(category);
    }
}
