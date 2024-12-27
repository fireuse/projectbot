package commands.handlers.faq;

import data.QuestionManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.List;
import java.util.function.Consumer;

public class CreateFaqHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        try {
            List<Button> fields = QuestionManager.getCategories().stream().map(x-> Button.primary("faq" + x.id(), x.category())).toList();
            if (fields.isEmpty()) {
                event.reply("Brak kategorii do wyboru").withEphemeral(true).subscribe();
                return;
            }
            ActionRow categories = ActionRow.of(fields);
            EmbedCreateSpec spec = EmbedCreateSpec.builder().title("Najczęściej zadawane pytania").description("Wybierz kategorię").build();
            event.reply().withEmbeds(spec).withComponents(categories).subscribe();
        } catch (Exception e) {
            event.reply("Something went wrong").withEphemeral(true).subscribe();
            throw new RuntimeException(e);
        }
    }
}
