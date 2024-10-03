package commands.handlers;

import data.RoleManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.RoleEditSpec;

import java.sql.SQLException;
import java.util.function.Consumer;

public class LeaderCommandHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        try {
            Role r = event.getInteraction().getGuild().block().createRole().flatMap(x -> x.edit(RoleEditSpec.create().withName("Leader"))).block();
            RoleManager.createLeader(r.getId().asLong());
            event.reply("Success, role %s created".formatted(r.getMention())).withEphemeral(true).subscribe();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
