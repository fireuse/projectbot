package commands;

import data.QuestionManager;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CommandRegistry {
    private final ApplicationService applicationService;
    private final long appId;
    private final long guildId;
    private static CommandRegistry instance;

    private CommandRegistry(RestClient restClient, long guild) {
        applicationService = restClient.getApplicationService();
        appId = restClient.getApplicationId().block();
        guildId = guild;
    }

    private void createGuildCommands() {
        applicationService.getGuildApplicationCommands(appId, guildId).flatMap(x -> applicationService.deleteGuildApplicationCommand(appId, guildId, x.id().asLong())).subscribe();
        ApplicationCommandRequest createProject = ApplicationCommandRequest.builder().defaultMemberPermissions("0").description("Creates a project").name("create-project")
                .addOption(ApplicationCommandOptionData.builder().name("name").description("name of project").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build())
                .addOption(ApplicationCommandOptionData.builder().name("leader").description("Project leader").required(true).type(ApplicationCommandOption.Type.USER.getValue()).build())
                .addOption(ApplicationCommandOptionData.builder().name("description").description("short description of project").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build()).build();
        ApplicationCommandRequest listProject = ApplicationCommandRequest.builder().defaultMemberPermissions("0").description("Lists current projects").name("list-project").build();
        ApplicationCommandRequest deleteProject = ApplicationCommandRequest.builder().defaultMemberPermissions("0").description("Delete project").name("delete-project").build();
        ApplicationCommandRequest help = ApplicationCommandRequest.builder().description("Ważne informacje o sekcji").name("help").build();
        ApplicationCommandRequest role = ApplicationCommandRequest.builder().description("Role na serwerze").name("role").build();
        ApplicationCommandRequest channelCreate = ApplicationCommandRequest.builder().description("Utwórz kanał").name("create-channel")
                .addOption(ApplicationCommandOptionData.builder().description("Nazwa kanału").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).name("name").build())
                .addOption(ApplicationCommandOptionData.builder().description("Utwórz nowy kanał").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).name("type").addAllChoices(channelChoice()).build()).build();
        ApplicationCommandRequest channelDelete = ApplicationCommandRequest.builder().description("Usuń kanał").name("delete-channel")
                .addOption(ApplicationCommandOptionData.builder().description("Kanał do usunięcia").required(true).type(ApplicationCommandOption.Type.CHANNEL.getValue()).name("channel").build()).build();
        ApplicationCommandRequest createCategory = ApplicationCommandRequest.builder().description("Dodaj kategorię pytań").name("create-category").defaultMemberPermissions("0")
                .addOption(ApplicationCommandOptionData.builder().name("name").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).description("Name").build()).build();
        ApplicationCommandRequest createQuestion = ApplicationCommandRequest.builder().name("create-question").description("Dodaj pytanie").defaultMemberPermissions("0")
                .addOption(ApplicationCommandOptionData.builder().name("question").description("Treść pytania").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build())
                .addOption(ApplicationCommandOptionData.builder().name("answer").description("Treść odpowiedzi").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build())
                .addOption(ApplicationCommandOptionData.builder().name("cat").required(true).description("Kategoria").type(ApplicationCommandOption.Type.INTEGER.getValue()).addAllChoices(questionCategory()).build()).build();
        ApplicationCommandRequest createFaq = ApplicationCommandRequest.builder().name("create-faq").description("Tworzy faq").defaultMemberPermissions("0").build();
        ApplicationCommandRequest deleteCategory = ApplicationCommandRequest.builder().name("delete-category").description("Usuwa kategorie").defaultMemberPermissions("0")
                .addOption(ApplicationCommandOptionData.builder().name("cat").required(true).description("Kategoria").type(ApplicationCommandOption.Type.INTEGER.getValue()).addAllChoices(questionCategory()).build()).build();
        ApplicationCommandRequest deleteQuestion = ApplicationCommandRequest.builder().name("delete-question").description("Usuwa pytanie").defaultMemberPermissions("0")
                .addOption(ApplicationCommandOptionData.builder().name("cat").required(true).description("Kategoria").type(ApplicationCommandOption.Type.INTEGER.getValue()).addAllChoices(questionCategory()).build()).build();
        applicationService.createGuildApplicationCommand(appId, guildId, channelCreate).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, channelDelete).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, createProject).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, listProject).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, help).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, role).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, deleteProject).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, createCategory).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, createQuestion).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, createFaq).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, deleteCategory).subscribe();
        applicationService.createGuildApplicationCommand(appId, guildId, deleteQuestion).subscribe();
    }

    private static List<ApplicationCommandOptionChoiceData> channelChoice() {
        ApplicationCommandOptionChoiceData text = ApplicationCommandOptionChoiceData.builder().name("Kanał Tekstowy").value("text").build();
        ApplicationCommandOptionChoiceData voice = ApplicationCommandOptionChoiceData.builder().name("Kanał Głosowy").value("voice").build();
        return List.of(text, voice);
    }

    private static List<ApplicationCommandOptionChoiceData> questionCategory() {
        List<ApplicationCommandOptionChoiceData> options = new LinkedList<>();
        try {
            for (QuestionManager.Category i : QuestionManager.getCategories()) {
                options.add(ApplicationCommandOptionChoiceData.builder().value(i.id()).name(i.category()).build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return options;
    }

    private void updateCat() {
        ApplicationCommandRequest createQuestion = ApplicationCommandRequest.builder().name("create-question").description("Dodaj pytanie").defaultMemberPermissions("0")
                .addOption(ApplicationCommandOptionData.builder().name("question").description("Treść pytania").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build())
                .addOption(ApplicationCommandOptionData.builder().name("answer").description("Treść odpowiedzi").required(true).type(ApplicationCommandOption.Type.STRING.getValue()).build())
                .addOption(ApplicationCommandOptionData.builder().name("cat").required(true).description("Kategoria").type(ApplicationCommandOption.Type.INTEGER.getValue()).addAllChoices(questionCategory()).build()).build();
        long commandId = applicationService.getGuildApplicationCommands(appId, guildId).filter(x -> x.name().equals("create-question")).blockFirst().id().asLong();
        applicationService.modifyGuildApplicationCommand(appId, guildId, commandId, createQuestion).subscribe();
        ApplicationCommandRequest deleteCategory = ApplicationCommandRequest.builder().name("delete-category").description("Usuwa kategorie").defaultMemberPermissions("0")
                .addOption(ApplicationCommandOptionData.builder().name("cat").required(true).description("Kategoria").type(ApplicationCommandOption.Type.INTEGER.getValue()).addAllChoices(questionCategory()).build()).build();
        commandId = applicationService.getGuildApplicationCommands(appId, guildId).filter(x -> x.name().equals("delete-category")).blockFirst().id().asLong();
        applicationService.modifyGuildApplicationCommand(appId, guildId, commandId, deleteCategory).subscribe();
        ApplicationCommandRequest deleteQuestion = ApplicationCommandRequest.builder().name("delete-question").description("Usuwa pytanie").defaultMemberPermissions("0")
                .addOption(ApplicationCommandOptionData.builder().name("cat").required(true).description("Kategoria").type(ApplicationCommandOption.Type.INTEGER.getValue()).addAllChoices(questionCategory()).build()).build();
        commandId = applicationService.getGuildApplicationCommands(appId, guildId).filter(x -> x.name().equals("delete-Question")).blockFirst().id().asLong();
        applicationService.modifyGuildApplicationCommand(appId, guildId, commandId, deleteQuestion).subscribe();
    }

    public static void init(RestClient restClient, long guild) {
        instance = new CommandRegistry(restClient, guild);
        instance.createGuildCommands();
    }

    public static void updateCategories(){
        instance.updateCat();
    }
}
