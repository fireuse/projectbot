import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public class Main {
    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClientBuilder.create("MTI5MDYyMDgzOTU5ODg4Mjg1OQ.GJtvjB.owudl-zMUGsIeFN4injtY4FMsZz83W0uGloTEM").build().login().block(); //TODO change in future
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();

            if (message.getContent().equalsIgnoreCase("!ping")) {
                message.getChannel().flatMap(channel -> channel.createMessage("pong!")).block();
            }
        });
        client.onDisconnect().block();
    }
}