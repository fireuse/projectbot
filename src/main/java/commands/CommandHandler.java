package commands;

import data.Project;
import data.ProjectManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class CommandHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        switch (event.getCommandName()){
            case "create-project": //TODO refactor it
                try {
                    ProjectManager.addProject(new Project(event.getOption("name").get().getName(),
                            event.getInteraction().getMember().get().getId().asLong()));
                    event.reply("Successful").subscribe();
                } catch (SQLException e) {
                    event.reply("Error").subscribe();
                    throw new RuntimeException(e);
                }
                break;
            case "list-project":
                try {
                    List<Project> projects = ProjectManager.getProjects();
                    StringBuilder sb = new StringBuilder("Project list:\n");
                    int counter = 1;
                    for(Project project : projects){
                        Member m = event.getClient().getMemberById(event.getInteraction().getGuildId().get(), Snowflake.of(project.getAuthor())).block();
                        sb.append("%d. %s - %s\n".formatted(counter, project.getName(), m.getMention()));
                    }
                    event.reply(sb.toString()).withEphemeral(true).subscribe();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                event.reply("wtf").subscribe();
        }
    }
}
