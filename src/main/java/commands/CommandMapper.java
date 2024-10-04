package commands;

import commands.handlers.CreateHandler;
import commands.handlers.LeaderCommandHandler;
import commands.handlers.ListCommandHandler;
import commands.handlers.SetupHandler;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.GroupedFlux;

import java.util.Map;
import java.util.function.Consumer;

public class CommandMapper implements Consumer<GroupedFlux<String, ChatInputInteractionEvent>> {
    private final Map<String, Consumer<ChatInputInteractionEvent>> maps = Map.of("list-project", new ListCommandHandler(), "leader", new LeaderCommandHandler(), "create-project", new CreateHandler(),
            "setup", new SetupHandler());

    @Override
    public void accept(GroupedFlux<String, ChatInputInteractionEvent> event) {
        event.subscribe(maps.get(event.key()));
    }
}
