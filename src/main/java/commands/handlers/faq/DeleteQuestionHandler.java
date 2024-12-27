package commands.handlers.faq;

import data.QuestionManager;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class DeleteQuestionHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        try {
            int cat = (int) event.getOption("cat").get().getValue().get().asLong();
            List<SelectMenu.Option> options = new LinkedList<>();
            for (QuestionManager.Question i : QuestionManager.getQuestions(cat)){
                options.add(SelectMenu.Option.of(i.question(), String.valueOf(i.id())));
            }
            SelectMenu menu = SelectMenu.of("delete-q", options);
            Mono<Void> tempListener = event.getClient().on(SelectMenuInteractionEvent.class, x-> {
                try {
                    QuestionManager.deleteQuestion(Integer.parseInt(x.getValues().get(0)));
                    return x.reply("Usunięto pytanie").withEphemeral(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }).timeout(Duration.ofMinutes(10)).then();
            event.reply("wybierz pytanie do usunięcia").withComponents(ActionRow.of(menu))
                    .withEphemeral(true).thenEmpty(tempListener).subscribe();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
