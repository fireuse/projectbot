package commands;

import commands.handlers.*;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.GroupedFlux;

import java.util.Map;
import java.util.function.Consumer;

public class CommandMapper implements Consumer<GroupedFlux<String, ChatInputInteractionEvent>> {
    private final Map<String, Consumer<ChatInputInteractionEvent>> maps = Map.of("list-project", new ListCommandHandler(), "create-project", new CreateHandler(), "delete-project", new DeleteHandler(),
            "help", new HelpHandler(), "role", new RoleHandler(), "create-channel", new CreateChannelHandler(), "delete-channel", new DeleteChannelHandler());

    @Override
    public void accept(GroupedFlux<String, ChatInputInteractionEvent> event) {
        event.subscribe(maps.get(event.key()));
    }
}
