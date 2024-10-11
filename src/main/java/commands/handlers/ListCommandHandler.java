package commands.handlers;

import data.Project;
import data.ProjectManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.DeferrableInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class ListCommandHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        processEvent(event);
    }

    public static void processEvent(DeferrableInteractionEvent event) {
        try {
            List<Project> projects = ProjectManager.getProjects();
            List<SelectMenu.Option> options = new LinkedList<>();
            StringBuilder sb = new StringBuilder("Project list:\n");
            int counter = 1;
            for (Project project : projects) {
                Member m = event.getClient().getMemberById(event.getInteraction().getGuildId().get(), project.getAuthor()).block();
                sb.append("%d. %s - %s\n".formatted(counter, project.getName(), m.getMention()));
                options.add(SelectMenu.Option.of(project.getName(), String.valueOf(counter)));
                counter++;
            }
            SelectMenu menu = SelectMenu.of("list", options);
            Mono<Void> tempListener = event.getClient().getEventDispatcher().on(SelectMenuInteractionEvent.class).flatMap(new DescriptionHandler(projects)).timeout(Duration.ofMinutes(10))
                    .onErrorResume(TimeoutException.class, ignore -> Mono.empty()).then();
            event.reply(sb.toString()).withEphemeral(true).withComponents(ActionRow.of(menu)).thenEmpty(tempListener).subscribe();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
