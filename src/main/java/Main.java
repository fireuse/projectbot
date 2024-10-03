import commands.CommandMapper;
import commands.CommandRegistry;
import data.RoleManager;
import data.SQLConnector;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.role.RoleDeleteEvent;

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
        new CommandRegistry(client.getRestClient()).createGuildCommands(1290623087011823681L);
        client.getEventDispatcher().on(ChatInputInteractionEvent.class).groupBy(ApplicationCommandInteractionEvent::getCommandName).subscribe(new CommandMapper(), Throwable::printStackTrace);
        client.getEventDispatcher().on(RoleDeleteEvent.class).map(RoleDeleteEvent::getRoleId).map(Snowflake::asLong).subscribe(RoleManager::deleteLeader);
        client.onDisconnect().block();
    }

    private static void initDb() throws SQLException {
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