package commands;

import commands.handlers.*;
import commands.handlers.faq.*;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.GroupedFlux;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandMapper implements Consumer<GroupedFlux<String, ChatInputInteractionEvent>> {
    private final Map<String, Consumer<ChatInputInteractionEvent>> maps = new HashMap<>();
    public CommandMapper() {
        maps.put("list-project", new ListCommandHandler());
        maps.put("create-project", new CreateHandler());
        maps.put("delete-project", new DeleteHandler());
        maps.put("help", new HelpHandler());
        maps.put("role", new RoleHandler());
        maps.put("create-channel", new CreateChannelHandler());
        maps.put("delete-channel", new DeleteChannelHandler());
        maps.put("create-category", new CreateCategoryHandler());
        maps.put("create-question", new CreateQuestionHandler());
        maps.put("create-faq", new CreateFaqHandler());
        maps.put("delete-category", new DeleteCategoryHandler());
        maps.put("delete-question", new DeleteQuestionHandler());
    }
    @Override
    public void accept(GroupedFlux<String, ChatInputInteractionEvent> event) {
        event.subscribe(maps.get(event.key()));
    }
}
