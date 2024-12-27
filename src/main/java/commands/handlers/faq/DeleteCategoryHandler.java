package commands.handlers.faq;

import commands.CommandRegistry;
import data.QuestionManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;

import java.sql.SQLException;
import java.util.function.Consumer;

public class DeleteCategoryHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        try {
            QuestionManager.deleteCategory((int) event.getOption("cat").get().getValue().get().asLong());
            event.reply("Kategoria usunięta").withEphemeral(true).subscribe();
            CommandRegistry.updateCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
