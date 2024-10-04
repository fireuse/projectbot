package commands.handlers;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.function.Consumer;

public class SetupHandler implements Consumer<ChatInputInteractionEvent> {

    @Override
    public void accept(ChatInputInteractionEvent event) {
        Button b = Button.primary("list-project", "Lista projektów");
        EmbedCreateSpec spec = EmbedCreateSpec.builder()
                .title("Ogłoszenia")
                .description("Miejsce na ważne informacje")
                .build();
        event.reply().withEmbeds(spec).withComponents(ActionRow.of(b)).subscribe();
    }
}
