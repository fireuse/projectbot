package commands.handlers;

import data.Project;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public class DescriptionHandler implements Function<SelectMenuInteractionEvent, Mono<Void>> {
    private List<Project> projects;

    public DescriptionHandler(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public Mono<Void> apply(SelectMenuInteractionEvent selectMenuInteractionEvent) {
        Project p = projects.get(Integer.parseInt(selectMenuInteractionEvent.getValues().get(0)) - 1);
        Member author = selectMenuInteractionEvent.getClient().getMemberById(selectMenuInteractionEvent.getInteraction().getGuildId().get(), p.getAuthor()).block();
        EmbedCreateSpec spec = EmbedCreateSpec.builder()
                .title(p.getName())
                .description(p.getDescription())
                .addField(EmbedCreateFields.Field.of("Author", author.getMention(), true))
                .build();
        return selectMenuInteractionEvent.reply().withEphemeral(true).withEmbeds(spec);
    }
}
