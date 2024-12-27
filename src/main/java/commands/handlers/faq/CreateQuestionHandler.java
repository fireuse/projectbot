package commands.handlers.faq;

import data.QuestionManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;

import java.sql.SQLException;
import java.util.function.Consumer;

public class CreateQuestionHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        String question = event.getOption("question").get().getValue().get().asString();
        String answer = event.getOption("answer").get().getValue().get().asString();
        int category = (int) event.getOption("cat").get().getValue().get().asLong();
        try {
            QuestionManager.createQuestion(question, answer, category);
            event.reply("Dodano pytanie").withEphemeral(true).subscribe();
        } catch (SQLException e) {
            event.reply("Something went wrong").withEphemeral(true).subscribe();
            throw new RuntimeException(e);
        }
    }
}
