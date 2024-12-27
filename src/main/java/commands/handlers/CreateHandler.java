package commands.handlers;

import data.Project;
import data.ProjectManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.*;
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
            Member leader = event.getOption("leader").get().getValue().get().asUser().flatMap(x->x.asMember(g.getId())).block();
            event.reply("Successful").withEphemeral(true).subscribe();
            Role projectRole = g.createRole(RoleCreateSpec.create().withName("Projekt - " + name)).block();
            Snowflake team = g.createCategory(CategoryCreateSpec.builder().name(name).build()).block().getId();
            createChannels(g, description, projectRole, team, leader);
            leader.addRole(projectRole.getId()).block();
            ProjectManager.addProject(new Project(name, leader.getId(), description, projectRole.getId(), team));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createChannels(Guild g, String description, Role role, Snowflake team, Member leader) {
        List<PermissionOverwrite> overwrites = privateChannel(role, g);
        g.createVoiceChannel(VoiceChannelCreateSpec.builder().permissionOverwrites(overwrites).parentId(team).name("Praca").build()).block();
        EmbedCreateSpec projectCard = EmbedCreateSpec.builder()
                .title("Karta projektu")
                .addField("Lider zespołu", leader.getMention(), false)
                .description(description)
                .build();
        g.createTextChannel(TextChannelCreateSpec.builder().name("Forum").topic("Miejsce na chwalenie się postępami").parentId(team).build())
                .flatMap(x -> x.createMessage(projectCard)).flatMap(Message::pin).block();
        EmbedCreateSpec help = EmbedCreateSpec.builder()
                .title("Wasza przestrzeń")
                .description("To jest kanał prywatny do komunikacji między członkami projektu, poniżej znajdziecie listę komend do zarządzania kanałami waszego projektu")
                .addField("/create-channel", "Tworzy kanał projektu", false)
                .addField("/delete-channel", "Usuwa wybrany kanał projektu", false)
                .build();
        g.createTextChannel(TextChannelCreateSpec.builder().permissionOverwrites(overwrites).parentId(team).name("Wewnętrzny").build())
                .flatMap(x -> x.createMessage(help)).flatMap(Message::pin).block();
    }

    public static List<PermissionOverwrite> privateChannel(Role role, Guild g) {
        PermissionOverwrite everyone = PermissionOverwrite.forRole(g.getEveryoneRole().block().getId(), PermissionSet.none(), PermissionSet.of(Permission.VIEW_CHANNEL));
        PermissionOverwrite priv = PermissionOverwrite.forRole(role.getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none());
        PermissionOverwrite bot = PermissionOverwrite.forMember(g.getSelfMember().block().getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none());
        return List.of(everyone, priv, bot);
    }
}
