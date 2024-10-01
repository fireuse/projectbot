import data.SQLConnector;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        initDb();
        GatewayDiscordClient client = DiscordClientBuilder.create("MTI5MDYyMDgzOTU5ODg4Mjg1OQ.GJtvjB.owudl-zMUGsIeFN4injtY4FMsZz83W0uGloTEM").build().login().block(); //TODO change in future
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();

            if (message.getContent().equalsIgnoreCase("!ping")) {
                message.getChannel().flatMap(channel -> channel.createMessage("pong!")).block();
            }
        });
        client.onDisconnect().block();
    }

    private static void initDb() throws SQLException, IOException {
        Connection conn = SQLConnector.getConnection();
        InputStream resource = conn.getClass().getClassLoader().getResourceAsStream("db_init.sql");
        String result = new BufferedReader(new InputStreamReader(resource))
                .lines().collect(Collectors.joining("\n"));
        Statement stmt = conn.createStatement();
        for(String i : result.split(";")){
            if(i.isBlank()) continue;
            stmt.execute(i);
        }
        conn.close();
    }
}