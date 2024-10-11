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
        //applicationService.getGuildApplicationCommands(appId, guildId).flatMap(x -> applicationService.deleteGuildApplicationCommand(appId, guildId, x.id().asLong())).subscribe();
        ApplicationCommandRequest createProject = ApplicationCommandRequest.builder().defaultMemberPermissions("0").description("Creates a project").name("create-project")
                .addOption(ApplicationCommandOptionData.builder().name("name").description("name of project").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build())
                .addOption(ApplicationCommandOptionData.builder().name("description").description("short description of project").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build()).build();
        ApplicationCommandRequest listProject = ApplicationCommandRequest.builder().description("Lists current projects").name("list-project").build();
        ApplicationCommandRequest deleteProject = ApplicationCommandRequest.builder().defaultMemberPermissions("0").description("Delete project").name("delete-project").build();
        ApplicationCommandRequest help = ApplicationCommandRequest.builder().description("Ważne informacje o sekcji").name("help").build();
        ApplicationCommandRequest role = ApplicationCommandRequest.builder().description("Role na serwerze").name("role").build();
        applicationService.createGuildApplicationCommand(appId, guildId, createProject).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, listProject).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, help).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, role).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, deleteProject).subscribe();
    }
}
