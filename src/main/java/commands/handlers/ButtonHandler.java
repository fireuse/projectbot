package commands.handlers;

import discord4j.core.event.domain.interaction.*;

import java.util.function.Consumer;

public class ButtonHandler implements Consumer<ButtonInteractionEvent> {
    @Override
    public void accept(ButtonInteractionEvent event) {
        if(event.getCustomId().equals("list-project")){
            ListCommandHandler.processEvent(event);
        }
    }
}
