package commands.handlers;

import data.Project;
import data.ProjectManager;
import data.RoleManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class CreateHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        try {
            List<Long> roles = RoleManager.getLeader();
            boolean allowed = event.getInteraction().getMember().get().getRoleIds().stream().map(Snowflake::asLong).anyMatch(roles::contains);
            if (!allowed) {
                event.reply("Only leader roles can use this command").withEphemeral(true).subscribe();
                return;
            }
            ProjectManager.addProject(new Project(event.getOption("name").get().getValue().get().asString(),
                    event.getInteraction().getMember().get().getId().asLong(), event.getOption("description").get().getValue().get().asString()));
            event.reply("Successful").withEphemeral(true).subscribe();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
