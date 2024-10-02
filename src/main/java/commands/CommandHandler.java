package commands;

import data.Project;
import data.ProjectManager;
import data.RoleManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.RoleEditSpec;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class CommandHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        switch (event.getCommandName()) {
            case "create-project": //TODO refactor it
                try {
                    List<Long> roles = RoleManager.getLeader();
                    boolean allowed = event.getInteraction().getMember().get().getRoleIds().stream().map(Snowflake::asLong).anyMatch(roles::contains);
                    if (!allowed) {
                        event.reply("Only leader roles can use this command").withEphemeral(true).subscribe();
                        break;
                    }
                    ProjectManager.addProject(new Project(event.getOption("name").get().getValue().get().asString(),
                            event.getInteraction().getMember().get().getId().asLong(), event.getOption("description").get().getValue().get().asString()));
                    event.reply("Successful").withEphemeral(true).subscribe();
                } catch (SQLException e) {
                    event.reply("Error").subscribe();
                    throw new RuntimeException(e);
                }
                break;
            case "list-project":
                try {
                    List<Project> projects = ProjectManager.getProjects();
                    List<SelectMenu.Option> options = new LinkedList<>();
                    StringBuilder sb = new StringBuilder("Project list:\n");
                    int counter = 1;
                    for (Project project : projects) {
                        Member m = event.getClient().getMemberById(event.getInteraction().getGuildId().get(), Snowflake.of(project.getAuthor())).block();
                        sb.append("%d. %s - %s\n".formatted(counter, project.getName(), m.getMention()));
                        options.add(SelectMenu.Option.of(project.getName(), String.valueOf(counter)));
                    }
                    SelectMenu menu = SelectMenu.of("list", options);
                    Mono<Void> tempListener = event.getClient().getEventDispatcher().on(SelectMenuInteractionEvent.class).flatMap(new DescriptionHandler(projects)).timeout(Duration.ofMinutes(10))
                            .onErrorResume(TimeoutException.class, ignore -> Mono.empty()).then();
                    event.reply(sb.toString()).withEphemeral(true).withComponents(ActionRow.of(menu)).thenEmpty(tempListener).subscribe();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "leader":
                try {
                    Role r = event.getInteraction().getGuild().block().createRole().flatMap(x -> x.edit(RoleEditSpec.create().withName("Leader"))).block();
                    RoleManager.createLeader(r.getId().asLong());
                    event.reply("Success, role %s created".formatted(r.getMention())).withEphemeral(true).subscribe();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                event.reply("wtf").subscribe();
        }
    }
}
