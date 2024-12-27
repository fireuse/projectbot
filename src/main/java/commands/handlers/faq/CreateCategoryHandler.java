package commands.handlers.faq;

import commands.CommandRegistry;
import data.QuestionManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;

import java.sql.SQLException;
import java.util.function.Consumer;

public class CreateCategoryHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        try {
            QuestionManager.createCategory(event.getOption("name").get().getValue().get().asString());
            event.reply("Category created!").withEphemeral(true).subscribe();
            CommandRegistry.updateCategories();
        } catch (SQLException e) {
            event.reply("Something went wrong").withEphemeral(true).subscribe();
            throw new RuntimeException(e);
        }
    }
}
