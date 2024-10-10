package commands.handlers;

import data.Project;
import data.ProjectManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.*;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class CreateHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        try {
            Guild g = event.getInteraction().getGuild().block();
            String name = event.getOption("name").get().getValue().get().asString();
            String description = event.getOption("description").get().getValue().get().asString();
            Role projectRole = g.createRole(RoleCreateSpec.create().withName("Projekt - " + name)).block();
            createChannels(g, name, description, projectRole);
            ProjectManager.addProject(new Project(name, event.getInteraction().getMember().get().getId().asLong(), description, projectRole.getId()));
            event.reply("Successful").withEphemeral(true).subscribe();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createChannels(Guild g, String name, String description, Role role) {
        Snowflake team = g.createCategory(CategoryCreateSpec.builder().name(name).build()).block().getId();
        List<PermissionOverwrite> overwrites = privateChannel(role, g);
        g.createVoiceChannel(VoiceChannelCreateSpec.builder().permissionOverwrites(overwrites).parentId(team).name("Praca").build()).block();
        EmbedCreateSpec projectCard = EmbedCreateSpec.builder()
                .title("Karta projektu")
                .description(description)
                .build();
        g.createTextChannel(TextChannelCreateSpec.builder().name("Forum").topic("Miejsce na chwalenie się postępami").parentId(team).build())
                .flatMap(x -> x.createMessage(projectCard)).map(Message::pin).block();
        EmbedCreateSpec help = EmbedCreateSpec.builder()
                .title("Wasza przestrzeń")
                .description("To jest kanał prywatny do komunikacji między członkami projektu, poniżej znajdziecie listę komend do zarządzania kanałami waszego projektu")
                .build();
        g.createTextChannel(TextChannelCreateSpec.builder().parentId(team).name("Wewnętrzny").build())
                .flatMap(x -> x.createMessage(help)).map(Message::pin).block();
    }

    private static List<PermissionOverwrite> privateChannel(Role role, Guild g) {
        PermissionOverwrite everyone = PermissionOverwrite.forRole(g.getEveryoneRole().block().getId(), PermissionSet.none(), PermissionSet.of(Permission.VIEW_CHANNEL));
        PermissionOverwrite priv = PermissionOverwrite.forRole(role.getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none());
        return List.of(everyone, priv);
    }
}
