import commands.CommandMapper;
import commands.CommandRegistry;
import commands.NewMemberGreet;
import commands.handlers.ButtonHandler;
import data.RoleManager;
import data.SQLConnector;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.role.RoleDeleteEvent;
import discord4j.gateway.intent.IntentSet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws SQLException {
        initDb();
        GatewayDiscordClient client = DiscordClientBuilder.create(System.getenv("TOKEN")).build().gateway().setEnabledIntents(IntentSet.all()).login().block();
        new CommandRegistry(client.getRestClient()).createGuildCommands(Long.parseLong(System.getenv("GUILD")));
        client.getEventDispatcher().on(ButtonInteractionEvent.class).subscribe(new ButtonHandler());
        client.getEventDispatcher().on(ChatInputInteractionEvent.class).groupBy(ApplicationCommandInteractionEvent::getCommandName).subscribe(new CommandMapper(), Throwable::printStackTrace);
        client.getEventDispatcher().on(RoleDeleteEvent.class).map(RoleDeleteEvent::getRoleId).map(Snowflake::asLong).subscribe(RoleManager::deleteLeader);
        client.getEventDispatcher().on(MemberJoinEvent.class).subscribe(new NewMemberGreet(), Throwable::printStackTrace);
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