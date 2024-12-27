package commands.handlers;

import data.QuestionManager;
import discord4j.core.event.domain.interaction.*;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ButtonHandler implements Consumer<ButtonInteractionEvent> {
    @Override
    public void accept(ButtonInteractionEvent event) {
        String id = event.getCustomId();
        if (id.equals("list-project")) {
            ListCommandHandler.processEvent(event);
            return;
        }
        if (id.startsWith("faq")) {
            try {
                List<EmbedCreateFields.Field> fields = new ArrayList<>();
                int cat = Integer.parseInt(id.substring(3));
                for (QuestionManager.Question i : QuestionManager.getQuestions(cat)){
                    fields.add(EmbedCreateFields.Field.of( i.question(), i.answer(), false));
                }
                EmbedCreateSpec spec = EmbedCreateSpec.builder().title("FAQ").addAllFields(fields).build();
                event.reply().withEmbeds(spec).withEphemeral(true).subscribe();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
