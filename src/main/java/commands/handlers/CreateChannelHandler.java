package commands.handlers;

import data.RoleManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.CategorizableChannel;
import discord4j.core.spec.TextChannelCreateSpec;
import discord4j.core.spec.VoiceChannelCreateSpec;

import java.util.Optional;
import java.util.function.Consumer;

public class CreateChannelHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        String name = event.getOption("name").get().getValue().get().asString();
        String type = event.getOption("type").get().getValue().get().asString();
        Guild g = event.getInteraction().getGuild().block();
        Optional<Snowflake> category = event.getInteraction().getChannel().cast(CategorizableChannel.class).map(x -> x.getCategoryId()).block();
        if (category.isEmpty()) {
            event.reply("Nie jest to kanał projektu").withEphemeral(true).subscribe();
            return;
        }
        Snowflake role = RoleManager.getRoleByCategory(category.get());
        Member m = event.getInteraction().getMember().get();
        if (role == null || !m.getRoleIds().contains(role)) {
            event.reply("Nie masz uprawnień").withEphemeral(true).subscribe();
            return;
        }
        if (type.equals("text")) {
            g.createTextChannel(TextChannelCreateSpec.builder().name(name).parentId(category.get()).permissionOverwrites(CreateHandler.privateChannel(g.getRoleById(role).block(), g)).build()).block();
        }
        if (type.equals("voice")) {
            g.createVoiceChannel(VoiceChannelCreateSpec.builder().name(name).parentId(category.get()).permissionOverwrites(CreateHandler.privateChannel(g.getRoleById(role).block(), g)).build()).block();
        }
        event.reply("Sukces").withEphemeral(true).subscribe();
    }
}
