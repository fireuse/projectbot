package commands.handlers;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.function.Consumer;

public class HelpHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        EmbedCreateSpec spec = EmbedCreateSpec.builder().title("**Pomocy, Pomocy!!**")
                .description("Poniżej znajdziesz pomocne informacje na temat kanałów i sekcji na serwerze.\n\n" +
                        "Aby dowiedzieć się więcej na temat roli na serwerze napisz /role")
                .thumbnail("https://scontent-waw2-1.xx.fbcdn.net/v/t39.30808-1/415127678_122115490724142773_470244489980226963_n.jpg?stp=c14.14.172.172a_dst-jpg_p200x200&_nc_cat=108&ccb=1-7&_nc_sid=f4b9fd&_nc_ohc=Eao8NnXJIDYQ7kNvgF94W-W&_nc_ht=scontent-waw2-1.xx&_nc_gid=AEMb51MINCiE1xCkzd39fPS&oh=00_AYDETcsqBqAXJqnDaodcdR55TITRJFkTG9hY3kMWNd10rw&oe=67080360")
                .addField("Organizacja", "<#1227904893562388543> – kanał z aktualnymi informacjami organizacyjnymi na temat funkcjonowania sekcji\n" +
                        "<#1082740831359881298> – informacje n.t. najnowszych spotkań", false)
                .addField("Sekcja Materiały", "Sekcja z przydatnymi materiałami do nauki na temat ML i nie tylko", false)
                .addField("Ogólne", "<#1087823695604613290>– otwarty kanał do swobodnych rozmów dla wszystkich\n" +
                        "<#1082742168424284180> – miejsce na nietypowe rozmowy na temat ML i nie tylko\n" +
                        "<#1082742145347244052> – Sekcja na inne wydarzenia, takie jak hackathony, konferencje, szkolenia itd.", false)
                .addField("Projekty", "<#1187147612193832960> – kanał na wymianę konstruktywnej krytyki na temat projektów\n" +
                        "<#1239282502950981683> – kanał do dzielenia się swoimi projektami", false)
                .addField("", "Poniżej możesz zapoznać się z regulaminami sekcji, oraz znaleźć nasze strony na social mediach i inne przydatne ankiety.", false)
                .addField("", "**[Facebook](https://www.facebook.com/sekcja.ai.kino)**", true)
                .addField("", "**[LinkedIn](https://www.linkedin.com/company/sekcja-ai-kino)**", true)
                .addField("", "Jeżeli nie znalazłeś tu odpowiedzi na swoje pytanie skontaktuj się z Zarządem Sekcji. ", false)
                .build();
        Button verification = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSd0QivKoErs0QVrnCwmEDaiWRnKz62_5Vh4E2Oz7jOeIew7lA/viewform?usp=sf_link", "Weryfikacja");
        Button newProject = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSeJDhzJo2wy9Op231N0Uda6ZZdFrzF39BxZgTafQ2ISxnAJ7g/viewform?usp=sf_link", "Nowy Projekt");
        Button join = Button.link("https://docs.google.com/forms/d/e/1FAIpQLScNwQMv_ie8SWmc2LFFMFqkkaItpa1QiU5V9H4KX0K6P-3XVA/viewform?usp=sf_link", "Dołącz do projektu");
        Button feedback = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSe601l0Icq6Ep7o5zjwDAZy65U8AiJj_lddC_mIvFOoJfMVkw/viewform?usp=sf_link", "Anonimowy Feedback");
        Button rules = Button.link("https://drive.google.com/file/d/159L_2ZfMwiTAQvhSdHXB08q_xSok3GMS/view?usp=drive_link", "Regulamin PŁ");
        Button rules_second = Button.link("https://drive.google.com/file/d/1H1gVk160c_vZcezslS7gxd7wCvKcT9ea/view?usp=drive_link", "Regulamin UŁ");
        Button rodo = Button.link("https://port.edu.p.lodz.pl/mod/page/view.php?id=1554&forceview=1", "Klauzula Informcyjna");
        event.reply().withEmbeds(spec).withEphemeral(true).withComponents(ActionRow.of(rules, rules_second, rodo), ActionRow.of(verification, feedback), ActionRow.of(newProject, join)).subscribe();
    }
}
