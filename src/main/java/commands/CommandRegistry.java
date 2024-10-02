package commands;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;

public class CommandRegistry {
    private final ApplicationService applicationService;
    private final long appId;

    public CommandRegistry(RestClient restClient) {
        applicationService = restClient.getApplicationService();
        appId = restClient.getApplicationId().block();
    }

    public void createGuildCommands(long guildId) {
        ApplicationCommandRequest setup = ApplicationCommandRequest.builder().description("Create announcement").name("setup").build();
        ApplicationCommandRequest createProject = ApplicationCommandRequest.builder().description("Creates a project").name("create-project").addOption(
                ApplicationCommandOptionData.builder().name("name").description("name of project").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build()).build();
        ApplicationCommandRequest listProject = ApplicationCommandRequest.builder().description("Lists current projects").name("list-project").build();
        applicationService.createGuildApplicationCommand(appId, guildId, setup).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, createProject).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, listProject).subscribe();
    }
}
